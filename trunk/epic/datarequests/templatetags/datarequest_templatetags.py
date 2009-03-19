from django import template
from epic.datarequests.models import DataRequest

register = template.Library()

@register.inclusion_tag('templatetags/display_datarequests.html', takes_context=True)
def display_datarequests(context, datarequests):
	user = context['user']
	return {'datarequests':datarequests, 'user':user}

@register.inclusion_tag("templatetags/display_recent_requests.html")
def display_recent_requests(limit=4):
	datarequests = DataRequest.objects.exclude(status='C').order_by('-created_at')[:limit]
	return {'datarequests':datarequests}

@register.inclusion_tag("templatetags/display_recently_fulfilled_requests.html")
def display_recently_fulfilled_requests(limit=2):
	datarequests = DataRequest.objects.filter(status='F').order_by('-created_at')[:limit]
	return {'datarequests':datarequests}