from datetime import datetime
from django.core.paginator import Paginator, InvalidPage, EmptyPage
from django.core.urlresolvers import reverse
import functools
import logging
import inspect
import subprocess

def request_user_is_authenticated(request):
    if request.user is not None and request.user.is_authenticated():
        return True

    return False

# user should be an actual User object.
def user_is_item_creator(user, item):
    if user == item.creator or user.id == item.creator.id:
        return True
    
    return False

def get_item_url(item, item_view_function_name):
    item_url_reverse_data = {'item_id': item.id, 'slug': item.slug,}
    item_url = reverse(item_view_function_name, kwargs=item_url_reverse_data)
    
    return item_url

def get_specifics_from_item_ids(model, item_ids):
    specifics = model.objects.filter(is_active=True).\
                              filter(id__in=item_ids)
    
    return specifics

DEFAULT_OBJECTS_PER_PAGE = 10
# http://docs.djangoproject.com/en/1.1/topics/pagination/#using-paginator-in-a-view
def paginate(object_list, params, per_page=DEFAULT_OBJECTS_PER_PAGE, orphans=0):
    paginator = Paginator(object_list, per_page, orphans)
    
    # Make sure page request is an int. If not, deliver first page.
    try:
        page = int(params.get('page', '1'))
    except ValueError:
        page = 1

    # If page request is out of range, deliver last page of results.
    try:
        return paginator.page(page)
    except (EmptyPage, InvalidPage):
        return paginator.page(paginator.num_pages)



def convert_quotes(message_item):
    return message_item.replace('"', "'")

def as_log_message(params):
    return ' '.join(['%s="%s"' % (k, convert_quotes(str(v))) for k, v in params.items()])

UNKNOWN_LOG_VALUE = 'unknown'

def set_if_defined(key, dict1, dict2):
	if key in dict2:
		dict1[key] = dict2[key]
	else:
		dict1[key] = UNKNOWN_LOG_VALUE

def logged_view(view):
    module_logger = logging.getLogger(view.__module__)
	
    def decorated_view(request, *args, **kwargs):
        params = dict()
        
        # Store request and argument information
        params['path'] = request.path
        params['method'] = request.method
        params['user'] = request.user
        params['view_name'] = view.__name__
        
        set_if_defined('REMOTE_ADDR', params, request.META)
        set_if_defined('HTTP_USER_AGENT', params, request.META)
        set_if_defined('HTTP_REFERER', params, request.META)

        for index, arg in enumerate(args):
            params['arg:' + str(index)] = arg
        
        for kwarg, kwval in kwargs.items():
            params['kwarg:' + kwarg] = kwval

        # Process view with timing
        params['request_time'] = datetime.now()
        response = view(request, *args, **kwargs)
        params['response_time'] = datetime.now()
        
        view_timedelta = params['response_time'] - params['request_time']
        params['view_timedelta_microseconds'] = view_timedelta.microseconds

        # Log
        module_logger.info(as_log_message(params))

        return response
    if inspect.ismethod(view.__name__):
        view.__name__ = view.__name__()
        view.func_name = view.__name__
    return functools.update_wrapper(decorated_view, view)

def send_mail_via_system_call(to_email, subject, body, from_email=None):
    '''This method is used to send email using system calls instead of the 
    User.email_user() method provided in User model. We need to do this because
    email_user uses direct email_host & email_port information for sending emails
    but we need to route emails via ssmtp, as suggested by Jon.
    Hence we create system command in the form of -
    
    echo "EMAIL_BODY" | mail -s "EMAIL_SUBJECT" to@email.com
    
    Or, if the from_email is provided then use this foramt,
    echo "EMAIL_BODY" | nail -s "EMAIL_SUBJECT" -r "from_email" to@email.com
    
    '''
    
    echo_body_command_call = subprocess.Popen(
        ['echo', body],
        stderr=subprocess.STDOUT,
        stdout=subprocess.PIPE
     ) 
    
    if from_email:
        mail_command = ['nail', '-s', subject, '-r', '"%s"' % from_email, to_email]
    else:
        mail_command = ['mail', '-s', subject, to_email]
    
    mail_command_call = subprocess.Popen(
        mail_command,
        stdin=echo_body_command_call.stdout,
        stderr=subprocess.STDOUT,
        stdout=subprocess.PIPE
    )