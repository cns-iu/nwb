from epic.core.test import CustomTestCase
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

class ProfileLinkTestCase(CustomTestCase):
	fixtures = ['core_just_users']
	
	def setUp(self):
		self.profile_url = reverse('epic.core.views.view_profile')
	
	def testLinkForLoggedIn(self):
		self.tryLogin(username='bob', password='bob')
		response = self.client.get('/')
		self.assertContains(response, 'href="%(profile_link)s"' % 
						              {'profile_link':self.profile_url})
	
	def testLinkForNotLoggedIn(self):
		response = self.client.get('/')
		self.assertNotContains(response, 'href="%(profile_link)s"' % 
								         {'profile_link':self.profile_url})
