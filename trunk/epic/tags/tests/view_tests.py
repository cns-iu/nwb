from django.test import TestCase

from django.contrib.auth.models import User

class ViewTests(TestCase):
	fixtures = ['tags']
	
	def setUp(self):
		pass
	
	def tearDown(self):
		pass
	
	def testTagsIndex(self):
		# Verify that all the tags show up on the index page
		response = self.client.get('/tags/')
		self.assertEqual(response.status_code, 200)
		self.assertTrue('<a href="/tags/tag1/">tag1</a>' in response.content, response.content)
		self.assertTrue('<a href="/tags/tag2/">tag2</a>' in response.content, response.content)
		self.assertTrue('<a href="/tags/tag3/">tag3</a>' in response.content, response.content)
		self.assertTrue('<a href="/tags/tag4/">tag4</a>' in response.content, response.content)
	
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
		self.assertTrue('<a href="/tags/tag1/">tag1</a>' in response.content, response.content)
		self.assertTrue('<a href="/tags/tag2/">tag2</a>' in response.content, response.content)
		self.assertTrue('<a href="/tags/tag3/">tag3</a>' in response.content, response.content)
		self.assertTrue('<a href="/tags/tag4/">tag4</a>' not in response.content, response.content)
		# Test the page for dataset2
		response = self.client.get('/datasets/2/')
		self.assertEqual(response.status_code, 200)
		self.assertTrue('<a href="/tags/tag1/">tag1</a>' not in response.content, response.content)
		self.assertTrue('<a href="/tags/tag2/">tag2</a>' not in response.content, response.content)
		self.assertTrue('<a href="/tags/tag3/">tag3</a>' in response.content, response.content)
		self.assertTrue('<a href="/tags/tag4/">tag4</a>' in response.content, response.content)