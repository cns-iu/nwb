from django import template
from epic.datarequests.models import DataRequest

register = template.Library()

@register.inclusion_tag('templatetags/display_datarequests.html', takes_context=True)
def display_datarequests(context, datarequests=None):
	user = context['user']
	if not datarequests:
		datarequests = DataRequest.objects.active().exclude(status='C').order_by('-created_at')
	return {'datarequests':datarequests, 'user':user}

@register.inclusion_tag("templatetags/display_recent_requests.html")
def display_recent_requests(limit=3):
	datarequests = DataRequest.objects.active().exclude(status='C').order_by('-created_at')[:limit]
	return {'datarequests':datarequests}

@register.inclusion_tag("templatetags/display_recently_fulfilled_requests.html")
def display_recently_fulfilled_requests(limit=2):
	datarequests = DataRequest.objects.active().filter(status='F').order_by('-created_at')[:limit]
	return {'datarequests':datarequests}