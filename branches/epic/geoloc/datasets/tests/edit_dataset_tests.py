from django.test import TestCase

from django.contrib.auth.models import User
from epic.datasets.models import DataSet

class ViewEditDataSetPageTestCase(TestCase):
	fixtures = [ "initial_data"]
	
	def setUp(self):
		pass
	
	def tearDown(self):
		pass
	
	def testLoggedOut(self):
		# Create objects to be used for this test
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", slug="dataset", creator=peebs)
		
		# Verify that log in is required to view the edit page
		response = self.client.get('%sedit/' % (ds1.get_absolute_url()))
		self.assertRedirects(response, "/login/?next=%sedit/" % (ds1.get_absolute_url()))
	def testLoggedInNotCreator(self):
		# Create objects to be used for this test
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", slug="dataset", creator=peebs)
		
		# Verify that only the owner can view the edit page and that they'll be redirected to the dataset page
		login = self.client.login(username='peebs', password='map')
		self.failUnless(login, 'Could not login')
		response = self.client.get('%sedit/' % (ds1.get_absolute_url()))
		self.assertRedirects(response, ds1.get_absolute_url())
		
	def testLoggedInCreator(self):
		# Create objects to be used for this test
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", slug="dataset", creator=peebs)
		
		# Verify that the owner can view the edit page
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		response = self.client.get('%sedit/' % (ds1.get_absolute_url()))
		self.assertEqual(response.status_code, 200)

		# Check that the correct stuff is on the page
		self.assertTrue('description' in response.content)
		self.assertTrue('name' in response.content)
		self.assertTrue('Cancel Metadata Changes' in response.content)
		
class ActionEditDataSetPageTestCase(TestCase):
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
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", slug="dataset", creator=peebs)
		
		# The changes to the data
		post_data = {
			"name": "dataset2",
			"description": "description2",
			"tags": "slashdotted"
		}
		
		# Edit the dataset
		response = self.client.post('%sedit/' % (ds1.get_absolute_url()), post_data)
		self.assertRedirects(response, "/login/?next=%sedit/" % (ds1.get_absolute_url()))
		
	def testLoggedInNotCreator(self):
		""" 
		Verify that only the creator can edit data
		"""
		
		# Create objects to be used for this test
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", slug="dataset", creator=peebs)
		
		# The changes to the data
		post_data = {
			"name": "dataset2",
			"description": "description2",
			"tags": "slashdotted"
		}
		
		# Log in as NOT the creator
		login = self.client.login(username='peebs', password='map')
		self.failUnless(login, 'Could not login')
		
		# Edit the dataset
		response = self.client.post('%sedit/' % (ds1.get_absolute_url()), post_data)
		self.assertRedirects(response, ds1.get_absolute_url())

	def testLoggedInCreator(self):
		"""
		Verify that the creator can edit data.
		"""
		
		# Create objects to be used for this test
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", slug="dataset", creator=peebs)
		
		# The changes to the data
		post_data = {
			"name": "dataset2",
			"description": "description2",
			"tags": "slashdotted"
		}
		# log in as creator
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		
		# Check that the slug will change
		old_slug = ds1.slug
		
		# Edit the dataset
		# TODO: make this a reverse
		response = self.client.post('%sedit/' % (ds1.get_absolute_url()), post_data)
		
		# Grad the dataset again since it has hopfully changed in the database
		ds1 = DataSet.objects.get(pk=ds1.id)
		
		new_slug = ds1.slug
		self.assertFalse(old_slug == new_slug)
		
		# Verify that the changes were made
		response = self.client.get(ds1.get_absolute_url())
		self.assertTrue('dataset2' in response.content)
		self.assertTrue('description2' in response.content)
		self.assertTrue('slashdotted' in response.content)
		self.assertFalse('Important Data' in response.content)
		self.assertFalse('piece' in response.content)		
