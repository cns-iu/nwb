from epic.core.models import Item
from epic.datasets.forms import NewDataSetForm, EditDataSetForm, RatingDataSetForm, TagDataSetForm
from epic.datasets.models import DataSetFile, DataSet, RATING_SCALE

from django.conf import settings
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404
from django.template.defaultfilters import slugify
from django.template import RequestContext
from django.utils import simplejson
from django.utils.datastructures import MultiValueDictKeyError
from django.contrib.auth.decorators import login_required

from epic.comments.models import Comment
from epic.comments.forms import PostCommentForm
from epic.comments.views import make_comment_view
from epic.geoloc.utils import get_best_location, CouldNotFindLocation
from epic.geoloc.models import GeoLoc

from datetime import datetime
from decimal import Decimal

def index(request):
	datasets = DataSet.objects.all().order_by('-created_at')
	return render_to_response('datasets/index.html',
    	{ 'datasets': datasets, 'user': request.user },
    	context_instance=RequestContext(request))

def view_dataset(request, item_id=None, slug=None):
	dataset = get_object_or_404(DataSet, pk=item_id)
	post_comment_form = PostCommentForm()
	user = request.user
	
	return render_to_response('datasets/view_dataset.html', 
		{ 'dataset': dataset, 'user': user, 'post_comment_form': post_comment_form },
		context_instance=RequestContext(request))

post_dataset_comment = make_comment_view(
	DataSet,
	"epic.datasets.views.view_dataset",
	"datasets/view_dataset.html",
	"dataset")

@login_required
def remove_location_from_dataset(request, item_id, slug=None):
	dataset = get_object_or_404(DataSet, pk=item_id)
	user = request.user
	xhr = request.GET.has_key('xhr')
	responseData = {}
	
	# Make sure the current user is the creator of the dataset.
	if user != dataset.creator:
		return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
										     kwargs={ 'item_id': dataset.id, 'slug':slug, }))

	if request.method != 'POST':
		return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
										     kwargs={ 'item_id': dataset.id, 'slug':slug, }))
	else:
		if not xhr:
			return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
											     kwargs={ 'item_id': dataset.id, 'slug':slug, }))
		else:
			lat = Decimal(request.POST['lat'])
			lng = Decimal(request.POST['lng'])
			canonical_name = request.POST['canonical_name']	
			geoloc = GeoLoc.objects.get(longitude=lng, latitude=lat, canonical_name=canonical_name)
			dataset.geolocations.remove(geoloc)
			
			# TODO: Document what is going on here in detail.
			query_list = GeoLoc.objects.filter(datasets=item_id)
			locations = []
			for location in query_list:
				location_name_urls = []
				for dataset in location.datasets.all():
					location_name_urls.append( [ dataset.name, dataset.get_absolute_url() ])
				locations.append([ str(location.longitude), str(location.latitude), location.canonical_name, location_name_urls])
			print locations
			responseData['success'] = '%s was successfully removed from the dataset' % geoloc.canonical_name
			responseData['locations'] = locations
			json = simplejson.dumps(responseData)
			return HttpResponse(json, mimetype='application/json')
	
# TODO: Also document this very well.
@login_required
def add_location_to_dataset(request, item_id, slug=None):
	dataset = get_object_or_404(DataSet, pk=item_id)
	location_list = GeoLoc.objects.filter(datasets=item_id)
	user = request.user
	xhr = request.GET.has_key('xhr')
	responseData = {}
	# Make sure the current user is the creator of the dataset.
	if user != dataset.creator:
		return HttpResponseRedirect(reverse("epic.datasets.views.view_dataset",
			kwargs={ "item_id": dataset.id, "slug":slug, }))
	
	if request.method == "POST":
		if xhr:
			try:
				location_string = request.POST['location_string']
			except:
				location_string = None
				
			if location_string is not None:
				try:
					# TODO: does the location_string need to be cleaned somehow?
					location = get_best_location(location_string)
					lng = Decimal(str(location[1][1]))
					lat = Decimal(str(location[1][0]))
					canonical_name=location[0]	
				except CouldNotFindLocation:
					responseData['failure'] = "%s could not be resolved to a location" % location_string
					json = simplejson.dumps(responseData)
					return HttpResponse(json, mimetype='application/json')
			else:
				lat = Decimal(request.POST['lat'])
				lng = Decimal(request.POST['lng'])
				try:
					canonical_name = request.POST['canonical_name']
					if not canonical_name:
						canonical_name = 'Unknown Location'
				except:
					canonical_name = 'Unknown Location'
				
			try:
				geoloc = GeoLoc.objects.get(longitude=lng,latitude=lat)
			except:
				geoloc = GeoLoc(longitude=lng, latitude=lat, canonical_name=canonical_name)
				geoloc.save()
			
			dataset.geolocations.add(geoloc)
			responseData['success'] = 'A marker was added for %s' % geoloc.canonical_name
			responseData['locLng'] = str(geoloc.longitude)
			responseData['locLat'] = str(geoloc.latitude)
			responseData['loccanonical_name'] = geoloc.canonical_name
			
			loc_url_list = []
			for dataset in geoloc.datasets.all():
				loc_url_list.append( [ dataset.name, dataset.get_absolute_url() ])
			responseData['locUrlList'] = loc_url_list

			json = simplejson.dumps(responseData)
			return HttpResponse(json, mimetype='application/json')
		
	return render_to_response("datasets/add_locations_to_dataset.html", 
							  {
							  	"dataset": dataset,
								"user": user,
								"GOOGLE_KEY":settings.GOOGLE_KEY,
								"location_list":location_list,
							  }
							 )

@login_required
def create_dataset(request):
	if request.method != 'POST':
		#user has not filled out the upload form yet
		form = NewDataSetForm()
		return render_to_response('datasets/create_dataset.html', {'form': form, 'user':request.user})
	else:
		#user has filled out the upload form
		
		form = NewDataSetForm(request.POST, request.FILES)
		
		if form.is_valid():
			name = form.cleaned_data['name']
			description = form.cleaned_data['description']
			uploaded_files = form.cleaned_data['files']
			tags = form.cleaned_data['tags']
			
			new_dataset = DataSet.objects.create(creator=request.user, name=name, description=description, slug=slugify(name))
			new_dataset.tags.update_tags(tags, user=request.user)
			
			try:
				# TODO: Make this so Baby Patrick does not cry.
				locations = request.POST['MapPoints']
				locations = locations.split('[[')
				locations.remove("")
	
				for location in locations:
					location = location.replace("'],", '')
					location = location.replace("],'", ',')
					location = location.replace("']", "")
					location = location.split(',')
					print location
					lat = location[0]
					lng = location[1]
					canonical_name = location[2]
					print "%s, %s = %s" % (lng, lat, canonical_name)
					
					try:
						geoloc = GeoLoc.objects.get(longitude=lng,latitude=lat)
					except:
						geoloc = GeoLoc(longitude=lng, latitude=lat, canonical_name=canonical_name)
						geoloc.save()
					
					new_dataset.geolocations.add(geoloc)
			
			except MultiValueDictKeyError:
				# The user didn't post any locations
				pass

			
			
			for uploaded_file in uploaded_files:
			 	new_datasetfile = DataSetFile(parent_dataset=new_dataset, file_contents=uploaded_file)
				new_datasetfile.file_contents.save(uploaded_file.name, uploaded_file, save=True)
				new_datasetfile.save()
			
			#show them the page for the dataset we just created
			return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'item_id':new_dataset.id,'slug':new_dataset.slug}))
		else:
			#form wasn't filled out correctly
			return render_to_response('datasets/create_dataset.html', {'form':form, 'user':request.user})

@login_required
def edit_dataset(request, item_id, slug=None):
	dataset = get_object_or_404(DataSet, pk=item_id)
	user = request.user
	
	# Make sure the current user is the creator of the dataset.
	if user != dataset.creator:
		return HttpResponseRedirect(reverse("epic.datasets.views.view_dataset",
			kwargs={ "item_id": dataset.id, "slug":slug, }))
	
	if request.method != "POST":
		current_tags = dataset.tags.get_edit_string(user=request.user)
		initial_data_dictionary = {
			"name": dataset.name,
			"description": dataset.description,
			"tags": current_tags,
		}
		
		edit_dataset_metadata_form = EditDataSetForm(initial=initial_data_dictionary)
	else:
		edit_dataset_metadata_form = EditDataSetForm(request.POST)
		
		if edit_dataset_metadata_form.is_valid():
			dataset.name = edit_dataset_metadata_form.cleaned_data["name"]
			dataset.description = edit_dataset_metadata_form.cleaned_data["description"]
			dataset.slug = slugify(dataset.name)
			dataset.save()
			
			tags = edit_dataset_metadata_form.cleaned_data["tags"]
			dataset.tags.update_tags(tags, user=user)
			return HttpResponseRedirect(reverse("epic.datasets.views.view_dataset",
				kwargs={ "item_id": dataset.id, 'slug':slug, }))
	
	return render_to_response("datasets/edit_dataset.html", {
		"dataset": dataset,
		"user": user,
		"edit_dataset_metadata_form": edit_dataset_metadata_form
	})

@login_required
def rate_dataset(request, item_id, input_rating=None, slug=None):
	#TODO: Make commenting ajaxified?
	if input_rating:
		dataset = DataSet.objects.get(pk = item_id)
		user = request.user
		ip_address = request.META['REMOTE_ADDR']
		#Rely on the rating.add to make sure the that rating is valid
		rating = int(input_rating) 
		dataset.rating.add(rating, user, ip_address)
		dataset.save()
		return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'item_id':dataset.id, 'slug':slug,}))
	else:
		if request.method != 'POST':
			#show them the rate form
	  		form = RatingDataSetForm()
		else:
			# handle the submission of their rate form
			form = RatingDataSetForm(request.POST)
			if form.is_valid():
				rating = int(form.cleaned_data['rating']) #TODO: why doesn't it just return an int?
				ip_address = request.META['REMOTE_ADDR']
				user = request.user
				
				dataset = DataSet.objects.get(pk = item_id)
				dataset.rating.add(rating, user, ip_address)
				dataset.save()
				
				return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'item_id':dataset.id, 'slug':slug,}))
			else:
				#print request.POST
				# form.errors
				pass
				
		return render_to_response('datasets/rate_dataset.html', {'form':form, 'user':request.user, 'item':dataset,})

@login_required
def tag_dataset(request, item_id, slug=None):
	dataset = DataSet.objects.get(pk = item_id)
	if request.method != 'POST':
		current_tags = dataset.tags.get_edit_string(user=request.user)
		form = TagDataSetForm(initial={'tags': current_tags})
		return render_to_response('datasets/tag_dataset.html', {'form':form, 'user':request.user,})
	else:
		form = TagDataSetForm(request.POST)
		if form.is_valid():
			tags = form.cleaned_data['tags']
			dataset.tags.update_tags(tags, user=request.user)
			return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'item_id':dataset.id, 'slug':slug,}))
		else:
			return render_to_response('datasets/tag_dataset.html', {'form':form, 'user':request.user, 'item':dataset})