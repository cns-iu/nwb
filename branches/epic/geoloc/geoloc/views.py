from django.template import RequestContext, Context, loader
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User, UserManager
from django.contrib.auth import logout
from django.utils import simplejson

from epic.geoloc.utils import get_best_location, CouldNotFindLocation
from epic.geoloc.models import GeoLoc

def geoloc_get_best_location(request):
	
	responseData = {}
	
	try:
		location_string = request.POST['location_string']
	except:
		responseData['failure'] = "No location_string given"
		json = simplejson.dumps(responseData)
		return HttpResponse(json, mimetype='application/json')
		

	try:
		# TODO: does the location_string need to be cleaned somehow?
		location = get_best_location(location_string)
		lng = str(location[1][1])
		lat = str(location[1][0])
		canonical_name=location[0]
		
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