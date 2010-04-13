from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User, UserManager
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404
from django.template import RequestContext, Context, loader
from django.utils import simplejson
from epic.geoloc.models import GeoLoc
from epic.geoloc.utils import get_best_location, CouldNotFindLocation
from geopy.geocoders.base import GeocoderError, GeocoderResultError



def geoloc_get_best_location(request):	
	responseData = {}
	
	if 'location_string' not in request.POST:
		responseData['failure'] = "No location_string given"
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')
	
	location_string = request.POST['location_string']
		
	# Get the best location and return it and a success message or return a failure
	try:
		# TODO: does the location_string need to be cleaned somehow?
		canonical_name, (lat, lng) = get_best_location(location_string)
		
		responseData['lng'] = lng
		responseData['lat'] = lat
		responseData['canonical_name'] = canonical_name
		responseData['success'] = 'A marker was added for %s' % canonical_name
		
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')
	
	except (GeocoderResultError, CouldNotFindLocation):
		responseData['failure'] = "'%s' could not be resolved to a location" % location_string
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')
	except GeocoderError:
		# TODO This represents a library problem and should be logged.
		responseData['failure'] = "Problem resolving '%s' to a location" % location_string
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')
	except Exception, e:
		# TODO This represents a library error and should be logged.
		responseData['failure'] = "Error resolving '%s' to a location" % location_string
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')