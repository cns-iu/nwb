from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project
from epic.tags.models import Tagging
from epic.tags.views import _get_datasets_for_tags, _get_datarequests_for_tags, _get_projects_for_tags


def common_setUp(self):
    # dataset1 is created by bob.
    self.bob = User.objects.get(username="bob")
    
    # TODO: Put this in a fixture.
    self.dataset1 = DataSet.objects.get(creator=self.bob,
                                   name="dataset1",
                                   description="dataset number one")

    # dataset2 is created by bill.
    self.bill = User.objects.get(username="bill")
    
    self.dataset2 = DataSet.objects.get(creator=self.bill,
                                   name="dataset2",
                                   description="dataset number two")
    
    self.tag1 = Tagging.objects.get(tag="tag1")
    self.tag2 = Tagging.objects.get(tag="tag2")
    self.tag3 = Tagging.objects.get(tag="tag3", user=self.bill, item=self.dataset2)
    self.tag4 = Tagging.objects.get(tag="tag4")
    
    self.tag_index_url = reverse("epic.tags.views.index")
    
    self.tag1_url = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag1.tag })
    
    self.tag2_url = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag2.tag })
    
    self.tag3_url = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag3.tag })
    
    self.tag4_url = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag4.tag })
    
    self.dataset1_url = reverse("epic.datasets.views.view_dataset",
                           kwargs={ "item_id": self.dataset1.id,
                                       "slug": self.dataset1.slug, })
    
    self.dataset2_url = reverse("epic.datasets.views.view_dataset",
                           kwargs={ "item_id": self.dataset2.id,
                                       "slug": self.dataset2.slug, })

class ViewTestCase(CustomTestCase):
    fixtures = [ "tags_just_users", "tags_tags" ]
    
    def setUp(self):
        common_setUp(self)
    
    def tearDown(self):
        pass
    
    def testTagsIndex(self):
        # Verify that all the tags show up on the index page
        response = self.client.get(self.tag_index_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, 'href="%s' % self.tag1_url)
        self.assertContains(response, 'href="%s' % self.tag2_url)
        self.assertContains(response, 'href="%s' % self.tag3_url)
        self.assertContains(response, 'href="%s' % self.tag4_url)
        
    
    def testTagsPage(self):
        # Test the page for tag 1
        response = self.client.get(self.tag1_url)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.dataset1_url)
        self.assertNotContains(response,'href="%s' % self.dataset2_url)
        
        # Test the page for tag 2
        response = self.client.get(self.tag2_url)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.dataset1_url)
        self.assertNotContains(response,'href="%s' % self.dataset2_url)
        
        # Test the page for tag 3
        response = self.client.get(self.tag3_url)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.dataset1_url)
        self.assertContains(response,'href="%s' % self.dataset2_url)
        
        # Test the page for tag 4
        response = self.client.get(self.tag4_url)
        self.assertEqual(response.status_code, 200)
        self.assertNotContains(response,'href="%s' % self.dataset1_url)
        self.assertContains(response,'href="%s' % self.dataset2_url)
        
    def testDataSetPage(self):
        # Test the page for dataset 1
        response = self.client.get(self.dataset1_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response,'href="%s' % self.tag1_url)
        self.assertContains(response,'href="%s' % self.tag2_url)
        self.assertContains(response,'href="%s' % self.tag3_url)
        self.assertNotContains(response,'href="%s' % self.tag4_url)
        
        # Test the page for dataset2
        response = self.client.get(self.dataset2_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertNotContains(response,'href="%s' % self.tag1_url)
        self.assertNotContains(response,'href="%s' % self.tag2_url)
        self.assertContains(response,'href="%s' % self.tag3_url)
        self.assertContains(response,'href="%s' % self.tag4_url)


def save_n_data_requests_with_tag(user, n, tag):    	
    for i in range(n):
    	data_request = DataRequest(creator=user)
    	data_request.name = 'test_data_request_name_' + str(i)
    	data_request.is_active = True
    	data_request.save()
    	Tagging.objects.add_tags_and_return_added_tag_names(tag, data_request, user)	

class ViewDatarequestsForTagsPaginatedTestCase(CustomTestCase):
    fixtures = ['tags_just_users']

    def setUp(self):
    	from epic.datarequests.views import PER_PAGE
    	self.good_tag = 'test_good_tag'
    	self.bad_tag = 'test_bad_tag'
    	save_n_data_requests_with_tag(User.objects.get(username="bob"), PER_PAGE, self.good_tag)
    	save_n_data_requests_with_tag(User.objects.get(username="bob"), 7, self.bad_tag)    	
    
    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.tags.views.view_datarequests_for_tag', kwargs={'tag_name': self.good_tag}))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for data_request in _get_datarequests_for_tags(self.good_tag):
    		self.assertContains(response, data_request.get_absolute_url())
        for data_request in _get_datarequests_for_tags(self.bad_tag):
    		self.assertNotContains(response, data_request.get_absolute_url())


def save_n_datasets_with_tag(user, n, tag):    	
    for i in range(n):
    	dataset = DataSet(creator=user)
    	dataset.name = 'test_dataset_name_' + str(i)
    	dataset.is_active = True
    	dataset.save()
    	Tagging.objects.add_tags_and_return_added_tag_names(tag, dataset, user)	

class ViewDatasetsForTagsPaginatedTestCase(CustomTestCase):
    fixtures = ['tags_just_users']

    def setUp(self):
    	from epic.datasets.views import PER_PAGE
    	self.good_tag = 'test_good_tag'
    	self.bad_tag = 'test_bad_tag'
    	save_n_datasets_with_tag(User.objects.get(username="bob"), PER_PAGE, self.good_tag)
    	save_n_datasets_with_tag(User.objects.get(username="bob"), 7, self.bad_tag)    	
    
    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.tags.views.view_datasets_for_tag', kwargs={'tag_name': self.good_tag}))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for dataset in _get_datasets_for_tags(self.good_tag):
    		self.assertContains(response, dataset.get_absolute_url())
        for dataset in _get_datasets_for_tags(self.bad_tag):
    		self.assertNotContains(response, dataset.get_absolute_url())


def save_n_projects_with_tag(user, n, tag):    	
    for i in range(n):
    	project = Project(creator=user)
    	project.name = 'test_project_name_' + str(i)
    	project.is_active = True
    	project.save()
    	Tagging.objects.add_tags_and_return_added_tag_names(tag, project, user)	

class ViewProjectsForTagsPaginatedTestCase(CustomTestCase):
    fixtures = ['tags_just_users']

    def setUp(self):
    	from epic.projects.views import PER_PAGE
    	self.good_tag = 'test_good_tag'
    	self.bad_tag = 'test_bad_tag'
    	save_n_projects_with_tag(User.objects.get(username="bob"), PER_PAGE, self.good_tag)
    	save_n_projects_with_tag(User.objects.get(username="bob"), 7, self.bad_tag)    	
    
    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.tags.views.view_projects_for_tag', kwargs={'tag_name': self.good_tag}))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for project in _get_projects_for_tags(self.good_tag):
    		self.assertContains(response, project.get_absolute_url())
        for project in _get_projects_for_tags(self.bad_tag):
    		self.assertNotContains(response, project.get_absolute_url())


class ViewAddTagsTestCase(CustomTestCase):
    fixtures = [ "tags_just_users", "tags_tags" ]
    
    def setUp(self):
        common_setUp(self)
    
        self.datasets_url = reverse("epic.datasets.views.view_datasets")
        self.dataset_tagged_url = self.dataset1_url
        self.dataset_not_tagged_url = self.dataset2_url
        
        self.dataset_tagged_unique_dataset_javascript_variable = "var ITEM_TO_BE_TAGGED = '%s'" % self.dataset1.id
        self.dataset_not_tagged_unique_dataset_javascript_variable = "var ITEM_TO_BE_TAGGED = '%s'" % self.dataset2.id
        
        self.add_tags_url = "javascript:showAddTagsBox('add_tag'); return false;"
    
    def tearDown(self):
        pass
    
    def testNoAddTagsOnDataSets(self):
        response = self.client.get(self.datasets_url)
        
        self.assertNotContains(response, self.add_tags_url)
        
    def testAddTagsOnTaggedDataSetNotLoggedIn(self):
        response = self.client.get(self.dataset_tagged_url)
        
        self.assertNotContains(response, self.add_tags_url)
        
    def testAddTagsOnNotTaggedDataSetNotLoggedIn(self):
        response = self.client.get(self.dataset_not_tagged_url)
        
        self.assertNotContains(response, self.add_tags_url)
    
    def testAddTagsOnTaggedDataSet(self):
        self.tryLogin(username="bob", password="bob")
        
        response = self.client.get(self.dataset_tagged_url)
        
        self.assertContains(response, self.dataset_tagged_unique_dataset_javascript_variable)
        self.assertNotContains(response, self.dataset_not_tagged_unique_dataset_javascript_variable)
        self.assertContains(response, self.add_tags_url)

        
        
    def testAddTagsOnNotTaggedDataSet(self):
        self.tryLogin(username="bob", password="bob")
        
        response = self.client.get(self.dataset_not_tagged_url)
        
        self.assertNotContains(response, self.dataset_tagged_unique_dataset_javascript_variable)
        self.assertContains(response, self.dataset_not_tagged_unique_dataset_javascript_variable)
        self.assertContains(response, self.add_tags_url)
        

class DeleteFromDataSetPageTestCase(CustomTestCase):
    fixtures = [ "tags_just_users", "tags_tags" ]
    
    def setUp(self):
        common_setUp(self)
        self.remove_tag_url = reverse('epic.tags.views.delete_tag')
        self.dataset_tagged_url = self.dataset1_url
        self.valid_post_data = {'tag_name': self.tag1.tag, 'item_id': self.dataset1.id}
        self.blank_tag_post_data = {'tag_name': '', 'item_id': self.dataset1.id}
        self.blank_dataset_post_data = {'tag_name': self.tag1.tag, 'item_id': ''}
        
    def testRemoveLoggedOut(self):
        #===============================================================================
        #        Test that a logged out user cannot delete a tag
        #===============================================================================
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 302)
        
        post_response = self.client.post(self.remove_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 302)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertContains(dataset_response, self.tag1.tag)
    
    def testRemoveNotOwner(self):
        #===============================================================================
        #        Test that a user may not remove another user's tag
        #===============================================================================
        
        self.tryLogin('bill')
        
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.remove_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertContains(dataset_response, self.tag1.tag)
        
    def testRemoveOwnerNoDataset(self):
        #===============================================================================
        #        Test that a user must provide a dataset id to delete a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.remove_tag_url, self.blank_dataset_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertContains(dataset_response, self.tag1.tag)
        
    def testRemoveOwnerNoTag(self):
        #===============================================================================
        #        Test that a user must provide a tag name to delete a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.remove_tag_url, self.blank_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertContains(dataset_response, self.tag1.tag)
        
    def testRemoveOwnerValid(self):
        #===============================================================================
        #        Test that a user can delete a tag if they give the tag name, the dataset
        #         id and are the creator of the tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.remove_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertNotContains(dataset_response, self.tag1.tag)
        
class AddTagDataSetPageTestCase(CustomTestCase):
    fixtures = [ "tags_just_users", "tags_tags" ]
    
    def setUp(self):
        common_setUp(self)
        self.add_tag_url = reverse('epic.tags.views.add_tags_and_return_successful_tag_names')
        self.dataset_tagged_url = self.dataset1_url
        self.valid_post_data = {'unparsed_tag_names': 'asdf205', 'item_id': self.dataset1.id}
        self.blank_tag_post_data = {'unparsed_tag_names': '', 'item_id': self.dataset1.id}
        self.blank_dataset_post_data = {'unparsed_tag_names': 'asdf205', 'item_id': ''}
        self.duplicate_tag_post_data = {'unparsed_tag_names':self.tag1.tag, 'item_id': self.dataset1.id}
        
    def testAddTagLoggedOut(self):
        #===============================================================================
        #        Test that a logged out user cannot add a tag
        #===============================================================================
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 302)
        
        post_response = self.client.post(self.add_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 302)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertNotContains(dataset_response, self.valid_post_data['unparsed_tag_names'])
    
    def testAddTagNotOwner(self):
        #===============================================================================
        #        Test that a logged in user can add a tag even if they don't own the ds
        #===============================================================================
        self.tryLogin('bill')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertContains(dataset_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerNoDataset(self):
        #===============================================================================
        #        Test that the owner must provide a dataset to add a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.blank_dataset_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertNotContains(dataset_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerNoTag(self):
        #===============================================================================
        #        Test that the owner must provide a tagname to add a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.blank_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertNotContains(dataset_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerDuplicateTag(self):
        #===============================================================================
        #        Test that adding a duplicate tag won't actually put it in the db/taglist
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.duplicate_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertContains(dataset_response, self.duplicate_tag_post_data['unparsed_tag_names'], count=7)
        
    def testAddTagOwnerValid(self):
        #===============================================================================
        #        Test that the owner can add a tag given a dataset id and a tag name
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.dataset_tagged_url)
        self.assertContains(dataset_response, self.valid_post_data['unparsed_tag_names'], count=7)