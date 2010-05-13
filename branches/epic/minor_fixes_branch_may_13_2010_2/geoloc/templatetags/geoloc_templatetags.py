from django import template
from django.conf import settings
from django.shortcuts import get_list_or_404
from django.shortcuts import get_object_or_404
from django.shortcuts import render_to_response


from epic.datasets.models import DataSet
from epic.geoloc.models import GeoLoc


register = template.Library()

@register.inclusion_tag('templatetags/location_display_map.html')
def location_display_map(item_id=None):
    if item_id:
        all_locations_list = GeoLoc.objects.filter(datasets=item_id)
    else:
        all_locations_list = GeoLoc.objects.all()
    
    # TODO: Remove locations if they are not associated with a dataset.
    # TODO: Either delete locations or do something cool with filters.
    
    # The issue here is that the locations returned contain locations that are
    # not attached to any datasets.
    # We are not interested in these and so we don't put them in the
    # final list.
    
    # TODO: make a manager which would only return the locations that we'd be
    # interested in...
    
    location_list = []
    
    for location in all_locations_list:
        if location.datasets.all():
            location_list.append(location)
    return {'location_list': location_list,
             'GOOGLE_KEY': settings.GOOGLE_KEY}

@register.inclusion_tag('templatetags/location_edit_map.html')
def location_edit_map(geoloc_add_formset, geoloc_remove_formset):
    return ({'geoloc_add_formset': geoloc_add_formset,
              'geoloc_remove_formset': geoloc_remove_formset,
               'GOOGLE_KEY' : settings.GOOGLE_KEY})