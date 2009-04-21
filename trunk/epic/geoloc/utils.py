from geopy import geocoders
from django.conf import settings
from decimal import Decimal, getcontext

def parse_geolocation(location):
    """Return a dictionary containing the lat, lng, and canonical_name parsed from a string 
    representation of a geoloc.
    
    Arguments:
    location -- the string representation of a location.  Should be in the form [[-#.#, -#.#], '*', ...
    
    """
    
    import re
    # This pattern will match on [[-#.#, -#.#], '*', *
    location_pattern = re.compile(r"""^\[\[(?P<lat>-?\d+\.\d+), (?P<lng>-?\d+\.\d+)\], '(?P<name>.+?)',(?:.*)""")
    location_match = location_pattern.match(location)
    location_dict = location_match.groupdict()
    lat = location_dict['lat']
    lng = location_dict['lng']
    canonical_name = unicode(location_dict['name'])
    
    return {'lat': lat, 'lng': lng, 'canonical_name': canonical_name,}

def get_best_location(location_string):
	geocoder = geocoders.Google(settings.GOOGLE_KEY)
	places = geocoder.geocode(location_string, exactly_one=False)
	try:
		first_returned_location = places.next()
	except StopIteration:
		raise CouldNotFindLocation, "The location string '%s' could not be resolved to a location" % (location_string)
	places.close()	
	return first_returned_location

class CouldNotFindLocation(Exception):
	def __init__(self, value):
		self.value = value
	
	def __str__(self):
		return repr(self.value)
	