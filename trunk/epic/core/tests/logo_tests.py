from django.test import TestCase

from epic.datarequests.models import DataRequest
from django.contrib.auth.models import User
from django.template import TemplateDoesNotExist

class LogoTestCase(TestCase):
	
	def setUp(self):
		pass
	def tearDown(self):
		pass
	
	def logoShouldBeLink(self):
		# setup the links
		logo_link = reverse('epic.core.views.browse')
		browse_url = reverse('epic.core.views.browse')
		upload_url = reverse('epic.datasets.views.create_dataset')
		
		# Check that the logo_link is on each page.  Best I can do.
		response = self.client.get(browse_url)
		self.assertContains(response, logo_link)
		response = self.client.get(upload_url)
		self.assertContains(response, logo_link)