from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from epic.core.test import CustomTestCase

from epic.core.test import CustomTestCase
from epic.datarequests.models import DataRequest
from epic.tags.models import Tagging

def common_setUp(self):
    # datarequest1 is created by bob.
    self.bob = User.objects.get(username="bob")
    
    # TODO: Put this in a fixture.
    self.datarequest1 = DataRequest.objects.create(creator=self.bob,
                                   name="datarequest1",
                                   description="datarequest number one",
                                   is_active=True)

    # datarequest2 is created by bill.
    self.bill = User.objects.get(username="bill")
    
    self.datarequest2 = DataRequest.objects.create(creator=self.bill,
                                   name="datarequest2",
                                   description="datarequest number two",
                                   is_active=True)
    
    bob_tags_1 = 'tagRequest1 tagRequest2'
    bob_tags_2 = 'tagRequest4'
    bill_tags_1 = 'tagRequest3'
    bill_tags_2 = 'tagRequest3'
    
    Tagging.objects.add_tags_and_return_added_tag_names(
                    bob_tags_1,
                    item=self.datarequest1,
                    user=self.bob)
    Tagging.objects.add_tags_and_return_added_tag_names(
                    bill_tags_1,
                    item=self.datarequest1,
                    user=self.bill)
    Tagging.objects.add_tags_and_return_added_tag_names(
                    bob_tags_2,
                    item=self.datarequest2,
                    user=self.bob)
    Tagging.objects.add_tags_and_return_added_tag_names(
                    bill_tags_2,
                    item=self.datarequest2,
                    user=self.bill)
    
    self.tag1 = Tagging.objects.get(tag='tagRequest1')
    self.tag2 = Tagging.objects.get(tag='tagRequest2')
    self.tag3 = Tagging.objects.get(tag='tagRequest3', user=self.bill, item=self.datarequest2)
    self.tag4 = Tagging.objects.get(tag='tagRequest4')
    
    self.TAG_INDEX_URL = reverse("epic.tags.views.index")
    
    self.TAG1_URL = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag1.tag })
    
    self.TAG2_URL = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag2.tag })
    
    self.TAG3_URL = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag3.tag })
    
    self.TAG4_URL = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag4.tag })
    
    self.datarequest1_URL = reverse("epic.datarequests.views.view_datarequest",
                           kwargs={ "item_id": self.datarequest1.id,
                                       "slug": self.datarequest1.slug, })
    
    self.datarequest2_URL = reverse("epic.datarequests.views.view_datarequest",
                           kwargs={ "item_id": self.datarequest2.id,
                                       "slug": self.datarequest2.slug, })

class ViewTestCase(CustomTestCase):
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
        self.assertContains(response,'href="%s' % self.datarequest1_URL)
        self.assertNotContains(response,'href="%s' % self.datarequest2_URL)
        
        # Test the page for tag 2
        response = self.client.get(self.TAG2_URL)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.datarequest1_URL)
        self.assertNotContains(response,'href="%s' % self.datarequest2_URL)
        
        # Test the page for tag 3
        response = self.client.get(self.TAG3_URL)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.datarequest1_URL)
        self.assertContains(response,'href="%s' % self.datarequest2_URL)
        
        # Test the page for tag 4
        response = self.client.get(self.TAG4_URL)
        self.assertEqual(response.status_code, 200)
        self.assertNotContains(response,'href="%s' % self.datarequest1_URL)
        self.assertContains(response,'href="%s' % self.datarequest2_URL)
        
    def testDataRequestPage(self):
        # Test the page for datarequest 1
        response = self.client.get(self.datarequest1_URL)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response,'href="%s' % self.TAG1_URL)
        self.assertContains(response,'href="%s' % self.TAG2_URL)
        self.assertContains(response,'href="%s' % self.TAG3_URL)
        self.assertNotContains(response,'href="%s' % self.TAG4_URL)
        
        # Test the page for datarequest2
        response = self.client.get(self.datarequest2_URL)
        self.assertEqual(response.status_code, 200)
        
        self.assertNotContains(response,'href="%s' % self.TAG1_URL)
        self.assertNotContains(response,'href="%s' % self.TAG2_URL)
        self.assertContains(response,'href="%s' % self.TAG3_URL)
        self.assertContains(response,'href="%s' % self.TAG4_URL)
        
class ViewAddTagsTestCase(CustomTestCase):
    fixtures = [ "tags_just_users"]
    
    def setUp(self):
        common_setUp(self)
    
        self.DATAREQUESTS_URL = reverse("epic.datarequests.views.view_datarequests")
        self.DATAREQUEST_TAGGED_URL = self.datarequest1_URL
        self.DATAREQUEST_NOT_TAGGED_URL = self.datarequest2_URL
        
        self.DATAREQUEST_TAGGED_UNIQUE_DATAREQUEST_JAVASCRIPT_VARIABLE = "var ITEM_TO_BE_TAGGED = '%s'" % self.datarequest1.id
        self.DATAREQUEST_NOT_TAGGED_UNIQUE_DATAREQUEST_JAVASCRIPT_VARIABLE = "var ITEM_TO_BE_TAGGED = '%s'" % self.datarequest2.id
        
        self.ADD_TAGS_URL = "javascript:showAddTagsBox('add_tag'); return false;"
    
    def tearDown(self):
        pass
    
    def testNoAddTagsOnDataRequests(self):
        response = self.client.get(self.DATAREQUESTS_URL)
        
        self.assertNotContains(response, self.ADD_TAGS_URL)
        
    def testAddTagsOnTaggedDataRequestNotLoggedIn(self):
        response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        
        self.assertNotContains(response, self.ADD_TAGS_URL)
        
    def testAddTagsOnNotTaggedDataRequestNotLoggedIn(self):
        response = self.client.get(self.DATAREQUEST_NOT_TAGGED_URL)
        
        self.assertNotContains(response, self.ADD_TAGS_URL)
    
    def testAddTagsOnTaggedDataRequest(self):
        self.tryLogin(username="bob", password="bob")
        
        response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        
        self.assertContains(response, self.DATAREQUEST_TAGGED_UNIQUE_DATAREQUEST_JAVASCRIPT_VARIABLE)
        self.assertNotContains(response, self.DATAREQUEST_NOT_TAGGED_UNIQUE_DATAREQUEST_JAVASCRIPT_VARIABLE)
        self.assertContains(response, self.ADD_TAGS_URL)

        
        
    def testAddTagsOnNotTaggedDataRequest(self):
        self.tryLogin(username="bob", password="bob")
        
        response = self.client.get(self.DATAREQUEST_NOT_TAGGED_URL)
        
        self.assertNotContains(response, self.DATAREQUEST_TAGGED_UNIQUE_DATAREQUEST_JAVASCRIPT_VARIABLE)
        self.assertContains(response, self.DATAREQUEST_NOT_TAGGED_UNIQUE_DATAREQUEST_JAVASCRIPT_VARIABLE)
        self.assertContains(response, self.ADD_TAGS_URL)
        

class DeleteFromDataRequestPageTestCase(CustomTestCase):
    fixtures = [ "tags_just_users"]
    
    def setUp(self):
        common_setUp(self)
        self.REMOVE_TAG_URL = reverse('epic.tags.views.delete_tag')
        self.DATAREQUEST_TAGGED_URL = self.datarequest1_URL
        self.valid_post_data = {'tag_name': self.tag1.tag, 'item_id': self.datarequest1.id}
        self.blank_tag_post_data = {'tag_name': '', 'item_id': self.datarequest1.id}
        self.blank_datarequest_post_data = {'tag_name': self.tag1.tag, 'item_id': ''}
        
    def testRemoveLoggedOut(self):
        #===============================================================================
        #        Test that a logged out user cannot delete a tag
        #===============================================================================
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 302)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 302)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertContains(datarequest_response, self.tag1.tag)
    
    def testRemoveNotOwner(self):
        #===============================================================================
        #        Test that a user may not remove another user's tag
        #===============================================================================
        
        self.tryLogin('bill')
        
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertContains(datarequest_response, self.tag1.tag)
        
    def testRemoveOwnerNoDataRequest(self):
        #===============================================================================
        #        Test that a user must provide a datarequest id to delete a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.blank_datarequest_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertContains(datarequest_response, self.tag1.tag)
        
    def testRemoveOwnerNoTag(self):
        #===============================================================================
        #        Test that a user must provide a tag name to delete a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.blank_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertContains(datarequest_response, self.tag1.tag)
        
    def testRemoveOwnerValid(self):
        #===============================================================================
        #        Test that a user can delete a tag if they give the tag name, the datarequest
        #         id and are the creator of the tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.REMOVE_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.REMOVE_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertNotContains(datarequest_response, self.tag1.tag)
        
class AddTagDataRequestPageTestCase(CustomTestCase):
    fixtures = [ "tags_just_users" ]
    
    def setUp(self):
        common_setUp(self)
        self.ADD_TAG_URL = reverse('epic.tags.views.add_tags_and_return_successful_tag_names')
        self.DATAREQUEST_TAGGED_URL = self.datarequest1_URL
        self.valid_post_data = {'unparsed_tag_names': 'asdf205', 'item_id': self.datarequest1.id}
        self.blank_tag_post_data = {'unparsed_tag_names': '', 'item_id': self.datarequest1.id}
        self.blank_datarequest_post_data = {'unparsed_tag_names': 'asdf205', 'item_id': ''}
        self.duplicate_tag_post_data = {'unparsed_tag_names':self.tag1.tag, 'item_id': self.datarequest1.id}
        
    def testAddTagLoggedOut(self):
        #===============================================================================
        #        Test that a logged out user cannot add a tag
        #===============================================================================
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 302)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 302)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertNotContains(datarequest_response, self.valid_post_data['unparsed_tag_names'])
    
    def testAddTagNotOwner(self):
        #===============================================================================
        #        Test that a logged in user can add a tag even if they don't own the ds
        #===============================================================================
        self.tryLogin('bill')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertContains(datarequest_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerNoDataRequest(self):
        #===============================================================================
        #        Test that the owner must provide a datarequest to add a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.blank_datarequest_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertNotContains(datarequest_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerNoTag(self):
        #===============================================================================
        #        Test that the owner must provide a tagname to add a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.blank_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertNotContains(datarequest_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerDuplicateTag(self):
        #===============================================================================
        #        Test that adding a duplicate tag won't actually put it in the db/taglist
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.duplicate_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertContains(datarequest_response, self.duplicate_tag_post_data['unparsed_tag_names'], count=7)
        
    def testAddTagOwnerValid(self):
        #===============================================================================
        #        Test that the owner can add a tag given a datarequest id and a tag name
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.ADD_TAG_URL)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.ADD_TAG_URL, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.DATAREQUEST_TAGGED_URL)
        self.assertContains(datarequest_response, self.valid_post_data['unparsed_tag_names'], count=7)