from django.core.urlresolvers import reverse
from django.test import TestCase


BOB_USER_USERNAME = 'bob'
BOB_USER_PASSWORD = 'bob'

class LogoTestCase(TestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        pass
    def tearDown(self):
        pass
    
    def testLogoShouldBeLink(self):
        """
        Test that the site_index link appears on several pages to hopefully
        determine that the logo can be clicked.
        """
        
        # Setup the links.
        
        logo_link = reverse('epic.core.views.site_index')
        browse_url = reverse('epic.core.views.browse')
        upload_url = reverse('epic.datasets.views.create_dataset')
        
        # Check that the logo_link is on each page.  Best I can do.
        response = self.client.get(browse_url)
        self.assertContains(response, logo_link)
        
        # You must be logged in to see the upload page.
        login = self.client.login(username=BOB_USER_USERNAME,
                                  password=BOB_USER_PASSWORD)
        self.failUnless(login, 'Could not login')
        
        response = self.client.get(upload_url)
        self.assertContains(response, logo_link)