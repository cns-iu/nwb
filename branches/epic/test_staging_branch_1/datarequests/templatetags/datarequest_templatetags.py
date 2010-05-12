from django import template

from epic.datarequests.models import DataRequest


register = template.Library()

@register.inclusion_tag('templatetags/datarequest_list.html', takes_context=True)
def datarequest_list(context, datarequests=None):
    return {'datarequests': datarequests,
		    'user': context['user']}

@register.inclusion_tag('templatetags/datarequest_list_short.html',
                        takes_context=True)
def datarequest_list_short(context, datarequests=None):
    return {'datarequests': datarequests, 'user': context['user']}

@register.inclusion_tag('templatetags/datarequest_list_tiny.html',
                        takes_context=True)
def datarequest_list_tiny(context, datarequests=None):
    return {'datarequests': datarequests, 'user': context['user']}

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
