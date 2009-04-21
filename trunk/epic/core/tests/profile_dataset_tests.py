from django.contrib.auth.models import User

from epic.core.test import CustomTestCase


BOB_USERNAME = 'bob'
BOB_PASSWORD = 'bob'

BILL_USERNAME = 'bill'
BILL_PASSWORD = 'bill'

class ProfileDatasetTestCase(CustomTestCase):
    fixtures = ['core_profile_datasets']
    
    def setUp(self):
        pass
    
    def testForNoDataSets(self):
        self.tryLogin(BOB_USERNAME)
        response = self.client.get('/user/')
        self.failUnless(response.status_code, 200)
        self.assertNotContains(response, 'Your Datasets')
    
    def testForDataSets(self):
        self.tryLogin(BILL_USERNAME)
        response = self.client.get('/user/')
        self.failUnless(response.status_code, 200)
        self.assertContains(response, 'Your Datasets')
        self.assertContains(response, 'edit')
