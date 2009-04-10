from django.test import TestCase
from django.core.urlresolvers import reverse
from django.contrib.auth.models import User
from epic.tags.models import Tagging
from epic.datasets.models import DataSet

def common_setUp(self):
	# DS1 is created by bob.
	self.bob = User.objects.get(username="bob")
	
	# TODO: Put this in a fixture.
	self.ds1 = DataSet.objects.get(creator=self.bob,
								   name="ds1",
								   slug="ds1",
								   description="dataset number one")

	# DS2 is created by bill.
	self.bill = User.objects.get(username="bill")
	
	self.ds2 = DataSet.objects.get(creator=self.bill,
								   name="ds2",
								   slug="ds2",
								   description="dataset number two")
	
	self.TAG1 = "tag1"
	self.TAG2 = "tag2"
	self.TAG3 = "tag3"
	self.TAG4 = "tag4"
	
	self.TAG_INDEX_URL = reverse("epic.tags.views.index")
	
	self.TAG1_URL = reverse("epic.tags.views.view_items_for_tag",
							kwargs={ "tag_name": self.TAG1 })
	
	self.TAG2_URL = reverse("epic.tags.views.view_items_for_tag",
							kwargs={ "tag_name": self.TAG2 })
	
	self.TAG3_URL = reverse("epic.tags.views.view_items_for_tag",
							kwargs={ "tag_name": self.TAG3 })
	
	self.TAG4_URL = reverse("epic.tags.views.view_items_for_tag",
							kwargs={ "tag_name": self.TAG4 })
	
	self.DS1_URL = reverse("epic.datasets.views.view_dataset",
						   kwargs={ "item_id": self.ds1.id,
						   			"slug": self.ds1.slug, })
	
	self.DS2_URL = reverse("epic.datasets.views.view_dataset",
						   kwargs={ "item_id": self.ds2.id,
						   			"slug": self.ds2.slug, })

class ViewTestCase(TestCase):
	fixtures = [ "tags_just_users", "tags_tags" ]
	
	def setUp(self):
		common_setUp(self)
	
	def tearDown(self):
		pass
	
	def testTagsIndex(self):
		# Verify that all the tags show up on the index page
		response = self.client.get(self.TAG_INDEX_URL)
		self.assertEqual(response.status_code, 200)
		
		self.assertContains(response, 'href="%s' % self.TAG1_URL)
		self.assertContains(response, 'href="%s' % self.TAG2_URL)
		self.assertContains(response, 'href="%s' % self.TAG3_URL)
		self.assertContains(response, 'href="%s' % self.TAG4_URL)
		
	
	def testTagsPage(self):
		# Test the page for tag 1
		response = self.client.get(self.TAG1_URL)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response,'href="%s' % self.DS1_URL)
		self.assertNotContains(response,'href="%s' % self.DS2_URL)
		
		# Test the page for tag 2
		response = self.client.get(self.TAG2_URL)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response,'href="%s' % self.DS1_URL)
		self.assertNotContains(response,'href="%s' % self.DS2_URL)
		
		# Test the page for tag 3
		response = self.client.get(self.TAG3_URL)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response,'href="%s' % self.DS1_URL)
		self.assertContains(response,'href="%s' % self.DS2_URL)
		
		# Test the page for tag 4
		response = self.client.get(self.TAG4_URL)
		self.assertEqual(response.status_code, 200)
		self.assertNotContains(response,'href="%s' % self.DS1_URL)
		self.assertContains(response,'href="%s' % self.DS2_URL)
		
	def testDataSetPage(self):
		# Test the page for dataset 1
		response = self.client.get(self.DS1_URL)
		self.assertEqual(response.status_code, 200)
		
		self.assertContains(response,'href="%s' % self.TAG1_URL)
		self.assertContains(response,'href="%s' % self.TAG2_URL)
		self.assertContains(response,'href="%s' % self.TAG3_URL)
		self.assertNotContains(response,'href="%s' % self.TAG4_URL)
		
		# Test the page for dataset2
		response = self.client.get(self.DS2_URL)
		self.assertEqual(response.status_code, 200)
		
		self.assertNotContains(response,'href="%s' % self.TAG1_URL)
		self.assertNotContains(response,'href="%s' % self.TAG2_URL)
		self.assertContains(response,'href="%s' % self.TAG3_URL)
		self.assertContains(response,'href="%s' % self.TAG4_URL)
		
class ViewAddTagsTestCase(TestCase):
	fixtures = [ "tags_just_users", "tags_tags" ]
	
	def setUp(self):
		common_setUp(self)
	
		self.DATASETS_URL = reverse("epic.datasets.views.view_datasets")
		self.DS_TAGGED_URL = self.DS1_URL
		self.DS_NOT_TAGGED_URL = self.DS2_URL
		
		self.DS_TAGGED_ADD_TAGS_URL = reverse("epic.datasets.views.tag_dataset",
											  kwargs={ "item_id": self.ds1.id,
											  		   "slug":self.ds1.slug, })
		
		self.DS_NOT_TAGGED_ADD_TAGS_URL = reverse("epic.datasets.views.tag_dataset",
												  kwargs={ "item_id": self.ds2.id,
												  		   "slug":self.ds2.slug, })
	
	def tearDown(self):
		pass
	
	def testHelpTextOnDataSets(self):
		login = self.client.login(username="bob", password="bob")
		self.failUnless(login, "Could not login")
		
		response = self.client.get(self.DS_TAGGED_ADD_TAGS_URL)

		self.assertContains(response, 'Type your tags in the input field.')
	
	def testNoAddTagsOnDataSets(self):
		response = self.client.get(self.DATASETS_URL)
		
		self.assertNotContains(response, self.DS_NOT_TAGGED_ADD_TAGS_URL)
		self.assertNotContains(response, self.DS_TAGGED_ADD_TAGS_URL)
		
	def testAddTagsOnTaggedDataSetNotLoggedIn(self):
		response = self.client.get(self.DS_TAGGED_URL)
		
		self.assertNotContains(response, self.DS_NOT_TAGGED_ADD_TAGS_URL)
		self.assertNotContains(response, self.DS_TAGGED_ADD_TAGS_URL)
		
	def testAddTagsOnNotTaggedDataSetNotLoggedIn(self):
		response = self.client.get(self.DS_NOT_TAGGED_URL)
		
		self.assertNotContains(response, self.DS_NOT_TAGGED_ADD_TAGS_URL)
		self.assertNotContains(response, self.DS_TAGGED_ADD_TAGS_URL)
	
	def testAddTagsOnTaggedDataSet(self):
		login = self.client.login(username="bob", password="bob")
		self.failUnless(login, "Could not login")
		
		response = self.client.get(self.DS_TAGGED_URL)
		
		self.assertNotContains(response, self.DS_NOT_TAGGED_ADD_TAGS_URL)
		self.assertContains(response, self.DS_TAGGED_ADD_TAGS_URL)
		
	def testAddTagsOnNotTaggedDataSet(self):
		login = self.client.login(username="bob", password="bob")
		self.failUnless(login, "Could not login")
		
		response = self.client.get(self.DS_NOT_TAGGED_URL)
		
		self.assertContains(response, self.DS_NOT_TAGGED_ADD_TAGS_URL)
		self.assertNotContains(response, self.DS_TAGGED_ADD_TAGS_URL)