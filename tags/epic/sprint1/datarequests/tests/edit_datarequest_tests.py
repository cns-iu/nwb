from django.test import TestCase
from epic.datarequests.models import DataRequest
from django.core.urlresolvers import reverse
from django.contrib.auth.models import User

class ViewEditDataRequestTestCase(TestCase):
	fixtures = ['initial_data']
	
	def setUp(self):
		pass
	
	def tearDown(self):
		pass
	
	def testFailure(self):
		pass

	def testLoggedOut(self):		
		""" 
		Verify that logged out users get directed to the login view
		"""
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		dr1 = DataRequest.objects.create(name="Important Data Needed!", description="A very important piece of data", slug="important-data-needed", creator=admin)
		dr2 = DataRequest.objects.create(name="datarequest", description="description", slug="datarequest", creator=peebs)
		
		# Verify that log in is required to view the edit page
		dr1_edit_url = reverse("epic.datarequests.views.edit_datarequest", args=[], kwargs={'item_id':dr1.id})
		response = self.client.get(dr1_edit_url)
		self.assertRedirects(response, "/login/?next=%s" % dr1_edit_url)
		
	def testLoggedInNotCreator(self):
		"""
		Verify that users who are not the creator cannot edit the request
		"""
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		dr1 = DataRequest.objects.create(name="Important Data Needed!", description="A very important piece of data", slug="important-data-needed", creator=admin)
		dr2 = DataRequest.objects.create(name="datarequest", description="description", slug="datarequest", creator=peebs)
		
		# log in as someone other than the creator
		login = self.client.login(username='peebs', password='map')
		self.failUnless(login, 'Could not login')
		
		# Verify only the owner can edit and that cheaters to go the datarequest url
		dr1_edit_url = reverse("epic.datarequests.views.edit_datarequest", args=[], kwargs={'item_id':dr1.id})
		response = self.client.get(dr1_edit_url)
		dr1_url = reverse("epic.datarequests.views.view_datarequest", args=[], kwargs={'item_id':dr1.id})
		self.assertRedirects(response, dr1_url)
		
	def testLoggedInCreator(self):
		"""
		Verify that the creator can view the edit page
		"""
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		dr1 = DataRequest.objects.create(name="Important Data Needed!", description="A very important piece of data", slug="important-data-needed", creator=admin)
		dr2 = DataRequest.objects.create(name="datarequest", description="description", slug="datarequest", creator=peebs)
		
		# login as the creator
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		
		# Go to the edit page
		dr1_edit_url = reverse("epic.datarequests.views.edit_datarequest", args=[], kwargs={'item_id':dr1.id})
		response = self.client.get(dr1_edit_url)
		self.assertEqual(response.status_code, 200)

		# Check that the correct stuff is on the page
		self.assertTrue(dr1.description in response.content)
		self.assertTrue(dr1.name in response.content)
		self.assertTrue('Edit Data Request' in response.content)
		
class ActionEditDataRequestPageTestCase(TestCase):
	fixtures = [ "initial_data"]
	
	def setUp(self):
		pass
	
	def tearDown(self):
		pass
	
	def testLoggedOut(self):
		"""
		Make sure that logged out users can't edit the data
		"""
		
		# Create objects to be used for this test
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		dr1 = DataRequest.objects.create(name="Important Data Needed!", description="A very important piece of data", slug="important-data-needed", creator=admin)
		dr2 = DataRequest.objects.create(name="datarequest", description="description", slug="datarequest", creator=peebs)
		
		# The changes to the data
		post_data = {
			"name": "datarequest2",
			"description": "description2",
		}
		
		# Edit the datarequest
		dr1_edit_url = reverse("epic.datarequests.views.edit_datarequest", args=[], kwargs={'item_id':dr1.id})
		response = self.client.post(dr1_edit_url, post_data)
		self.assertRedirects(response, "/login/?next=%s" % (dr1_edit_url))
		
	def testLoggedInNotCreator(self):
		""" 
		Verify that only the creator can edit data
		"""
		
		# Create objects to be used for this test
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		dr1 = DataRequest.objects.create(name="Important Data Needed!", description="A very important piece of data", slug="important-data-needed", creator=admin)
		dr2 = DataRequest.objects.create(name="datarequest", description="description", slug="datarequest", creator=peebs)
		
		# The changes to the data
		post_data = {
			"name": "datarequest2",
			"description": "description2",
		}
		
		# Log in as NOT the creator
		login = self.client.login(username='peebs', password='map')
		self.failUnless(login, 'Could not login')
		
		# Edit the datarequest
		dr1_edit_url = reverse("epic.datarequests.views.edit_datarequest", args=[], kwargs={'item_id':dr1.id})
		response = self.client.post(dr1_edit_url, post_data)
		dr1_url = reverse("epic.datarequests.views.view_datarequest", args=[], kwargs={'item_id':dr1.id})
		self.assertRedirects(response, dr1_url)

	def testLoggedInCreator(self):
		"""
		Verify that the creator can edit data.
		"""
		
		# Create objects to be used for this test
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		dr1 = DataRequest.objects.create(name="Important Data Needed!", description="A very important piece of data", slug="important-data-needed", creator=admin)
		dr2 = DataRequest.objects.create(name="datarequest", description="description", slug="datarequest", creator=peebs)
		
		# The changes to the data
		post_data = {
			"name": "datarequest2",
			"description": "description2",
		}
		# log in as creator
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		
		# Check that the slug will change
		old_slug = dr1.slug
		
		# Edit the datarequest
		dr1_edit_url = reverse("epic.datarequests.views.edit_datarequest", args=[], kwargs={'item_id':dr1.id})
		response = self.client.post(dr1_edit_url, post_data)
		
		# Grad the datarequest again since it has hopfully changed in the database
		dr1 = DataRequest.objects.get(pk=dr1.id)
		
		# Verify that the changes were made
		dr1_url = reverse("epic.datarequests.views.view_datarequest", args=[], kwargs={'item_id':dr1.id})
		response = self.client.get(dr1_url)
		self.assertTrue(post_data['name'] in response.content)
		self.assertTrue(post_data['description'] in response.content)
		self.assertFalse('Important Data' in response.content)
		self.assertFalse('piece' in response.content)
		
		new_slug = dr1.slug
		self.assertFalse(old_slug == new_slug)	
