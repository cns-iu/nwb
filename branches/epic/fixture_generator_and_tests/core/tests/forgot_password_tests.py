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
		
		submit_unbound_username_response = self.client.post(self.forgot_password_url, data={ "username_or_email": "abcd" })
		self.failUnlessEqual(submit_unbound_username_response.status_code, 200)
		
		self.failUnless("The username abcd is not valid." in submit_unbound_username_response.content)
	
	def testForgotPasswordSubmittingUnboundEmail(self):
		response = self.client.get(self.forgot_password_url)
		self.failUnlessEqual(response.status_code,200)
		
		submit_unbound_email_response = self.client.post(self.forgot_password_url, data={ "username_or_email": "a@b.com" })
		self.failUnlessEqual(submit_unbound_email_response.status_code, 200)
		
		self.failUnless("The email a@b.com is not valid." in submit_unbound_email_response.content)
	
	def testForgotUsernameSuccessWithBoundUsername(self):
		response = self.client.get(self.forgot_password_url)
		self.failUnlessEqual(response.status_code,200)
		
		submit_bound_username_response = self.client.post(self.forgot_password_url, data={ "username_or_email": "peebs" })
		self.failUnlessEqual(submit_bound_username_response.status_code, 200)
		
		self.failUnless("The password for <b>peebs</b> has been reset and e-mailed to <b>markispeebs@gmail.com" in submit_bound_username_response.content)
	
	def testForgotUsernameSuccessWithBoundEmail(self):
		response = self.client.get(self.forgot_password_url)
		self.failUnlessEqual(response.status_code,200)
		
		submit_bound_email_response = self.client.post(self.forgot_password_url, data={ "username_or_email": "markispeebs@gmail.com" })
		self.failUnlessEqual(submit_bound_email_response.status_code, 200)
		
		self.failUnless("The password for <b>peebs</b> has been reset and e-mailed to <b>markispeebs@gmail.com" in submit_bound_email_response.content)
