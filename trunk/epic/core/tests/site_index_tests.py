from django.test import TestCase

from epic.datarequests.models import DataRequest
from django.contrib.auth.models import User
from django.template import TemplateDoesNotExist

class SiteIndexTestCase(TestCase):
	
	def setUp(self):
		pass
	def tearDown(self):
		pass
	
	def index_page_exists(self):
		# setup the links
		logo_link = reverse('epic.core.views.site_index')
		
		# Check that the logo_link is on each page.  Best I can do.
		response = self.client.get(browse_url)
		self.assertEquals(response.status_code, 200)