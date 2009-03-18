from django.test import TestCase

from epic.datasets.models import DataSet
from epic.datasets.models import DataSetFile
from django.utils.datastructures import MultiValueDict
from django.core.urlresolvers import reverse
from django.conf import settings
import os

class UploadTestCase(TestCase):
	fixtures = ['initial_data', 'single_dataset', 'initial_users']

	def testUploadingDatasetsRequiresLogin(self):
		response = self.client.get(reverse('epic.datasets.views.create_dataset'))
		#fail if no redirect
		self.failIf(response.status_code != 302)
		

	def testUploadingDatasets(self):		
		client = self.client
		
		#Log in	
		login_successful = client.login(username='bob', password='bob')
		self.assertTrue(login_successful)
		
		#make files for us to upload 
		current_dir = os.curdir
		test_file = open("test_file.txt", "w")
		test_file.write("This is a test file1")
		test_file.close()
		
		test_file2 = open("test_file2.txt", "w")
		test_file2.write("This is a test file2")
		test_file2.close()
		
		#create post data (file and metadata)
		test_file_to_read = open("test_file.txt", "r") #this time we open for reading
		test_file_to_read2 = open("test_file2.txt", "r")
		upload_form_data = {
				   'name': 'Dataset123', 
				   'description' : 'This is a pretty swell dataset',
				   'files[]' : [test_file_to_read, test_file_to_read2]}
		
		#upload file with basic metadata
		upload_response = client.post(reverse('epic.datasets.views.create_dataset'), upload_form_data)
		
		test_file_to_read.close()
		os.remove("test_file.txt")
		
		test_file_to_read2.close()
		os.remove("test_file2.txt")
		
		#find and test uploaded dataset
		datasets_like_our_upload = DataSet.objects.filter(name='Dataset123', description="This is a pretty swell dataset")
		self.assertEquals(len(datasets_like_our_upload), 1)
	
		our_dataset = datasets_like_our_upload[0]
	
		#find and test uploaded datasetfiles
		datasetfiles_like_our_upload = DataSetFile.objects.all()
		self.assertEquals(len(datasetfiles_like_our_upload), 2)
	
		our_datasetfile1 = datasetfiles_like_our_upload[0]
		our_datasetfile2 = datasetfiles_like_our_upload[1]
	
		#go to the page that displays the uploaded dataset, and check that it has the right name, description, and files
		view_dataset_response = client.get(reverse('epic.datasets.views.view_dataset', kwargs={'item_id':our_dataset.id,}))
		self.assertContains(view_dataset_response, "Dataset123")
		self.assertContains(view_dataset_response, "This is a pretty swell dataset")
		self.assertContains(view_dataset_response, our_datasetfile1.__unicode__())
		self.assertContains(view_dataset_response, our_datasetfile2.__unicode__())
		
		#actually download the files, and make sure they contain the text we originally put in them
		
		download_response_1 = client.get(our_datasetfile1.file_contents.url)
		download_response_2 = client.get(our_datasetfile2.file_contents.url)
		
		self.assertTrue(str(download_response_1).find("This is a test file1") != -1)
		self.assertTrue(str(download_response_2).find("This is a test file2") != -1)

		#remove files we uploaded (or throw an exception if they aren't in the right place)
		
		root = settings.MEDIA_ROOT
		
		self.assertEqual(our_datasetfile1.get_upload_to(), our_datasetfile2.get_upload_to())
		
		upload_subdir = our_datasetfile1.get_upload_to()
		upload_directory = os.path.join(root, upload_subdir)
	
		os.remove(os.path.join(upload_directory, "test_file.txt"))
		os.remove(os.path.join(upload_directory, "test_file2.txt"))
	
		os.rmdir(upload_directory)
			
			
	#TODO: eventually test simultaneous upload/download speeds on multiple big files
	