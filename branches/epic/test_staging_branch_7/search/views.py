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
    if 'q' in request.GET and request.GET.get('q'):
        return HttpResponseRedirect(reverse(
            'all-query-search', kwargs={'query': request.GET.get('q')}))
    else:
        return HttpResponseRedirect(reverse('all-empty-search'))

def search_all(request, query=None):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query:
        data_requests = perform_search_for_item('DataRequest', query)[:3]
        datasets = perform_search_for_item('DataSet', query)[:3]
        projects = perform_search_for_item('Project', query)[:3]
        template_objects = {
            'data_requests': data_requests, 'datasets': datasets, 'projects': projects,
        }
    else:
        template_objects = {}

    return generic_search(request, query, template_objects, 'search/search_all.html')

def search_data_requests(request, query=None):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query is not None and query != '':
        data_requests_page = paginate(perform_search_for_item('DataRequest', query), request.GET)
        template_objects = {'data_requests_page': data_requests_page,}
    else:
        template_objects = {}

    return generic_search(request, query, template_objects, 'search/search_data_requests.html')

def search_datasets(request, query=None):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query is not None and query != '':
        datasets_page = paginate(perform_search_for_item('DataSet', query), request.GET)
        template_objects = {'datasets_page': datasets_page,}
    else:
        template_objects = {}

    return generic_search(request, query, template_objects, 'search/search_datasets.html')

def search_projects(request, query=None):
    display_form = SearchForm(initial={'q': query,}, load_all=True)

    if query is not None and query != '':
        projects_page = paginate(perform_search_for_item('Project', query), request.GET)
        template_objects = {'projects_page': projects_page,}
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
            template, render_to_response_data, context_instance=RequestContext(request))
    else:
        return render_to_response(
            template,
            {'form': display_form, 'query': query,},
            context_instance=RequestContext(request))
