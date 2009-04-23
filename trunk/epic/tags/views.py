from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext
from django.utils import simplejson
from django.utils.datastructures import MultiValueDictKeyError

from epic.core.models import Item
from epic.datasets.models import DataSet
from epic.tags.models import Tagging
from epic.tags.utils import parse_tag_input

def index(request):
    count_tag_list = Tagging.objects.get_tags()
    return render_to_response('tags/index.html', {'tags':count_tag_list}, context_instance=RequestContext(request))

def view_items_for_tag(request, tag_name):
    tags = Tagging.objects.filter(tag=tag_name)
    datasets = []
    for tag in tags:
        dataset = tag.item.specific
        if dataset.is_active:
            datasets.append(tag.item.specific)
    return render_to_response('tags/tag_view.html', {'tags':tags, 'tag_name':tag_name, 'datasets':datasets}, context_instance=RequestContext(request))

@login_required
def delete_tag(request):
    """Remove a tag from the database.
    
    Post variables:
    tag_name -- the name of the tag to be removed
    dataset_id -- the dataset the tag is attached to
    
    """
    
    user = request.user
    responseData = {}
    try:
        dataset_id = request.POST['dataset_id']
        dataset = DataSet.objects.get(pk=dataset_id)
        
        tag_name = request.POST['tag_name']
        tag = Tagging.objects.get(tag=tag_name, item=dataset)
        
        if (user == tag.user or user == dataset.creator):
            tag.remove()
            responseData['success'] = 'The tag "%(tag)s" was removed.' % {'tag': tag,}
        else:
            responseData['failure'] = 'You do not have permission to remove the tag "%(tag)s" on dataset "%(dataset)s".' % {'tag': tag, 'dataset': dataset,}
    
    except MultiValueDictKeyError:
        responseData['failure'] = 'Either the tag_name or the dataset_id was not given.'
    except DataSet.DoesNotExist:
        responseData['failure'] = 'The dataset_id %s was invalid.' % (dataset_id)
    except Tagging.DoesNotExist:
        responseData['failure'] = 'The tag %s for dataset %s was invalid.' % (tag_name, dataset_id)

    json = simplejson.dumps(responseData)
    return HttpResponse(json, mimetype='application/json')
    
@login_required
def add_tags_and_return_successful_tag_names(request):
    """Add tags to the database and return the name of the successfully added tags in a success response.
    
    Post variables:
    unparsed_tag_names -- the raw string containing the tags to be added
    dataset_id -- the dataset the tags are to be added to
    
    """
    
    user = request.user
    responseData = {}
    try:
        dataset_id = request.POST['dataset_id']
        dataset = DataSet.objects.get(pk=dataset_id)
        
        unparsed_tag_names = request.POST['unparsed_tag_names']

        added_tags = Tagging.objects.add_tags_and_return_added_tag_names(unparsed_tag_names, dataset, user)
        
        responseData['success'] = added_tags

    except MultiValueDictKeyError:
        responseData['failure'] = 'Either the unparsed_tag_names or the dataset_id was not given.'
    except DataSet.DoesNotExist:
        responseData['failure'] = 'The dataset_id %s was invalid.' % (dataset_id)
    
    json = simplejson.dumps(responseData)
    return HttpResponse(json, mimetype='application/json')