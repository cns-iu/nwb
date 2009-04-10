from django.test import TestCase
from django.core.urlresolvers import reverse
from django.contrib.auth.models import User

class ForgotPasswordTestCase(TestCase):
	fixtures = [ "just_users" ]
	
	def setUp(self):
		self.bob = User.objects.get(username="bob")
		self.forgot_password_url = reverse('epic.core.views.forgot_password')
	
	def tearDown(self):
		pass
	
	def testForgotPasswordSubmittingBlankForm(self):
		"""
		Test that submitting a blank form yeilds an error
		"""
		response = self.client.get(self.forgot_password_url)
		self.failUnlessEqual(response.status_code, 200)
		
		post_data = {
			'username_or_email': '',
		}
		
		response = self.client.post(self.forgot_password_url, post_data)
		self.failUnlessEqual(response.status_code, 200)
		
		self.assertFormError(response, 'form', 'username_or_email', 'This field is required.')
	
	#TODO: I stopped cleaning core here.
	def testForgotPasswordSubmittingUnboundUsername(self):
		response = self.client.get(self.forgot_password_url)
		self.failUnlessEqual(response.status_code,200)
		
		post_data = {
			'username_or_email': 'asdf0 jw35yj[ j0q2rj',
		}
		
		response = self.client.post(self.forgot_password_url, post_data)
		self.failUnlessEqual(response.status_code, 200)
		
		self.assertFormError(response, 'form', 'username_or_email', "'%s' is not a valid username." % post_data['username_or_email'])
	
	def testForgotPasswordSubmittingUnboundEmail(self):
		response = self.client.get(self.forgot_password_url)
		self.failUnlessEqual(response.status_code,200)
		
		post_data = {
			'username_or_email': 'asdf323@asdf.com',
		}
		
		response = self.client.post(self.forgot_password_url, post_data)
		self.failUnlessEqual(response.status_code, 200)
		
		self.assertFormError(response, 'form', 'username_or_email', "There is no user registered with the email address '%s'." % post_data['username_or_email'])
	
	def testForgotUsernameSuccessWithBoundUsername(self):
		response = self.client.get(self.forgot_password_url)
		self.failUnlessEqual(response.status_code,200)
		
		post_data = {
			'username_or_email': self.bob.username,
		}
		
		response = self.client.post(self.forgot_password_url, post_data)
		self.failUnlessEqual(response.status_code, 200)
		self.assertContains(response, "An email has been sent to your &#39;%s&#39; address with a new password." % self.bob.email.split('@')[1])	

	def testForgotUsernameSuccessWithBoundEmail(self):
		response = self.client.get(self.forgot_password_url)
		self.failUnlessEqual(response.status_code,200)
		
		post_data = {
			'username_or_email': self.bob.email,
		}
		
		response = self.client.post(self.forgot_password_url, post_data)
		self.failUnlessEqual(response.status_code, 200)
		self.assertContains(response, "An email has been sent to your &#39;%s&#39; address with a new password." % self.bob.email.split('@')[1])