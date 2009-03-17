from django.test import TestCase

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
		response = self.client.get('/datasets/')
		self.failUnlessEqual(response.status_code, 200, "Error listing datasets!")
		data = response.context[0]['datasets'] #due to template inheritance
		self.failUnlessEqual(len(data), 1, "We don't have as many datasets as we expect!")
		self.failUnlessEqual(data[0].name, "Important Data", "The default data we loaded is named incorrectly!")
		self.failUnless("Important Data" in response.content, "Data set names aren't displayed on the list page!")