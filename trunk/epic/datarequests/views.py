from epic.core.models import Item

from epic.datarequests.models import DataRequest
from epic.datarequests.forms import NewDataRequestForm

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
		form = NewDataRequestForm()
	else:
		form = NewDataRequestForm(request.POST)
		if form.is_valid():
			item_name = form.cleaned_data['item_name']
			item_description = form.cleaned_data['item_description']
			datarequest = DataRequest(creator=user, name=item_name, description=item_description)
			datarequest.save()
			return HttpResponseRedirect(reverse('epic.datarequests.views.view_datarequest', kwargs={'datarequest_id':datarequest.id,}))
		else:
			#Form will have errors which will be displayed by the new_datarequest.html page
			pass
	return render_to_response('datarequests/new_datarequest.html', {'form':form, 'user':user,})