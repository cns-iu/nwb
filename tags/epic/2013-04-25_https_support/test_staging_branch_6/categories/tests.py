from django.core.urlresolvers import reverse
from django.contrib.auth.models import User

from epic.categories.constants import NO_CATEGORY
from epic.categories.models import Category
from epic.categories.models import CannotDeleteNoCategoryException
from epic.categories.models import default_category
from epic.categories.views import _get_datarequests_for_category
from epic.categories.views import _get_datasets_for_category
from epic.categories.views import _get_projects_for_category
from epic.core.test import CustomTestCase
from epic.core.util.view_utils import *
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project


class ViewCategoriesTestCase(CustomTestCase):
    def setUp(self):
        self.view_categories_url = reverse('epic.categories.views.view_categories')
    
    def testNoCategoriesExist(self):
        Category.objects.all().delete()
        
        response = self.client.get(self.view_categories_url)
        self.assertContains(response, 'There are currently no categories available.')
    
    def testCategoriesExist(self):
        category = Category.objects.create(name='Test Category', description='Test Description')
        
        response = self.client.get(self.view_categories_url)
        self.assertContains(response, category.name)


def save_n_data_requests_with_category(user, n, category):    	
    for i in range(n):
    	data_request = DataRequest(creator=user)
    	data_request.name = 'test_data_request_name_' + str(i)
    	data_request.is_active = True
    	data_request.category = category
    	data_request.save()	

class ViewDatarequestsForCategoryPaginatedTestCase(CustomTestCase):
    fixtures = ['core_just_users']

    def setUp(self):
    	from epic.datarequests.views import PER_PAGE
    	self.good_category = Category(id=2359237, name='test_good_category')
    	self.good_category.save()
    	self.bad_category = Category(id=6822349, name='test_bad_category')
    	self.bad_category.save()
    	save_n_data_requests_with_category(User.objects.get(username="bob"), PER_PAGE, self.good_category)
    	save_n_data_requests_with_category(User.objects.get(username="bob"), 7, self.bad_category)    	
    
    def testPaginated(self):	
    	response = self.client.get(reverse('epic.categories.views.view_datarequests_for_category', kwargs={'category_id': self.good_category.id}))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for data_request in _get_datarequests_for_category(self.good_category):
    		self.assertContains(response, data_request.get_absolute_url())
        for data_request in _get_datarequests_for_category(self.bad_category):
    		self.assertNotContains(response, data_request.get_absolute_url())


def save_n_datasets_with_category(user, n, category):    	
    for i in range(n):
    	dataset = DataSet(creator=user)
    	dataset.name = 'test_dataset_name_' + str(i)
    	dataset.is_active = True
    	dataset.category = category
    	dataset.save()	

class ViewDatasetsForCategoryPaginatedTestCase(CustomTestCase):
    fixtures = ['core_just_users']

    def setUp(self):
    	from epic.datasets.views import PER_PAGE
    	self.good_category = Category(id=2359237, name='test_good_category')
    	self.good_category.save()
    	self.bad_category = Category(id=6822349, name='test_bad_category')
    	self.bad_category.save()
    	save_n_datasets_with_category(User.objects.get(username="bob"), PER_PAGE, self.good_category)
    	save_n_datasets_with_category(User.objects.get(username="bob"), 7, self.bad_category)    	
    
    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.categories.views.view_datasets_for_category', kwargs={'category_id': self.good_category.id}))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for dataset in _get_datasets_for_category(self.good_category):
    		self.assertContains(response, dataset.get_absolute_url())
        for dataset in _get_datasets_for_category(self.bad_category):
    		self.assertNotContains(response, dataset.get_absolute_url())


def save_n_projects_with_category(user, n, category):    	
    for i in range(n):
    	project = Project(creator=user)
    	project.name = 'test_project_name_' + str(i)
    	project.is_active = True
    	project.category = category
    	project.save()

class ViewProjectsForCategoryPaginatedTestCase(CustomTestCase):
    fixtures = ['core_just_users']

    def setUp(self):
    	from epic.projects.views import PER_PAGE
    	self.good_category = Category(id=2359237, name='test_good_category')
    	self.good_category.save()
    	self.bad_category = Category(id=6822349, name='test_bad_category')
    	self.bad_category.save()
    	save_n_projects_with_category(User.objects.get(username="bob"), PER_PAGE, self.good_category)
    	save_n_projects_with_category(User.objects.get(username="bob"), 7, self.bad_category)    	
    
    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.categories.views.view_projects_for_category', kwargs={'category_id': self.good_category.id}))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for project in _get_projects_for_category(self.good_category):
    		self.assertContains(response, project.get_absolute_url())
        for project in _get_projects_for_category(self.bad_category):
    		self.assertNotContains(response, project.get_absolute_url())


class ViewItemsForCategoryTestCase(CustomTestCase):
    
    fixtures = ['categories_categories']
    
    def setUp(self):
        self.category1 = Category.objects.get(name='Test Category1')
        self.datasets = DataSet.objects.filter(category=self.category1)
        self.projects = Project.objects.filter(category=self.category1)
        self.datarequests = DataRequest.objects.filter(category=self.category1)
        
        self.view_all_items_url = reverse(
            'epic.categories.views.view_items_for_category',
            kwargs={'category_id': self.category1.id})
        
        self.view_datasets_url = reverse(
            'epic.categories.views.view_datasets_for_category',
            kwargs={'category_id': self.category1.id})
        
        self.view_projects_url = reverse(
            'epic.categories.views.view_projects_for_category',
            kwargs={'category_id': self.category1.id})
        
        self.view_datarequests_url = reverse(
            'epic.categories.views.view_datarequests_for_category',
            kwargs={'category_id': self.category1.id})
    
    def testInvalidCategory(self):
        invalid_all_items_for_category_url = reverse(
            'epic.categories.views.view_items_for_category',
            kwargs={'category_id': 1337})
        all_items_response = self.client.get(invalid_all_items_for_category_url)
        self.assertStatusCodeIsAFailure(all_items_response.status_code)
        
        invalid_datasets_for_category_url = reverse(
            'epic.categories.views.view_datasets_for_category',
            kwargs={'category_id': 1337})
        datasets_response = self.client.get(invalid_datasets_for_category_url)
        self.assertStatusCodeIsAFailure(datasets_response.status_code)
        
        invalid_projects_for_category_url = reverse(
            'epic.categories.views.view_projects_for_category',
            kwargs={'category_id': 1337})
        projects_response = self.client.get(invalid_projects_for_category_url)
        self.assertStatusCodeIsAFailure(projects_response.status_code)
        
        invalid_datarequests_for_category_url = reverse(
            'epic.categories.views.view_datarequests_for_category',
            kwargs={'category_id': 1337})
        datarequests_response = self.client.get(invalid_datarequests_for_category_url)
        self.assertStatusCodeIsAFailure(datarequests_response.status_code)
    
    """
    NOTE: these tests will fail once we implement pagination,
     since not all items will be displayed on the first page.
    """
    
    def testAllItemsInValidCategory(self):
        datasets = self.datasets.all()
        projects = self.projects.all()
        datarequests = self.projects.all()

        response = self.client.get(self.view_all_items_url)
        
        for dataset in datasets:
            self.assertContains(response, dataset.name)
        
        for project in projects:
            self.assertContains(response, project.name)
        
        for datarequest in datarequests:
            self.assertContains(response, datarequest.name)
            
    def testDatasetsInValidCategory(self):
        datasets = list(self.datasets.all())
        
        response = self.client.get(self.view_datasets_url)
        
        for dataset in datasets:
            self.assertContains(response, dataset.name)

    def testDataRequestsInValidCategory(self):
        datarequests = list(self.datarequests.all())
        
        response = self.client.get(self.view_datarequests_url)
        
        for datarequest in datarequests:
            self.assertContains(response, datarequest.name)

    def testProjectsInValidCategory(self):
        projects = list(self.projects.all())
        
        response = self.client.get(self.view_projects_url)
        
        for project in projects:
            self.assertContains(response, project.name)
            
class CategoryTemplateTagsTestCase(CustomTestCase):
    fixtures = ['categories_categories']
    
    def setUp(self):
        self.category1 = Category.objects.get(name='Test Category1')
        self.category2 = Category.objects.get(name='Test Category2')
        self.dataset = DataSet.objects.active()[0]
        self.project = Project.objects.active()[0]
        self.datarequest = DataRequest.objects.active()[0]
        
        self.view_categories_url = reverse('epic.categories.views.view_categories')
        self.view_all_items_url = reverse(
            'epic.categories.views.view_items_for_category',
            kwargs={'category_id': self.category1.id})
        
        self.view_dataset_url = get_item_url(self.dataset, 'epic.datasets.views.view_dataset')
        self.view_project_url = get_item_url(self.project, 'epic.projects.views.view_project')
        self.view_datarequest_url = get_item_url(
            self.datarequest, 'epic.datarequests.views.view_datarequest')
    
    def testCategories(self):
        response = self.client.get(self.view_categories_url)
        self.assertStatusCodeIsASuccess(response.status_code)
        self.assertContains(response, self.category1.name)
        self.assertContains(response, self.category2.name)
    
    def testCategoryListingInItemHeaders(self):
        view_category_url = '<a href="%s">' % self.view_all_items_url
        
        dataset_response = self.client.get(self.view_dataset_url)
        self.assertContains(dataset_response, view_category_url)
        
        project_response = self.client.get(self.view_project_url)
        self.assertContains(project_response, view_category_url)
        
        datarequest_response = self.client.get(self.view_datarequest_url)
        self.assertContains(datarequest_response, view_category_url)

class DeleteCategoryTestCase(CustomTestCase):
    fixtures = ['categories_categories']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.category = Category.objects.create(name='category1', description='category2')
        self.dataset_name = 'a38yyth'
        self.dataset_description = 'asd09g4h6'
        self.dataset = DataSet.objects.create(
            name=self.dataset_name,
            description=self.dataset_description,
            category=self.category,
            creator=self.bob)
        
    def testDeleting(self):
        # I've overwritten the delete method so make sure that
        # deleting a category won't delete the dataset attached to it.
        self.category.delete()
        
        try:
            dataset = DataSet.objects.get(
                name=self.dataset_name, description=self.dataset_description, creator=self.bob)
        except DataSet.DoesNotExist:
            self.fail()
    
    def testDeletingNoCategory(self):
        no_category = default_category()
        test_passed = False
        
        try:
            no_category.delete()
        except CannotDeleteNoCategoryException:
            test_passed = True
        
        self.failIfNot(test_passed)
