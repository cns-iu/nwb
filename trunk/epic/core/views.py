from django.contrib.auth.views import login
from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required, user_passes_test
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
from epic.core.util.view_utils import paginate, request_user_is_authenticated, send_mail_via_system_call
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet, DataSetFile, DataSetDownload
from epic.projects.models import Project, ProjectDownload
from epic.settings import DEACTIVATED_ACCOUNT_VIEW
from xmlrpclib import datetime


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
    is_admin = request.user.is_staff or request.user.is_superuser
    return render_to_response('core/about.html',
                               {'is_admin': is_admin},  
                              context_instance=RequestContext(request),
                              )

@user_passes_test(lambda user: user.is_staff or user.is_superuser)
def stats(request):
    from collections import defaultdict
    from datetime import date
    
    '''The terminology "event" refers to any activity doen my the user gets recorded in the 
    database. For e.g. "downloading a file" is an event which gets recorded, for the most part
    when we refer to event it is a "download activity" in this case.
    '''
    def create_date_to_num_events_dict(query_resultset, date_field):
        '''This method populates the "date, count" dict from the fields in the objects of the 
        resultset of respective query. It also returns aggregation of counts over a month. It 
        works upon the counts of the field directly rather than collecting the elements (which 
        is done in the create_date_to_events_dict method) directly.'''
        date_to_daily_num_events = defaultdict(int)
        date_to_monthly_num_events = defaultdict(int)

        for result in query_resultset:
            date_creation = getattr(result, date_field)
            date_to_daily_num_events[date_creation.date()] += 1
            date_to_monthly_num_events[date(date_creation.year, date_creation.month, 1)] +=1
            
        return date_to_daily_num_events, date_to_monthly_num_events
    
    def create_date_to_events_dict(query_resultset, date_field, element_field):
        date_to_daily_events = defaultdict(set)
        date_to_monthly_events = defaultdict(set)

        for result in query_resultset:
            date_creation = result[date_field]
            date_to_daily_events[date_creation.date()].add(result[element_field])
            date_to_monthly_events[date(date_creation.year, date_creation.month, 1)].add(result[element_field])
            
        return date_to_daily_events, date_to_monthly_events

    def sort_by_date(date_to_num_events):
        '''Used to sort the map according to the key & return a list of 2 element tuples 
        containing key-value pair.'''
        items = date_to_num_events.items()
        items.sort()
        return [(key, value) for key, value in items]
    
    def create_comparative_date_to_num_events(category_1_events, category_2_events):
        '''For certain comparative visualizations like project v/s unique project downloads 
        we require a data structure in the form of <Date, Category 1 count, Category count 2>.
        This method formats the data & returns it.'''
        
        def get_event_count(src_date, date_to_num_events):
            '''This method returns the event count depending upon whether the dict entry for 
            that date exists & whether the value is plain int or a collection of elements. Since 
            this is used repetitively I abstracted this method out.'''
            if src_date in date_to_num_events:
                if type(date_to_num_events[src_date]) == set:
                    return len(date_to_num_events[src_date])
                elif type(date_to_num_events[src_date]) == int:
                    return date_to_num_events[src_date]
            else:
                return 0
            
        unique_dates = set(category_1_events.keys())
        unique_dates.union(category_2_events.keys())
        
        comparative_date_to_num_events = []
        
        for event_date in sorted(unique_dates):
            category_1_num_events = get_event_count(event_date, category_1_events)    
            category_2_num_events = get_event_count(event_date, category_2_events)
            comparative_date_to_num_events.append((event_date, category_1_num_events, category_2_num_events))
        
        return comparative_date_to_num_events

#    Query to get all the user objects and then extract the "registrations over time" data from it.
    users = User.objects.all()
    user_daily_count, user_monthly_count = create_date_to_num_events_dict(users, "date_joined")

#    Query to get all the datarequest objects and then extract the "requests over time" data from it.
    data_requests = DataRequest.objects.all()
    data_req_daily_count, data_req_monthly_count = create_date_to_num_events_dict(data_requests, 
																				  "created_at")

#    Query to get all the datarequest objects and then extract the "requests over time" data from it.
    dataset_files = DataSetFile.objects.all()
    dataset_file_daily_count, dataset_file_monthly_count = create_date_to_num_events_dict(dataset_files, 
																					 	  "uploaded_at")
    
#    Query to get all the datasetdownload objects. This will be used as a base in different extractions.    
    file_downloads = DataSetDownload.objects.all()
    
#    Working off of the base query we excelude all the zip downloads. Since a zip download is actually a 
#    collection of all the files in a particular dataset, hence it is more appropriate in "dataset download" stats
#    & not in the "file download" stat.
    download_file_base_query = file_downloads.exclude(is_download_all=True)    

#    Only consider the files which are NOT readme files & then get the "data file download count"
    download_data_file_query = download_file_base_query.exclude(is_readme=True) 
    download_data_file_daily_count, download_data_file_monthly_count = create_date_to_num_events_dict(download_data_file_query, 
																								 	  "downloaded_at")

#    Only consider the files which are readme files & then get the "readme file download count"
    download_readme_file_query = download_file_base_query.exclude(is_readme=False)
    download_readme_file_daily_count, download_readme_file_monthly_count = create_date_to_num_events_dict(download_readme_file_query, 
																									 "downloaded_at")

#    Query to get "dataset download stats". We are have included only those fields which are useful
#    for building the count dict. 
    dataset_download_base_query = file_downloads.values('parent_dataset', 'downloaded_at', 'is_readme')

#    Query for  Count of (Unique) Datasets from which there was download of any data file over time.
    dataset_data_file_query = dataset_download_base_query.exclude(is_readme=True)
    dataset_data_file_daily_count, dataset_data_file_monthly_count = create_date_to_events_dict(dataset_data_file_query, 
    																						   "downloaded_at", 
    																						   "parent_dataset")

#    Query for Count of (Unique) Datasets from which there was download of any readme file over time. 
    dataset_readme_file_query = dataset_download_base_query.exclude(is_readme=False)
    dataset_readme_file_daily_count, dataset_readme_file_monthly_count = create_date_to_events_dict(dataset_readme_file_query, 
    																							   "downloaded_at", 
    																							   "parent_dataset")

#    Query for Count of Project downloads over time.
    project_downloads = ProjectDownload.objects.all()
    project_downloads_daily_count, project_downloads_monthly_count = create_date_to_num_events_dict(project_downloads, 
																							   		"downloaded_at")

#    Query for Count of Unique project downloads over time.
    distinct_project_downloads = project_downloads.values('parent_project', 'downloaded_at')
    distinct_project_downloads_daily_count, distinct_project_downloads_monthly_count = create_date_to_events_dict(distinct_project_downloads, 
    																											 "downloaded_at", 
    																											 "parent_project")

    return render_to_response('core/stats.html',
                              {'data_req_date_count': [sort_by_date(data_req_daily_count), 
													   sort_by_date(data_req_monthly_count)],
													   
                               'user_date_count': [sort_by_date(user_daily_count), 
												   sort_by_date(user_monthly_count)],
												   
                               'dataset_file_date_count': [sort_by_date(dataset_file_daily_count), 
														   sort_by_date(dataset_file_monthly_count)],
														   
                               'download_file_count': [create_comparative_date_to_num_events(download_data_file_daily_count, 
                                                                                   download_readme_file_daily_count),
                                                       create_comparative_date_to_num_events(download_data_file_monthly_count, 
                                                                                    download_readme_file_monthly_count)],
                                                                                    
                               'download_dataset_count': [create_comparative_date_to_num_events(dataset_data_file_daily_count, 
                                                                                      dataset_readme_file_daily_count),
                                                          create_comparative_date_to_num_events(dataset_data_file_monthly_count, 
                                                                                       dataset_readme_file_monthly_count)],
                                                                                       
                               'download_project_count': [create_comparative_date_to_num_events(project_downloads_daily_count, 
                                                                                      distinct_project_downloads_daily_count),
                                                          create_comparative_date_to_num_events(project_downloads_monthly_count, 
                                                                                       distinct_project_downloads_monthly_count)]
                               },
                              context_instance=RequestContext(request))

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
    send_mail_via_system_call(user.email, 
                              REGISTRATION_EMAIL_SUBJECT,
                              rendered_email)

def _email_user_about_password_changed(request, user, new_password):
    rendered_email = _form_email_about_password_changed(request, user, new_password)
    send_mail_via_system_call(user.email, 
                              'EpiC Account Password Reset',
                              rendered_email)
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
