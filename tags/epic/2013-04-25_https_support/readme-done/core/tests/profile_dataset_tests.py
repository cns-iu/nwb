from epic.core.test import CustomTestCase

from django.contrib.auth.models import User

class ProfileDatasetTestCase(CustomTestCase):
	fixtures = ['core_profile_datasets']
	
	def setUp(self):
		pass
	
	def testForNoDataSets(self):
		self.tryLogin(username='bob', password='bob')
		response = self.client.get('/user/')
		self.failUnless(response.status_code, 200)
		self.failIf("Your Datasets" in response.content)
	
	def testForDataSets(self):
		self.tryLogin(username='bill', password='bill')
		response = self.client.get('/user/')
		self.failUnless(response.status_code, 200)
		self.failUnless("Your Datasets" in response.content, response.content)
		self.failUnless("edit" in response.content, response.content)
