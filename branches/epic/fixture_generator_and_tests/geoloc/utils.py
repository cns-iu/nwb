from geopy import geocoders
from django.conf import settings
from decimal import Decimal, getcontext

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
	