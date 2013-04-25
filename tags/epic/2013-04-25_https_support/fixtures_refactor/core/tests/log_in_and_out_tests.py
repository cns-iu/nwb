from django.core.urlresolvers import reverse
from django.contrib.auth.models import User
from django.test import TestCase

from epic.settings import LOGIN_REDIRECT_URL


BOB_USER_USERNAME = 'bob'
BOB_USER_PASSWORD = 'bob'
BOB2_USER_USERNAME = 'bob2'
BOB2_USER_PASSWORD = 'bob2'

USERNAME_KEY = 'username'
PASSWORD_KEY = 'password'

class LogInAndOutTestCase(TestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.login_url = reverse('django.contrib.auth.views.login')
        self.logout_url = reverse('epic.core.views.logout_view')
        self.bob = User.objects.get(username=BOB_USER_USERNAME)
        
    def tearDown(self):
        pass
    
    def testLoginSuccess(self):
        """
        Test that a user can successfully login.
        """
        
        response = self.client.get(self.login_url)
        self.failUnlessEqual(response.status_code, 200)
        
        post_data = {
            USERNAME_KEY: self.bob.username,
            PASSWORD_KEY: BOB_USER_PASSWORD,        
        }
        
        response = self.client.post(self.login_url, post_data)
        self.failUnlessEqual(response.status_code, 302)
        # TODO Once the follow=true actually works it should be used here.
        self.assertRedirects(response, LOGIN_REDIRECT_URL)
    
    def testLoginFailure(self):
        """
        Test that a user must give correct the username/password to log in.
        """
        
        post_data = {
            USERNAME_KEY: self.bob.username,
            PASSWORD_KEY: 'incorrect password',        
        }
        
        response = self.client.post(self.login_url, post_data)
        self.failUnlessEqual(response.status_code, 200)
        self.assertFormError(response,
                             'form',
                             '__all__',
                             'Please enter a correct username and ' + \
                                'password. Note that both fields are ' + \
                                'case-sensitive.')
    
    def testLogout(self):
        """
        Test that logging out using the view works correctly.
        """
        
        login = self.client.login(username=BOB2_USER_USERNAME,
                                  password=BOB2_USER_PASSWORD)
        self.failUnless(login, 'Could not login')
        
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
        login = self.client.login(username=BOB2_USER_USERNAME,
                                  password=BOB2_USER_PASSWORD)
        self.failUnless(login, 'Could not login')
        
        response = self.client.get('/')
        
        self.assertContains(response, self.logout_url)