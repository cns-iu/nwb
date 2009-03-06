from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.contrib.auth.decorators import login_required
from models import Profile

@login_required
def index(request):
	user = request.user
	try:
		profile = user.get_profile()
	except:
		profile = Profile(user=user)
		profile.save()
	
	return render_to_response('core/user/index.html', { "profile": profile, "user": user })

@login_required
def change_password(request):
	from django.contrib.auth.views import password_change
	return password_change(request, post_change_redirect="/user/", template_name='core/user/change_password.html')
