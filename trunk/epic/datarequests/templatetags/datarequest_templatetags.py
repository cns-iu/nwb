from django import template

from epic.datarequests.models import DataRequest


register = template.Library()

@register.inclusion_tag('templatetags/datarequest_list.html', 
                        takes_context=True)
def datarequest_list(context, datarequests=None):
    user = context['user']
    
    if not datarequests:
        datarequests = DataRequest.objects.active().exclude(status='C'). \
            order_by('-created_at')
    
    return {'datarequests': datarequests, 'user': user}

@register.inclusion_tag('templatetags/recent_requests.html',
                        takes_context=True)
def recent_requests(context, limit=3, use_simple_display=False):
    user = context['user']
    
    # (Because you can't pass a boolean from a template . . .)
    if use_simple_display == 'use simple display':
        use_simple_display = True
    
    datarequests = DataRequest.objects.active().exclude(status='C'). \
        order_by('-created_at')[:limit]
    
    return {
        'datarequests': datarequests,
        'user': user,
        'use_simple_display': use_simple_display
    }

@register.inclusion_tag('templatetags/recently_fulfilled_requests.html',
                        takes_context=True)
def recently_fulfilled_requests(context, limit=2):
    user = context['user']
    datarequests = DataRequest.objects.active().filter(status='F'). \
        order_by('-created_at')[:limit]
    
    return {'datarequests':datarequests, 'user':user}

@register.inclusion_tag('templatetags/fulfills_request_button.html',
                        takes_context=True)
def fulfills_request_button(context, dataset):
    user = context['user']
    datarequests = DataRequest.objects.active().filter(status='U'). \
        filter(creator=user.id)
    
    return {'dataset': dataset, 'user': user, 'datarequests': datarequests,}
