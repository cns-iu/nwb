from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet

class DataRequestTestCase(CustomTestCase):
    """ Test that the model for datarequests is working correctly """
    
    fixtures = ['datarequests_just_users', 'datarequests_datarequests']
    
    def setUp(self):
        self.admin = User.objects.get(username='admin')
        self.bob = User.objects.get(username='bob')
        
        self.canceled_datarequest1 = DataRequest.objects.get(creator=self.bob, name='canceled_datarequest1', description='The first canceled datarequest', status='C')
        self.canceled_datarequest2 = DataRequest.objects.get(creator=self.admin, name='canceled_datarequest2', description='The second canceled datarequest', status='C')
        self.canceled_datarequest3 = DataRequest.objects.get(creator=self.bob, name='canceled_datarequest3', description='The third canceled datarequest', status='C')
        self.canceled_datarequest4 = DataRequest.objects.get(creator=self.admin, name='canceled_datarequest4', description='The fourth canceled datarequest', status='C')
        self.canceled_datarequest5 = DataRequest.objects.get(creator=self.bob, name='canceled_datarequest5', description='The fifth canceled datarequest', status='C')
        
        self.fulfilled_datarequest1 = DataRequest.objects.get(creator=self.bob, name='fulfilled_datarequest1', description='The first fulfilled datarequest', status='F')
        self.fulfilled_datarequest2 = DataRequest.objects.get(creator=self.admin, name='fulfilled_datarequest2', description='The second fulfilled datarequest', status='F')
        self.fulfilled_datarequest3 = DataRequest.objects.get(creator=self.bob, name='fulfilled_datarequest3', description='The third fulfilled datarequest', status='F')
        self.fulfilled_datarequest4 = DataRequest.objects.get(creator=self.admin, name='fulfilled_datarequest4', description='The fourth fulfilled datarequest', status='F')
        self.fulfilled_datarequest5 = DataRequest.objects.get(creator=self.bob, name='fulfilled_datarequest5', description='The fifth fulfilled datarequest', status='F')
        self.fulfilled_datarequest6 = DataRequest.objects.get(creator=self.admin, name='fulfilled_datarequest6', description='The sixth fulfilled datarequest', status='F')
    
        self.unfulfilled_datarequest1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U')
        self.unfulfilled_datarequest2 = DataRequest.objects.get(creator=self.admin, name='unfulfilled_datarequest2', description='The second unfulfilled datarequest', status='U')
        self.unfulfilled_datarequest3 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest3', description='The third unfulfilled datarequest', status='U')
        self.unfulfilled_datarequest4 = DataRequest.objects.get(creator=self.admin, name='unfulfilled_datarequest4', description='The fourth unfulfilled datarequest', status='U')
    
    def tearDown(self):
        pass
    
    def testUnfulfilled(self):
        self.assertEqual(DataRequest.objects.unfulfilled().count(), 4)
        self.unfulfilled_datarequest1.cancel()
        self.unfulfilled_datarequest1.save()
        self.assertEqual(DataRequest.objects.unfulfilled().count(), 3)
        for dr in DataRequest.objects.unfulfilled():
            self.assertEqual('unfulfilled', dr.get_status_display())
        
    def testFulfilled(self):
        self.assertEqual(DataRequest.objects.fulfilled().count(), 6)
        self.unfulfilled_datarequest1.fulfill()
        self.unfulfilled_datarequest1.save()
        self.assertEqual(DataRequest.objects.fulfilled().count(), 7)
        for dr in DataRequest.objects.fulfilled():
            self.assertEqual('fulfilled', dr.get_status_display())
    
    def testCanceled(self):
        self.assertEqual(DataRequest.objects.canceled().count(), 5)
        self.unfulfilled_datarequest1.cancel()
        self.unfulfilled_datarequest1.save()
        self.assertEqual(DataRequest.objects.canceled().count(), 6)
        for dr in DataRequest.objects.canceled():
            self.assertEqual('canceled', dr.get_status_display())
    
    def testLifeCycle(self):
        self.assertEqual(DataRequest.objects.unfulfilled().count(), 4)
        dr = DataRequest.objects.create(creator=self.bob, name="Amazing request", description="this is the description", is_active=True)
        self.assertEqual(DataRequest.objects.unfulfilled().count(), 5)
        dr.fulfill()
        dr.save()
        self.assertEqual(DataRequest.objects.unfulfilled().count(), 4)
        self.assertEqual(DataRequest.objects.fulfilled().count(), 7)
        self.assertTrue(dr in DataRequest.objects.fulfilled())
        
class UrlsTestCase(CustomTestCase):
    """ Test all the urls to make sure that the view for each works """
    
    fixtures = ['datarequests_just_users', 'datarequests_datarequests'] # TODO: I just need one datarequest and a user
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U')
    
    def tearDown(self):
        pass
    
    def testViewDataRequests(self):
        url = reverse('epic.datarequests.views.view_datarequests')
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 200)
        
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 200)
    
    def testNewDataReqeusts(self):
        url = reverse('epic.datarequests.views.new_datarequest')
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 302)
        
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 200)
        
    def testEditDataReqeusts(self):
        url = reverse('epic.datarequests.views.edit_datarequest', kwargs={'item_id':self.dr1.id, 'slug':self.dr1.slug })
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 302)
        
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 200)
        
    def testCancelDataRequests(self):
        url = reverse('epic.datarequests.views.cancel_datarequest', kwargs={'item_id':self.dr1.id, 'slug':self.dr1.slug })
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 302)
        
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 302)
        
    def testFulfillDataReqeusts(self):
        url = reverse('epic.datarequests.views.fulfill_datarequest', kwargs={'item_id':self.dr1.id, 'slug':self.dr1.slug })
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 302)
        
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(url)
        self.failUnlessEqual(response.status_code, 302)
        
class ViewDatarequestsTestCase(CustomTestCase):
    """ Test the view datarequests page/view """
    
    fixtures = ['datarequests_just_users', 'datarequests_datarequests']
    
    def setUp(self):
        self.bob = User.objects.get(username="bob")
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U')
    
        self.datarequests_url = reverse('epic.datarequests.views.view_datarequests')
        self.edit_url = reverse('epic.datarequests.views.edit_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        self.cancel_url = reverse('epic.datarequests.views.cancel_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        self.fulfill_url = reverse('epic.datarequests.views.fulfill_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        
    def tearDown(self):
        pass
    
    def testIndexLoggedOut(self):
        response = self.client.get(self.datarequests_url)
        self.assertEqual(response.status_code, 200)
        
        for dr in DataRequest.objects.fulfilled():
            self.assertContains(response, dr.name)
        
        for dr in DataRequest.objects.unfulfilled():
            self.assertContains(response, dr.name)
        
        for dr in DataRequest.objects.canceled():
            self.assertNotContains(response, dr.name)
            
        self.assertNotContains(response, self.edit_url)
        self.assertNotContains(response, self.cancel_url)
        self.assertNotContains(response, self.fulfill_url)
            
    def testIndexUnOwned(self):
        self.tryLogin(username='admin', password='admin')
        
        response = self.client.get(self.datarequests_url)
        self.assertEqual(response.status_code, 200)
        
        for dr in DataRequest.objects.fulfilled():
            self.assertContains(response, dr.name)
        
        for dr in DataRequest.objects.unfulfilled():
            self.assertContains(response, dr.name)
        
        for dr in DataRequest.objects.canceled():
            self.assertNotContains(response, dr.name)
            
        self.assertNotContains(response, self.edit_url)
        self.assertNotContains(response, self.cancel_url)
        self.assertNotContains(response, self.fulfill_url)
            
    def testIndexOwned(self):
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.datarequests_url)
        self.assertEqual(response.status_code, 200)
        
        for dr in DataRequest.objects.fulfilled():
            self.assertContains(response, dr.name)
        
        for dr in DataRequest.objects.unfulfilled():
            self.assertContains(response, dr.name)
        
        for dr in DataRequest.objects.canceled():
            self.assertNotContains(response, dr.name)
            
        self.assertContains(response, self.edit_url)
        self.assertContains(response, self.cancel_url)
        self.assertContains(response, self.fulfill_url)
        
class ViewDataRequestTestCase(CustomTestCase):
    """ Test the view datarequest page/view """
    
    fixtures = ['datarequests_just_users', 'datarequests_datarequests']
    
    def setUp(self):
        self.bob = User.objects.get(username="bob")
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, 
                                           name='unfulfilled_datarequest1', 
                                           description='The first unfulfilled datarequest', 
                                           status='U')
        
        self.datarequest_url = reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        self.edit_url = reverse('epic.datarequests.views.edit_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        self.cancel_url = reverse('epic.datarequests.views.cancel_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        self.fulfill_url = reverse('epic.datarequests.views.fulfill_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        
    def tearDown(self):
        pass
    
    def testLoggedOut(self):
        response = self.client.get(self.datarequest_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.dr1.name)
        self.assertContains(response, self.dr1.description)
        self.assertContains(response, self.dr1.get_status_display())
        
        self.assertNotContains(response, self.edit_url)
        self.assertNotContains(response, self.cancel_url)
        self.assertNotContains(response, self.fulfill_url)
    
    def testLoggedInNotOwner(self):
        self.tryLogin(username='admin', password='admin')
        
        response = self.client.get(self.datarequest_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.dr1.name)
        self.assertContains(response, self.dr1.description)
        self.assertContains(response, self.dr1.get_status_display())
        
        self.assertNotContains(response, self.edit_url)
        self.assertNotContains(response, self.cancel_url)
        self.assertNotContains(response, self.fulfill_url)
    
    def testLoggedInOwner(self):
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.datarequest_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.dr1.name)
        self.assertContains(response, self.dr1.description)
        self.assertContains(response, self.dr1.get_status_display())
        
        self.assertContains(response, self.edit_url)
        self.assertContains(response, self.cancel_url)
        self.assertContains(response, self.fulfill_url)
        
    def testNonExistentPage(self):
        response = self.client.get(reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id': 10000000000000000, 'slug': 'fake-slug',}))
        self.failUnless(response.status_code, 404)
        
class NewDataRequestTestCase(CustomTestCase):
    """ Test the new datarequest page/view """
    
    fixtures = ['datarequests_just_users', 'datarequests_datarequests']
    
    def setUp(self):
        self.new_datarequest_url = reverse('epic.datarequests.views.new_datarequest')    
        self.datarequests_url = reverse('epic.datarequests.views.view_datarequests')
        
        self.post_data = {
            'name': 'This is a new datarequest that is asdf983205',
            'description': 'Jump, Jump, Jump Around!',
        }
        
    def tearDown(self):
        pass
    
    def testLoggedOut(self):
        response = self.client.get(self.new_datarequest_url)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.post(self.new_datarequest_url, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.get(self.datarequests_url)
        self.assertNotContains(response, self.post_data['name'])
        self.assertNotContains(response, self.post_data['description'])
    
    def testLogged(self):
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.new_datarequest_url)
        self.assertEqual(response.status_code, 200)
        
        response = self.client.post(self.new_datarequest_url, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.get(self.datarequests_url)
        self.assertContains(response, self.post_data['name'])
        self.assertContains(response, self.post_data['description'])
        
class CancelDatarequestsTestCase(CustomTestCase):
    """ Test the cancel datarequests page/view """
    
    fixtures = ['datarequests_just_users', 'datarequests_datarequests']
    
    def setUp(self):
        self.bob = User.objects.get(username="bob")
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest')

        self.cancel_url = reverse('epic.datarequests.views.cancel_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        
    def tearDown(self):
        pass
    
    def testLoggedOut(self):
        response = self.client.get(self.cancel_url)
        self.assertEqual(response.status_code, 302)
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest')
        
        self.assertEqual(self.dr1.status, 'U')

            
    def testUnOwned(self):
        self.tryLogin(username='admin', password='admin')
        
        response = self.client.get(self.cancel_url)
        self.assertEqual(response.status_code, 302)
        
        self.assertEqual(self.dr1.status, 'U')
            
    def testOwned(self):
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.cancel_url)
        self.assertEqual(response.status_code, 302)

        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest')
        
        self.assertEqual(self.dr1.status, 'C')

class FulfillDatarequestsTestCase(CustomTestCase):
    """ Test the fulfill datarequests page/view """
    
    fixtures = ['datarequests_just_users', 'datarequests_datarequests']
    
    def setUp(self):
        self.bob = User.objects.get(username="bob")
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest')
        
        self.fulfill_url = reverse('epic.datarequests.views.fulfill_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        
    def tearDown(self):
        pass
    
    def testLoggedOut(self):
        response = self.client.get(self.fulfill_url)
        self.assertEqual(response.status_code, 302)
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest')
        
        self.assertEqual(self.dr1.status, 'U')

            
    def testUnOwned(self):
        self.tryLogin(username='admin', password='admin')
        
        response = self.client.get(self.fulfill_url)
        self.assertEqual(response.status_code, 302)
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest')
        
        self.assertEqual(self.dr1.status, 'U')
            
    def testOwned(self):
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.fulfill_url)
        self.assertEqual(response.status_code, 302)
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest')
        
        self.assertEqual(self.dr1.status, 'F')

class FulfillDatarequestWithItemTestCase(CustomTestCase):
    """ Test that fulfilling a datarequest with an item adds
        that item as a fulfilling item 
    
    """
    fixtures = ['datarequests_just_users', 'datarequests_datarequests']
    
    def setUp(self):
        self.bob = User.objects.get(username="bob")
        self.dataset1 = DataSet.objects.create(creator=self.bob, 
                                               name='asdfnasdf', 
                                               description='asdf3')
        self.datarequest1 = DataRequest.objects.get(
                        creator=self.bob, 
                        name='unfulfilled_datarequest1', 
                        description='The first unfulfilled datarequest')
        
        self.fulfill_url = reverse(
            'epic.datarequests.views.fulfill_datarequest', 
            kwargs={'item_id': self.datarequest1.id, 
                    'slug':self.datarequest1.slug, 
                    'fulfilling_item_id':self.dataset1.id })
    
    def test_loggedout(self):
        # Logged out users shouldn't be able to add the fulfilling item.
        
        response = self.client.get(self.fulfill_url)
        
        self.datarequest1 = DataRequest.objects.get(
                            creator=self.bob, 
                            name='unfulfilled_datarequest1', 
                            description='The first unfulfilled datarequest')
        
        self.assertFalse(self.datarequest1.fulfilling_item)
    
    def test_loggedin_notowner(self):
        # Non owners should not be able to add the fulfilling item.
        
        self.tryLogin('bob2')
        response = self.client.get(self.fulfill_url)
        
        self.datarequest1 = DataRequest.objects.get(
                            creator=self.bob, 
                            name='unfulfilled_datarequest1', 
                            description='The first unfulfilled datarequest')
        
        self.assertFalse(self.datarequest1.fulfilling_item)
    
    def test_loggedin_owner(self):
        # The owner should be able to add the fulfilling item.
        
        self.tryLogin('bob')
        response = self.client.get(self.fulfill_url)
        
        self.datarequest1 = DataRequest.objects.get(
                            creator=self.bob, 
                            name='unfulfilled_datarequest1', 
                            description='The first unfulfilled datarequest')
        
        self.assertEqual(self.datarequest1.fulfilling_item.id, self.dataset1.id)

class EditDatarequestsTestCase(CustomTestCase):
    """ Test the edit datarequests page/view """
    
    fixtures = ['datarequests_just_users', 'datarequests_datarequests']
    
    def setUp(self):
        self.bob = User.objects.get(username="bob")
        self.admin = User.objects.get(username="admin")
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U')
        self.edit_url = reverse('epic.datarequests.views.edit_datarequest', kwargs={'item_id': self.dr1.id, 'slug':self.dr1.slug })
        
        self.post_data = {
            'name': 'new nameasdf 2589w',
            'description': 'I have changed!',
        }
        
    def tearDown(self):
        pass
    
    def testLoggedOut(self):
        response = self.client.get(self.edit_url)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.post(self.edit_url, self.post_data)
        self.assertEqual(response.status_code, 302)
    
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U')
        
    def testUnOwned(self):
        self.tryLogin(username='admin', password='admin')
        
        response = self.client.get(self.edit_url)
        self.assertEqual(response.status_code, 302)
        
        response = self.client.post(self.edit_url, self.post_data)
        self.assertEqual(response.status_code, 302)
        
        self.dr1 = DataRequest.objects.get(creator=self.bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U')
        
    def testOwned(self):
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.edit_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertNotContains(response, self.post_data['name'])
        self.assertNotContains(response, self.post_data['description'])
        
        response = self.client.post(self.edit_url, self.post_data)
        self.assertEqual(response.status_code, 302)
    
        self.dr1 = DataRequest.objects.get(creator=self.bob, name=self.post_data['name'], description=self.post_data['description'], status='U')
        