from geopy import geocoders
from django.conf import settings
from decimal import Decimal, getcontext

def get_best_location(location_string):
	g = geocoders.Google(settings.GOOGLE_KEY)
	places = g.geocode(location_string, exactly_one=False)
	try:
		best_guess = places.next()
	except StopIteration:
		raise CouldNotFindLocation, "The location string '%s' could not be resolved to a location" % (location_string)
	places.close()	
	return best_guess

class CouldNotFindLocation(Exception):
	def __init__(self, value):
		self.value = value
	
	def __str__(self):
		return repr(self.value)
	