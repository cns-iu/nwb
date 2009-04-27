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
from epic.projects.forms import AddDatasetToProjectForm
from epic.projects.forms import EditProjectForm
from epic.projects.forms import NewProjectForm
from epic.projects.forms import RemoveDatasetFromProjectForm
from epic.projects.models import Project


@login_required
def check_dataset_url(request, dataset_url):
    pass

@login_required
def create_project(request):
    if request.method != 'POST':
        form = NewProjectForm()
    else:
        form = NewProjectForm(request.POST)
        
        if form.is_valid():
            name = form.cleaned_data['name']
            description = form.cleaned_data['description']
            slug = slugify(name)
            
            new_project = Project.objects.create(creator=request.user,
                                                 name=name,
                                                 description=description,
                                                 slug=slug)
            
            edit_project_url = \
                get_item_url(new_project, 'epic.projects.views.edit_project')
            
            return HttpResponseRedirect(edit_project_url)
    
    render_to_response_data = {
        'form': form,
    }
    
    return render_to_response('projects/create_project.html',
                              render_to_response_data,
                              context_instance=RequestContext(request))

@login_required
def edit_project(request, item_id, slug):
    project = get_object_or_404(Project, pk=item_id)
    user = request.user
    
    view_project_url = \
        get_item_url(project, 'epic.projects.views.view_project')
    
    if not user_is_item_creator(user, project):
        return HttpResponseRedirect(view_project_url)
    
    if request.method != "POST":
        initial_remove_datasets_data = []
        
        for dataset in project.datasets.all():
            initial_remove_datasets_data.append({'dataset_id': dataset.id})
        
        edit_project_form = _make_edit_project_form_from_project(project)
        add_dataset_to_project_form = AddDatasetToProjectForm()
    else:
        edit_project_form = EditProjectForm(request.POST)
        add_dataset_to_project_form = AddDatasetToProjectForm(request.POST)
            
        if edit_project_form.is_valid():
            project.name = edit_project_form.cleaned_data['name']
            project.description = \
                edit_project_form.cleaned_data['description']
            project.slug = slugify(project.name)
            project.save()
        
        if add_dataset_to_project_form.is_valid():
            # AddDatasetToProjectForm's validation should make sure dataset_id
            # is a valid dataset id.
            dataset = add_dataset_to_project_form.cleaned_data['dataset']
            
            if dataset is not None:
                add_dataset_to_project_form = AddDatasetToProjectForm()
                
                try:
                    project.datasets.get(pk=dataset.id)
                except:
                    project.datasets.add(dataset)
        
        # Remove datasets.
        for dataset in project.datasets.all():
            should_remove_this_dataset_id = 'remove-dataset-%s' % dataset.id
            
            try:
                remove_this_dataset_field_on = \
                    request.POST[should_remove_this_dataset_id]
                
                if remove_this_dataset_field_on == 'on':
                    should_remove_this_dataset = True
                else:
                    should_remove_this_dataset = False
                
                if should_remove_this_dataset:
                    project.datasets.remove(dataset)
            except:
                pass
        
        try:
            if request.POST['save_and_finish_editing']:
                return HttpResponseRedirect(view_project_url)
        except:
            pass
    
    
    render_to_response_data = {
        'project': project,
        'edit_project_form': edit_project_form,
        'add_dataset_to_project_form': add_dataset_to_project_form,
        'datasets': project.datasets.all(),
    }
            
    return render_to_response('projects/edit_project.html', 
                              render_to_response_data,
                              context_instance=RequestContext(request))

def view_projects(request):
    projects = Project.objects.all().order_by('-created_at')
    
    return render_to_response('projects/view_projects.html',
                              {'projects': projects,},
                              context_instance=RequestContext(request))
def view_project(request, item_id=None, slug=None):
    project = get_object_or_404(Project, pk=item_id)
    form = PostCommentForm()
    user = request.user
    
    return render_to_response('projects/view_project.html', 
                              {'project': project, 'form': form},
                              context_instance=RequestContext(request))

def view_user_project_list(request, user_id):
    requested_user = get_object_or_404(User, pk=user_id)
    projects = \
        Project.objects.filter(creator=requested_user).order_by('-created_at')
    
    return render_to_response('projects/view_user_project_list.html',
                              {'projects': projects,},
                              context_instance=RequestContext(request))

def _make_edit_project_form_from_project(project):
    initial_edit_project_data = {
        'name': project.name,
        'description': project.description,
    }
    
    edit_project_form = EditProjectForm(initial=initial_edit_project_data)
    
    return edit_project_form