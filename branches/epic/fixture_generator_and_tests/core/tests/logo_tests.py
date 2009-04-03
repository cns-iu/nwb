from django.test import TestCase

from epic.datarequests.models import DataRequest
from django.contrib.auth.models import User
from django.template import TemplateDoesNotExist
from django.core.urlresolvers import reverse

class LogoTestCase(TestCase):
	fixtures = [ "just_users" ]
	
	def setUp(self):
		pass
	def tearDown(self):
		pass
	
	def testLogoShouldBeLink(self):
		# setup the links
		logo_link = reverse('epic.core.views.site_index')
		browse_url = reverse('epic.core.views.browse')
		upload_url = reverse('epic.datasets.views.create_dataset')
		
		# Check that the logo_link is on each page.  Best I can do.
		response = self.client.get(browse_url)
		self.assertContains(response, logo_link)
		
		# You must be logged in to see the upload page.
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(upload_url)
		self.assertContains(response, logo_link)