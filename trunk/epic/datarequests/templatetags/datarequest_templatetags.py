from django import template
from epic.datarequests.models import DataRequest

register = template.Library()

@register.inclusion_tag('templatetags/display_datarequests.html', 
                        takes_context=True)
def display_datarequests(context, datarequests=None):
    user = context['user']
    if not datarequests:
        datarequests = DataRequest.objects.active().exclude(status='C').order_by('-created_at')
    return {'datarequests':datarequests, 'user':user}

@register.inclusion_tag('templatetags/recent_requests.html',
                        takes_context=True)
def recent_requests(context, limit=3, show_tags=True):
    user = context['user']
    if show_tags == 'False': #(Because you can't pass a boolean from a template)
        show_tags = False
    datarequests = DataRequest.objects.active().exclude(status='C').order_by('-created_at')[:limit]
    return {'datarequests':datarequests, 'user':user, 'show_tags':show_tags}

@register.inclusion_tag(
    "templatetags/display_recently_fulfilled_requests.html", 
    takes_context=True)
def display_recently_fulfilled_requests(context, limit=2):
    user = context['user']
    datarequests = DataRequest.objects.active().filter(status='F').order_by('-created_at')[:limit]
    return {'datarequests':datarequests, 'user':user}

@register.inclusion_tag('templatetags/fulfills_request_button.html',
                        takes_context=True)
def fulfills_request_button(context, dataset):
    user = context['user']
    datarequests = DataRequest.objects.active().filter(status='U').filter(creator=user.id)
    return {'dataset':dataset, 'user':user, 'datarequests':datarequests}