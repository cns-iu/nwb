from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

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
    
    self.tag_index_url = reverse("epic.tags.views.index")
    
    self.tag1_url = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag1.tag })
    
    self.tag2_url = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag2.tag })
    
    self.tag3_url = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag3.tag })
    
    self.tag4_url = reverse("epic.tags.views.view_items_for_tag",
                            kwargs={ "tag_name": self.tag4.tag })
    
    self.datarequest1_url = reverse("epic.datarequests.views.view_datarequest",
                           kwargs={ "item_id": self.datarequest1.id,
                                       "slug": self.datarequest1.slug, })
    
    self.datarequest2_url = reverse("epic.datarequests.views.view_datarequest",
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
        self.assertContains(response,'href="%s' % self.datarequest1_url)
        self.assertNotContains(response,'href="%s' % self.datarequest2_url)
        
        # Test the page for tag 2
        response = self.client.get(self.tag2_url)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.datarequest1_url)
        self.assertNotContains(response,'href="%s' % self.datarequest2_url)
        
        # Test the page for tag 3
        response = self.client.get(self.tag3_url)
        self.assertEqual(response.status_code, 200)
        self.assertContains(response,'href="%s' % self.datarequest1_url)
        self.assertContains(response,'href="%s' % self.datarequest2_url)
        
        # Test the page for tag 4
        response = self.client.get(self.tag4_url)
        self.assertEqual(response.status_code, 200)
        self.assertNotContains(response,'href="%s' % self.datarequest1_url)
        self.assertContains(response,'href="%s' % self.datarequest2_url)
        
    def testDataRequestPage(self):
        # Test the page for datarequest 1
        response = self.client.get(self.datarequest1_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response,'href="%s' % self.tag1_url)
        self.assertContains(response,'href="%s' % self.tag2_url)
        self.assertContains(response,'href="%s' % self.tag3_url)
        self.assertNotContains(response,'href="%s' % self.tag4_url)
        
        # Test the page for datarequest2
        response = self.client.get(self.datarequest2_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertNotContains(response,'href="%s' % self.tag1_url)
        self.assertNotContains(response,'href="%s' % self.tag2_url)
        self.assertContains(response,'href="%s' % self.tag3_url)
        self.assertContains(response,'href="%s' % self.tag4_url)
        
class ViewAddTagsTestCase(CustomTestCase):
    fixtures = [ "tags_just_users"]
    
    def setUp(self):
        common_setUp(self)
    
        self.datarequests_url = reverse("epic.datarequests.views.view_datarequests")
        self.datarequest_tagged_url = self.datarequest1_url
        self.datarequest_not_tagged_url = self.datarequest2_url
        
        self.datarequest_tagged_unique_datarequest_javascript_variable = "var ITEM_TO_BE_TAGGED = '%s'" % self.datarequest1.id
        self.datarequest_not_tagged_unique_datarequest_javascript_variable = "var ITEM_TO_BE_TAGGED = '%s'" % self.datarequest2.id
        
        self.add_tags_url = "javascript:showAddTagsBox('add_tag'); return false;"
    
    def tearDown(self):
        pass
    
    def testNoAddTagsOnDataRequests(self):
        response = self.client.get(self.datarequests_url)
        
        self.assertNotContains(response, self.add_tags_url)
        
    def testAddTagsOnTaggedDataRequestNotLoggedIn(self):
        response = self.client.get(self.datarequest_tagged_url)
        
        self.assertNotContains(response, self.add_tags_url)
        
    def testAddTagsOnNotTaggedDataRequestNotLoggedIn(self):
        response = self.client.get(self.datarequest_not_tagged_url)
        
        self.assertNotContains(response, self.add_tags_url)
    
    def testAddTagsOnTaggedDataRequest(self):
        self.tryLogin(username="bob", password="bob")
        
        response = self.client.get(self.datarequest_tagged_url)
        
        self.assertContains(response, self.datarequest_tagged_unique_datarequest_javascript_variable)
        self.assertNotContains(response, self.datarequest_not_tagged_unique_datarequest_javascript_variable)
        self.assertContains(response, self.add_tags_url)

        
        
    def testAddTagsOnNotTaggedDataRequest(self):
        self.tryLogin(username="bob", password="bob")
        
        response = self.client.get(self.datarequest_not_tagged_url)
        
        self.assertNotContains(response, self.datarequest_tagged_unique_datarequest_javascript_variable)
        self.assertContains(response, self.datarequest_not_tagged_unique_datarequest_javascript_variable)
        self.assertContains(response, self.add_tags_url)
        

class DeleteFromDataRequestPageTestCase(CustomTestCase):
    fixtures = [ "tags_just_users"]
    
    def setUp(self):
        common_setUp(self)
        self.remove_tag_url = reverse('epic.tags.views.delete_tag')
        self.datarequest_tagged_url = self.datarequest1_url
        self.valid_post_data = {'tag_name': self.tag1.tag, 'item_id': self.datarequest1.id}
        self.blank_tag_post_data = {'tag_name': '', 'item_id': self.datarequest1.id}
        self.blank_datarequest_post_data = {'tag_name': self.tag1.tag, 'item_id': ''}
        
    def testRemoveLoggedOut(self):
        #===============================================================================
        #        Test that a logged out user cannot delete a tag
        #===============================================================================
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 302)
        
        post_response = self.client.post(self.remove_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 302)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertContains(datarequest_response, self.tag1.tag)
    
    def testRemoveNotOwner(self):
        #===============================================================================
        #        Test that a user may not remove another user's tag
        #===============================================================================
        
        self.tryLogin('bill')
        
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.remove_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertContains(datarequest_response, self.tag1.tag)
        
    def testRemoveOwnerNoDataRequest(self):
        #===============================================================================
        #        Test that a user must provide a datarequest id to delete a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.remove_tag_url, self.blank_datarequest_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertContains(datarequest_response, self.tag1.tag)
        
    def testRemoveOwnerNoTag(self):
        #===============================================================================
        #        Test that a user must provide a tag name to delete a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.remove_tag_url, self.blank_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertContains(datarequest_response, self.tag1.tag)
        
    def testRemoveOwnerValid(self):
        #===============================================================================
        #        Test that a user can delete a tag if they give the tag name, the datarequest
        #         id and are the creator of the tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.remove_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.remove_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertNotContains(datarequest_response, self.tag1.tag)
        
class AddTagDataRequestPageTestCase(CustomTestCase):
    fixtures = [ "tags_just_users" ]
    
    def setUp(self):
        common_setUp(self)
        self.add_tag_url = reverse('epic.tags.views.add_tags_and_return_successful_tag_names')
        self.datarequest_tagged_url = self.datarequest1_url
        self.valid_post_data = {'unparsed_tag_names': 'asdf205', 'item_id': self.datarequest1.id}
        self.blank_tag_post_data = {'unparsed_tag_names': '', 'item_id': self.datarequest1.id}
        self.blank_datarequest_post_data = {'unparsed_tag_names': 'asdf205', 'item_id': ''}
        self.duplicate_tag_post_data = {'unparsed_tag_names':self.tag1.tag, 'item_id': self.datarequest1.id}
        
    def testAddTagLoggedOut(self):
        #===============================================================================
        #        Test that a logged out user cannot add a tag
        #===============================================================================
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 302)
        
        post_response = self.client.post(self.add_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 302)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertNotContains(datarequest_response, self.valid_post_data['unparsed_tag_names'])
    
    def testAddTagNotOwner(self):
        #===============================================================================
        #        Test that a logged in user can add a tag even if they don't own the ds
        #===============================================================================
        self.tryLogin('bill')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertContains(datarequest_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerNoDataRequest(self):
        #===============================================================================
        #        Test that the owner must provide a datarequest to add a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.blank_datarequest_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertNotContains(datarequest_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerNoTag(self):
        #===============================================================================
        #        Test that the owner must provide a tagname to add a tag
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.blank_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertNotContains(datarequest_response, self.valid_post_data['unparsed_tag_names'])
        
    def testAddTagOwnerDuplicateTag(self):
        #===============================================================================
        #        Test that adding a duplicate tag won't actually put it in the db/taglist
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.duplicate_tag_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertContains(datarequest_response, self.duplicate_tag_post_data['unparsed_tag_names'], count=7)
        
    def testAddTagOwnerValid(self):
        #===============================================================================
        #        Test that the owner can add a tag given a datarequest id and a tag name
        #===============================================================================
        self.tryLogin('bob')
        
        get_response = self.client.get(self.add_tag_url)
        self.assertEqual(get_response.status_code, 200)
        
        post_response = self.client.post(self.add_tag_url, self.valid_post_data)
        self.assertEqual(post_response.status_code, 200)
        
        datarequest_response = self.client.get(self.datarequest_tagged_url)
        self.assertContains(datarequest_response, self.valid_post_data['unparsed_tag_names'], count=7)