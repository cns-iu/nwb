from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from epic.core.test import CustomTestCase


BOB_USER_USERNAME = 'bob'
BOB_USER_PASSWORD = 'bob'

FORM_KEY = 'form'
USERNAME_OR_EMAIL_KEY = 'username_or_email'

class ForgotPasswordTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.bob = User.objects.get(username=BOB_USER_USERNAME)
        self.forgot_password_url = reverse('epic.core.views.forgot_password')
    
    def tearDown(self):
        pass
    
    def testForgotPasswordSubmittingBlankForm(self):
        """
        Test that submitting a blank form yeilds an error.
        """
        
        response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(response.status_code, 200)
        
        post_data = {
            USERNAME_OR_EMAIL_KEY: '',
        }
        
        response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(response.status_code, 200)
        
        self.assertFormError(response,
                             FORM_KEY,
                             USERNAME_OR_EMAIL_KEY,
                             'This field is required.')
    
    #TODO: I stopped cleaning core here.
    def testForgotPasswordSubmittingUnboundUsername(self):
        response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(response.status_code,200)
        
        post_data = {
            USERNAME_OR_EMAIL_KEY: 'asdf0 jw35yj[ j0q2rj',
        }
        
        response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(response.status_code, 200)
        
        self.assertFormError(response,
                             FORM_KEY,
                             USERNAME_OR_EMAIL_KEY,
                             "'%s' is not a valid username." % \
                                post_data[USERNAME_OR_EMAIL_KEY])
    
    def testForgotPasswordSubmittingUnboundEmail(self):
        response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(response.status_code,200)
        
        post_data = {
            USERNAME_OR_EMAIL_KEY: 'asdf323@asdf.com',
        }
        
        response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(response.status_code, 200)
        
        self.assertFormError(response,
                             FORM_KEY,
                             USERNAME_OR_EMAIL_KEY,
                             "There is no user registered with the " + \
                                "email address '%s'." % \
                                post_data[USERNAME_OR_EMAIL_KEY])
    
    def testForgotUsernameSuccessWithBoundUsername(self):
        response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(response.status_code,200)
        
        post_data = {
            USERNAME_OR_EMAIL_KEY: self.bob.username,
        }
        
        response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(response.status_code, 200)
        self.assertContains(
            response, ('An email has been sent to your &#39;%s&#39; ' + \
                      'address with a new password.') % \
                      self.bob.email.split('@')[1])    

    def testForgotUsernameSuccessWithBoundEmail(self):
        response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(response.status_code,200)
        
        post_data = {
            USERNAME_OR_EMAIL_KEY: self.bob.email,
        }
        
        response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(response.status_code, 200)
        self.assertContains(
            response, ('An email has been sent to your &#39;%s&#39; ' + \
                      'address with a new password.') % \
                      self.bob.email.split('@')[1])