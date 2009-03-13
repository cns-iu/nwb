from django import template
from epic.datasets.models import DataSet

register = template.Library()

@register.inclusion_tag('templatetags/show_rating.html')
def show_rating(dataset):
	rating = dataset.rating.get_average()
	votes = dataset.rating.votes
	return {'rating':rating, 'votes':votes}

@register.inclusion_tag('templatetags/rate_box.html')
def rate_box(dataset, user):
	baseurl = "%srate/" % (dataset.get_absolute_url())
	user_rating = dataset.rating.get_rating(user=user, ip_address='127.0.0.1') #TODO: ip_address is kind of a hack
	return {'baseurl':baseurl, 'user_rating':user_rating}