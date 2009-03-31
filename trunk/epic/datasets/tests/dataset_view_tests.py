from django.test import TestCase
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from epic.datasets.models import DataSet
from epic.datasets.models import DataSetFile

class IndexTestCase(TestCase):
	fixtures = ['initial_data', 'single_dataset', 'initial_users']
	
	TEST_FILE_NAME = "test_file.txt"
	def setUp(self):
		self.data_set = DataSet.objects.all()[0]

	def tearDown(self):
		pass
		
	
	def testIndex(self):
		index_url = reverse("epic.datasets.views.view_datasets")
		response = self.client.get(index_url)
		self.failUnlessEqual(response.status_code, 200, "Error listing datasets!")
		data = response.context[0]['datasets'] #due to template inheritance
		self.failUnlessEqual(len(data), 1, "We don't have as many datasets as we expect!")
		self.failUnlessEqual(data[0].name, "Important Data", "The default data we loaded is named incorrectly!")
		self.failUnless("Important Data" in response.content, "Data set names aren't displayed on the list page!")
		
class ViewDataSetTestCase(TestCase):
	fistures = ['initial_data']
	
	def testExistance(self):
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		
		# Go to the page
		view_dataset_url = reverse("epic.datasets.views.view_dataset", args=[], kwargs={'item_id':ds1.id, 'slug':ds1.slug})
		response = self.client.get(view_dataset_url)
		self.failUnlessEqual(response.status_code, 200)
		
		# Check for the values on the page
		self.assertTrue(ds1.name in response.content)
		self.assertTrue(ds1.description in response.content)
		self.assertTrue("Reference:" in response.content)
		
class SlugTestCase(TestCase):
	fixtures = ['initial_data']
	
	def setUp(self):
		pass

	def tearDown(self):
		pass
		
	
	def testNavigationWithoutSlug(self):
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", creator=peebs)
		
		view_dataset_url = reverse("epic.datasets.views.view_dataset", args=[], kwargs={'item_id':ds2.id})
		response = self.client.get(view_dataset_url)
		self.failUnlessEqual(response.status_code, 200)
		
	def testNavigationWithSlug(self):
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", creator=peebs)
		
		view_dataset_url = reverse("epic.datasets.views.view_dataset", args=[], kwargs={'item_id':ds1.id, 'slug':ds1.slug})
		response = self.client.get(view_dataset_url)
		self.failUnlessEqual(response.status_code, 200)
	
	def testNavigationWithAnySlug(self):
		admin = User.objects.get(username="admin")
		peebs = User.objects.get(username="peebs")
		ds1 = DataSet.objects.create(name="Important Data", description="A very important piece of data", slug="important-data", creator=admin)
		ds2 = DataSet.objects.create(name="dataset", description="description", creator=peebs)
		
		view_dataset_url = reverse("epic.datasets.views.view_dataset", args=[], kwargs={'item_id':ds2.id, 'slug':'this-could-be-any-slug',})
		response = self.client.get(view_dataset_url)
		self.failUnlessEqual(response.status_code, 200)