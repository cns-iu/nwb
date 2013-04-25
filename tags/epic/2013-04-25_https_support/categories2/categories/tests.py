from django.core.urlresolvers import reverse

from epic.categories.models import Category
from epic.core.test import CustomTestCase
from epic.core.util.view_utils import *
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project


class ViewCategoriesTestCase(CustomTestCase):
    def setUp(self):
        self.view_categories_url = \
            reverse('epic.categories.views.view_categories')
    
    def testNoCategoriesExist(self):
        Category.objects.all().delete()
        
        response = self.client.get(self.view_categories_url)
        self.assertContains(response,
                            'There are currently no categories available.')
    
    def testCategoriesExist(self):
        category = Category.objects.create(name='Test Category',
                                           description='Test Description')
        
        response = self.client.get(self.view_categories_url)
        self.assertContains(response, category.name)

class ViewItemsForCategoryTestCase(CustomTestCase):
    fixtures = ['categories_categories']
    
    def setUp(self):
        self.category1 = Category.objects.get(name='Test Category1')
        self.datasets = DataSet.objects.filter(category=self.category1)
        self.projects = Project.objects.filter(category=self.category1)
        self.datarequests = Project.objects.filter(category=self.category1)
        
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
        all_items_response = \
            self.client.get(invalid_all_items_for_category_url)
        self.assertStatusCodeIsAFailure(all_items_response.status_code)
        
        invalid_datasets_for_category_url = reverse(
            'epic.categories.views.view_datasets_for_category',
            kwargs={'category_id': 1337})
        datasets_response = \
            self.client.get(invalid_datasets_for_category_url)
        self.assertStatusCodeIsAFailure(datasets_response.status_code)
        
        invalid_projects_for_category_url = reverse(
            'epic.categories.views.view_projects_for_category',
            kwargs={'category_id': 1337})
        projects_response = \
            self.client.get(invalid_projects_for_category_url)
        self.assertStatusCodeIsAFailure(projects_response.status_code)
        
        invalid_datarequests_for_category_url = reverse(
            'epic.categories.views.view_datarequests_for_category',
            kwargs={'category_id': 1337})
        datarequests_response = \
            self.client.get(invalid_datarequests_for_category_url)
        self.assertStatusCodeIsAFailure(datarequests_response.status_code)
    
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

class CategoryTemplateTagsTestCase(CustomTestCase):
    fixtures = ['categories_categories']
    
    def setUp(self):
        self.category1 = Category.objects.get(name='Test Category1')
        self.category2 = Category.objects.get(name='Test Category2')
        self.dataset = DataSet.objects.active()[0]
        self.project = Project.objects.active()[0]
        self.datarequest = DataRequest.objects.active()[0]
        
        self.view_categories_url = \
            reverse('epic.categories.views.view_categories')
        
        self.view_all_items_url = reverse(
            'epic.categories.views.view_items_for_category',
            kwargs={'category_id': self.category1.id})
        
        self.view_dataset_url = \
            get_item_url(self.dataset, 'epic.datasets.views.view_dataset')
        
        self.view_project_url = \
            get_item_url(self.project, 'epic.projects.views.view_project')
        
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
