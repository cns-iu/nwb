import tempfile

from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.models import AcademicReference
from epic.core.test import CustomTestCase
from epic.datasets.models import DataSet

class AcademicReferenceTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.create_dataset_url = reverse('epic.datasets.views.create_dataset')
        
        self.bob = User.objects.get(username='bob') 
        
        
    def testReferenceAdded(self):
        # When uploading a valid academic reference, the model should be
        #    created in the database
        self.tryLogin('bob')
        
        test_file1 = tempfile.TemporaryFile()
        test_file2 = tempfile.TemporaryFile()
        
        test_file1.write("This is a test file1")
        test_file2.write("This is a test file2")
        
        test_file1.flush()
        test_file2.flush()

        test_file1.seek(0)
        test_file2.seek(0)
        
        post_data = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]' : [test_file1, test_file2],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1,
            'reference-0-reference': 'this is my reference!!!!!',
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        
        response = self.client.post(self.create_dataset_url, post_data)

        dataset = DataSet.objects.get(creator=self.bob, name=post_data['name'])
        acad_ref = AcademicReference.objects.get(item=dataset)
        self.assertTrue(acad_ref.reference, post_data['reference-0-reference'])
        
    def testMultipleReferencedAdded(self):
        # When uploading several valid academic reference, the models should be
        #    created in the database
        self.tryLogin('bob')
        
        test_file1 = tempfile.TemporaryFile()
        test_file2 = tempfile.TemporaryFile()
        
        test_file1.write("This is a test file1")
        test_file2.write("This is a test file2")
        
        test_file1.flush()
        test_file2.flush()

        test_file1.seek(0)
        test_file2.seek(0)
        
        post_data = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]' : [test_file1, test_file2],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 2,
            'reference-0-reference': 'this is my refernece',
            'reference-1-reference': 'asdf9234t sdg',
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        
        response = self.client.post(self.create_dataset_url, post_data)

        dataset = DataSet.objects.get(creator=self.bob, name=post_data['name'])
        
        try:
            AcademicReference.objects.get(item=dataset, reference=post_data['reference-0-reference'])
            AcademicReference.objects.get(item=dataset, reference=post_data['reference-1-reference'])
        except AcademicReference.ItemNotFound:
            self.fail()