from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase


class ForgotPasswordTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.forgot_password_url = reverse('epic.core.views.forgot_password')
    
    def tearDown(self):
        pass
    
    def testForgotPasswordSubmittingBlankForm(self):
        """
        Test that submitting a blank form yeilds an error.
        """
        
        get_response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(get_response.status_code, 200)
        
        post_data = {'username_or_email': '',}
        post_response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(post_response.status_code, 200)
        self.assertFormError(
            post_response, 'form', 'username_or_email', 'This field is required.')
    
    def testForgotPasswordSubmittingUnboundUsername(self):
        get_response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(get_response.status_code, 200)
        
        post_data = {'username_or_email': 'asdf0 jw35yj[ j0q2rj',}
        post_response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(post_response.status_code, 200)
        self.assertFormError(
            post_response,
            'form',
            'username_or_email',
            "'%s' is not a valid username." % post_data['username_or_email'])
    
    def testForgotPasswordSubmittingUnboundEmail(self):
        get_response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(get_response.status_code, 200)
        
        post_data = {'username_or_email': 'asdf323@asdf.com',}
        post_response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(post_response.status_code, 200)
        self.assertFormError(
            post_response,
            'form',
            'username_or_email',
            "There is no user registered with the email address '%s'." % \
                post_data['username_or_email'])
    
    def testForgotUsernameSuccessWithBoundUsername(self):
        get_response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(get_response.status_code,200)
        
        post_data = {'username_or_email': self.bob.username,}
        post_response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(post_response.status_code, 200)
        self.assertContains(
            post_response,
            'An email has been sent to your &#39;%s&#39; address with a new password.' % \
                self.bob.email.split('@')[1])    

    def testForgotUsernameSuccessWithBoundEmail(self):
        get_response = self.client.get(self.forgot_password_url)
        self.failUnlessEqual(get_response.status_code,200)
        
        post_data = {'username_or_email': self.bob.email,}
        post_response = self.client.post(self.forgot_password_url, post_data)
        self.failUnlessEqual(post_response.status_code, 200)
        self.assertContains(
            post_response,
            'An email has been sent to your &#39;%s&#39; address with a new password.' % \
                self.bob.email.split('@')[1])
