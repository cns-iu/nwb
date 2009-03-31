from django.test import TestCase

from django.contrib.auth.models import User

class ViewBasicUserProfileTestCase(TestCase):
	fixtures = ['initial_users']
	
	def setUp(self):
		pass
	
	def testViewProfileNotLoggedIn(self):
		user_response = self.client.get("/user/")
		self.failUnlessEqual(user_response.status_code, 302, "Did not redirect to login!")
	
	def testViewProfileLoggedIn(self):
		self.client.login(username="bob", password="bob")
		
		user_response = self.client.get("/user/")
		self.failUnlessEqual(user_response.status_code, 200, "Error reaching user!")
		
		self.failUnless("<h3>Displaying profile of  .</h3>" in user_response.content)
