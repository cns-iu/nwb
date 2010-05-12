from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.shortcuts import get_object_or_404
from django.shortcuts import render_to_response
from django.template import RequestContext
from django.utils import simplejson
from django.utils.datastructures import MultiValueDictKeyError

from epic.core.models import Item
from epic.core.util import active_user_required
from epic.datasets.models import DataSet
from epic.datarequests.models import DataRequest
from epic.projects.util.util import *
from epic.tags.models import Tagging
from epic.tags.utils import parse_tag_input
from epic.core.util.view_utils import paginate


def index(request):
    count_tag_list = Tagging.objects.get_tags()

    return render_to_response(
        'tags/index.html', {'tags': count_tag_list}, context_instance=RequestContext(request))

def view_items_for_tag(request, tag_name):
    tags = Tagging.objects.filter(tag=tag_name)
    datasets = _get_datasets_for_tags(tags)[:3]
    projects = get_projects_containing_datasets(datasets)[:3]
    datarequests = _get_datarequests_for_tags(tags)[:3]
    
    return render_to_response(
        'tags/view_all.html',
        {
            'tags': tags,
            'tag_name': tag_name,
            'datasets': datasets,
            'projects': projects,
            'datarequests': datarequests
        },
        context_instance=RequestContext(request))

def view_datasets_for_tag(request, tag_name):
    tags = Tagging.objects.filter(tag=tag_name)

    datasets_page = paginate(_get_datasets_for_tags(tags), request.GET)

    return render_to_response(
        'tags/view_datasets.html', 
        {'tags': tags, 'tag_name': tag_name, 'datasets_page': datasets_page},
        context_instance=RequestContext(request))

def view_projects_for_tag(request, tag_name):
    tags = Tagging.objects.filter(tag=tag_name)
    projects_page = paginate(_get_projects_for_tags(tags), request.GET)
    
    return render_to_response(
        'tags/view_projects.html', 
        {'tags': tags, 'tag_name': tag_name, 'projects_page': projects_page}, 
        context_instance=RequestContext(request))

def view_datarequests_for_tag(request, tag_name):
    tags = Tagging.objects.filter(tag=tag_name)
    datarequests_page = paginate(_get_datarequests_for_tags(tags), request.GET)

    return render_to_response(
        'tags/view_datarequests.html', 
        {'tags': tags, 'tag_name': tag_name, 'datarequests_page': datarequests_page},
        context_instance=RequestContext(request))

@login_required
@active_user_required
def delete_tag(request):
    """Remove a tag from the database.
    
    Post variables:
    tag_name -- the name of the tag to be removed
    item_id -- the item the tag is attached to
    
    """
    
    user = request.user
    responseData = {}

    try:
        item_id = request.POST['item_id']

        if not item_id:
            item_id = None

        item = Item.objects.get(pk=item_id)
        tag_name = request.POST['tag_name']

        if not tag_name:
            tag_name = None

        tag = Tagging.objects.get(tag=tag_name, item=item)

        if (user == tag.user or user == item.creator):
            tag.remove()
            responseData['success'] = 'The tag "%(tag)s" was removed.' % {'tag': tag,}
        else:
            responseData['failure'] = \
                'You do not have permission to remove the tag "%(tag)s" on item "%(item)s".' % \
                    {'tag': tag, 'item': item,}
    
    except MultiValueDictKeyError:
        responseData['failure'] = 'Either the tag_name or the item_id was not given.'
    except Item.DoesNotExist:
        responseData['failure'] = 'The item_id %s was invalid.' % (item_id)
    except Tagging.DoesNotExist:
        responseData['failure'] = 'The tag %s for item %s was invalid.' % (tag_name, item_id)

    json = simplejson.dumps(responseData)

    return HttpResponse(json, mimetype='application/json')
    
@login_required
@active_user_required
def add_tags_and_return_successful_tag_names(request):
    """Add tags to the database and return the name of the successfully added tags in a
    success response.
    
    Post variables:
    unparsed_tag_names -- the raw string containing the tags to be added
    item_id -- the item the tags are to be added to
    
    """
    
    user = request.user
    responseData = {}

    try:
        item_id = request.POST['item_id']

        if not item_id:
            item_id = None

        item = Item.objects.get(pk=item_id)
        unparsed_tag_names = request.POST['unparsed_tag_names']
        added_tags = Tagging.objects.add_tags_and_return_added_tag_names(
            unparsed_tag_names, item, user)
        responseData['success'] = added_tags

    except MultiValueDictKeyError:
        responseData['failure'] = 'Either the unparsed_tag_names or the item_id was not given.'
    except Item.DoesNotExist:
        responseData['failure'] = 'The item_id %s was invalid.' % (item_id)
    
    json = simplejson.dumps(responseData)

    return HttpResponse(json, mimetype='application/json')

def _get_datasets_for_tags(tags):
    datasets = \
        DataSet.objects.filter(is_active=True).filter(tags__in=tags).order_by('-created_at')

    return datasets

def _get_datarequests_for_tags(tags):
    datarequests = \
        DataRequest.objects.filter(is_active=True).filter(tags__in=tags).order_by('-created_at')
    
    return datarequests

def _get_projects_for_tags(tags):
    return get_projects_containing_datasets(_get_datasets_for_tags(tags))
