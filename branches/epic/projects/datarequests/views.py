from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext
from django.template.defaultfilters import slugify

from epic.comments.forms import PostCommentForm
from epic.core.models import Item
from epic.core.util.view_utils import *
from epic.datarequests.forms import DataRequestForm
from epic.datarequests.models import DataRequest
from epic.tags.models import Tagging


def view_datarequests(request):
    datarequests = \
        DataRequest.objects.active().exclude(status='C').order_by('-created_at')
    datarequest_form = DataRequestForm()
    
    return render_to_response(
        'datarequests/view_datarequests.html', 
        {'datarequests': datarequests, 'datarequest_form': datarequest_form,}, 
        context_instance=RequestContext(request))

def view_datarequest(request, item_id, slug):
    datarequest = get_object_or_404(DataRequest, pk=item_id)
    form = PostCommentForm()
    user = request.user
    
    return render_to_response('datarequests/view_datarequest.html', 
                              {'datarequest': datarequest, 'form': form},
                               context_instance=RequestContext(request))

@login_required
def new_datarequest(request):
    user = request.user
    if request.method != 'POST':
        form = DataRequestForm()
    else:
        form = DataRequestForm(request.POST)
        
        if form.is_valid():
            # The request instance from from.save() would not have an creator
            # since we don't let the user set this.  It is therefore necessary
            # that we do not commit and handle setting the creator here. 
            datarequest = form.save(commit=False)
            datarequest.creator = user
            datarequest.slug = slugify(datarequest.name)
            datarequest.is_active = True
            datarequest.save()
            
            tag_names = form.cleaned_data["tags"]
            Tagging.objects.update_tags(tag_names=tag_names, 
                                        item=datarequest, 
                                        user=user)
            
            view_datarequest_url = get_item_url(
                datarequest, 'epic.datarequests.views.view_datarequest')
            
            return HttpResponseRedirect(view_datarequest_url)
    return render_to_response('datarequests/new_datarequest.html', 
                              {'form': form}, 
                              context_instance=RequestContext(request))

@login_required
def edit_datarequest(request, item_id, slug):
    user = request.user
    datarequest = get_object_or_404(DataRequest, pk=item_id)
    
    if datarequest.creator != user:
        view_datarequest_url = get_item_url(
            datarequest, 'epic.datarequests.views.view_datarequest')
        
        return HttpResponseRedirect(view_datarequest_url)
    else:
        if request.method != 'POST':
            current_tags = \
                Tagging.objects.get_edit_string(item=datarequest, user=user)
            
            form = DataRequestForm(
                instance=datarequest, initial={'tags': current_tags})
        else:
            form = DataRequestForm(request.POST, instance=datarequest)
            
            if form.is_valid():
                tag_names = form.cleaned_data["tags"]
                Tagging.objects.update_tags(tag_names=tag_names, 
                                        item=datarequest, 
                                        user=user)
                datarequest = form.save(commit=False)
                datarequest.slug = slugify(datarequest.name)  
                datarequest.save()
                
                view_datarequest_url = get_item_url(
                    datarequest, 'epic.datarequests.views.view_datarequest')
                
                return HttpResponseRedirect(view_datarequest_url)
        
        return render_to_response('datarequests/edit_datarequest.html', 
                                  {'form': form, 'datarequest': datarequest},
                                  context_instance=RequestContext(request))
    
@login_required
def cancel_datarequest(request, item_id, slug):
    user = request.user
    datarequest = get_object_or_404(DataRequest, pk=item_id)
    
    if datarequest.creator != user:
        view_datarequest_url = get_item_url(
            datarequest, 'epic.datarequests.views.view_datarequest')
        
        return HttpResponseRedirect(view_datarequest_url)
    else:
        datarequest.cancel()
        datarequest.save()
        
        view_datarequest_url = get_item_url(
            datarequest, 'epic.datarequests.views.view_datarequest')
        
        return HttpResponseRedirect(view_datarequest_url)

@login_required
def fulfill_datarequest(request, item_id, slug):
    user = request.user
    datarequest = get_object_or_404(DataRequest, pk=item_id)
    
    if datarequest.creator != user:
        view_datarequest_url = get_item_url(
            datarequest, 'epic.datarequests.views.view_datarequest')
        
        return HttpResponseRedirect(view_datarequest_url)
    else:
        datarequest.fulfill()
        datarequest.save()
        
        view_datarequest_url = get_item_url(
            datarequest, 'epic.datarequests.views.view_datarequest')
        
        return HttpResponseRedirect(view_datarequest_url)
