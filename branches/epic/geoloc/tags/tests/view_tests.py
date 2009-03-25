from django.test import TestCase
from django.core.urlresolvers import reverse
from django.contrib.auth.models import User
from epic.tags.models import Tagging

class ViewTests(TestCase):
	fixtures = ['tags']
	
	def setUp(self):
		self.tag1 = Tagging.objects.get(pk=1)
		self.tag2 = Tagging.objects.get(pk=2)
		self.tag3 = Tagging.objects.get(pk=3)
		# Fixtures are broken tag3 is in twice
		self.tag4 = Tagging.objects.get(pk=5)
	
	def tearDown(self):
		pass
	
	def testTagsIndex(self):
		# Verify that all the tags show up on the index page
		response = self.client.get('/tags/')
		self.assertEqual(response.status_code, 200)
		
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag1.tag}) +'"')
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag2.tag}) +'"')
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag3.tag}) +'"')
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag4.tag}) +'"')
		
	
	def testTagsPage(self):
		# Test the page for tag 1
		response = self.client.get('/tags/tag1/')
		self.assertEqual(response.status_code, 200)
		self.assertTrue('<a href="/datasets/1/">Dataset ds1</a>' in response.content, response.content)
		self.assertTrue('<a href="/datasets/2/">Dataset ds2</a>' not in response.content, response.content)
		# Test the page for tag 2
		response = self.client.get('/tags/tag2/')
		self.assertEqual(response.status_code, 200)
		self.assertTrue('<a href="/datasets/1/">Dataset ds1</a>' in response.content, response.content)
		self.assertTrue('<a href="/datasets/2/">Dataset ds2</a>' not in response.content, response.content)
		# Test the page for tag 3
		response = self.client.get('/tags/tag3/')
		self.assertEqual(response.status_code, 200)
		self.assertTrue('<a href="/datasets/1/">Dataset ds1</a>' in response.content, response.content)
		self.assertTrue('<a href="/datasets/2/">Dataset ds2</a>' in response.content, response.content)
		# Test the page for tag 4
		response = self.client.get('/tags/tag4/')
		self.assertEqual(response.status_code, 200)
		self.assertTrue('<a href="/datasets/1/">Dataset ds1</a>' not in response.content, response.content)
		self.assertTrue('<a href="/datasets/2/">Dataset ds2</a>' in response.content, response.content)
	
	def testDataSetPage(self):
		# Test the page for dataset 1
		response = self.client.get('/datasets/1/')
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag1.tag}) +'"')
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag2.tag}) +'"')
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag3.tag}) +'"')
		self.assertNotContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag4.tag}) +'"')
		
		# Test the page for dataset2
		response = self.client.get('/datasets/2/')
		self.assertEqual(response.status_code, 200)
		self.assertNotContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag1.tag}) +'"')
		self.assertNotContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag2.tag}) +'"')
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag3.tag}) +'"')
		self.assertContains(response, '<a href="'+ reverse('epic.tags.views.view_items_for_tag', kwargs={'tag_name': self.tag4.tag}) +'"')