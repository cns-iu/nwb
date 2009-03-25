from django import template
from epic.datasets.models import DataSet
from epic.geoloc.models import GeoLoc
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404

register = template.Library()

@register.inclusion_tag("templatetags/display_map_for_datasets.html")
def display_map_for_datasets(item_id=None):
	
	if item_id:
		all_locations_list = GeoLoc.objects.filter(datasets=item_id)
	else:
		all_locations_list = GeoLoc.objects.all()
	
	# The issue here is that the locations returned contain locations that are not attached to any datasets
	#	We are not interested in these and so we don't put them in the final list.  TODO: make a manager
	#	which would only return the locations that we'd be interested in...
	location_list = []
	for location in all_locations_list:
		if location.datasets.all():
			location_list.append(location)
	
	return {'location_list':location_list,}

@register.inclusion_tag("templatetags/add_locations_to_map_for_dataset.html")
def add_locations_to_map_for_dataset(item_id=None):
	
	if item_id:
		dataset = get_object_or_404(DataSet, pk=item_id)
		location_list = GeoLoc.objects.filter(datasets=item_id)
	else:
		dataset = None
		location_list = None
		
	return {'dataset':dataset, 'location_list':location_list,}