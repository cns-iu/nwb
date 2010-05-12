from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import get_object_or_404
from django.shortcuts import render_to_response
from django.template import RequestContext

from epic.categories.models import Category
from epic.core.models import Item
from epic.core.util.view_utils import paginate
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project
from epic.projects.util.util import *


def view_categories(request):
    categories = Category.objects.all()
    
    return render_to_response(
        'categories/view_categories.html',
        {'categories': categories},
        context_instance=RequestContext(request))

def view_items_for_category(request, category_id):
    category = get_object_or_404(Category, name=category_id)
    datasets = _get_datasets_for_category(category)[:3]
    projects = _get_projects_for_category(category)[:3]
    datarequests = _get_datarequests_for_category(category)[:3]
    
    return render_to_response(
        'categories/view_all.html', 
        {
            'category': category,
            'datasets': datasets,
            'projects': projects,
            'datarequests': datarequests
        },
        context_instance=RequestContext(request))

def view_datasets_for_category(request, category_id):
    category = get_object_or_404(Category, name=category_id)
    datasets_page = paginate(_get_datasets_for_category(category), request.GET)    
    
    return render_to_response(
        'categories/view_datasets.html', 
        {'category': category, 'datasets_page': datasets_page},
        context_instance=RequestContext(request))

def view_projects_for_category(request, category_id):
    category = get_object_or_404(Category, name=category_id)
    projects_page = paginate(_get_projects_for_category(category), request.GET)
    
    return render_to_response(
        'categories/view_projects.html', 
        {'category': category, 'projects_page': projects_page},
        context_instance=RequestContext(request))

def view_datarequests_for_category(request, category_id):
    category = get_object_or_404(Category, name=category_id)
    datarequests_page = paginate(_get_datarequests_for_category(category), request.GET)
    
    return render_to_response(
        'categories/view_datarequests.html', 
        {'category': category, 'datarequests_page': datarequests_page},
        context_instance=RequestContext(request))

def _get_datasets_for_category(category):
    return DataSet.objects.active().filter(category=category).order_by('-created_at')

def _get_projects_for_category(category):
    datasets = _get_datasets_for_category(category)
    projects_from_datasets = get_projects_containing_datasets(datasets)
    projects_from_category = \
        Project.objects.active().filter(category=category).order_by('-created_at').distinct()

    return (projects_from_datasets | projects_from_category).order_by('-created_at')

def _get_datarequests_for_category(category):
    return DataRequest.objects.active().filter(category=category).order_by('-created_at')
