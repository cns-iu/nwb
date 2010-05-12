import urllib

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User, UserManager
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404
from django.template import RequestContext, Context, loader
from django.utils import simplejson

from haystack.forms import SearchForm
from haystack.query import EmptySearchQuerySet

from epic.core.models import Item
from epic.core.util.view_utils import *
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project
from epic.projects.util.util import *
from epic.search import perform_search
from epic.search import perform_search_for_item


def get_search(request):
    # TODO: Just redirect to search_all.
    if 'q' in request.GET:
        return search_all(request, request.GET.get('q'))
    else:
        return search_all(request, '')

def search_all(request, query=None):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query:
        data_requests = perform_search_for_item('DataRequest', query)
        datasets = perform_search_for_item('DataSet', query)
        projects = perform_search_for_item('Project', query)
        template_objects = {
            'data_requests': data_requests, 'datasets': datasets, 'projects': projects,
        }
    else:
        template_objects = {}

    return generic_search(request, query, template_objects, 'search/search_all.html')

def search_data_requests(request, query=None):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query is not None and query != '':
        data_requests = perform_search_for_item('DataRequest', query)
        template_objects = {'data_requests': data_requests,}
    else:
        template_objects = {}

    return generic_search(request, query, template_objects, 'search/search_data_requests.html')

def search_datasets(request, query=None):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query is not None and query != '':
        datasets = perform_search_for_item('DataSet', query)
        template_objects = {'datasets': datasets,}
    else:
        template_objects = {}

    return generic_search(request, query, template_objects, 'search/search_datasets.html')

def search_projects(request, query=None):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query is not None and query != '':
        projects = perform_search_for_item('Project', query)
        template_objects = {'projects': projects,}
    else:
        template_objects = {}

    return generic_search(request, query, template_objects, 'search/search_projects.html')

def generic_search(request, query, template_objects, template):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query is not None and query != '':
        render_to_response_data = {'form': display_form, 'query': query}

        for key in template_objects:
            render_to_response_data[key] = template_objects[key]

        return render_to_response(
            template,
            render_to_response_data,
            context_instance=RequestContext(request))
    else:
        return render_to_response(
            template,
            {'form': display_form, 'query': query,},
            context_instance=RequestContext(request))

#SERVELET_URL = 'http://localhost:8182/'
#
#def search(request):
#    # TODO:  TEST THIS!  Test 1 result and test many results
#    responseData = {}
#    error_message = None
#    datasets = None
#    projects = None
#    datarequests = None
#    
#    SEARCH_PARAM = 'search_string'
#    search_string = None
#    
#    if SEARCH_PARAM in request.POST:
#        search_string = request.POST[SEARCH_PARAM]
#    elif SEARCH_PARAM in request.GET:
#        search_string = request.GET[SEARCH_PARAM]
#      
#    if search_string:
#        try:
#            # The java needs spaces to be +s
#            search_string = search_string.replace(" ", "+")
#            
## Uncomment to use servlet            
#            raw_data = urllib.urlopen(SERVELET_URL + 
#                                      '?search_string=' + 
#                                      search_string)
#
##            raw_data = urllib.urlopen('http://epic.slis.indiana.edu/fake.json')
#            json_object = simplejson.loads(raw_data.read())
#            
#            if 'result' in json_object:
#                item_ids = []
#                scores = {}
#                
#                for result in json_object['result']:
#                    item_id = result['item_id']
#                    score = result['item_score']
#                    
#                    item_ids.append(item_id)
#                    scores['%s' % item_id] = score
#                
#                datasets = get_specifics_from_item_ids(DataSet, item_ids)
#                _apply_scores_to_results(datasets, scores)
#                
#                projects_from_search = \
#                    get_specifics_from_item_ids(Project, item_ids)
#                projects = projects_from_search
#                
#                _apply_scores_to_results(projects, scores)
##                projects_from_datasets = \
##                    get_projects_containing_datasets(datasets)
##                projects = projects_from_search | projects_from_datasets
#                
#                datarequests = \
#                    get_specifics_from_item_ids(DataRequest, item_ids)
#                _apply_scores_to_results(datarequests, scores)
#        except IOError:    
#            error_message = 'There is a problem with the search, ' + \
#                'please try again later.'
#        
#    return render_to_response('search/view_search_results.html', 
#                              {'datasets': datasets,
#                               'projects': projects,
#                               'datarequests': datarequests,
#                               'search_string': search_string,
#                               'error_message': error_message},
#                              context_instance=RequestContext(request))
#
#def _apply_scores_to_results(results, scores):
#    for result in results:
#        result.score = scores['%s' % result.id]
