import re

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.forms.util import ErrorList
from django.shortcuts import get_list_or_404
from django.shortcuts import get_object_or_404
from django.shortcuts import render_to_response
from django.template import RequestContext
from django.template.defaultfilters import slugify

from epic.comments.forms import PostCommentForm
from epic.core.models import Item
from epic.core.util.view_utils import *
from epic.datasets.models import DataSet
from epic.projects.forms import AddDatasetToProjectForm
from epic.projects.forms import AddDatasetToProjectFormSet
from epic.projects.forms import ProjectForm
from epic.projects.forms import RemoveDatasetFromProjectForm
from epic.projects.models import Project


@login_required
def create_project(request):
    if request.method != 'POST':
        new_project_form = ProjectForm()
    else:
        new_project_form = ProjectForm(request.POST)
        
        if new_project_form.is_valid():
            name = new_project_form.cleaned_data['name']
            description = new_project_form.cleaned_data['description']
            slug = slugify(name)
            
            new_project = Project.objects.create(creator=request.user,
                                                 name=name,
                                                 description=description,
                                                 slug=slug,
                                                 is_active=True)
            
            edit_project_url = \
                get_item_url(new_project, 'epic.projects.views.edit_project')
            
            return HttpResponseRedirect(edit_project_url)
    
    return render_to_response(
        'projects/create_project.html',
        {'new_project_form': new_project_form,},
        context_instance=RequestContext(request))

@login_required
def confirm_delete_project(request, item_id, slug):
    project = get_object_or_404(Project, pk=item_id)
    user = request.user
    
    view_project_url = \
        get_item_url(project, 'epic.projects.views.view_project')
    
    if not user_is_item_creator(user, project):
        return HttpResponseRedirect(view_project_url)
    
    return render_to_response(
        'projects/confirm_delete_project.html', 
        {'project': project,},
        context_instance=RequestContext(request))

@login_required
def delete_project(request, item_id, slug):
    project = get_object_or_404(Project, pk=item_id)
    user = request.user
    
    view_project_url = \
        get_item_url(project, 'epic.projects.views.view_project')
    
    if not user_is_item_creator(user, project):
        return HttpResponseRedirect(view_project_url)
    
    project.is_active = False
    project.save()
    
    view_profile_url = reverse('epic.core.views.view_profile')
    
    return HttpResponseRedirect(view_profile_url)

@login_required
def edit_project(request, item_id, slug):
    project = get_object_or_404(Project, pk=item_id)
    user = request.user
    
    view_project_url = \
        get_item_url(project, 'epic.projects.views.view_project')
    
    if not user_is_item_creator(user, project):
        return HttpResponseRedirect(view_project_url)
    
    if request.method != "POST":
        initial_edit_project_data = {
            'name': project.name,
            'description': project.description,
        }
    
        edit_form = ProjectForm(initial=initial_edit_project_data)
        add_dataset_form = AddDatasetToProjectForm()
    else:
        edit_form = ProjectForm(request.POST)
        add_dataset_form = AddDatasetToProjectForm(request.POST)
            
        if edit_form.is_valid() and add_dataset_form.is_valid():
            _save_project(edit_form, project)
            add_dataset_form = \
                _update_datasets_for_project(request, add_dataset_form, project)
        
        if 'save_and_finish_editing' in request.POST:
            return HttpResponseRedirect(view_project_url)
    
    
    render_to_response_data = {
        'project': project,
        'edit_project_form': edit_form,
        'add_dataset_to_project_form': add_dataset_form,
        'datasets': project.datasets.all(),
    }
            
    return render_to_response('projects/edit_project.html', 
                              render_to_response_data,
                              context_instance=RequestContext(request))

def view_projects(request):
    projects = Project.objects.active().order_by('-created_at')
    
    return render_to_response('projects/view_projects.html',
                              {'projects': projects,},
                              context_instance=RequestContext(request))

def view_project(request, item_id, slug):
    project = get_object_or_404(Project, pk=item_id)
    form = PostCommentForm()
    user = request.user
    
    return render_to_response('projects/view_project.html', 
                              {'project': project, 'form': form},
                              context_instance=RequestContext(request))

def view_user_project_list(request, user_id):
    requested_user = get_object_or_404(User, pk=user_id)
    
    projects = Project.objects.active().\
        filter(creator=requested_user).order_by('-created_at')
    
    return render_to_response(
        'projects/view_user_project_list.html',
        {'projects': projects, 'requested_user': requested_user},
        context_instance=RequestContext(request))



def _save_project(form, project):
    project.name = form.cleaned_data['name']
    project.description = form.cleaned_data['description']
    project.slug = slugify(project.name)
    project.save()

def _update_datasets_for_project(request, form, project):
    # AddDatasetToProjectForm's validation should make sure dataset_id
    # is a valid dataset id.
    dataset = form.cleaned_data['dataset']
    
    if dataset is not None:
        # Form is reset to an empty form if the dataset has already been added
        # to this project.
        form = AddDatasetToProjectForm()
        
        # TODO: Error upon duplication of a dataset (in a project)?
        if dataset not in project.datasets.all():
            project.datasets.add(dataset)

    # Remove datasets.
    
    remove_dataset_expression = \
        r'^remove-dataset-(?P<item_id>\d+)$'
    
    pattern = re.compile(remove_dataset_expression) 
    
    for post_key in request.POST:
        match = pattern.match(post_key)
        
        if match is not None:
            item_id = match.group('item_id')
            dataset_to_remove = get_object_or_404(DataSet, pk=item_id)
            
            project.datasets.remove(dataset_to_remove)
    
    return form
