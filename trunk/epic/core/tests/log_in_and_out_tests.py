from django.core.urlresolvers import reverse
from django.contrib.auth.models import User

from epic.core.test import CustomTestCase
from epic.settings import LOGIN_REDIRECT_URL


class LogInAndOutTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.login_url = reverse('django.contrib.auth.views.login')
        self.logout_url = reverse('epic.core.views.logout_view')
    
    def testLoginSuccess(self):
        """ Test that a user can successfully login.
        """
        
        response = self.client.get(self.login_url)
        self.failUnlessEqual(response.status_code, 200)
        
        post_data = {'username': self.bob.username, 'password': 'bob',}
        response = self.client.post(self.login_url, post_data)
        self.failUnlessEqual(response.status_code, 302)
        # TODO Once the follow=true actually works it should be used here.
        self.assertRedirects(response, LOGIN_REDIRECT_URL)
    
    def testLoginFailure(self):
        """ Test that a user must give correct the username/password to
            log in.
        """
        
        post_data = {'username': self.bob.username, 'password': 'incorrect password',}
        response = self.client.post(self.login_url, post_data)
        self.failUnlessEqual(response.status_code, 200)
        self.assertFormError(
            response,
            'form',
            '__all__',
            'Please enter a correct username and password. ' + \
                'Note that both fields are case-sensitive.')
    
    def testLogout(self):
        """ Test that logging out using the view works correctly.
        """
        
        self.tryLogin(username='bob2', password='bob2')
        
        response = self.client.get(self.logout_url)
        self.failUnlessEqual(response.status_code, 302)

        response = self.client.get('/')
        self.assertContains(response, self.login_url)
        
    def testLoginFrontPage(self):
        """
        Verify that the login url appears somewhere on the front page.
        """
        
        response = self.client.get('/')
        self.assertContains(response, self.login_url)
        
    def testLogoutFrontPage(self):
        """
        Test that the logout link appears for logged in users
        """        
        self.tryLogin(username='bob2', password='bob2')
        
        response = self.client.get('/')
        self.assertContains(response, self.logout_url)