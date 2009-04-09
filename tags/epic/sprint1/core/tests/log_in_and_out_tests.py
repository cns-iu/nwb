from django.test import TestCase
from django.contrib.auth.models import User

from epic.settings import LOGIN_REDIRECT_URL

class LogInAndOutTestCase(TestCase):
	fixtures = ['initial_users']
	
	def setUp(self):
		pass
	
	def testLoginSuccess(self):
		response = self.client.get('/login/')
		self.failUnlessEqual(response.status_code, 200, "Error reaching login!")
		response = self.client.post("/login/", data={'username': 'bob', 'password':'bob',})
		self.failUnlessEqual(response.status_code, 302, "Error logging in!")
		#TODO Once the follow=true actually works it should be used here.
		self.assertRedirects(response, LOGIN_REDIRECT_URL)
	
	def testLoginFailure(self):
		response = self.client.post("/login/", {'username': 'bob', 'password':'bob2',})
		self.failUnlessEqual(response.status_code, 200)
		response = self.client.post("/login/", {'username': 'bob', 'password':'bob2',})
		self.failUnless("Your username and password didn't match. Please try again." in response.content)
	
	def testLogout(self):
		response = self.client.get('/logout/')
		self.failUnlessEqual(response.status_code, 302)
		response = self.client.get('/')
		self.failUnless("login" in response.content)
		
	def testLoginFrontPage(self):
		response = self.client.get('/')
		self.failUnless('username' in response.content)
		self.failUnless('password' in response.content)
		