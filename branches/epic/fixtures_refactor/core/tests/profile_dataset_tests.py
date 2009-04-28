from django.test import TestCase

from django.contrib.auth.models import User

class ProfileDatasetTestCase(TestCase):
	fixtures = ['core_profile_datasets']
	
	def setUp(self):
		pass
	
	def testForNoDataSets(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/')
		self.failUnless(response.status_code, 200)
		self.failIf("Your Datasets" in response.content)
	
	def testForDataSets(self):
		login = self.client.login(username='bill', password='bill')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/')
		self.failUnless(response.status_code, 200)
		self.failUnless("Your Datasets" in response.content, response.content)
		self.failUnless("edit" in response.content, response.content)
