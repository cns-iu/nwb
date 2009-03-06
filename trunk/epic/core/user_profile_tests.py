from django.test import TestCase

from django.contrib.auth.models import User

class ViewBasicUserProfileTestCase(TestCase):
	fixtures = ['initial_users']
	
	def setUp(self):
		pass
	
	def testViewProfileNotLogged(self):
		user_response = self.client.get("/user/")
		self.failUnlessEqual(user_response.status_code, 200, "Error reaching user!")
		
		self.failUnless('<td><label for="id_username">Username</label></td>' in response.content)
	
	def testViewProfileLoggedIn(self):
		self.client.login(username="peebs", password="map")
		
		user_response = self.client.get("/user/")
		self.failUnlessEqual(user_response.status_code, 200, "Error reaching user!")
		
		self.failUnless("<h3>Displaying profile of Mark Peebs.</h3>" in response.content)
