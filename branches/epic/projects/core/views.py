from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.contrib.auth.models import UserManager
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
from epic.core.forms import UserForm
from epic.core.models import Profile
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project


def site_index(request):
    return render_to_response('core/site_index.html', context_instance=RequestContext(request))

def browse(request):
    datasets = DataSet.objects.all().order_by('-created_at')
    return render_to_response('core/browse.html',
        {'datasets': datasets, },
        context_instance=RequestContext(request))

def about (request):
    return render_to_response('core/about.html', context_instance=RequestContext(request))

@login_required
def view_profile(request):
    """ Used to display the basic/home page for logged in user. """
    user = request.user
    profile = Profile.objects.for_user(user)

	#TODO: Go back to original form
    datasets = _get_ordered_users_datasets(user)
    projects = _get_ordered_users_projects(user)
    datarequests = _get_not_canceled_ordered_users_data_requests(user)
    
    render_to_response_data = {
        'datarequests': datarequests,
        'datasets': datasets,
        'profile': profile,
        'projects': projects
    }
    
    # HTML content relating to user, it's profile, datasets uploaded & data
    # requests made is fetched. 
    return render_to_response('core/view_profile.html',
                              render_to_response_data,
                              context_instance=RequestContext(request))
    
@login_required
def edit_profile(request):
    """ Used to allow a user to edit their own profile """
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
    return render_to_response('core/edit_profile.html', {'profile_form':profile_form, 'user_form':user_form,}, context_instance=RequestContext(request))

def logout_view(request):
    logout(request)
    return HttpResponseRedirect(reverse('epic.core.views.site_index',))

@login_required
def change_password(request):
    from django.contrib.auth.views import password_change
    redirect_url = reverse('epic.core.views.view_profile')
    return password_change(request, post_change_redirect=redirect_url, template_name='core/change_password.html')

def forgot_password(request):
    if request.method != 'POST':
        form = ForgotPasswordForm()
    else:
        form = ForgotPasswordForm(request.POST)
        
        if form.is_valid():
            user = form.cleaned_data['user']
            new_random_password = UserManager().make_random_password()
            user.set_password(new_random_password)
            user.save()
            
            # Load a template for the message body and render it to a string
            # to be emailed to the user.
            
            email_body = loader.get_template('core/password_reset_email.html')
            template_context = Context(
                                        {
                                              'first_name': user.first_name,
                                             'last_name': user.last_name,
                                              'username': user.username,
                                              'password': new_random_password,
                                              'login_url': reverse('django.contrib.auth.views.login')
                                          }
                                       )
            
            user.email_user('EpiC Account Password Reset', email_body.render(template_context))
            success_message = "An email has been sent to your '%(truncated_email)s' address with a new password." % {'truncated_email':user.email.split('@')[1]}
            return render_to_response('core/forgot_password_done.html',
                                      {'success_message':success_message,},
                                      context_instance=RequestContext(request))
    
    return render_to_response('core/forgot_password.html', {'form':form,}, context_instance=RequestContext(request))

def _get_ordered_users_datasets(user):
    users_datasets = DataSet.objects.filter(creator=user)
    ordered_users_datasets = users_datasets.order_by('-created_at')
    
    return ordered_users_datasets

def _get_ordered_users_projects(user):
    users_projects = Project.objects.filter(creator=user)
    ordered_users_projects = users_projects.order_by('-created_at')
    
    return ordered_users_projects

def _get_not_canceled_ordered_users_data_requests(user):
    users_datarequests = DataRequest.objects.filter(creator=user)
    not_canceled_users_datarequests = users_datarequests.exclude(status='C')
    ordered_not_canceled_users_datarequests = \
        not_canceled_users_datarequests.order_by('-created_at')
    
    return ordered_not_canceled_users_datarequests
