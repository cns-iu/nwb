from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from epic.core.test import CustomTestCase
from epic.geoloc.models import GeoLoc
from epic.geoloc.utils import CouldNotFindLocation, get_best_location
from geopy.geocoders.base import GeocoderResultError
import decimal


def closeEnough(number1, number2):
	TOLERANCE = 0.01        
	return (abs(number1 - number2) <= TOLERANCE)

class GeoLocTestCase(CustomTestCase):
	""" Test that the model and utils for geoloc is working as expected """
	# TODO: Add a test of the view that tests London.
	
	fixture = []
    
	def setUp(self):
		pass
	
	def tearDown(self):
		pass

	def testGeoLoc(self):
		init_data = {
			'longitude': decimal.Decimal('345.3423342'),
			'latitude': decimal.Decimal('-0.23342'),
			'canonical_name': 'I have no idea where this might be',
		}
		
		g = GeoLoc.objects.create(longitude=init_data['longitude'],
								  latitude=init_data['latitude'],
								  canonical_name=init_data['canonical_name'],)
		
		self.assertEqual(g.longitude, init_data['longitude'])
		self.assertEqual(g.latitude, init_data['latitude'])
		self.assertEqual(g.canonical_name, init_data['canonical_name'])
		
	def testGetBestLocationUtil(self):
		location_string = 'Bloomington, IN, USA'
		best = get_best_location(location_string)
		EXPECTED_BEST = (u'Bloomington, IN, USA', (39.164287000000002, -86.526904000000002))
		self.assertTrue(best[0] == EXPECTED_BEST[0])
		self.assertTrue(reduce(lambda p, q: p and q, [closeEnough(x1, x2) for x1, x2 in zip(best[1], EXPECTED_BEST[1])]))
		
		
		location_string = ''
		try:
			best = get_best_location(location_string)
			self.fail('The empty string as the location should return a CouldNotFindLocation error')
		except GeocoderResultError:
			#this is expected
			pass

class GeoLocGetBestLocationViewTestCase(CustomTestCase):
	""" Test that the model and utils for geoloc is working as expected """
	
	fixture = []
	
	def setUp(self):
		self.geoloc_get_best_location_url = reverse('epic.geoloc.views.geoloc_get_best_location')
	
	def tearDown(self):
		pass
	
	def testViewGeolocGetBestLocationNoLocation(self):
		response = self.client.get(self.geoloc_get_best_location_url)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, "No location_string given")
		
		response = self.client.post(self.geoloc_get_best_location_url)
		self.assertEqual(response.status_code, 200)
		self.assertNotContains(response, 'success')
		self.assertContains(response, 'failure')
	
	def testViewGeolocGetBestLocationBadLocation(self):
		post_data = {
			'location_string':'',
		}
		response = self.client.post(self.geoloc_get_best_location_url, post_data)
		self.assertEqual(response.status_code, 200)
		self.assertNotContains(response, 'success')
		self.assertContains(response, "failure")
	
	def testViewGeolocGetBestLocationGoodLocation(self):
		post_data = {
			'location_string':'Bloomington, IN, USA',
		}
		response = self.client.post(self.geoloc_get_best_location_url, post_data)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, 'success')
		self.assertNotContains(response, 'failure')