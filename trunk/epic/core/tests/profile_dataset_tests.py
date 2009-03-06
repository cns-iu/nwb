from django.test import TestCase

from django.contrib.auth.models import User

class ProfileDatasetTestCase(TestCase):
	fixtures = ['profile_datasets']
	
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
	
	def testForOnlyDataSets(self):
		login = self.client.login(username='bill', password='bill')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/')
		self.failUnless(response.status_code, 200)
		#This is a datarequest object and should not be here
		self.failIf("Item 3" in response.content)
