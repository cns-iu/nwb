from epic.core.models import Item
from epic.datasets.forms import NewDataSetForm, EditDataSetForm, RatingDataSetForm, TagDataSetForm
from epic.datasets.models import DataSetFile, DataSet, RATING_SCALE

from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template.defaultfilters import slugify
from django.template import RequestContext

from django.contrib.auth.decorators import login_required

from epic.comments.models import Comment
from epic.comments.forms import PostCommentForm
from epic.comments.views import make_comment_view

from datetime import datetime


def index(request):
    datasets = DataSet.objects.all().order_by('-created_at')
    return render_to_response('datasets/index.html',
							  {'datasets': datasets, 'user': request.user},
							  context_instance=RequestContext(request))

def view_dataset(request, dataset_id=None, slug=None):
	dataset = get_object_or_404(DataSet, pk=dataset_id)
	post_comment_form = PostCommentForm()
	user = request.user
	
	return render_to_response('datasets/view_dataset.html', {
		'dataset': dataset,
		'user': user,
		'post_comment_form': post_comment_form
	})

post_dataset_comment = make_comment_view(
	DataSet,
	"epic.datasets.views.view_dataset",
	"datasets/view_dataset.html",
	"dataset")

@login_required
def create_dataset(request):
    if request.method != 'POST':
        #user has not filled out the upload form yet
        form = NewDataSetForm()
        return render_to_response('datasets/create_dataset.html', {'form': form, 'user':request.user,})
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
			
        	for uploaded_file in uploaded_files:
        	 	new_datasetfile = DataSetFile(parent_dataset=new_dataset, file_contents=uploaded_file)
        		new_datasetfile.file_contents.save(uploaded_file.name, uploaded_file, save=True)
        		new_datasetfile.save()
        	
        	#show them the page for the dataset we just created
        	return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'dataset_id':new_dataset.id,'slug':new_dataset.slug}))
        else:
        	#form wasn't filled out correctly
            return render_to_response('datasets/create_dataset.html', {'form':form, 'user':request.user,})

@login_required
def edit_dataset(request, dataset_id, slug=None):
	dataset = get_object_or_404(DataSet, pk=dataset_id)
	user = request.user
	
	# Make sure the current user is the creator of the dataset.
	if user != dataset.creator:
		return HttpResponseRedirect(reverse("epic.datasets.views.view_dataset",
			kwargs={ "dataset_id": dataset.id, "slug":slug, }))
	
	if request.method != "POST":
		current_tags = dataset.tags.get_edit_string(user=request.user)
		initial_data_dictionary = {
			"name": dataset.name,
			"description": dataset.description,
			"tags": current_tags
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
				kwargs={ "dataset_id": dataset.id, 'slug':slug, }))
	
	return render_to_response("datasets/edit_dataset.html", {
		"dataset": dataset,
		"user": user,
		"edit_dataset_metadata_form": edit_dataset_metadata_form
	})

@login_required
def rate_dataset(request, dataset_id, input_rating=None, slug=None):
	#TODO: Make commenting ajaxified?
	if input_rating:
		dataset = DataSet.objects.get(pk = dataset_id)
		user = request.user
		ip_address = request.META['REMOTE_ADDR']
		#Rely on the rating.add to make sure the that rating is valid
		rating = int(input_rating) 
		dataset.rating.add(rating, user, ip_address)
		dataset.save()
		return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'dataset_id':dataset.id, 'slug':slug,}))
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
				
				dataset = DataSet.objects.get(pk = dataset_id)
				dataset.rating.add(rating, user, ip_address)
				dataset.save()
				
				return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'dataset_id':dataset.id, 'slug':slug,}))
			else:
				print request.POST
				print form.errors
				
		return render_to_response('datasets/rate_dataset.html', {'form':form, 'user':request.user,})

@login_required
def tag_dataset(request, dataset_id, slug=None):
	dataset = DataSet.objects.get(pk = dataset_id)
	if request.method != 'POST':
		current_tags = dataset.tags.get_edit_string(user=request.user)
		form = TagDataSetForm(initial={'tags': current_tags})
		return render_to_response('datasets/tag_dataset.html', {'form':form, 'user':request.user,})
	else:
		form = TagDataSetForm(request.POST)
		print request.POST
		if form.is_valid():
			tags = form.cleaned_data['tags']
			dataset.tags.update_tags(tags, user=request.user)
			return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'dataset_id':dataset.id, 'slug':slug,}))
		else:
			return render_to_response('datasets/tag_dataset.html', {'form':form, 'user':request.user,})