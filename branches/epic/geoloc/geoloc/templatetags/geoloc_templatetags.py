from django import template
from epic.datasets.models import DataSet
from epic.geoloc.models import GeoLoc
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404

register = template.Library()

@register.inclusion_tag("templatetags/display_map_for_datasets.html")
def display_map_for_datasets():
	# TODO: create a manager for geoloc so empty locations are not returned
	all_location_list = GeoLoc.objects.all()
	location_list = []
	for location in all_location_list:
		if location.datasets.all():
			location_list.append(location)
	return {'location_list':location_list,}

@register.inclusion_tag("templatetags/display_map_for_dataset.html")
def display_map_for_dataset(item_id):
	dataset = get_object_or_404(DataSet, pk=item_id)
	location_list = GeoLoc.objects.filter(datasets=item_id)
	
	return {'dataset':dataset, 'location_list':location_list,}

@register.inclusion_tag("templatetags/add_locations_to_dataset_map.html")
def add_locations_to_dataset_map(item_id):
	dataset = get_object_or_404(DataSet, pk=item_id)
	location_list = GeoLoc.objects.filter(datasets=item_id)
	
	return {'dataset':dataset, 'location_list':location_list,}

@register.inclusion_tag("templatetags/add_locations_to_map_for_potential_dataset.html")
def add_locations_to_map_for_potential_dataset():
	return