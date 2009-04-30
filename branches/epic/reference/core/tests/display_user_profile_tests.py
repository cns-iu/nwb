from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from epic.core.test import CustomTestCase

class ViewBasicUserProfileTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.bob2 = User.objects.get(username='bob2')
        self.bob_profile_url = reverse('epic.core.views.view_profile',
                                       kwargs={'user_id':self.bob.id})
        self.login_url = reverse('django.contrib.auth.views.login')
        
    def tearDown(self):
        pass
    
    def testViewProfileNotLoggedIn(self):
        response = self.client.get(self.bob_profile_url)
        # TODO: This should not just append the next variable
        redirect_url = '%(base_url)s?next=%(next_url)s' % \
            {'base_url': self.login_url, 'next_url': self.bob_profile_url}
        self.assertRedirects(response, redirect_url, 302)
    
    def testViewProfileLoggedInBob(self):
        self.tryLogin(username='bob')
        
        response = self.client.get(self.bob_profile_url)
        self.failUnlessEqual(response.status_code, 200)
        
        user = User.objects.get(username='bob')
        self.assertContains(response, user.get_profile().full_title())
        
    def testViewProfileLoggedInNotBob(self):
        self.tryLogin(username='bob2')
        
        response = self.client.get(self.bob_profile_url)
        self.failUnlessEqual(response.status_code, 200)
        
        self.assertNotContains(response, self.bob.username)
        self.assertContains(response, self.bob.get_profile().full_title())