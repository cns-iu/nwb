import os
import tempfile
import zipfile

from django.contrib.auth.models import User
from django.core.files.base import ContentFile
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.datasets.forms import AcademicReferenceForm
from epic.datasets.forms import AuthorForm
from epic.datasets.models import DataSet, DataSetFile


class UrlsTestCase(CustomTestCase):
    """ Test all the urls to make sure that the view for each works """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.dataset1 = DataSet.objects.get(
            creator=self.bob, name='dataset1', description='this is the first dataset')
        self.error_page_codes = [404, 500]
    
    def tearDown(self):
        pass
    
    def test_view_datasets(self):
        url = reverse('epic.datasets.views.view_datasets')
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
    def test_view_dataset(self):
        url = reverse('epic.datasets.views.view_dataset', kwargs={'item_id': self.dataset1.id,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
        url = reverse(
            'epic.datasets.views.view_dataset',
            kwargs={'item_id': self.dataset1.id, 'slug': self.dataset1.slug,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
    def test_view_user_dataset_list(self):
        url = reverse(
            'epic.datasets.views.view_user_dataset_list', kwargs={'user_id': self.bob.id,})
        response = self.client.get(url)
        
        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
    def create_dataset(self):
        url = reverse('epic.datasets.views.create_dataset')
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
    def test_edit_dataset(self):
        url = reverse('epic.datasets.views.edit_dataset', kwargs={'item_id': self.dataset1.id,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
        url = reverse(
            'epic.datasets.views.edit_dataset',
            kwargs={'item_id': self.dataset1.id, 'slug': self.dataset1.slug,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
    def test_rate_dataset(self):
        url = reverse('epic.datasets.views.rate_dataset', kwargs={'item_id': self.dataset1.id,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
            
        url = reverse(
            'epic.datasets.views.rate_dataset', 
            kwargs={'item_id': self.dataset1.id, 'slug': self.dataset1.slug,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
    def test_rate_dataset_using_input_rating(self):
        url = reverse(
            'epic.datasets.views.rate_dataset_using_input_rating', 
            kwargs={'item_id': self.dataset1.id, 'input_rating': 3,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
        url = reverse(
            'epic.datasets.views.rate_dataset_using_input_rating', 
            kwargs={'item_id': self.dataset1.id, 'slug': self.dataset1.slug, 'input_rating': 3,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
    def test_tag_dataset(self):
        url = reverse('epic.datasets.views.tag_dataset', kwargs={'item_id': self.dataset1.id,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
        
        url = reverse(
            'epic.datasets.views.tag_dataset', 
            kwargs={'item_id': self.dataset1.id, 'slug': self.dataset1.slug,})
        response = self.client.get(url)

        for code in self.error_page_codes:
            self.assertNotEqual(code, response.status_code)
    
        def test_delete_dataset_files(self):
            url = reverse(
                'epic.datasets.views.delete_dataset_files', 
                kwargs={'item_id': self.dataset1.id, 'slug': self.dataset1.slug})
            response = self.client.get(url)

            for code in self.error_page_codes:
                self.assertNotEqual(code, response.status_code)

class ViewDatasetTestCase(CustomTestCase):
	""" Test the view_dataset view """
	
	fixtures = ['just_users', 'datasets']
	
	def setUp(self):
		self.bob = User.objects.get(username='bob')
		self.admin = User.objects.get(username='admin')
		
		longDescription = '''Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur commodo, lacus at volutpat pellentesque, ipsum ligula hendrerit augue, eget molestie nisi elit in augue. Nullam et lorem. Fusce porta nunc eget massa. Phasellus mi urna, hendrerit eu, lobortis id, lobortis sit amet, tellus. Sed lacus. Duis nec sem. Quisque volutpat. Suspendisse nec sem. Praesent facilisis volutpat lacus. Nunc nec lectus at quam euismod mollis.

					Integer dapibus nunc in nisl. Donec aliquet. Sed facilisis, nibh nec euismod auctor, augue quam pharetra ipsum, vel tempor quam lacus nec tortor. Aenean feugiat pharetra augue. Vivamus molestie, orci vitae vehicula mattis, orci metus mollis leo, quis mollis augue est suscipit nisl. Aenean in arcu a dolor dictum tempus. Vivamus scelerisque metus. Suspendisse luctus, nisi et luctus egestas, tortor diam accumsan tortor, vitae gravida lorem leo vitae lacus. Cras vulputate. Proin aliquam dolor.
					
					Sed ac erat. Praesent elementum lorem eu quam. Donec egestas, elit eget dictum molestie, massa diam consectetur arcu, id ullamcorper risus magna nec nisi. Nulla eros urna, scelerisque vel, adipiscing ut, iaculis eu, lorem. Duis pellentesque arcu. Aliquam pharetra mi in purus. Etiam sit amet nibh. Suspendisse molestie. Aenean pellentesque. Mauris porttitor facilisis ipsum. Quisque est lectus, rutrum sit amet, consectetur vulputate, mattis at, nulla. Donec purus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Curabitur a justo. Maecenas non tellus ut ipsum laoreet cursus. Etiam in urna. Maecenas quis tortor quis odio tempus dignissim.
					
					Aenean accumsan, sem at tincidunt egestas, libero mi convallis ante, et ultricies nisi urna sit amet nulla. Suspendisse potenti. In leo. Nullam nec augue quis leo consectetur aliquam. Nulla luctus, nulla vitae malesuada pretium, nisi eros sagittis turpis, non pulvinar erat quam quis augue. Quisque nec massa sed elit rutrum interdum. Nam ante. Donec suscipit rhoncus massa. Fusce eleifend eleifend odio. Fusce nulla risus, sollicitudin eu, venenatis at, auctor sed, magna.
					
					Maecenas sit amet magna id metus elementum viverra. Duis bibendum turpis non turpis. Curabitur ac massa fringilla felis fermentum luctus. Integer in nunc sed mi cursus porttitor. Integer dapibus justo ac metus. Vestibulum dolor dui, vehicula sed, vulputate at, fringilla id, lorem. Suspendisse in massa sed enim vulputate congue. Aliquam est felis, varius luctus, faucibus in, blandit ac, dui. Mauris dapibus. Duis justo massa, convallis ac, hendrerit id, accumsan ut, erat. Nunc sollicitudin tristique purus. Phasellus diam. '''
					
		self.dataset1 = DataSet.objects.get(
            creator=self.bob, name='dataset1', description='this is the first dataset')
		self.dataset2 = DataSet.objects.create(
            creator=self.admin,
            name='dataset2',
            description='this is the second dataset',
            is_active=True)
		self.dataset3 = DataSet.objects.create(
            creator=self.admin,
            name='dataset3',
            description='this is the third dataset with short description',
            is_active=True)
		self.dataset4 = DataSet.objects.create(
            creator=self.admin, name='dataset4', description=longDescription, is_active=True)
		
		self.view_dataset_url_1 = reverse(
            'epic.datasets.views.view_dataset', kwargs={'item_id': self.dataset1.id,})
		self.view_dataset_url_2 = reverse(
            'epic.datasets.views.view_dataset',
            kwargs={'item_id': self.dataset2.id, 'slug': self.dataset2.slug,})
		self.view_dataset_url_3 = reverse(
            'epic.datasets.views.view_dataset', 
            kwargs={'item_id': self.dataset3.id, 'slug': self.dataset3.slug,})
		self.view_dataset_url_4 = reverse(
            'epic.datasets.views.view_dataset', 
            kwargs={'item_id': self.dataset4.id, 'slug': self.dataset4.slug,})
		
	def tearDown(self):
		pass
	
	def testLoggedOut(self):
		response = self.client.get(self.view_dataset_url_1)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset1.name)
		self.assertContains(response, self.dataset1.description)

		response = self.client.get(self.view_dataset_url_2)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset2.name)
		self.assertContains(response, self.dataset2.description)

	def testDescriptionVariations(self):
		'''
		Test used to test if for longer descriptions having more than 1000 characters 
		'Read the rest of this entry' option is displayed hiding rest of the characters.
		'''	
		
		readMoreMarker = '<span id="read-more-'
		
#		For Short Descriptions no need to display readMoreMarker
		response = self.client.get(self.view_dataset_url_3)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset3.name)
		self.assertContains(response, self.dataset3.description)
		self.assertNotContains(response, readMoreMarker)
		
#		For Longer Descriptions readMoreMarker has to be displayed
		response = self.client.get(self.view_dataset_url_4)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset4.name)
		self.assertContains(response, readMoreMarker)
				
	def testLoggedOut(self):
		response = self.client.get(self.view_dataset_url_1)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset1.name)
		self.assertContains(response, self.dataset1.description)
	
		response = self.client.get(self.view_dataset_url_2)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset2.name)
		self.assertContains(response, self.dataset2.description)	
		
	def testLoggedInNotOwner(self):
		self.tryLogin(username='admin', password='admin')
	
		response = self.client.get(self.view_dataset_url_1)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset1.name)
		self.assertContains(response, self.dataset1.description)
	
		response = self.client.get(self.view_dataset_url_2)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset2.name)
		self.assertContains(response, self.dataset2.description)
		
	def testLoggedInOwner(self):
		self.tryLogin(username='bob', password='bob')
		
		response = self.client.get(self.view_dataset_url_1)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset1.name)
		self.assertContains(response, self.dataset1.description)
	
		response = self.client.get(self.view_dataset_url_2)
		self.assertEqual(response.status_code, 200)
		self.assertContains(response, self.dataset2.name)
		self.assertContains(response, self.dataset2.description)
		
def save_n_datasets(user, n):
    for i in range(n):
        dataset = DataSet(creator=user)
    	dataset.name = 'test_dataset_name_' + str(i)
    	dataset.is_active = True
    	dataset.save()

class ViewDatasetsFitExactlyOnePageTestCase(CustomTestCase):
    fixtures = ['just_users']

    def setUp(self):
    	from views import PER_PAGE    	
    	save_n_datasets(User.objects.get(username="bob"), PER_PAGE)

    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.datasets.views.view_datasets'))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for dataset in DataSet.objects.all():
    		self.assertContains(response, dataset.get_absolute_url())
    	
class ViewDatasetsPartialPageTestCase(CustomTestCase):
    fixtures = ['just_users']

    def setUp(self):
    	from views import PER_PAGE
    	save_n_datasets(User.objects.get(username="bob"), PER_PAGE - 1)
    
    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.datasets.views.view_datasets'))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for dataset in DataSet.objects.all():
    		self.assertContains(response, dataset.get_absolute_url())
    	
class ViewDatasetsTwoPagesTestCase(CustomTestCase):
    fixtures = ['just_users']

    def setUp(self):
    	from views import PER_PAGE
    	self.per_page = PER_PAGE
    	save_n_datasets(User.objects.get(username="bob"), 2 * self.per_page)
    
    def testFirstPage(self):    	
    	response = self.client.get(reverse('epic.datasets.views.view_datasets'))

    	self.assertContains(response, '<div class="pagination"')
    	self.assertContains(response, 'href="?page=2">2')
    	self.assertContains(response, 'href="?page=2">next')
    	self.assertContains(response, 'href="?page=2">last')    	
    	self.assertNotContains(response, 'href="?page=1"')

    	datasets = DataSet.objects.active().order_by('-created_at')
    	on_first_page, on_second_page = datasets[:self.per_page], datasets[self.per_page:]
    	
    	for dataset in on_first_page:
    		self.assertContains(response, dataset.get_absolute_url())
    	
    	for dataset in on_second_page:
    		self.assertNotContains(response, dataset.get_absolute_url())
    
    def testSecondPage(self):
    	response = self.client.get(reverse('epic.datasets.views.view_datasets'), {'page': '2'})
    	
    	self.assertContains(response, '<div class="pagination"')
    	self.assertContains(response, 'href="?page=1">1')
    	self.assertNotContains(response, 'href="?page=2"')

    	datasets = DataSet.objects.active().order_by('-created_at')
    	on_first_page, on_second_page = datasets[:self.per_page], datasets[self.per_page:]
    	
    	for dataset in on_first_page:
    		self.assertNotContains(response, dataset.get_absolute_url())
    	
    	for dataset in on_second_page:
    		self.assertContains(response, dataset.get_absolute_url())

    
class ViewUserDatasetListTestCase(CustomTestCase):
    """ Test the view_user_dataset_list view """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset1 = DataSet.objects.get(creator=self.bob, 
                                            name='dataset1', 
                                            description='this is the first dataset')
        self.dataset2 = DataSet.objects.create(creator=self.admin, name='dataset2', 
                                               description='this is the second dataset', 
                                               is_active=True)
        
        self.view_user_dataset_list_url = reverse('epic.datasets.views.view_user_dataset_list', kwargs={'user_id':self.bob.id,})
        
    def tearDown(self):
        pass
    
    def testLoggedOut(self):
        response = self.client.get(self.view_user_dataset_list_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.dataset1.name)
        self.assertContains(response, self.dataset1.description)
        
        self.assertNotContains(response, self.dataset2.name)
        self.assertNotContains(response, self.dataset2.name)
    
    def testLoggedInNotOwner(self):
        self.tryLogin(username='admin', password='admin')
        
        response = self.client.get(self.view_user_dataset_list_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.dataset1.name)
        self.assertContains(response, self.dataset1.description)
        
        self.assertNotContains(response, self.dataset2.name)
        self.assertNotContains(response, self.dataset2.name)
        
    def testLoggedInOwner(self):
        self.tryLogin(username='bob', password='bob')
    
        response = self.client.get(self.view_user_dataset_list_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.dataset1.name)
        self.assertContains(response, self.dataset1.description)
        
        self.assertNotContains(response, self.dataset2.name)
        self.assertNotContains(response, self.dataset2.name)
        
class CreateDatasetTestCase(CustomTestCase):
    """ Test the create_dataset view """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset1 = DataSet.objects.get(creator=self.bob, 
                                            name='dataset1', 
                                            description='this is the first dataset')
        self.dataset2 = DataSet.objects.create(creator=self.admin, name='dataset2', 
                                               description='this is the second dataset', 
                                               is_active=True)
        
        self.create_dataset_url = reverse('epic.datasets.views.create_dataset')
        
        
        #make files for us to upload 

        test_file1 = tempfile.TemporaryFile()
        test_file2 = tempfile.TemporaryFile()
        
        test_file1.write("This is a test file1")
        test_file2.write("This is a test file2")
        
        test_file1.flush()
        test_file2.flush()

        test_file1.seek(0)
        test_file2.seek(0)

        self.post_data = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]' : [test_file1, test_file2],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1,
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        
    def tearDown(self):
        pass
    
    def testLoggedOut(self):
        response = self.client.get(self.create_dataset_url)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.post(self.create_dataset_url, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        for ds in DataSet.objects.active():
            self.assertNotEqual(ds.name,self.post_data['name'])
            self.assertNotEqual(ds.description,self.post_data['description'])
    
    def testLoggedIn(self):
        self.tryLogin(username='bob', password='bob')

        response = self.client.get(self.create_dataset_url)
        self.assertEqual(response.status_code, 200)
        
        response = self.client.post(self.create_dataset_url, self.post_data)
        self.assertEqual(response.status_code, 302, response.content)
        
        ds = DataSet.objects.get(name=self.post_data['name'], description=self.post_data['description'])
    
    def testBlankAuthor(self):
        # Posting '     ' for authors is not allowed.
        self.tryLogin('bob')

        response = self.client.get(self.create_dataset_url)
        self.assertEqual(response.status_code, 200)
        
        test_file1 = tempfile.TemporaryFile()
        test_file2 = tempfile.TemporaryFile()
        
        test_file1.write("This is a test file1")
        test_file2.write("This is a test file2")
        
        test_file1.flush()
        test_file2.flush()

        test_file1.seek(0)
        test_file2.seek(0)
        
        post_data_blank_author = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]' : [test_file1, test_file2],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 0,
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 1,
            'author-0-author': '     ',
        }
        
        response = self.client.post(self.create_dataset_url, post_data_blank_author)
        self.assertContains(response, AuthorForm.EMPTY_AUTHOR_ERROR_MESSAGE, count=1)
        
    def testBlankReference(self):
        # Posting '     ' for references is not allowed.
        self.tryLogin('bob')

        response = self.client.get(self.create_dataset_url)
        self.assertEqual(response.status_code, 200)
        
        test_file1 = tempfile.TemporaryFile()
        test_file2 = tempfile.TemporaryFile()
        
        test_file1.write("This is a test file1")
        test_file2.write("This is a test file2")
        
        test_file1.flush()
        test_file2.flush()

        test_file1.seek(0)
        test_file2.seek(0)
        
        post_data_blank_reference = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]' : [test_file1, test_file2],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1,
            'reference-0-reference': '          ',
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        
        response = self.client.post(self.create_dataset_url, post_data_blank_reference)
        self.assertContains(response, AcademicReferenceForm.EMPTY_REFERENCE_ERROR_MESSAGE, count=1)

class EditDatasetTestCase(CustomTestCase):
    """ Test the edit_dataset view """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset1 = DataSet.objects.get(creator=self.bob, 
                                            name='dataset1', 
                                            description='this is the first dataset')
        self.dataset2 = DataSet.objects.create(creator=self.admin, 
                                               name='dataset2', 
                                               description='this is the second dataset', 
                                               is_active=True)
        
        self.edit_url_1 = reverse('epic.datasets.views.edit_dataset', 
                                  kwargs={'item_id':self.dataset1.id,})
        self.edit_url_2 = reverse('epic.datasets.views.edit_dataset', 
                                  kwargs={'item_id':self.dataset1.id, 'slug':self.dataset1.slug,})

        self.post_data = {
            'name': '3456 345y,th[-k-0dfgh0 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1
        }
        
    def tearDown(self):
        pass
    
    def testLoggedOut(self):
        response = self.client.get(self.edit_url_1)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.post(self.edit_url_1, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        for ds in DataSet.objects.active():
            self.assertNotEqual(ds.name,self.post_data['name'])
            self.assertNotEqual(ds.description,self.post_data['description'])
            
        response = self.client.get(self.edit_url_2)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.post(self.edit_url_2, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        for ds in DataSet.objects.active():
            self.assertNotEqual(ds.name,self.post_data['name'])
            self.assertNotEqual(ds.description,self.post_data['description'])
        
    def testLoggedInNotOwner(self):
        self.tryLogin(username='admin', password='admin')
        
        response = self.client.get(self.edit_url_1)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.post(self.edit_url_1, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        for ds in DataSet.objects.active():
            self.assertNotEqual(ds.name,self.post_data['name'])
            self.assertNotEqual(ds.description,self.post_data['description'])
            
        response = self.client.get(self.edit_url_2)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.post(self.edit_url_2, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        for ds in DataSet.objects.active():
            self.assertNotEqual(ds.name,self.post_data['name'])
            self.assertNotEqual(ds.description,self.post_data['description'])
    
    def testLoggedInOwner(self):
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.edit_url_1)
        self.assertEqual(response.status_code, 200)
        
        response = self.client.post(self.edit_url_1, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        ds = DataSet.objects.get(name=self.post_data['name'], description=self.post_data['description'])
        
        response = self.client.get(self.edit_url_2)
        self.assertEqual(response.status_code, 200)
        
        response = self.client.post(self.edit_url_2, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        ds = DataSet.objects.get(name=self.post_data['name'], description=self.post_data['description'])
        
class DeleteDatasetFilesTestCase(CustomTestCase):
    """ Test the delete_dataset_files view """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset = DataSet.objects.get(creator=self.bob, 
                                           name='dataset1', 
                                           description='this is the first dataset')
        self.dataset_file = DataSetFile.objects.create(parent_dataset=self.dataset)
        self.delete_url = reverse('epic.datasets.views.delete_dataset_files', 
                                  kwargs={'item_id':self.dataset.id, 'slug':self.dataset.slug})

        self.post_data = {'confirmed': True}

    def testLoggedOut(self):
        # Logged out users cannot remove the files by either
        # getting or posting the url
        
        get_response = self.client.get(self.delete_url)
        self.assertTrue(self.dataset.files.all())
    
        post_response = self.client.post(self.delete_url, self.post_data)
        self.assertTrue(self.dataset.files.all())
        
    def testLoggedInNotOwner(self):
        # Non-owner users cannot remove the files by either
        # getting or posting the url
        
        self.tryLogin('bob2')
        get_response = self.client.get(self.delete_url)
        self.assertTrue(self.dataset.files.all())
            
        post_response = self.client.post(self.delete_url, self.post_data)
        self.assertTrue(self.dataset.files.all())
        
    def testLoggedInOwner(self):
        # The owner user can only remove the files by
        # posting the url, not getting
        
        self.tryLogin('bob')
        get_response = self.client.get(self.delete_url)
        self.assertTrue(self.dataset.files.all())
            
        post_response = self.client.post(self.delete_url, self.post_data)
        self.assertFalse(self.dataset.files.all())

class UnActiveDatasetTestCase(CustomTestCase):
    """  Test that is_active=False datasets are not visible 
        (An inactive dataset should never appear on any page)
    
    """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset = DataSet.objects.create(creator=self.bob, 
                                              name='asdfn83tn54yj', 
                                              description='this is the first dataset', 
                                              is_active=False)
        
        self.view_dataset_url = reverse('epic.datasets.views.view_dataset', 
                                        kwargs={'item_id':self.dataset.id, 'slug':self.dataset.slug,})
        self.view_datasets_url = reverse('epic.datasets.views.view_datasets')
        self.view_profile_url = reverse('epic.core.views.view_profile')

 
    def testLoggedOut(self):
        # An inactive dataset should never be visible for logged out users.
        
        response = self.client.get(self.view_dataset_url)
        self.assertNotContains(response, self.dataset.name)
        
        response = self.client.get(self.view_datasets_url)
        self.assertNotContains(response, self.dataset.name)
    
    def testLoggedInNotOwner(self):
        # An inactive dataset should never be visible to non-owners.
        
        self.tryLogin('bob2')
        
        response = self.client.get(self.view_dataset_url)
        self.assertNotContains(response, self.dataset.name)
        
        response = self.client.get(self.view_datasets_url)
        self.assertNotContains(response, self.dataset.name)
        
        response = self.client.get(self.view_profile_url)
        self.assertNotContains(response, self.dataset.name)
        
    def testLoggedInOwner(self):
        # An inactive dataset should never be visible to the owner.
        
        self.tryLogin('bob')
        
        response = self.client.get(self.view_dataset_url)
        self.assertNotContains(response, self.dataset.name)
        
        response = self.client.get(self.view_datasets_url)
        self.assertNotContains(response, self.dataset.name)
        
        response = self.client.get(self.view_profile_url)
        self.assertNotContains(response, self.dataset.name)
        
class UploadReadMePostTestCase(CustomTestCase):
    """  Test uploading a readme with the upload_readme view """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset = DataSet.objects.create(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset',
            is_active=False)
        
        self.view_dataset_url = reverse('epic.datasets.views.view_dataset', 
                                        kwargs={'item_id':self.dataset.id, 
                                                'slug':self.dataset.slug,})
        self.create_dataset_url = \
            reverse('epic.datasets.views.create_dataset')
        self.upload_readme_url = reverse('epic.datasets.views.upload_readme', 
                                         kwargs={'item_id':self.dataset.id, 
                                                 'slug':self.dataset.slug,})
        
        self.test_readme_file = open('readme.txt', 'w')
        self.test_readme_file.write("This is a valid test file")
        self.test_readme_file.close()
        self.test_readme_file = open('readme.txt', 'r')
        
        self.invalid_readme_file = open('r3456.txt', 'w')
        self.invalid_readme_file.write("This is an invalid test file")
        self.invalid_readme_file.close()
        self.invalid_readme_file = open('r3456.txt', 'r')
        
        self.upload_readme_file_post_data = {'readme': self.test_readme_file}
        self.upload_readme_file_invalid_post_data = \
            {'readme': self.invalid_readme_file}
    
    def tearDown(self):
        self.test_readme_file.close()
        self.invalid_readme_file.close()
        os.remove(self.test_readme_file.name)
        os.remove(self.invalid_readme_file.name)
    
    def testUploadInvalidReadme_loggedout(self):
        # A logged out user should not be able to upload an invalid readme
        
        response = self.client.post(self.upload_readme_url, 
                                    self.upload_readme_file_invalid_post_data)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset')
        self.assertFalse(self.dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
        
    def testUploadInvalidReadme_loggedin_notowner(self):
        # A user that does not own the dataset should not be able to upload an invalid readme
        self.tryLogin('bob2')
    
        response = self.client.post(self.upload_readme_url, 
                                    self.upload_readme_file_invalid_post_data)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset')
        self.assertFalse(self.dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
        
    def testUploadInvalidReadme_loggedin_owner(self):
        # The owner should not be able to upload an invalid readme
        self.tryLogin('bob')
    
        response = self.client.post(self.upload_readme_url, 
                                    self.upload_readme_file_invalid_post_data)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset',)
        self.assertFalse(self.dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
       
    def testUploadValidReadme_loggedout(self):
        # Anon users should not be able to uploadd a readme
        response = self.client.post(self.upload_readme_url, 
                                    self.upload_readme_file_post_data)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset',)
        self.assertFalse(self.dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
        
    def testUploadValidReadme_loggedin_notowner(self):
        # A non-owner should not be able to upload a readme
        self.tryLogin('bob2')
    
        response = self.client.post(self.upload_readme_url, 
                                    self.upload_readme_file_post_data)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset',)
        self.assertFalse(self.dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
        
    def testUploadValidReadme_loggedin_owner(self):
        # The owner should be albe to upload a valid readme
        self.tryLogin('bob')
    
        response = self.client.post(self.upload_readme_url, 
                                    self.upload_readme_file_post_data)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset',)
        self.assertTrue(self.dataset.is_active)
        self.assertTrue(self.dataset.files.filter(is_readme=True))

class UploadReadmeCreateDatasetsTestCase(CustomTestCase):
    """  Test uploading a compressed readme from the 
    create datasets page and the upload_readme page 
    
    """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset = DataSet.objects.create(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset', 
            is_active=False)
        
        self.view_dataset_url = reverse('epic.datasets.views.view_dataset',
                                        kwargs={'item_id':self.dataset.id,
                                                'slug':self.dataset.slug,})
        self.create_dataset_url = \
            reverse('epic.datasets.views.create_dataset')
        self.upload_readme_url = reverse('epic.datasets.views.upload_readme',
                                         kwargs={'item_id':self.dataset.id, 
                                                 'slug':self.dataset.slug,})
        
        self.test_readme_file = open('readme.txt', 'w')
        self.test_readme_file.write("This is a valid test file")
        self.test_readme_file.close()
        self.test_readme_file = open('readme.txt', 'r')
        
        self.invalid_readme_file = open('r3456.txt', 'w')
        self.invalid_readme_file.write("This is an invalid test file")
        self.invalid_readme_file.close()
        self.invalid_readme_file = open('r3456.txt', 'r')
        
        self.invalid_zip = zipfile.ZipFile('invalid.zip', 'w')
        self.valid_zip = zipfile.ZipFile('valid.zip', 'w')
        self.invalid_compressed = \
            self.invalid_zip.write(self.invalid_readme_file.name)
        self.compressed_readme = \
            self.valid_zip.write(self.test_readme_file.name)
        self.invalid_zip.close()
        self.valid_zip.close()
        
        self.invalid_zip = open('invalid.zip', 'r')
        self.valid_zip = open('valid.zip', 'r')   
    
    def tearDown(self):
        self.test_readme_file.close()
        self.invalid_readme_file.close()
        os.remove(self.test_readme_file.name)
        os.remove(self.invalid_readme_file.name)
        # TODO: get these from the objects, don't manually code them.
        self.invalid_zip.close()
        self.valid_zip.close()
        os.remove('invalid.zip')
        os.remove('valid.zip')
        
    def testUploadReadme_NoReadMe(self):
        # If no readme is uploaded, the dataset should not be active
        
        self.tryLogin('bob')
       
        post_data = {
            'readme' : self.invalid_readme_file                        
        }
        
        response = self.client.post(self.upload_readme_url, 
                                    post_data)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset')
        
        self.assertFalse(self.dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
    
    def testUploadReadme_ReadMe(self):
        # if a readme is uploaded as a file, the dataset should be active
        
        self.tryLogin('bob')
        post_data = {
            'readme' : self.test_readme_file 
        }
        response = self.client.post(self.upload_readme_url, 
                                    post_data)

        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset')
        
        self.assertTrue(self.dataset.is_active)
        self.assertTrue(self.dataset.files.filter(is_readme=True))
        
    def testUploadReadme_NoCompressedReadMe(self):
        # if a compressed file is uploaded the dataset should be inactive
        
        self.tryLogin('bob')
        post_data = {
            'readme' : self.invalid_zip     
        }
        response = self.client.post(self.upload_readme_url, 
                                    post_data)

        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset')
        
        self.assertFalse(self.dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
    
    def testUploadReadme_CompressedReadMe(self):
        # If a compressed file is uploaded and contains a readme file, 
        #     the dataset should be inactive
        
        self.tryLogin('bob')
        post_data = {
            'readme' : self.valid_zip
        }
        response = self.client.post(self.upload_readme_url, 
                                    post_data)

        self.dataset = DataSet.objects.get(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset')
        
        self.assertFalse(self.dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
    
    def testCreate_NoCompressedReadMe(self):
        # If a dataset is created with a compressed file that does not 
        #     contain a readme, the dataset should be inactive
        
        self.tryLogin('bob')
        post_data = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]': [self.invalid_zip],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1,
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        response = self.client.post(self.create_dataset_url, 
                                    post_data)
        
        dataset = DataSet.objects.get(
            creator=self.bob, 
            name=post_data['name'], 
            description=post_data['description'])
        self.assertFalse(dataset.is_active)
        self.assertFalse(dataset.files.filter(is_readme=True))
        
    
    def testCreate_CompressedReadMe(self):
        # If a dataset is created with a compressed file that does contain 
        #     a readme, the dataset should be active
        
        self.tryLogin('bob')
        post_data = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]': [self.valid_zip],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1,
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        response = self.client.post(self.create_dataset_url, 
                                    post_data)

        dataset = DataSet.objects.get(
            creator=self.bob, 
            name=post_data['name'], 
            description=post_data['description'])
        self.assertTrue(dataset.is_active)
        self.assertTrue(dataset.files.filter(is_readme=True))
        
    def testCreate_NoReadMe(self):
        # A dataset created without a readme should be inactive
        
        self.tryLogin('bob')
        post_data = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]': [self.invalid_readme_file],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1,
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        response = self.client.post(self.create_dataset_url, 
                                    post_data)

        dataset = DataSet.objects.get(
            creator=self.bob, 
            name=post_data['name'], 
            description=post_data['description'])
        self.assertFalse(dataset.is_active)
        self.assertFalse(self.dataset.files.filter(is_readme=True))
        
    def testCreate_NoReadMe(self):
        # A dataset created with a readme should be active
        
        self.tryLogin('bob')
        post_data = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'files[]': [self.test_readme_file],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1,
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        response = self.client.post(self.create_dataset_url, 
                                    post_data)

        dataset = DataSet.objects.get(
            creator=self.bob, 
            name=post_data['name'], 
            description=post_data['description'])
        self.assertTrue(dataset.is_active)
        self.assertTrue(dataset.files.filter(is_readme=True))
        
class DownloadAllTestCase(CustomTestCase):
    """  Test the functionality of download all
             TODO: This needs to actually add files and try to download them
        
    """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')   
        
        self.dataset = DataSet.objects.create(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset',
            is_active=True)
        
        self.datasetfile = DataSetFile.objects.create(
            parent_dataset=self.dataset,
            file_contents='asdfhnasdf')
        
        self.view_dataset_url = reverse('epic.datasets.views.view_dataset', 
                                        kwargs={'item_id':self.dataset.id, 
                                                'slug':self.dataset.slug,})
        
        self.download_all_url = reverse('epic.datasets.views.download_all_files', 
                                        kwargs={'item_id':self.dataset.id, 
                                                'slug':self.dataset.slug,})
        
    def testDownloadAll_loggedout(self):
        response = self.client.get(self.view_dataset_url)
        
        self.assertNotContains(response, self.download_all_url)
        
    def testDownloadAll_notowner(self):
        self.tryLogin('bob2')
        
        response = self.client.get(self.view_dataset_url)
        self.assertContains(response, self.download_all_url)
        
    def testDownloadAll_owner(self):
        self.tryLogin('bob')
        response = self.client.get(self.view_dataset_url)
        
        self.assertContains(response, self.download_all_url)

class PreviousNextTestCase(CustomTestCase):
    """  Test the functionality of there being next and previous datasets
        
    """
    
    fixtures = ['just_users', 'datasets']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')   
        
        self.create_dataset_url = reverse('epic.datasets.views.create_dataset')
        
        self.old_dataset = DataSet.objects.get(creator=self.admin, 
                                               name='dataset2', 
                                               description='this is the second dataset')
           
        self.view_old_dataset_url = reverse('epic.datasets.views.view_dataset', 
                                        kwargs={'item_id':self.old_dataset.id, 
                                                'slug':self.old_dataset.slug,})
        
        import tempfile
        #make files for us to upload 

        test_file1 = tempfile.TemporaryFile()
        test_file1.write("This is a test file1")        
        test_file1.flush()
        test_file1.seek(0)

        
        self.post_data = {
            'name': 'This is a new dataset 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'previous_version': self.old_dataset.id,
            'files[]' : [test_file1],
            'remove-INITIAL_FORMS': 0,
            'add-INITIAL_FORMS': 0,
            'add-TOTAL_FORMS': 0,
            'remove-TOTAL_FORMS': 0,
            'reference-INITIAL_FORMS': 0,
            'reference-TOTAL_FORMS': 1,
            'author-INITIAL_FORMS': 0,
            'author-TOTAL_FORMS': 0,
        }
        
    def test_loggedout(self):
        # Verify that logged out users can't cause the old dataset to 
        # have a new version.
        
        response = self.client.post(self.create_dataset_url, self.post_data)
        
        self.old_dataset = DataSet.objects.get(creator=self.admin, 
                                               name='dataset2', 
                                               description='this is the second dataset')
        self.assertFalse(self.old_dataset.next_version)
    
    def test_notowner(self):
        # Verify that users who don't own an old dataset can't cause
        # the old dataset to have a new version.
        
        self.tryLogin('bob')
        response = self.client.post(self.create_dataset_url, self.post_data)

        self.old_dataset = DataSet.objects.get(creator=self.admin, 
                                               name='dataset2', 
                                               description='this is the second dataset')
        self.assertFalse(self.old_dataset.next_version)
    
    def test_owner(self):
        # Verify that users when you upload a new version of a dataset,
        # that the old version points to the new one, and the new one 
        # points to the old one.
        
        self.tryLogin('admin')
        response = self.client.post(self.create_dataset_url, self.post_data)

        self.old_dataset = DataSet.objects.get(creator=self.admin, 
                                               name='dataset2', 
                                               description='this is the second dataset')
        self.assertTrue(self.old_dataset.next_version)
        self.assertEqual(self.old_dataset.next_version.previous_version, self.old_dataset)