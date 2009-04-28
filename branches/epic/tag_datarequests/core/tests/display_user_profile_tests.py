from django.core.urlresolvers import reverse
from epic.core.test import CustomTestCase

#TODO: Refactor these, and use "tryLogin"
BOB_USER_USERNAME = 'bob'
BOB_USER_PASSWORD = 'bob'

class ViewBasicUserProfileTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.profile_url = reverse('epic.core.views.view_profile')
        self.login_url = reverse('django.contrib.auth.views.login')
        
    def tearDown(self):
        pass
    
    def testViewProfileNotLoggedIn(self):
        response = self.client.get(self.profile_url)
        # TODO: This should not just append the next variable
        redirect_url = '%(base_url)s?next=%(next_url)s' % \
            {'base_url': self.login_url, 'next_url': self.profile_url}
        self.assertRedirects(response, redirect_url, 302)
    
    def testViewProfileLoggedIn(self):
        self.tryLogin(username=BOB_USER_USERNAME, password=BOB_USER_PASSWORD)
        
        response = self.client.get(self.profile_url)
        self.failUnlessEqual(response.status_code, 200)
        self.assertContains(response, 'Displaying profile of')
