from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.projects.models import Project


BOB_USERNAME = 'bob'
BOB_PASSWORD = 'bob'

BILL_USERNAME = 'bill'
BILL_PASSWORD = 'bill'

class ProfileProjectTestCase(CustomTestCase):
    fixtures = ['core_profile_projects']
    
    def setUp(self):
        self.profile_url = reverse('epic.core.views.view_profile')
        self.project1 = Project.objects.get(name='Test Project1')
    
    def testForNoProjects(self):
        self.tryLogin(BILL_USERNAME)
        
        response = self.client.get('/user/')
        
        self.failUnless(response.status_code, 200)
        self.assertNotContains(response, 'Your Projects')
    
    def testForProjects(self):
        self.tryLogin(BOB_USERNAME)
        
        response = self.client.get('/user/')
        
        self.failUnless(response.status_code, 200)
        self.assertContains(response, 'Your Projects')
        self.assertContains(response, 'edit')
        self.assertContains(response, self.project1.name)

