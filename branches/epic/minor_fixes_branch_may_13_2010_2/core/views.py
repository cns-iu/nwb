from django.contrib.auth.views import login
from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.contrib.auth.models import UserManager
from django.contrib.auth.views import password_change
from django.core.urlresolvers import reverse
from django.forms.formsets import formset_factory
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.shortcuts import get_object_or_404
from django.shortcuts import render_to_response
from django.template import Context
from django.template import loader
from django.template import RequestContext
from django.utils import simplejson

from epic.core.forms import ForgotPasswordForm
from epic.core.forms import ProfileForm
from epic.core.forms import RegistrationForm
from epic.core.forms import UserForm
from epic.core.models import Author
from epic.core.models import Profile
from epic.core.util import active_user_required
from epic.core.util.view_utils import paginate, request_user_is_authenticated
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project
from epic.settings import DEACTIVATED_ACCOUNT_VIEW


def site_index(request):
    datarequests = DataRequest.objects.active().order_by('-created_at')[:2]
    datasets = DataSet.objects.active().order_by('-created_at')[:2]
	
    return render_to_response(
        'core/site_index.html',
        {'datarequests': datarequests, 'datasets': datasets},
	    context_instance=RequestContext(request))

def terms_and_conditions(request):
    return render_to_response(
        'core/terms_and_conditions.html', context_instance=RequestContext(request))

def privacy_policy(request):
    return render_to_response('core/privacy_policy.html', context_instance=RequestContext(request))

def browse(request):
    datarequests = DataRequest.objects.active().order_by('-created_at')[:3]
    datasets = DataSet.objects.active().order_by('-created_at')[:3]
    projects = Project.objects.active().order_by('-created_at')[:3]
    
    return render_to_response(
        'core/browse.html',
        {'datarequests': datarequests, 'datasets': datasets, 'projects': projects},
        context_instance=RequestContext(request))

def about(request):
    return render_to_response('core/about.html', context_instance=RequestContext(request))

def view_items_for_author(request, author_name):
    author = Author.objects.get(author=author_name)
    items = author.items.all()

    return render_to_response(
        'authors/view_items_for_author.html',
        {'items': items, 'author': author},
        context_instance=RequestContext(request))

@login_required
@active_user_required
def view_profile(request, user_id=None):
    """ Used to display the basic/home page for logged in user. """

    requesting_user = request.user

    if user_id:
        requested_user = get_object_or_404(User, pk=user_id)
    else:
        requested_user = requesting_user
    
    profile = Profile.objects.for_user(requested_user)

    datasets = DataSet.objects.active().filter(creator=requested_user).order_by('-created_at')
    projects = Project.objects.active().filter(creator=requested_user).order_by('-created_at')
    datarequests = \
        DataRequest.objects.active(). \
        filter(creator=requested_user). \
        exclude(status='C'). \
        order_by('-created_at')
    
    render_to_response_data = {
        'datarequests': datarequests,
        'datasets': datasets,
        'profile': profile,
        'projects': projects
    }
    
    return render_to_response(
        'core/view_profile.html',
        render_to_response_data,
        context_instance=RequestContext(request))
    
@login_required
@active_user_required
def edit_profile(request):
    """ Used to allow a user to edit their own profile."""
    
    user = request.user
    profile = Profile.objects.for_user(user)
    
    if request.method != 'POST':
        profile_form = ProfileForm(instance=profile)
        user_form = UserForm(instance=user)
    else:
        profile_form = ProfileForm(request.POST, instance=profile)
        user_form = UserForm(request.POST, instance=user)
        
        if profile_form.is_valid() and user_form.is_valid():
            profile_form.save()
            user_form.save()
            
            return HttpResponseRedirect(reverse('epic.core.views.view_profile', kwargs={}))
    
    return render_to_response(
        'core/edit_profile.html',
        {'profile_form': profile_form, 'user_form': user_form,},
        context_instance=RequestContext(request))

def logout_view(request):
    logout(request)
    
    return HttpResponseRedirect(reverse('epic.core.views.site_index',))

@login_required
@active_user_required
def change_password(request):
    redirect_url = reverse('epic.core.views.view_profile')
    
    return password_change(
        request,
        post_change_redirect=redirect_url,
        template_name='core/change_password.html')

def forgot_password(request):
    if request.method != 'POST':
        form = ForgotPasswordForm()
    else:
        form = ForgotPasswordForm(request.POST)
        
        if form.is_valid():
            user = form.cleaned_data['user']

            if not user.is_active:
                return HttpResponseRedirect(reverse(DEACTIVATED_ACCOUNT_VIEW))

            new_password = UserManager().make_random_password()
            user.set_password(new_password)
            user.save()
            
            success_message = _email_user_about_password_changed(request, user, new_password)
            
            return render_to_response(
                'core/forgot_password_done.html',
                {'success_message': success_message,},
                context_instance=RequestContext(request))
    
    return render_to_response(
        'core/forgot_password.html', {'form': form,}, context_instance=RequestContext(request))

REGISTRATION_FORM_NAME = 'form'

def register(request):
    if request_user_is_authenticated(request):
        return HttpResponseRedirect(reverse('epic.core.views.view_profile',))

    if request.method == 'POST':
        form = RegistrationForm(request.POST)
        
        if form.is_valid():
            email = form.cleaned_data['email']
            username = form.cleaned_data['username']
            password = form.cleaned_data['password']
            first_name = form.cleaned_data['first_name']
            last_name = form.cleaned_data['last_name']
            affiliation = form.cleaned_data['affiliation']

            user = User.objects.create_user(email=email, username=username, password=password)
            user.first_name = first_name
            user.last_name = last_name
            user.is_active = False
            user.save()

            profile = Profile.objects.for_user(user)
            profile.affiliation = affiliation
            profile.save()
            
            _email_user_about_registration(request, user, profile)

            return render_to_response(
                'core/registration_complete.html', context_instance=RequestContext(request))
        else:
            return render_to_response(
                'core/register.html',
                {REGISTRATION_FORM_NAME: form},
                context_instance=RequestContext(request))
    else:
        form = RegistrationForm()

        return render_to_response(
            'core/register.html',
            {REGISTRATION_FORM_NAME: form},
            context_instance=RequestContext(request))

def activate(request, activation_key):
    profile = get_object_or_404(Profile, activation_key=activation_key)

    if not profile.has_activated_account:
        profile.user.is_active = True
        profile.user.save()
        profile.has_activated_account = True
        profile.save()

        return render_to_response('core/activate.html', context_instance=RequestContext(request))
    else:
        if profile.user.is_active:
            if profile.user.is_authenticated():
                return HttpResponseRedirect(reverse('epic.core.views.view_profile'))
            else:
                return HttpResponseRedirect(reverse('django.contrib.auth.views.login',))
        else:
            return HttpResponseRedirect(reverse(DEACTIVATED_ACCOUNT_VIEW))

def deactivated_account(request):
    return render_to_response(
        'core/deactivated_account.html', context_instance=RequestContext(request))

REGISTRATION_EMAIL_SUBJECT = 'EpiC Account Registration'

def _email_user_about_registration(request, user, profile):
    rendered_email = form_email_about_registration(request, user, profile)
    user.email_user(REGISTRATION_EMAIL_SUBJECT, rendered_email)

def _email_user_about_password_changed(request, user, new_password):
    rendered_email = _form_email_about_password_changed(request, user, new_password)
    user.email_user('EpiC Account Password Reset', rendered_email)
    
    success_message = _form_success_message(user)
    
    return success_message

def form_email_about_registration(request, user, profile):
    email_body = loader.get_template('core/registration_email.txt')
    activation_url = request.build_absolute_uri(
        reverse('epic.core.views.activate', kwargs={'activation_key': profile.activation_key}))
    login_url = request.build_absolute_uri(reverse('django.contrib.auth.views.login'))

    template_context_data = {
        'user': user, 'activation_url': activation_url, 'login_url': login_url
    }
    template_context = Context(template_context_data)
    rendered_email = email_body.render(template_context)

    return rendered_email

def _form_email_about_password_changed(request, user, new_password):
    email_body = loader.get_template('core/password_reset_email.txt')
    login_url = request.build_absolute_uri(reverse('django.contrib.auth.views.login'))
    
    # TODO: Probably not the best security to be sending a plaintext password.
    template_context_data = {
        'first_name': user.first_name,
        'last_name': user.last_name,
        'username': user.username,
        'password': new_password,
        'login_url': login_url,
    }
    
    template_context = Context(template_context_data)
    rendered_email = email_body.render(template_context)
    
    return rendered_email

def _form_success_message(user):
    split_user_email = user.email.split('@')
    user_email_domain = split_user_email[1]
    
    success_message = \
        ("An email has been sent to your '%(email_domain)s' address with a new password.") % \
            {'email_domain': user_email_domain,}
    
    return success_message
