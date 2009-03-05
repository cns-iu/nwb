from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.contrib.auth.decorators import login_required
from models import Profile

@login_required
def index(request):
	user = request.user
	profile = Profile.objects.get(user=user)
	return render_to_response('core/user/index.html', { "profile": profile })