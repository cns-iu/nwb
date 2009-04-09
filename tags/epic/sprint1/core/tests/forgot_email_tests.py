from django.test import TestCase

from django.contrib.auth.models import User

class ForgotEmailTestCase(TestCase):
	fixtures = [ "initial_data" ]
	
	def setUp(self):
		pass
	
	def testForgotEmailLoggedIn(self):
		login = self.client.login(username="peebs", password="map")
		self.failUnless(login, "Could not login")
		
		forgot_email_response = self.client.get("/user/forgotemail/")
		self.failUnlessEqual(forgot_email_response.status_code, 200, "Error reaching forgotemail!")
		
		self.failUnless("Your email is markispeebs@gmail.com." in forgot_email_response.content)
		
		self.client.logout()
	
	def testForgotEmailNotLoggedInButSubmittingBlankUsername(self):
		forgot_email_response = self.client.get("/user/forgotemail/")
		self.failUnlessEqual(forgot_email_response.status_code, 200, "Error reaching forgotemail!")
		
		submit_blank_username_response = self.client.post("/user/forgotemail/", data={ "username": "" })
		self.failUnlessEqual(submit_blank_username_response.status_code, 200, "Error reaching forgotemail!")
		
		self.failUnless("This field is required." in submit_blank_username_response.content)
	
	def testForgotEmailNotLoggedInButSubmittingUnboundEmail(self):
		forgot_email_response = self.client.get("/user/forgotemail/")
		self.failUnlessEqual(forgot_email_response.status_code, 200, "Error reaching forgotemail!")
		
		submit_unbound_username_response = self.client.post("/user/forgotemail/", data={ "username": "abcd" })
		self.failUnlessEqual(submit_unbound_username_response.status_code, 200, "Error reaching forgotemail!")
		
		self.failUnless("No email was found tied to the username abcd." in submit_unbound_username_response.content)
	
	def testForgotEmailSuccess(self):
		forgot_email_response = self.client.get("/user/forgotemail/")
		self.failUnlessEqual(forgot_email_response.status_code, 200, "Error reaching forgotemail!")
		
		submit_bound_username_response = self.client.post("/user/forgotemail/", data={ "username": "peebs" })
		self.failUnlessEqual(submit_bound_username_response.status_code, 200, "Error reaching forgotemail!")
		
		self.failUnless("Your email is markispeebs@gmail.com." in submit_bound_username_response.content)
