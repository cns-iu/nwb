from django.test import TestCase

from django.contrib.auth.models import User

class ProfileLinkTestCase(TestCase):
	fixtures = ['initial_users']
	
	def setUp(self):
		pass
	
	def testLinkForLoggedIn(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/')
		self.failUnless('<a href="/user/">Profile</a>' in response.content)
	
	def testLinkForNotLoggedIn(self):
		response = self.client.get('/')
		self.failIf('<a href="/user/">Profile</a>' in response.content)
