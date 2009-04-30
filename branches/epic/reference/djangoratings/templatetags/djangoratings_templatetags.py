from django import template
from django.core.urlresolvers import reverse
from epic.datasets.models import DataSet

register = template.Library()

@register.inclusion_tag('templatetags/show_rating.html')
def show_rating(dataset):
	rating = dataset.rating.get_average()
	votes = dataset.rating.votes
	return {'rating':rating, 'votes':votes}

@register.inclusion_tag('templatetags/rate_box_view_helper.html')
def rate_box_view_helper():
	"""
	Always include this template whenever loading 'rate_box_view' 
	template since it contains javascript function required for 
	rate_box_view 
	"""
	return

@register.inclusion_tag('templatetags/rate_box_view.html')
def rate_box_view(dataset, user):
	baseurl = reverse("epic.datasets.views.rate_dataset", args=[], kwargs={'item_id': dataset.id})
	try:
		user_rating = dataset.rating.get_rating(user=user, ip_address='127.0.0.1') #TODO: ip_address is kind of a hack
	except:
		user_rating = None
	rating = dataset.rating.get_average()
	votes = dataset.rating.votes
	return {
			'user_rating':user_rating, 
			'rating':rating, 
			'votes':votes, 
			'item_id': dataset.id
			}
	
@register.inclusion_tag('templatetags/rate_box_vote.html')
def rate_box_vote(dataset, user):
	baseurl = reverse("epic.datasets.views.rate_dataset", args=[], kwargs={'item_id': dataset.id})
	user_rating = dataset.rating.get_rating(user=user, ip_address='127.0.0.1') #TODO: ip_address is kind of a hack
	rating = dataset.rating.get_average()
	votes = dataset.rating.votes
	is_authenticated = user.is_authenticated()
	return {
			'ratebaseurl':baseurl, 
			'user_rating':user_rating, 
			'rating':rating, 
			'votes':votes, 
			'item_id': dataset.id,
			'is_authenticated': is_authenticated
			}	