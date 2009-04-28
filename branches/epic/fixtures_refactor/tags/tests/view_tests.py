from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.test import TestCase

from epic.core.tests.CustomTestCase import CustomTestCase
from epic.datasets.models import DataSet
from epic.tags.models import Tagging

def common_setUp(self):
    # dataset1 is created by bob.
    self.bob = User.objects.get(username="bob")
    
    # TODO: Put this in a fixture.
    self.dataset1 = DataSet.objects.get(creator=self.bob,
                                   name="dataset1",
                                   slug="dataset1",
                                   description="dataset number one")

    # dataset2 is created by bill.
    self.bill = User.objects.get(username="bill")
    
    self.dataset2 = DataSet.objects.get(creator=self.bill,
                                   name="dataset2",
                                   slug="dataset2",
                                   description="dataset number two")
    
    self.tag1 = Tagging.objects.get(tag="tag1")
    self.tag2 = Tagging.objects.get(tag="tag2")
    self.tag3 = Tagging.objects.get(tag="tag3", user=self.bill, item=self.dataset2)
    self.tag4 = Tagging.objects.get(tag="tag4")
    
    self.TAG_INDEX_URL = reverse("epic.tags.views.index")
    
    self.TAG1_URL = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag1.tag })
    
    self.TAG2_URL = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag2.tag })
    
    self.TAG3_URL = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag3.tag })
    
    self.TAG4_URL = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag4.tag })
    
    self.dataset1_URL = reverse("epic.datasets.views.view_dataset",
                           kwargs={ "item_id": self.dataset1.id,
                                       "slug": self.dataset1.slug, })
    
    self.dataset2_URL = reverse("epic.datasets.views.view_dataset",
                           kwargs={ "item_id": self.dataset2.id,
                                       "slug": self.dataset2.slug, })

class ViewTestCase(TestCase):
    fixtures = [ "tags_just_users", "tags_tags" ]
    
    def setUp(self):
        common_setUp(self)
    
    def tearDown(self):
        pass
    
    def testTagsIndex(self):
        # Verify that all the tags show up on the index page
        response = self.client.get(self.TAG_INDEX_URL)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, 'href="%s' % self.TAG1_URL)
        self.assertContains(response, 'href="%s' % self.TAG2_URL)
        self.assertContains(response, 'href="%s' % self.TAG3_URL)
        self.assertContains(response, 'href="%s' % self.TAG4_URL)
        
    
    def testTagsPage(self):
        # Test the page for tag 1
        response = self.client.get(self.TAG1_URL)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.dataset1_URL)
        self.assertNotContains(response,'href="%s' % self.dataset2_URL)
        
        # Test the page for tag 2
        response = self.client.get(self.TAG2_URL)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.dataset1_URL)
        self.assertNotContains(response,'href="%s' % self.dataset2_URL)
        
        # Test the page for tag 3
        response = self.client.get(self.TAG3_URL)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.dataset1_URL)
        self.assertContains(response,'href="%s' % self.dataset2_URL)
        
        # Test the page for tag 4
        response = self.client.get(self.TAG4_URL)
        self.assertEqual(response.status_code, 200)
        self.assertNotContains(response,'href="%s' % self.dataset1_URL)
        self.assertContains(response,'href="%s' % self.dataset2_URL)
        
    def testDataSetPage(self):
        # Test the page for dataset 1
        response = self.client.get(self.dataset1_URL)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response,'href="%s' % self.TAG1_URL)
        self.assertContains(response,'href="%s' % self.TAG2_URL)
        self.assertContains(response,'href="%s' % self.TAG3_URL)
        self.assertNotContains(response,'href="%s' % self.TAG4_URL)
        
        # Test the page for dataset2
        response = self.client.get(self.dataset2_URL)
        self.assertEqual(response.status_code, 200)
        
        self.assertNotContains(response,'href="%s' % self.TAG1_URL)
        self.assertNotContains(response,'href="%s' % self.TAG2_URL)
        self.assertContains(response,'href="%s' % self.TAG3_URL)
        self.assertContains(response,'href="%s' % self.TAG4_URL)
        
class ViewAddTagsTestCase(TestCase):
    fixtures = [ "tags_just_users", "tags_tags" ]
    
    def setUp(self):
        common_setUp(self)
    
        self.DATASETS_URL = reverse("epic.datasets.views.view_datasets")
        self.DATASET_TAGGED_URL = self.dataset1_URL
        self.DATASET_NOT_TAGGED_URL = self.dataset2_URL
        
        self.DATASET_TAGGED_UNIQUE_DATASET_JAVASCRIPT_VARIABLE = "var DATASET_TO_BE_TAGGED = '%s'" % self.dataset1.id
        self.DATASET_NOT_TAGGED_UNIQUE_DATASET_JAVASCRIPT_VARIABLE = "var DATASET_TO_BE_TAGGED = '%s'" % self.dataset2.id
        
        self.ADD_TAGS_URL = "javascript:showAddTagsBox('add_tag'); return false;"
    
    def tearDown(self):
        pass
    
    def testNoAddTagsOnDataSets(self):
        response = self.client.get(self.DATASETS_URL)
        
        self.assertNotContains(response, self.ADD_TAGS_URL)
        
    def testAddTagsOnTaggedDataSetNotLoggedIn(self):
        response = self.client.get(self.DATASET_TAGGED_URL)
        
        self.assertNotContains(response, self.ADD_TAGS_URL)
        
    def testAddTagsOnNotTaggedDataSetNotLoggedIn(self):
        response = self.client.get(self.DATASET_NOT_TAGGED_URL)
        
        self.assertNotContains(response, self.ADD_TAGS_URL)
    
    def testAddTagsOnTaggedDataSet(self):
        login = self.client.login(username="bob", password="bob")
        self.failUnless(login, "Could not login")
        
        response = self.client.get(self.DATASET_TAGGED_URL)
        
        self.assertContains(response, self.DATASET_TAGGED_UNIQUE_DATASET_JAVASCRIPT_VARIABLE)
        self.assertNotContains(response, self.DATASET_NOT_TAGGED_UNIQUE_DATASET_JAVASCRIPT_VARIABLE)
        self.assertContains(response, self.ADD_TAGS_URL)

        
        
    def testAddTagsOnNotTaggedDataSet(self):
        login = self.client.login(username="bob", password="bob")
        self.failUnless(login, "Could not login")
        
        response = self.client.get(self.DATASET_NOT_TAGGED_URL)
        
        self.assertNotContains(response, self.DATASET_TAGGED_UNIQUE_DATASET_JAVASCRIPT_VARIABLE)
        self.assertContains(response, self.DATASET_NOT_TAGGED_UNIQUE_DATASET_JAVASCRIPT_VARIABLE)
        self.assertContains(response, self.ADD_TAGS_URL)
        

class DeleteFromDataSetPageTestCase(CustomTestCase):
    fixtures = [ "tags_just_users", "tags_tags" ]
    
    def setUp(self):
        common_setUp(self)
        self.REMOVE_TAG_URL = reverse('epic.tags.views.delete_tag')
        self.DATASET_TAGGED_URL = self.dataset1_URL
        self.valid_post_data = {'tag_name': self.tag1.tag, 'dataset_id': self.dataset1.id}
        self.blank_tag_post_data = {'tag_name': '', 'dataset_id': self.dataset1.id}
        self.blank_dataset_post_data = {'tag_name': self.tag1.tag, 'dataset_id': ''}
        
    def testRemoveLoggedOut(self):
        #===============================================================================
        #        Test that a logged out user cannot delete a tag
        #===============================================================================
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 302)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 302)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertContains(dataset_response, self.tag1.tag)
    
    def testRemoveNotOwner(self):
        #===============================================================================
        #        Test that a user may not remove another user's tag
        #===============================================================================
        
        self.tryLogin('bill')
        
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertContains(dataset_response, self.tag1.tag)
        
    def testRemoveOwnerNoDataset(self):
        #===============================================================================
        #        Test that a user must provide a dataset id to delete a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.blank_dataset_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertContains(dataset_response, self.tag1.tag)
        
    def testRemoveOwnerNoTag(self):
        #===============================================================================
        #        Test that a user must provide a tag name to delete a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.blank_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertContains(dataset_response, self.tag1.tag)
        
    def testRemoveOwnerValid(self):
        #===============================================================================
        #        Test that a user can delete a tag if they give the tag name, the dataset
        #         id and are the creator of the tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertNotContains(dataset_response, self.tag1.tag)
        
class AddTagDataSetPageTestCase(CustomTestCase):
    fixtures = [ "tags_just_users", "tags_tags" ]
    
    def setUp(self):
        common_setUp(self)
        self.ADD_TAG_URL = reverse('epic.tags.views.add_tags_and_return_successful_tag_names')
        self.DATASET_TAGGED_URL = self.dataset1_URL
        self.valid_post_data = {'unparsed_tag_names': 'asdf205', 'dataset_id': self.dataset1.id}
        self.blank_tag_post_data = {'unparsed_tag_names': '', 'dataset_id': self.dataset1.id}
        self.blank_dataset_post_data = {'unparsed_tag_names': 'asdf205', 'dataset_id': ''}
        self.duplicate_tag_post_data = {'unparsed_tag_names':self.tag1.tag, 'dataset_id': self.dataset1.id}
        
    def testAddTagLoggedOut(self):
        #===============================================================================
        #        Test that a logged out user cannot add a tag
        #===============================================================================
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 302)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 302)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertNotContains(dataset_response, self.valid_post_data['unparsed_tag_names'])
    
    def testAddTagNotOwner(self):
        #===============================================================================
        #        Test that a logged in user can add a tag even if they don't own the ds
        #===============================================================================
        self.tryLogin('bill')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertContains(dataset_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerNoDataset(self):
        #===============================================================================
        #        Test that the owner must provide a dataset to add a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.blank_dataset_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertNotContains(dataset_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerNoTag(self):
        #===============================================================================
        #        Test that the owner must provide a tagname to add a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.blank_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertNotContains(dataset_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerDuplicateTag(self):
        #===============================================================================
        #        Test that adding a duplicate tag won't actually put it in the db/taglist
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.duplicate_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertContains(dataset_response, self.duplicate_tag_post_data['unparsed_tag_names'], count=7)
        
    def testAddTagOwnerValid(self):
        #===============================================================================
        #        Test that the owner can add a tag given a dataset id and a tag name
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        dataset_response = self.client.get(self.DATASET_TAGGED_URL)
        self.assertContains(dataset_response, self.valid_post_data['unparsed_tag_names'], count=7)