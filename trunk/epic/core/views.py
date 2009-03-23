from django.template import RequestContext, Context, loader
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render_to_response, get_object_or_404
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User, UserManager
from django.contrib.auth import logout
from django.utils import simplejson
from django.forms.formsets import formset_factory

from epic.datasets.models import DataSet
from epic.datarequests.models import DataRequest

from models import Profile
from forms import ForgotUsernameForm, ForgotEmailForm, ForgotPasswordForm, ProfileForm, UserForm

# This is the index for the entire site
def index(request):
	datasets = DataSet.objects.all().order_by('-created_at')
	return render_to_response('core/index.html',
							  {'user':request.user, 'datasets':datasets,},
							  context_instance=RequestContext(request))

@login_required
def view_profile(request):
	''' Used to display the basic/home page for logged in user. '''
	user = request.user
	profile = Profile.objects.for_user(user)

	datasets = DataSet.objects.filter(creator=user).order_by('-created_at')
	
	datarequests = DataRequest.objects.filter(creator=user).exclude(status='C').order_by('-created_at')
	
# 	HTML content relating to user, it's profile, datasets uploaded & data requests made 
#	is fetched. 
	return render_to_response('core/view_profile.html', 
								{ 
									"profile": profile, 
									"user": user, 
									"datasets":datasets, 
									"datarequests":datarequests 
								}
							)
@login_required
def edit_profile(request):
	''' Used to allow a user to edit their own profile '''
	# Get the user and profile objects.
	user = request.user
	profile = Profile.objects.for_user(user)
	if request.method != 'POST':
		# New form needed, set the fields to their current values
		profile_form = ProfileForm(instance=profile)
		user_form = UserForm(instance=user)
	else:
		# Create the form based on the filled out fields.  Include the old instance to get the required fields that we will not be showing, (e.g. the user)
		profile_form = ProfileForm(request.POST, instance=profile)
		user_form = UserForm(request.POST, instance=user)
		# Check to make sure that all the fields were filled out correctly
		if profile_form.is_valid() and user_form.is_valid():
			# Save the profile
			profile_form.save()
			user_form.save()
			return HttpResponseRedirect(reverse('epic.core.views.view_profile', kwargs={}))
		else:
			# Form will have errors which will be displayed by page
			pass
	return render_to_response('core/edit_profile.html', {'profile_form':profile_form, 'user_form':user_form, 'user':user,})

def logout_view(request):
	logout(request)
	return HttpResponseRedirect(reverse('epic.core.views.index',))

@login_required
def change_password(request):
	from django.contrib.auth.views import password_change
	return password_change(request, post_change_redirect="/user/", template_name='core/change_password.html')


def forgot_username(request):
	FORGOT_USERNAME_TEMPLATE = "core/forgot_username.html"
	
	if request.method == "GET":
		next_page = request.GET.get("next", "")
	else:
		next_page = ""
	
	if request.user.is_authenticated():
		# User is logged in, so just give them their username.
		return render_to_response(FORGOT_USERNAME_TEMPLATE, {
			"username": request.user.username,
			"next": next_page
		})
	
	if request.method != "POST":
		forgot_username_form = ForgotUsernameForm()
	else:
		forgot_username_form = ForgotUsernameForm(request.POST)
		
	if forgot_username_form.is_valid():
		user_email = forgot_username_form.cleaned_data["email"]
		
		try:
			user = User.objects.get(email=user_email)
		except:
			# No username could be found.
			return render_to_response(FORGOT_USERNAME_TEMPLATE, {
				"not_found": True,
				"form": forgot_username_form,
				"next": next_page,
				"email": user_email
			})
		
		# Username could be found (success).
		return render_to_response(FORGOT_USERNAME_TEMPLATE, {
			"username": user.username,
			"next": next_page
		})
	
	# No GET or POST data, so display the form (for the first time).
	return render_to_response(FORGOT_USERNAME_TEMPLATE, {
		"form": forgot_username_form,
		"next": next_page
	})

def forgot_email(request):
	FORGOT_EMAIL_TEMPLATE = "core/forgot_email.html"
	
	if request.method == "GET":
		next_page = request.GET.get("next", "")
	else:
		next_page = ""
	
	if request.user.is_authenticated():
		# User is logged in, so just give them their email.
		return render_to_response(FORGOT_EMAIL_TEMPLATE, {
			"email": request.user.email,
			"next": next_page
		})
	
	if request.method != "POST":
		forgot_email_form = ForgotEmailForm()
	else:
		forgot_email_form = ForgotEmailForm(request.POST)
		
	if forgot_email_form.is_valid():
		username = forgot_email_form.cleaned_data["username"]
		
		try:
			user = User.objects.get(username=username)
		except:
			# No email could be found.
			return render_to_response(FORGOT_EMAIL_TEMPLATE, {
				"not_found": True,
				"form": forgot_email_form,
				"next": next_page,
				"username": username
			})
		
		# Email could be found (success).
		return render_to_response(FORGOT_EMAIL_TEMPLATE, {
			"email": user.email,
			"next": next_page
		})
	
	# No GET or POST data, so display the form (for the first time).
	return render_to_response(FORGOT_EMAIL_TEMPLATE, {
		"form": forgot_email_form,
		"next": next_page
	})

def forgot_password(request):
	FORGOT_PASSWORD_TEMPLATE = "core/forgot_password.html"
	
	if request.method == "GET":
		next_page = request.GET.get("next", "")
	else:
		next_page = ""
	
	if request.method != "POST":
		forgot_password_form = ForgotPasswordForm()
	else:
		forgot_password_form = ForgotPasswordForm(request.POST)
	
	if forgot_password_form.is_valid():
		username_or_email = forgot_password_form.cleaned_data["username_or_email"]
		
		try:
			if username_or_email.count("@") > 0:
				username = ""
				email = username_or_email
				user = User.objects.get(email=username_or_email)
			else:
				username = username_or_email
				email = ""
				user = User.objects.get(username=username_or_email)
		except:
			# No username could be found.
			return render_to_response(FORGOT_PASSWORD_TEMPLATE, {
				"not_found": True,
				"form": forgot_password_form,
				"next": next_page,
				"username": username,
				"email": email
			})
		
		username = user.username
		email = user.email
		
		# Username/email could be found (success).
		
		# Reset the password.
		new_random_password = UserManager().make_random_password()
		user.set_password(new_random_password)
		user.save()
		
		# Load a template for the message body and render it (to a string).
		email_body_template = loader.get_template("core/password_reset.html")
		template_context = Context({
			"first_name": user.first_name,
			"last_name": user.last_name,
			"username": user.username,
			"password": new_random_password,
			"login_url": "http://localhost:8000/login/"
		})
		
		# Email the user with the new password.
		user.email_user("EpiC Account Password Reset", email_body_template.render(template_context))
		
		# Display the page confirming to the user that their password was reset.
		return render_to_response(FORGOT_PASSWORD_TEMPLATE, {
			"username": username,
			"email": email,
			"next": next_page
		})
	
	# No GET or POST data, so display the form (for the first time).
	return render_to_response(FORGOT_PASSWORD_TEMPLATE, {
		"form": forgot_password_form,
		"next": next_page
	})