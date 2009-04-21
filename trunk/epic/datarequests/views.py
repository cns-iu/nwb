from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext
from django.template.defaultfilters import slugify

from epic.comments.forms import PostCommentForm
from epic.comments.models import Comment
from epic.comments.views import make_comment_view
from epic.core.models import Item
from epic.datarequests.forms import DataRequestForm
from epic.datarequests.models import DataRequest

from datetime import datetime


def view_datarequests(request):
    datarequests = DataRequest.objects.exclude(status='C').order_by('-created_at')
    datarequest_form = DataRequestForm()
    return render_to_response('datarequests/view_datarequests.html', {'datarequests': datarequests,'datarequest_form':datarequest_form,}, context_instance=RequestContext(request))

def view_datarequest(request, item_id, slug):
	datarequest = get_object_or_404(DataRequest, pk=item_id)
	form = PostCommentForm()
	user = request.user
	
	return render_to_response("datarequests/view_datarequest.html", 
							  { # This required that view_datarequest MUST NOT pass
							    # in any template arguments but datarequest, user
							    # and postcommentform.
		 				  		"datarequest": datarequest,
						 		"form": form,
							   },
							   context_instance=RequestContext(request))

# This required that view_datarequest MUST NOT pass in any template arguments but
# datarequest, user and postcommentform.
post_datarequest_comment = make_comment_view(
	DataRequest,
	"epic.datarequests.views.view_datarequest",
	"datarequests/view_datarequest.html",
	"datarequest")

@login_required
def new_datarequest(request):
	user = request.user
	if request.method != 'POST':
		#New form needed
		form = DataRequestForm()
	else:
		form = DataRequestForm(request.POST)
		if form.is_valid():
			# The request instance from from.save() would not have an creator since we don't let the user set this
			#   It is therefore necessary that we do not commit and handle setting the creator here. 
			datarequest = form.save(commit=False)
			datarequest.creator = user
			datarequest.slug = slugify(datarequest.name)
			datarequest.save()
			return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':datarequest.id, 'slug':datarequest.slug, }))
		else:
			#Form will have errors which will be displayed by the new_datarequest.html page
			pass
	return render_to_response('datarequests/new_datarequest.html', {'form':form}, context_instance=RequestContext(request))

@login_required
def edit_datarequest(request, item_id, slug):
	user = request.user
	datarequest = get_object_or_404(DataRequest,pk=item_id)
	if datarequest.creator != user:
		#only a request's owner should be able to change it
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':datarequest.id, 'slug':datarequest.slug, }))
	else:
		if request.method != 'POST':
			#User hasn't seen the form yet, so fill in what we know
			form = DataRequestForm(instance=datarequest)
		else:
			form = DataRequestForm(request.POST, instance=datarequest)
			if form.is_valid():
				
				datarequest = form.save(commit=False)
				datarequest.slug = slugify(datarequest.name)  
				datarequest.save()
				
				return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':datarequest.id, 'slug':datarequest.slug, }))
			else:
				#Form will have errors which will be displayed by the new_datarequest.html page
				pass
		return render_to_response('datarequests/edit_datarequest.html', {'form':form, 'datarequest':datarequest}, context_instance=RequestContext(request))
	
@login_required
def cancel_datarequest(request, item_id, slug):
	user = request.user
	datarequest = get_object_or_404(DataRequest,pk=item_id)
	if datarequest.creator != user:
		#only a request's owner should be able to change it
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':datarequest.id, 'slug':datarequest.slug, }))
	else:
		datarequest.cancel()
		datarequest.save()
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':datarequest.id, 'slug':datarequest.slug, }))

@login_required
def fulfill_datarequest(request, item_id, slug):
	user = request.user
	datarequest = get_object_or_404(DataRequest,pk=item_id)
	if datarequest.creator != user:
		#only a request's owner should be able to change it
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':datarequest.id, 'slug':datarequest.slug, }))
	else:
		datarequest.fulfill()
		datarequest.save()
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':datarequest.id, 'slug':datarequest.slug, }))	