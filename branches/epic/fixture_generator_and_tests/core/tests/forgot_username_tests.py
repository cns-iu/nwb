from django.test import TestCase

from django.contrib.auth.models import User

class ForgotUsernameTestCase(TestCase):
	fixtures = [ "just_users" ]
	
	def setUp(self):
		pass
	
	def testForgotUsernameLoggedIn(self):
		login = self.client.login(username="peebs", password="map")
		self.failUnless(login, "Could not login")
		
		forgot_username_response = self.client.get("/user/forgotusername/")
		self.failUnlessEqual(forgot_username_response.status_code, 200, "Error reaching forgotusername!")
		
		self.failUnless("Your username is peebs." in forgot_username_response.content)
		
		self.client.logout()
	
	def testForgotUsernameNotLoggedInButSubmittingBlankEmail(self):
		forgot_username_response = self.client.get("/user/forgotusername/")
		self.failUnlessEqual(forgot_username_response.status_code, 200, "Error reaching forgotusername!")
		
		submit_blank_email_response = self.client.post("/user/forgotusername/", data={ "email": "" })
		self.failUnlessEqual(submit_blank_email_response.status_code, 200, "Error reaching forgotusername!")
		
		self.failUnless("This field is required." in submit_blank_email_response.content)
	
	def testForgotUsernameNotLoggedInButSubmittingInvalidEmail(self):
		forgot_username_response = self.client.get("/user/forgotusername/")
		self.failUnlessEqual(forgot_username_response.status_code, 200, "Error reaching forgotusername!")
		
		submit_invalid_email_response = self.client.post("/user/forgotusername/", data={ "email": "abcd" })
		self.failUnlessEqual(submit_invalid_email_response.status_code, 200, "Error reaching forgotusername!")
		
		self.failUnless("Enter a valid e-mail address." in submit_invalid_email_response.content)
	
	def testForgotUsernameNotLoggedInButSubmittingUnboundEmail(self):
		forgot_username_response = self.client.get("/user/forgotusername/")
		self.failUnlessEqual(forgot_username_response.status_code, 200, "Error reaching forgotusername!")
		
		submit_unbound_email_response = self.client.post("/user/forgotusername/", data={ "email": "a@b.com" })
		self.failUnlessEqual(submit_unbound_email_response.status_code, 200, "Error reaching forgotusername!")
		
		self.failUnless("No username was found tied to the e-mail address a@b.com." in submit_unbound_email_response.content)
	
	def testForgotUsernameSuccess(self):
		forgot_username_response = self.client.get("/user/forgotusername/")
		self.failUnlessEqual(forgot_username_response.status_code, 200, "Error reaching forgotusername!")
		
		submit_bound_email_response = self.client.post("/user/forgotusername/", data={ "email": "markispeebs@gmail.com" })
		self.failUnlessEqual(submit_bound_email_response.status_code, 200, "Error reaching forgotusername!")
		
		self.failUnless("Your username is peebs." in submit_bound_email_response.content)
