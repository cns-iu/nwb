from django.test import TestCase
from django.core.urlresolvers import reverse
from django.contrib.auth.models import User

class ForgotEmailTestCase(TestCase):
	fixtures = [ "just_users" ]
	
	def setUp(self):
		self.bob = User.objects.get(username="bob")
		self.forgot_email_url = reverse('epic.core.views.forgot_email')
	
	def tearDown(self):
		pass
	
	def testForgotEmailLoggedIn(self):
		"""
		Test that logged in users will be shown their email
		"""
		login = self.client.login(username="bob", password="bob")
		self.failUnless(login, "Could not login")
		
		response = self.client.get(self.forgot_email_url)
		self.failUnlessEqual(response.status_code, 200)
		self.assertContains(response, "Your email is %s." % self.bob.email)

	
	def testForgotEmailNotLoggedInButSubmittingBlankUsername(self):
		"""
		Test that users who are not logged in cannot submit a blank username
		"""
		response = self.client.get(self.forgot_email_url)
		self.failUnlessEqual(response.status_code, 200)
		
		post_data = {
			'username': '',
		}
		
		response = self.client.post(self.forgot_email_url, post_data)
		self.failUnlessEqual(response.status_code, 200)
		self.assertFormError(response, 'form', 'username', "This field is required.")
	
	def testForgotEmailNotLoggedInButSubmittingUnboundEmail(self):
		"""
		Test that submiting an invalid username displays an error to the user
		"""
		response = self.client.get(self.forgot_email_url)
		self.failUnlessEqual(response.status_code, 200)
		
		post_data = {
			'username': 'invalidusername',
		}
		
		response = self.client.post(self.forgot_email_url, post_data)
		self.failUnlessEqual(response.status_code, 200)
		
		self.assertFormError(response, 'form', 'username', "No email was found tied to the username %s." % post_data['username'])
	
	def testForgotEmailSuccess(self):
		"""
		Test that submiting a valid username will yeild an email address for that user
		TODO: this is a terrible, terrible idea.
		"""
		response = self.client.get(self.forgot_email_url)
		self.failUnlessEqual(response.status_code, 200)
		
		post_data = {
			'username': self.bob.username,
		}
		
		response = self.client.post(self.forgot_email_url, post_data)
		self.failUnlessEqual(response.status_code, 200)

		self.assertContains(response, "Your email is %s." % self.bob.email)
