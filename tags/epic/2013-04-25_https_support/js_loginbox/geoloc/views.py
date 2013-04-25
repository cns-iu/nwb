from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User, UserManager
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404
from django.template import RequestContext, Context, loader
from django.utils import simplejson

from epic.geoloc.utils import get_best_location, CouldNotFindLocation
from epic.geoloc.models import GeoLoc

def geoloc_get_best_location(request):
	
	responseData = {}
	
	# Get the location string out of the post data, or send back a failure message
	try:
		location_string = request.POST['location_string']
	except:
		responseData['failure'] = "No location_string given"
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')
		
	# Get the best location and return it and a success message or return a failure
	try:
		# TODO: does the location_string need to be cleaned somehow?
		best_location = get_best_location(location_string)
		
		lng = str(best_location[1][1])
		lat = str(best_location[1][0])
		canonical_name= best_location[0]
		
		responseData['lng'] = lng
		responseData['lat'] = lat
		responseData['canonical_name'] = canonical_name
		responseData['success'] = 'A marker was added for %s' % canonical_name
		
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')
	
	except CouldNotFindLocation:
		responseData['failure'] = "%s could not be resolved to a location" % location_string
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')