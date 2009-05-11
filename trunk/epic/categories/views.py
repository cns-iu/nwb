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
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project
from epic.projects.util.util import *


def view_categories(request):
    categories = Category.objects.all()
    
    return render_to_response('categories/view_categories.html',
                              {'categories': categories})

def view_items_for_category(request, category_id):
    category = get_object_or_404(Category, pk=category_id)
    
    # TODO: Give these to the template as separate lists and display them
    # separately.
    datasets = _get_datasets_for_category(category)
    projects_from_datasets = get_projects_containing_datasets(datasets)
    projects_from_category = _get_projects_for_category(category)
    projects = projects_from_datasets | projects_from_category
    datarequests = _get_datarequests_for_category(category)
    specifics = list(datasets) + list(projects) + list(datarequests)
    
    return render_to_response('categories/view_items_for_category.html', 
                              {'category': category, 'specifics': specifics}, 
                               context_instance=RequestContext(request))
    items = Item.objects.filter(category=category)

def view_datasets_for_category(request, category_id):
    category = get_object_or_404(Category, pk=category_id)
    
    datasets = _get_datasets_for_category(category)
    specifics = datasets
    
    return render_to_response('categories/view_items_for_category.html', 
                              {'category': category, 'specifics': specifics}, 
                               context_instance=RequestContext(request))

def view_projects_for_category(request, category_id):
    category = get_object_or_404(Category, pk=category_id)
    
    datasets = _get_datasets_for_category(category)
    projects_from_datasets = set(get_projects_containing_datasets(datasets))
    projects_from_category = set(_get_projects_for_category(category))
    projects = list(projects_from_datasets.union(projects_from_category))
    specifics = projects
    
    return render_to_response('categories/view_items_for_category.html', 
                              {'category': category, 'specifics': specifics}, 
                               context_instance=RequestContext(request))

def view_datarequests_for_category(request, category_id):
    category = get_object_or_404(Category, pk=category_id)
    
    datarequests = _get_datarequests_for_category(category)
    specifics = datarequests
    
    return render_to_response('categories/view_items_for_category.html', 
                              {'category': category, 'specifics': specifics}, 
                               context_instance=RequestContext(request))

def _get_datasets_for_category(category):
    datasets = DataSet.objects.active().filter(category=category)
    
    return datasets

def _get_projects_for_category(category):
    projects = Project.objects.active().filter(category=category)
    
    return projects

def _get_datarequests_for_category(category):
    datarequests = DataRequest.objects.active().filter(category=category)
    
    return datarequests
