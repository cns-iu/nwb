from epic.core.models import Item

from epic.datarequests.models import DataRequest

from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404

from django.contrib.auth.decorators import login_required

from datetime import datetime


def index(request):
    datarequests = DataRequest.objects.all().order_by('-created_at')
    return render_to_response('datarequests/index.html', {'datarequests': datarequests,'user':request.user})

def view_datarequest(request, datarequest_id):
	datarequest = get_object_or_404(DataRequest,pk=datarequest_id)
	return render_to_response('datarequests/view_datarequest.html', {'datarequest':datarequest, 'user':request.user})