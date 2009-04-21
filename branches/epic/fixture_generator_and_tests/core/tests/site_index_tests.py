from django.test import TestCase

from django.core.urlresolvers import reverse

class SiteIndexTestCase(TestCase):
	def setUp(self):
		pass
	
	def tearDown(self):
		pass
	
	def testIndexPageExists(self):
		"""
		Test that the site_index exists
		"""
		# setup the links
		logo_link = reverse('epic.core.views.site_index')
		
		# Check that the index page exists
		response = self.client.get(logo_link)
		self.assertEquals(response.status_code, 200)