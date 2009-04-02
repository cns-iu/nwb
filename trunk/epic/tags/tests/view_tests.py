from django.test import TestCase
from django.core.urlresolvers import reverse
from django.contrib.auth.models import User
from epic.tags.models import Tagging
from epic.datasets.models import DataSet

class ViewTests(TestCase):
	
	def setUp(self):
		# DS1 is created by peebs and is tagged tag1-peebs, tag2-peebs
		self.peebs = User.objects.get(username="peebs")
		self.ds1 = DataSet.objects.create(creator=self.peebs, name="ds1", slug="ds1", description="dataset number one")

		# DS2 is created by admin and is tagged tag3-admin, tag4-peebs
		self.admin = User.objects.get(username="admin")
		self.ds2 = DataSet.objects.create(creator=self.admin, name="ds2", slug="ds2", description="dataset number two")
		
		self.tag1 = Tagging.objects.create(item=self.ds1, tag="tag1", user=self.peebs)
		self.tag2 = Tagging.objects.create(item=self.ds1, tag="tag2", user=self.peebs)
		self.tag3 = Tagging.objects.create(item=self.ds2, tag="tag3", user=self.admin)
		self.tag4 = Tagging.objects.create(item=self.ds2, tag="tag4", user=self.peebs)
		
		self.tag_index_url = reverse('epic.tags.views.index')
		self.tag1_url = reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag1.tag})
		self.tag2_url = reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag2.tag})
		self.tag3_url = reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag3.tag})
		self.tag4_url = reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag4.tag})
	
		self.ds1 = DataSet.objects.get(pk=1)
		self.ds2 = DataSet.objects.get(pk=2)
		self.ds1_url = reverse('epic.datasets.views.view_dataset', kwargs={'item_id': self.ds1.id, 'slug': self.ds1.slug,})
		self.ds2_url = reverse('epic.datasets.views.view_dataset', kwargs={'item_id': self.ds2.id, 'slug': self.ds2.slug,})
	
	def tearDown(self):
		pass
	
	def testTagsIndex(self):
		# Verify that all the tags show up on the index page
		response = self.client.get(self.tag_index_url)
		self.assertEqual(response.status_code, 200)
		
		self.assertContains(response, '<a href="' + self.tag1_url)
		self.assertContains(response, '<a href="' + self.tag2_url)
		self.assertContains(response, '<a href="' + self.tag3_url)
		self.assertContains(response, '<a href="' + self.tag4_url)
		
	
	def testTagsPage(self):
		# Test the page for tag 1
		response = self.client.get(self.tag1_url)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response,'<a href="' + self.ds1_url)
		self.assertNotContains(response,'<a href="' + self.ds2_url)
		
		# Test the page for tag 2
		response = self.client.get(self.tag2_url)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response,'<a href="' + self.ds1_url)
		self.assertNotContains(response,'<a href="' + self.ds2_url)
		
		# Test the page for tag 3
		response = self.client.get(self.tag3_url)
		self.assertEqual(response.status_code, 200)
		self.assertNotContains(response,'<a href="' + self.ds1_url)
		self.assertContains(response,'<a href="' + self.ds2_url)
		
		# Test the page for tag 4
		response = self.client.get(self.tag4_url)
		self.assertEqual(response.status_code, 200)
		self.assertNotContains(response,'<a href="' + self.ds1_url)
		self.assertContains(response,'<a href="' + self.ds2_url)
		
	def testDataSetPage(self):
		# Test the page for dataset 1
		response = self.client.get(self.ds1_url)
		self.assertEqual(response.status_code, 200)
		
		self.assertContains(response,'<a href="' + self.tag1_url)
		self.assertContains(response,'<a href="' + self.tag2_url)
		self.assertNotContains(response,'<a href="' + self.tag3_url)
		self.assertNotContains(response,'<a href="' + self.tag4_url)
		
		# Test the page for dataset2
		response = self.client.get(self.ds2_url)
		self.assertEqual(response.status_code, 200)
		
		self.assertNotContains(response,'<a href="' + self.tag1_url)
		self.assertNotContains(response,'<a href="' + self.tag2_url)
		self.assertContains(response,'<a href="' + self.tag3_url)
		self.assertContains(response,'<a href="' + self.tag4_url)