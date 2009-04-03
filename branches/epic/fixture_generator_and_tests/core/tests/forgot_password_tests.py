from django.test import TestCase

from django.contrib.auth.models import User

class ForgotPasswordTestCase(TestCase):
	fixtures = [ "just_users" ]
	
	def setUp(self):
		pass
	
	def testForgotPasswordSubmittingBlankForm(self):
		forgot_password_response = self.client.get("/user/forgotpassword/")
		self.failUnlessEqual(forgot_password_response.status_code, 200, "Error reaching forgotpassword!")
		
		submit_blank_username_or_email_response = self.client.post("/user/forgotpassword/", data={ "username_or_email": "" })
		self.failUnlessEqual(submit_blank_username_or_email_response.status_code, 200, "Error reaching forgotpassword!")
		
		self.failUnless("This field is required." in submit_blank_username_or_email_response.content)
	
	def testForgotPasswordSubmittingUnboundUsername(self):
		forgot_password_response = self.client.get("/user/forgotpassword/")
		self.failUnlessEqual(forgot_password_response.status_code, 200, "Error reaching forgotusername!")
		
		submit_unbound_username_response = self.client.post("/user/forgotpassword/", data={ "username_or_email": "abcd" })
		self.failUnlessEqual(submit_unbound_username_response.status_code, 200, "Error reaching forgotpassword!")
		
		self.failUnless("The username abcd is not valid." in submit_unbound_username_response.content)
	
	def testForgotPasswordSubmittingUnboundEmail(self):
		forgot_password_response = self.client.get("/user/forgotpassword/")
		self.failUnlessEqual(forgot_password_response.status_code, 200, "Error reaching forgotusername!")
		
		submit_unbound_email_response = self.client.post("/user/forgotpassword/", data={ "username_or_email": "a@b.com" })
		self.failUnlessEqual(submit_unbound_email_response.status_code, 200, "Error reaching forgotpassword!")
		
		self.failUnless("The email a@b.com is not valid." in submit_unbound_email_response.content)
	
	def testForgotUsernameSuccessWithBoundUsername(self):
		forgot_password_response = self.client.get("/user/forgotpassword/")
		self.failUnlessEqual(forgot_password_response.status_code, 200, "Error reaching forgotusername!")
		
		submit_bound_username_response = self.client.post("/user/forgotpassword/", data={ "username_or_email": "peebs" })
		self.failUnlessEqual(submit_bound_username_response.status_code, 200, "Error reaching forgotpassword!")
		
		self.failUnless("The password for <b>peebs</b> has been reset and e-mailed to <b>markispeebs@gmail.com" in submit_bound_username_response.content)
	
	def testForgotUsernameSuccessWithBoundEmail(self):
		forgot_password_response = self.client.get("/user/forgotpassword/")
		self.failUnlessEqual(forgot_password_response.status_code, 200, "Error reaching forgotusername!")
		
		submit_bound_email_response = self.client.post("/user/forgotpassword/", data={ "username_or_email": "markispeebs@gmail.com" })
		self.failUnlessEqual(submit_bound_email_response.status_code, 200, "Error reaching forgotpassword!")
		
		self.failUnless("The password for <b>peebs</b> has been reset and e-mailed to <b>markispeebs@gmail.com" in submit_bound_email_response.content)
