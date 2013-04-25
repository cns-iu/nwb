from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.datarequests.models import DataRequest


BOB_USERNAME = 'bob'
BOB_PASSWORD = 'bob'

BILL_USERNAME = 'bill'
BILL_PASSWORD = 'bill'

class ProfileDataRequestTestCase(CustomTestCase):
    
    fixtures = ['core_profile_datarequests']
    
    def setUp(self):
        self.profile_url = reverse('epic.core.views.view_profile')

    def tearDown(self):
        pass
    
    def testForNoDataRequests(self):
        """Test if data requests are published even though not belonging to
        that user. Bob has no data requests.
        """
        
        self.tryLogin(BOB_USERNAME)
        
        response = self.client.get(self.profile_url)
        self.failUnless(response.status_code, 200)
        self.assertNotContains(response, 'Your Data Requests')

    def testForDataRequests(self):
        """Test if a User's Data Request is shown in that User's profile. In
        this case Bill has 4 data requests.
        """
        
        self.tryLogin(BILL_USERNAME)
        
        user = User.objects.get(username=BILL_USERNAME)
        
        datarequest1 = DataRequest(
            creator=user, 
            name='DataRequest object with Status U', 
            description='DataRequest object with Status U for Bill',
            status='U',
            slug='whatever',
            is_active=True)
        datarequest1.save()
        
        datarequest2 = DataRequest(
            creator=user, 
            name='DataRequest object with Status F', 
            description='DataRequest object with Status F for Bill',
            status='F',
            slug='whatever',
            is_active=True)
        datarequest2.save()
        
        response = self.client.get(self.profile_url)

        self.failUnless(response.status_code, 200)
        self.assertContains(response, 'Your Data Requests')
        self.assertContains(response, 'edit')
        self.assertContains(response, 'DataRequest object with Status U')
        self.assertContains(response, 'DataRequest object with Status F')

    def testForNoCanceledDataRequests(self):
        """Test if canceled data requests are published for the logged in
        user.  In this case, Bill has 1 canceled data request which should not
        be published.
        """
        
        self.tryLogin(BILL_USERNAME)

        user = User.objects.get(username=BILL_USERNAME)
        
        datarequest = DataRequest(
            creator=user, 
            name='DataRequest object with Status C (Should not be shown)', 
            description='DataRequest object with Status C for Bill',
            status='C',
            slug='whatever',
            is_active=True)
        datarequest.save()
        
        response = self.client.get(self.profile_url)
        
        self.failUnless(response.status_code, 200)
        self.assertContains(response, 'Your Data Requests')
        self.assertNotContains(response, 'DataRequest object with Status C')
        self.assertContains(response, 'edit')
