from django.contrib.auth.models import User
from django.contrib.auth import logout
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404

def index(request):
    return render_to_response('core/index.html', {'user':request.user})

def logout_view(request):
    logout(request)
    return HttpResponseRedirect(reverse('epic.core.views.index',))