from django.test import TestCase

from django.contrib.auth.models import User

class ChangePasswordTestCast(TestCase):
	fixtures = ['initial_users']
	
	def setUp(self):
		pass
	
	def testPage(self):
		# Login first.
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		# Make certain the changepassword page is there.
		response = self.client.get('/user/change_password/')
		self.assertEqual(response.status_code, 200)
	
	def testPageNotLoggedIn(self):
		response = self.client.get('/user/change_password/')
		self.assertRedirects(response, '/login/?next=/user/change_password/')
	
	def testFailToChangeMatchProblem(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		post_data = {
			'old_password': 'bob',
			'new_password1': 'b',
			'new_password2': 'blah'
		}
		response = self.client.post('/user/change_password/', post_data)
		self.failUnless(response.status_code, 200)
		self.failUnless("The two password fields didn&#39;t match." in response.content)
	
	def testFailToChangeOldPasswordProblem(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		post_data = {
			'old_password': 'incorrectOldPassword',
			'new_password1': 'blah',
			'new_password2': 'blah'
		}
		response = self.client.post('/user/change_password/', post_data)
		self.failUnless(response.status_code, 200)
		self.failUnless("Your old password was entered incorrectly. Please enter it again." in response.content)
	
	def testChangePasswordSuccess(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		post_data = {
			'old_password': 'bob',
			'new_password1': 'blah',
			'new_password2': 'blah'
		}
		response = self.client.post('/user/change_password/', post_data)
		#Redirected if success.
		self.failUnless(response.status_code, 302)
		self.assertRedirects(response, '/user/')