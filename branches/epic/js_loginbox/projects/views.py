import tempfile
import re
import zipfile

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.servers.basehttp import FileWrapper
from django.core.urlresolvers import reverse
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.forms.util import ErrorList
from django.shortcuts import get_list_or_404
from django.shortcuts import get_object_or_404
from django.shortcuts import render_to_response
from django.template import RequestContext

from epic.comments.forms import PostCommentForm
from epic.core.models import Item
from epic.core.util.view_utils import *
from epic.datasets.models import DataSet
from epic.projects.forms import ProjectForm
from epic.projects.forms import ProjectDataSetFormSet
from epic.projects.models import Project


@login_required
def create_project(request):
    if request.method != 'POST':
        new_project_form = ProjectForm()
        project_datasets = ProjectDataSetFormSet(prefix='project_datasets')
    else:
        project_datasets = ProjectDataSetFormSet(request.POST, 
                                                 prefix='project_datasets')
        new_project_form = ProjectForm(request.POST)
        
        if new_project_form.is_valid() and project_datasets.is_valid():
            name = new_project_form.cleaned_data['name']
            description = new_project_form.cleaned_data['description']
            
            new_project = Project.objects.create(creator=request.user,
                                                 name=name,
                                                 description=description,
                                                 is_active=True)
            
            for dataset_form in project_datasets.forms:
                if dataset_form.is_valid():
                    dataset = dataset_form.cleaned_data['dataset']
                    new_project.datasets.add(dataset)
            
            view_project_url = \
                get_item_url(new_project, 'epic.projects.views.view_project')
            return HttpResponseRedirect(view_project_url)
    
    return render_to_response(
        'projects/create_project.html',
        {'new_project_form': new_project_form,
         'project_datasets': project_datasets},
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
        
        initial_project_datasets = []
        for dataset in project.datasets.all():
            initial_project_datasets.append(
                {'dataset_url': dataset.get_absolute_url()})
            
        project_datasets = ProjectDataSetFormSet(
                                prefix='project_datasets',
                                initial=initial_project_datasets)
    else:
        edit_form = ProjectForm(request.POST)
        project_datasets = ProjectDataSetFormSet(request.POST, 
                                                 prefix='project_datasets')
            
        if edit_form.is_valid() and project_datasets.is_valid():
            project.name = edit_form.cleaned_data['name']
            project.description = edit_form.cleaned_data['description']
            project.save()
            for dataset in project.datasets.all():
                project.datasets.remove(dataset)
            for dataset_form in project_datasets.forms:
                if dataset_form.is_valid():
                    if 'dataset' in dataset_form.cleaned_data:
                        dataset = dataset_form.cleaned_data['dataset']
                        project.datasets.add(dataset)
            view_project_url = \
                get_item_url(project, 'epic.projects.views.view_project')
            return HttpResponseRedirect(view_project_url)
        
    render_to_response_data = {
        'project': project,
        'edit_project_form': edit_form,
        'project_datasets': project_datasets,
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

@login_required
def download_all(request, item_id, slug):
    project = get_object_or_404(Project, pk=item_id)
    
    if project.is_active:
        datasets = project.datasets.all()
        temp = tempfile.TemporaryFile()
        archive = zipfile.ZipFile(temp, 'w')
        
        for dataset in datasets:
            for file in dataset.files.all():
                file.file_contents.open('r')
                archive.writestr(dataset.name + '/' + file.get_short_name(),
                                 file.file_contents.read())
                file.file_contents.close()

        archive.close()
        
        wrapper = FileWrapper(temp)
        
        response = HttpResponse(wrapper, content_type='application/zip')
        response['Content-Disposition'] = 'attachment; filename=%s.zip' % project.name
        response['Content-Length'] = temp.tell()
        
        temp.seek(0)
        return response
    else:
        view_project_url = \
                get_item_url(project, 'epic.projects.views.view_project')
        return HttpResponseRedirect(view_project_url)