from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext
from epic.comments.forms import PostCommentForm
from epic.core.models import Item
from epic.core.util import active_user_required
from epic.core.util.view_utils import *
import epic.core.util.view_utils
from epic.datarequests.forms import DataRequestForm
from epic.datarequests.models import DataRequest
from epic.tags.models import Tagging

PER_PAGE = epic.core.util.view_utils.DEFAULT_OBJECTS_PER_PAGE
def view_datarequests(request):
    datarequests = \
        DataRequest.objects.active().exclude(status='C').order_by('-created_at')
    
    datarequests_page = paginate(datarequests, request.GET, PER_PAGE)
    
    return render_to_response(
        'datarequests/view_datarequests.html', 
        {'datarequests_page': datarequests_page},
        context_instance=RequestContext(request))

def view_datarequest(request, item_id, slug):
    datarequest = get_object_or_404(DataRequest, pk=item_id)
    form = PostCommentForm()
    user = request.user
    
    return render_to_response('datarequests/view_datarequest.html', 
                              {'datarequest': datarequest, 'form': form},
                               context_instance=RequestContext(request))

@login_required
@active_user_required
def new_datarequest(request):
    user = request.user
    if request.method != 'POST':
        form = DataRequestForm()
    else:
        form = DataRequestForm(request.POST)
        
        if form.is_valid():
            datarequest = form.save(commit=False)
            datarequest.creator = user
            datarequest.is_active = True
            datarequest.save()
            
            tag_names = form.cleaned_data['tags']
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
@active_user_required
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
                datarequest.save()
                
                view_datarequest_url = get_item_url(
                    datarequest, 'epic.datarequests.views.view_datarequest')
                
                return HttpResponseRedirect(view_datarequest_url)
        
        return render_to_response('datarequests/edit_datarequest.html',
                                  {'form': form, 'datarequest': datarequest},
                                  context_instance=RequestContext(request))
    
@login_required
@active_user_required
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
@active_user_required
def fulfill_datarequest(request, item_id, slug, fulfilling_item_id=None):
    user = request.user
    datarequest = get_object_or_404(DataRequest, pk=item_id)
    
    # Only a request's owner should be able to change it.
    if datarequest.creator != user:
        view_datarequest_url = get_item_url(
            datarequest, 'epic.datarequests.views.view_datarequest')
        
        return HttpResponseRedirect(view_datarequest_url)
    elif fulfilling_item_id:
        fulfilling_item = get_object_or_404(Item, pk=fulfilling_item_id)
        datarequest.fulfilling_item = fulfilling_item
        datarequest.fulfill()
        datarequest.save()  
    else:
        datarequest.fulfill()
        datarequest.save()
        
    view_datarequest_url = get_item_url(
            datarequest, 'epic.datarequests.views.view_datarequest')
        
    return HttpResponseRedirect(view_datarequest_url)

@login_required
@active_user_required
def choose_fulfilling_item(request, fulfilling_item_id):
    user = request.user
    item = get_object_or_404(Item, pk=fulfilling_item_id)
    requests = DataRequest.objects.active(). \
        filter(status='U', creator=user).order_by('-created_at')
    
    return render_to_response('datarequests/choose_fulfilling_item.html',
                              {'item': item,
                               'specific': item.specific,
                               'requests': requests},
                               context_instance=RequestContext(request))