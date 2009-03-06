from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.contrib.auth.decorators import login_required
from models import Profile

from epic.core.models import Item
from epic.datasets.models import DataSet

@login_required
def index(request):
	user = request.user
	profile = Profile.objects.for_user(user)

	datasets = DataSet.objects.filter(creator=user).order_by('-created_at')
	
	return render_to_response('core/user/index.html', { "profile": profile, "user": user, "datasets":datasets})

@login_required
def change_password(request):
	from django.contrib.auth.views import password_change
	return password_change(request, post_change_redirect="/user/", template_name='core/user/change_password.html')
