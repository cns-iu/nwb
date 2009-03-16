from django.test import TestCase

from django.contrib.auth.models import User
from epic.datasets.models import DataSet

class EditDataSetTestCase(TestCase):
	fixtures = [ "initial_data", "single_dataset" ]
	
	def setUp(self):
		self.data_set = DataSet.objects.all()[0]
		
		self.post_edited_metadata_form_data = {
			"name": "dataset2",
			"description": "description2",
			"tags": "slashdotted"
		}
		
		self.user = User.objects.get(username="peebs")
		
		# Create a dataset by peebs so we can test user privileges for editing it.
		
		self.peebs_data_set = DataSet(creator=self.user, name="dataset",
			description="description")
		
		self.peebs_data_set.save()
		
		# The URL to peebs' data set page.
		self.peebs_view_data_set_url = "/datasets/%s/" % self.peebs_data_set.id
		# The URL to peebs' edit data set page.
		self.peebs_edit_data_set_url = "/datasets/%s/edit/" % self.peebs_data_set.id
	
	def tearDown(self):
		pass
	
	def testUserViewEditDataSetPageAndNotLoggedIn(self):
		get_edit_dataset_page_response = self.client.get("/datasets/1/edit/")
		self.assertRedirects(get_edit_dataset_page_response, "/login/?next=/datasets/1/edit/")
	
	def testUserViewEditDataSetPageAndLoggedInAndNotDataSetCreator(self):
		self.client.login(username="admin", password="admin")
		
		get_edit_dataset_page_response = self.client.get(self.peebs_edit_data_set_url)
		
		self.assertRedirects(get_edit_dataset_page_response, self.peebs_view_data_set_url)
	
	def testUserViewEditDataSetPageAndLoggedInAndDataSetCreator(self):
		self.client.login(username="peebs", password="map")
		
		get_edit_dataset_page_response = self.client.get(self.peebs_edit_data_set_url)
		
		# Verify that all of the dataset's metadata is displayed in the form.
		
		self.assertContains(get_edit_dataset_page_response,
			'<input id="id_name" type="text" name="name" value="%s"' % self.peebs_data_set.name,
			1)
		
		self.assertContains(get_edit_dataset_page_response,
			'<textarea id="id_description" rows="10" cols="40" name="description">%s</textarea>' % self.peebs_data_set.description,
			1)
		
		self.assertContains(get_edit_dataset_page_response,
			'<input id="id_tags" type="text" name="tags"',
			1)
	
	def testUserGetToEditDataSetPageFromViewDataSetPageAndNotLoggedIn(self):
		# Verify that the not-logged-in user does NOT see an "Edit this dataset"
		# link on the view dataset page.
		
		get_view_dataset_page_response = self.client.get(self.peebs_view_data_set_url)
		
		self.assertNotContains(get_view_dataset_page_response, '<a href="/datasets/1/edit/">')
	
	def testUserGetToEditDataSetPageFromViewDataSetPageAndNotDataSetCreator(self):
		# Verify that the logged-in-but-not-dataset-creator user does NOT see an
		# "Edit this dataset" link on the view dataset page.
		
		self.client.login(username="admin", password="admin")
		
		get_view_dataset_page_response = self.client.get(self.peebs_view_data_set_url)
		
		self.assertNotContains(get_view_dataset_page_response, '<a href="/datasets/1/edit/">')
	
	def testUserGetToEditDataSetPageFromViewDataSetPageAndDataSetCreator(self):
		# Verify that the logged-in-and-is-dataset-creator user DOES see an
		# "Edit this dataset" link on the view dataset page.
		
		self.client.login(username="peebs", password="map")
		
		get_view_dataset_page_response = self.client.get(self.peebs_view_data_set_url)
		
		self.assertContains(get_view_dataset_page_response,
			'<a href="%s">' % self.peebs_edit_data_set_url)
	
	def testUserEditDataSetOnPageButCancel(self):
		# Verify that the "Cancel Changes" button is on the edit dataset page.
		# (Only the dataset creator should be able to get to this page, so there
		# only needs to be one test for this.)
		self.client.login(username="peebs", password="map")
		
		get_edit_dataset_page_response = self.client.get(self.peebs_edit_data_set_url)
		
		self.assertContains(get_edit_dataset_page_response,
			'<a href = "%s">' % self.peebs_view_data_set_url)
		
		self.assertContains(get_edit_dataset_page_response,
			"Cancel Metadata Changes")
	
	def testUserEditDataSetOnPageAndSaveAndNotLoggedIn(self):
		# Since we can't actually simulate interaction with the page directly, we
		# must test this functionality (here in Python) by posting directly to the
		# appropriate URL.  This means we DO need the separate tests for this.
		post_edited_dataset_response = self.client.post(self.peebs_edit_data_set_url,
			self.post_edited_metadata_form_data)
		
		self.assertRedirects(post_edited_dataset_response,
			"login/?next=%s" % self.peebs_edit_data_set_url)
		
		pass
	
	def testUserEditDataSetOnPageAndSaveAndNotDataSetCreator(self):
		# Verify that the logged-in-and-not-dataset-creator user CANNOT post data to
		# the edit dataset page.
		self.client.login(username="admin", password="admin")
		
		post_edited_dataset_response = self.client.post(self.peebs_edit_data_set_url,
			self.post_edited_metadata_form_data)
		
		self.assertRedirects(post_edited_dataset_response,
			self.peebs_view_data_set_url)
		
		pass
	
	def testUserEditDataSetOnPageAndSaveAndDataSetCreator(self):
		# Verify that the logged-in-and-is-dataset-creator user CAN post data to the
		# edit dataset page.
		self.client.login(username="peebs", password="map")
		
		post_edited_dataset_response = self.client.post(self.peebs_edit_data_set_url,
			self.post_edited_metadata_form_data)
		
		self.assertRedirects(post_edited_dataset_response,
			self.peebs_view_data_set_url)
		
		get_view_dataset_page_response = self.client.get(self.peebs_view_data_set_url)
		self.assertEqual(get_view_dataset_page_response.status_code, 200)
		
		updated_peebs_data_set = DataSet.objects.get(pk=self.peebs_data_set.id)
		
		self.assertEqual(updated_peebs_data_set.name,
			self.post_edited_metadata_form_data["name"])
		
		self.assertEqual(updated_peebs_data_set.description,
			self.post_edited_metadata_form_data["description"])

		# If you check for a number here, be aware that there will be two, one for the link and one for the text tag itself
		self.assertContains(get_view_dataset_page_response,
			"%s" % self.post_edited_metadata_form_data["tags"])
		
		# Verify that all of the dataset's new metadata is displayed in the form.
		self.assertContains(get_view_dataset_page_response,
			"%s" % self.peebs_data_set.name)
		
		self.assertContains(get_view_dataset_page_response,
			"%s" % self.peebs_data_set.description)
		
