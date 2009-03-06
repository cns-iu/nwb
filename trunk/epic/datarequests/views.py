from epic.core.models import Item

from epic.datarequests.models import DataRequest
from epic.datarequests.forms import DataRequestForm

from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404

from django.contrib.auth.decorators import login_required

from datetime import datetime


def index(request):
    datarequests = DataRequest.objects.exclude(status='C').order_by('-created_at')
    return render_to_response('datarequests/index.html', {'datarequests': datarequests,'user':request.user})

def view_datarequest(request, datarequest_id=None):
	datarequest = get_object_or_404(DataRequest,pk=datarequest_id)
	return render_to_response('datarequests/view_datarequest.html', {'datarequest':datarequest, 'user':request.user})

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
			datarequest.save()
			return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'datarequest_id':datarequest.id,}))
		else:
			#Form will have errors which will be displayed by the new_datarequest.html page
			pass
	return render_to_response('datarequests/new_datarequest.html', {'form':form, 'user':user,})

@login_required
def edit_datarequest(request, datarequest_id=None):
	user = request.user
	datarequest = get_object_or_404(DataRequest,pk=datarequest_id)
	if datarequest.creator != user:
		#only a request's owner should be able to change it
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'datarequest_id':datarequest.id,}))
	else:
		if request.method != 'POST':
			#User hasn't seen the form yet, so fill in what we know
			form = DataRequestForm(instance=datarequest)
		else:
			form = DataRequestForm(request.POST, instance=datarequest)
			if form.is_valid():
				form.save()
				return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'datarequest_id':datarequest.id,}))
			else:
				#Form will have errors which will be displayed by the new_datarequest.html page
				pass
		return render_to_response('datarequests/edit_datarequest.html', {'form':form, 'user':user, 'datarequest':datarequest})
	
@login_required
def cancel_datarequest(request, datarequest_id=None):
	user = request.user
	datarequest = get_object_or_404(DataRequest,pk=datarequest_id)
	if datarequest.creator != user:
		#only a request's owner should be able to change it
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'datarequest_id':datarequest.id,}))
	else:
		datarequest.cancel()
		datarequest.save()
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'datarequest_id':datarequest.id,}))

@login_required
def fulfill_datarequest(request, datarequest_id=None):
	user = request.user
	datarequest = get_object_or_404(DataRequest,pk=datarequest_id)
	if datarequest.creator != user:
		#only a request's owner should be able to change it
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'datarequest_id':datarequest.id,}))
	else:
		datarequest.fulfill()
		datarequest.save()
		return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'datarequest_id':datarequest.id,}))	