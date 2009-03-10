from django.test import TestCase

from epic.datasets.models import DataSet
from epic.datasets.models import DataSetFile

class IndexTestCase(TestCase):
	fixtures = ['initial_data', 'single_dataset', 'initial_users']
	
	TEST_FILE_NAME = "test_file.txt"
	def setUp(self):
		self.data_set = DataSet.objects.all()[0]
		
		test_file = open(self.TEST_FILE_NAME, "w")
		test_file.write("This is a test file")
		test_file.close()
		self.test_file = test_file
		
	def tearDown(self):
		import os
		os.remove(self.TEST_FILE_NAME)
		
	
	def testIndex(self):
		response = self.client.get('/datasets/')
		self.failUnlessEqual(response.status_code, 200, "Error listing datasets!")
		data = response.context[0]['datasets'] #due to template inheritance
		self.failUnlessEqual(len(data), 1, "We don't have as many datasets as we expect!")
		self.failUnlessEqual(data[0].name, "Important Data", "The default data we loaded is named incorrectly!")
		self.failUnless("Important Data" in response.content, "Data set names aren't displayed on the list page!")
	
	def testRatingView(self):
		response = self.client.get('/datasets/')
		self.failUnlessEqual(response.status_code, 200, "Error listing datasets!")
		self.failUnless("--" in response.content, "There should be unrated data")
		response = self.client.post("/login/", {'username': 'bob', 'password':'bob2',})
		response = self.client.get('/datasets/')
		self.failUnlessEqual(response.status_code, 200, "Error listing datasets!")
		self.failUnless("--" in response.content, "There should be unrated data")
	
		
	UPLOAD_URL = "/datasets/new/"
	VIEW_DATASETS_URL = "/datasets/"
	
	
	def testUploadingDatasetsRequiresLogin(self):
		from django.test.client import Client
		
		client = Client(REMOTE_ADDR='127.0.0.1')
		response = client.get(self.UPLOAD_URL)
		#fail if no redirect
		self.failIf(response.status_code != 301 and response.status_code != 302)
		
	def testUploadingDatasets(self):
		from django.test.client import Client
		
		client = Client(REMOTE_ADDR='127.0.0.1')
		
		#Log in	
		login_successful = client.login(username='bob', password='bob')
		self.assertTrue(login_successful)
		#Create a new dataset
		upload_form = {
					   'name': 'Dataset123', 
					   'description' : 'This is a pretty swell dataset',
					   'file' : open(self.TEST_FILE_NAME)}
		upload_response = client.post(self.UPLOAD_URL, upload_form)
		
		#find and test uploaded dataset
		datasets_like_our_upload = DataSet.objects.filter(name='Dataset123', description="This is a pretty swell dataset")
		self.assertEquals(len(datasets_like_our_upload), 1)
		
		our_dataset = datasets_like_our_upload[0]
		
		#find and test uploaded datasetfile
		datasetfiles_like_our_upload = DataSetFile.objects.filter(parent_dataset=our_dataset)
		self.assertEquals(len(datasetfiles_like_our_upload), 1)
		
		our_datasetfile = datasetfiles_like_our_upload[0]
		
		#go to the page that displays the uploaded dataset, and check that it has the right name, description, and file
		our_dataset_url = self.VIEW_DATASETS_URL + str(our_dataset.id) + "/" #this will break if we put the viewing a specific dataset URL somewhere els
		view_dataset_response = client.get(our_dataset_url)
		self.assertContains(view_dataset_response, "Dataset123")
		self.assertContains(view_dataset_response, "This is a pretty swell dataset")
		self.assertContains(view_dataset_response, our_datasetfile.__unicode__())
		
	#TODO: eventually test simultaneous upload/download speeds on multiple big files
	
	def testRating(self):
		from django.test.client import Client
		#TODO remove once we use a version that is past r9847
		# http://code.djangoproject.com/changeset/9847
		# http://code.djangoproject.com/ticket/8551
		c = Client(REMOTE_ADDR='127.0.0.1')
		# Make sure there is no rating
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)
		self.failIf("Rating:" in response.content)
		# Rate
		rate_location = dataset_location + "rate/3/"
		response = c.get(rate_location)
		self.failUnlessEqual(response.status_code, 302)
		# Make sure there still no rating because anon people can't rate
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)
		self.failIf("Rating:" in response.content)
		# Log in
		login = c.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		# Rate
		rate_location = dataset_location + "rate/3/"
		response = c.get(rate_location)
		self.failUnlessEqual(response.status_code, 302)
		# Make sure bob's rating counted
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)
		self.failUnless("Rating:" in response.content)
		self.failUnless("3" in response.content)
		# Log in as bob2
		login = c.login(username='bob2', password='bob2')
		self.failUnless(login, 'Could not login')
		# Rate
		rate_location = dataset_location + "rate/5/"
		response = c.get(rate_location)
		self.failUnlessEqual(response.status_code, 302)
		# Make sure bob2's rating counted
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)
		self.failUnless("Rating:" in response.content)
		self.failUnless("4" in response.content)
		# Rate AGIAN!
		rate_location = dataset_location + "rate/5/"
		response = c.get(rate_location)
		self.failUnlessEqual(response.status_code, 302)
		# Make sure bob2's second rating didn't count
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)
		self.failUnless("Rating:" in response.content)
		self.failUnless("4" in response.content)