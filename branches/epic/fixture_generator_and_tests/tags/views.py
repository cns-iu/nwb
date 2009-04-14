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

def index(request):
    count_tag_list = Tagging.objects.get_tags()
    return render_to_response('tags/index.html', {'tags':count_tag_list}, context_instance=RequestContext(request))

def view_items_for_tag(request, tag_name):
	tags = Tagging.objects.filter(tag=tag_name)
	datasets = []
	for tag in tags:
		datasets.append(tag.item.specific)
	return render_to_response('tags/tag_view.html', {'tags':tags, 'tag_name':tag_name, 'datasets':datasets}, context_instance=RequestContext(request))

@login_required
def delete_tag(request):
    user = request.user
    responseData = {}
    try:
        tag_name = request.POST['tag_name']
        dataset_id = request.POST['dataset_id']
        dataset = DataSet.objects.get(pk=dataset_id)
        if (user == dataset.creator):
            try:
                Tagging.objects.delete_tag(tag_name, dataset.id)
                responseData['success'] = 'The tag "%(tag_name)s" was removed.' % {'tag_name': tag_name,}
            except Tagging.DoesNotExist:
                responseData['failure'] = 'The tag "%(tag_name)s" on dataset "%(dataset)s" does not exist.' % {'tag_name': tag_name, 'dataset': dataset,}
        else:
            responseData['failure'] = 'You do not have permission to remove the tag "%(tag_name)s" on dataset "%(dataset)s".' % {'tag_name': tag_name, 'dataset': dataset,}
    
    except MultiValueDictKeyError:
        responseData['failure'] = 'Either the tag_name or the dataset_id was not given.'
    except DataSet.DoesNotExist:
        responseData['failure'] = 'The dataset_id %s was invalid.' % (dataset_id)
    
    json = simplejson.dumps(responseData)
    return HttpResponse(json, mimetype='application/json')
    
    
        
    