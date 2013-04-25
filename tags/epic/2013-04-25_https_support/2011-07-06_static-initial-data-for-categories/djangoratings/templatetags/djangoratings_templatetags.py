from django import template
from django.core.urlresolvers import reverse

from epic.datasets.models import DataSet


register = template.Library()

@register.inclusion_tag('templatetags/dataset_rating.html')
def dataset_rating(dataset):
	rating = dataset.rating.get_average()
	votes = dataset.rating.votes
    
	return {'rating': rating, 'votes': votes}

@register.inclusion_tag('templatetags/rate_box_javascript.html')
def rate_box_javascript():
	"""
	Always include this template whenever loading 'rating_display_box' 
	template since it contains javascript function required for 
	rating_display_box 
	"""
    
	return {}

@register.inclusion_tag('templatetags/rating_display_box.html')
def rating_display_box(dataset, user):
	baseurl = reverse('epic.datasets.views.rate_dataset',
                      args=[],
                      kwargs={'item_id': dataset.id})
    
	try:
        # TODO: ip_address is kind of a hack.
		user_rating = dataset.rating.get_rating(user=user,
                                                ip_address='127.0.0.1')
	except:
		user_rating = None
    
	rating = dataset.rating.get_average()
	votes = dataset.rating.votes
    
	return {
        'user_rating': user_rating, 
        'rating': rating, 
        'votes': votes, 
        'item_id': dataset.id
    }

@register.inclusion_tag('templatetags/rating_vote_box.html')
def rating_vote_box(dataset, user):
	base_url = reverse('epic.datasets.views.rate_dataset',
                      args=[],
                      kwargs={'item_id': dataset.id})
    # TODO: ip_address is kind of a hack.
	user_rating = dataset.rating.get_rating(user=user, ip_address='127.0.0.1')
	rating = dataset.rating.get_average()
	votes = dataset.rating.votes
	is_authenticated = user.is_authenticated()
    
	return {
        'rating_base_url':base_url, 
        'user_rating':user_rating, 
        'rating':rating, 
        'votes':votes, 
        'item_id': dataset.id,
        'is_authenticated': is_authenticated
    }	