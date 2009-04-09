from django.test import TestCase

from django.core.urlresolvers import reverse
from django.contrib.auth.models import User
from epic.core.models import Profile

class ViewEditProfilePageCase(TestCase):
	fixtures = ['initial_users']
	
	def setUp(self):
		pass
	def tearDown(self):
		pass
	
	def testLoggedOutView(self):
		""" 
		Verify that logged out users can't reach the edit profile page 
		"""
		
		# Verify that log in is required to view the edit page
		edit_profile_url = reverse("epic.core.views.edit_profile")
		response = self.client.get(edit_profile_url)
		self.assertRedirects(response, "/login/?next=%s" % (edit_profile_url))
	
	def testLoggedInView(self):
		""" 
		Verify that logged in users can reach the edit profile page 
		""" 
		
		# Log in and view the edit page
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		edit_profile_url = reverse("epic.core.views.edit_profile")
		response = self.client.get(edit_profile_url)
		self.assertEqual(response.status_code, 200)

		# Check that the correct stuff is on the page
		self.assertTrue('First name' in response.content, response.content)
		self.assertTrue('Last name' in response.content)
		self.assertTrue('E-mail address' in response.content)
		self.assertTrue('Affiliation' in response.content)
		
class ActionEditProfilePageCase(TestCase):
	fixtures = ['initial_users']
	
	def setUp(self):
		pass
	
	def tearDown(self):
		pass
	
	def testLoggedOutEdit(self):
		"""
		Make sure that logged out users can't edit the data
		"""

		# The changes to the data
		post_data = {
			"first_name": "Bob",
			"last_name": "L'Admin",
			"email": "bob@admin.com",
			"affiliation": "Harvard",
		}
		
		# Attempt to edit the dataset
		edit_profile_url = reverse("epic.core.views.edit_profile")
		response = self.client.get(edit_profile_url)
		self.assertRedirects(response, "/login/?next=%s" % (edit_profile_url))
		
	def testLoggedInEdit(self):
		""" 
		Verify that only the creator can edit data
		"""
		# Get the objects to be used for this test
		user = User.objects.get(username="admin")
		profile = Profile.objects.for_user(user)
		
		# Save the original values
		user_original_email = user.email
		user_original_first_name = user.first_name
		user_original_last_name = user.last_name
		user_original_affiliation = profile.affiliation
		
		# The changes to the data
		post_data = {
			"first_name": "Bob",
			"last_name": "L'Admin",
			"email": "bob@admin.com",
			"affiliation": "Harvard",
		}
		
		# Log in
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		
		# Edit the dataset
		edit_profile_url = reverse("epic.core.views.edit_profile")
		response = self.client.post(edit_profile_url, post_data)
		self.assertEqual(response.status_code, 302)
		
		# Get the objects to be used for this test again
		user = User.objects.get(username="admin")
		profile = Profile.objects.for_user(user)
		
		# Get the new values
		user_new_email = user.email
		user_new_first_name = user.first_name
		user_new_last_name = user.last_name
		user_new_affiliation = profile.affiliation
		
		# Make sure the values changed
		self.assertNotEqual(user_new_affiliation , user_original_affiliation)
		self.assertNotEqual(user_new_email , user_original_email)
		self.assertNotEqual(user_new_first_name , user_original_first_name)
		self.assertNotEqual(user_new_last_name , user_original_last_name)
		
		# Make sure the values changed correctly
		self.assertEqual(user_new_affiliation , post_data['affiliation'])
		self.assertEqual(user_new_email , post_data['email'])
		self.assertEqual(user_new_first_name , post_data['first_name'])
		self.assertEqual(user_new_last_name , post_data['last_name'])
		