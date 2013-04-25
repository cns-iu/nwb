from django.conf import settings
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.forms.formsets import formset_factory
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404
from django.template.defaultfilters import slugify
from django.template import RequestContext
from django.utils import simplejson
from django.utils.datastructures import MultiValueDictKeyError

from epic.comments.forms import PostCommentForm
from epic.comments.models import Comment
from epic.comments.views import make_comment_view
from epic.core.models import Item
from epic.datasets.forms import NewDataSetForm, EditDataSetForm, RatingDataSetForm, TagDataSetForm, GeoLocationFormSet, RemoveGeoLocationFormSet
from epic.datasets.models import DataSetFile, DataSet, RATING_SCALE
from epic.geoloc.models import GeoLoc
from epic.geoloc.utils import get_best_location, CouldNotFindLocation, parse_geolocation
from epic.tags.models import Tagging

from datetime import datetime
from decimal import Decimal

def view_datasets(request):
    datasets = DataSet.objects.all().order_by('-created_at')
    return render_to_response('datasets/view_datasets.html',
                              {'datasets': datasets,},
                              context_instance=RequestContext(request))

def view_dataset(request, item_id=None, slug=None):
    dataset = get_object_or_404(DataSet, pk=item_id)
    form = PostCommentForm()
    user = request.user
    
    return render_to_response('datasets/view_dataset.html', 
                              {'dataset': dataset, 'form': form},
                              context_instance=RequestContext(request))

post_dataset_comment = make_comment_view(
    DataSet,
    'epic.datasets.views.view_dataset',
    'datasets/view_dataset.html',
    'dataset')
    

def view_user_dataset_list(request, user_id=None):

    requested_user = get_object_or_404(User, pk=user_id)
    datasets = DataSet.objects.filter(creator=user_id).order_by('-created_at')
    return render_to_response('datasets/view_user_dataset_list.html', 
                              {'datasets': datasets, 'requested_user':requested_user,},
                              context_instance=RequestContext(request))

@login_required
def create_dataset(request):
    if request.method != 'POST':
        #user has not filled out the upload form yet
        form = NewDataSetForm()
        add_formset = GeoLocationFormSet(prefix='add')
        remove_formset = RemoveGeoLocationFormSet(prefix='remove')
    else:
        form = NewDataSetForm(request.POST, request.FILES)
        add_formset = GeoLocationFormSet(request.POST, prefix='add')
        remove_formset = RemoveGeoLocationFormSet(request.POST, prefix='remove')
        
        if form.is_valid():
            name = form.cleaned_data['name']
            description = form.cleaned_data['description']
            uploaded_files = form.cleaned_data['files']
            tags = form.cleaned_data['tags']
            
            new_dataset = DataSet.objects.create(creator=request.user, name=name, description=description, slug=slugify(name))
            Tagging.objects.update_tags(tags,item=new_dataset, user=request.user)
            
            for geoloc in _get_geolocs_from_formset(add_formset, 'add_location'):
                new_dataset.geolocations.add(geoloc)
            
            for geoloc in _get_geolocs_from_formset(remove_formset, 'remove_location'):
                new_dataset.geolocations.remove(geoloc)
            
            for uploaded_file in uploaded_files:
                new_datasetfile = DataSetFile(parent_dataset=new_dataset, file_contents=uploaded_file)
                new_datasetfile.file_contents.save(uploaded_file.name, uploaded_file, save=True)
                new_datasetfile.save()
            
            return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'item_id':new_dataset.id,'slug':new_dataset.slug}))
        
    return render_to_response('datasets/create_dataset.html', 
                              {'form':form, 'add_formset': add_formset, 'remove_formset':remove_formset,}, 
                              context_instance=RequestContext(request))

@login_required
def edit_dataset(request, item_id, slug=None):
    dataset = get_object_or_404(DataSet, pk=item_id)
    user = request.user
    
    # Make sure the current user is the creator of the dataset.
    if user != dataset.creator:
        return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
                                            kwargs={'item_id': dataset.id, 'slug':slug,}))
    
    if request.method != "POST":
        current_tags = Tagging.objects.get_edit_string(item=dataset, user=user)
        initial_dataset_data = {
            'name': dataset.name,
            'description': dataset.description,
            'tags': current_tags,
        }
        
        form = EditDataSetForm(initial=initial_dataset_data)
        initial_location_data = []
        geolocs = GeoLoc.objects.filter(datasets=dataset.id)
        for geoloc in geolocs:
            initial_location_data.append({'add_location':geoloc,})
        add_formset = GeoLocationFormSet(prefix='add', initial=initial_location_data)
        remove_formset = RemoveGeoLocationFormSet(prefix='remove')
    else:
        form = EditDataSetForm(request.POST)
        add_formset = GeoLocationFormSet(request.POST, prefix='add')
        remove_formset = RemoveGeoLocationFormSet(request.POST, prefix='remove')
            
        if form.is_valid():
            dataset.name = form.cleaned_data['name']
            dataset.description = form.cleaned_data['description']
            dataset.slug = slugify(dataset.name)
            dataset.save()

            tag_names = form.cleaned_data["tags"]
            Tagging.objects.update_tags(tag_names=tag_names, item=dataset, user=user)
            
            for geoloc in _get_geolocs_from_formset(add_formset, 'add_location'):
                dataset.geolocations.add(geoloc)
            
            for geoloc in _get_geolocs_from_formset(remove_formset, 'remove_location'):
                dataset.geolocations.remove(geoloc)
            
            return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
                                                kwargs={ "item_id": dataset.id, 'slug':slug, }))
            
    return render_to_response('datasets/edit_dataset.html', 
                              {'dataset': dataset, 'form': form, 'add_formset': add_formset,'remove_formset':remove_formset,},
                              context_instance=RequestContext(request))

@login_required
def rate_dataset(request, item_id, input_rating=None, slug=None):
    if input_rating:
        # TODO: surely this isn't supposed to be pass?!  What used to be here? Wont' it complain if no response is returned?!
        pass
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
                
                return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', 
                                                    kwargs={'item_id':dataset.id, 'slug':slug,}))
            else:
                #print request.POST
                # form.errors
                pass
                
        return render_to_response('datasets/rate_dataset.html', 
                                  {'form':form, 'item':dataset,}, 
                                  context_instance=RequestContext(request))
    
@login_required
def rate_dataset_using_input_rating(request, item_id, input_rating=None, slug=None):
    """
    Used specifically when the input_ratings field is provided. Mainly used 
    when voting from the UI for datasets. 
    """
    if input_rating:
        dataset = DataSet.objects.get(pk = item_id)
        user = request.user
        ip_address = request.META['REMOTE_ADDR']
        #Rely on the rating.add to make sure the that rating is valid
        rating = int(input_rating) 
        dataset.rating.add(rating, user, ip_address)
        dataset.save()
        return HttpResponse(simplejson.dumps("TRUE"), mimetype="application/javascript")
    else:
        # TODO: Surely this is not supposed to be pass.  Won't it compain if there is no response returned?
        pass
    
@login_required
def tag_dataset(request, item_id, slug=None):
    dataset = DataSet.objects.get(pk = item_id)
    
    if request.method != 'POST':
        current_tags = Tagging.objects.get_edit_string(item=dataset,user=request.user)
        form = TagDataSetForm(initial={'tags': current_tags})
        
        return render_to_response('datasets/tag_dataset.html',
                                  {'item_id': dataset.id, 'form': form,}, 
                                  context_instance=RequestContext(request))
    else:
        form = TagDataSetForm(request.POST)

        
        if form.is_valid():
            tags = form.cleaned_data['tags']
            Tagging.objects.update_tags(tags, item=dataset, user=request.user)
            
            return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
                                                kwargs={'item_id': dataset.id, 'slug': slug,}))
        else:
            return render_to_response('datasets/tag_dataset.html', 
                                      {'form':form,'item':dataset}, 
                                      context_instance=RequestContext(request))

def _get_geolocs_from_formset(formset, key='add_location'):
    """Return the proper 'Geoloc's from a formset.
    
    Arguments:
    formset -- the formset that contains the geolocations in text form
    
    Keyword Arguments:
    key -- the key into geolocation for the formset (default 'add_location')
    
    """
    
    geolocs = []
    if not formset.is_valid():
        print 'The formset for the geolocations for the edit dataset page was not valid'
    else:
        for form in formset.forms:
            location = form.cleaned_data[key]
            location_dict = parse_geolocation(location)
            lat = location_dict['lat']
            lng = location_dict['lng']
            canonical_name = location_dict['canonical_name']
            geoloc, created = GeoLoc.objects.get_or_create(longitude=lng, latitude=lat, canonical_name=canonical_name)
            geolocs.append(geoloc)
    return geolocs