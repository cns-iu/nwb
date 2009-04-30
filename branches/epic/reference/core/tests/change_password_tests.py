from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase


BOB_USERNAME = 'bob'
BOB_PASSWORD = 'bob'

CHANGE_PASSWORD_FORM_NAME = 'form'
OLD_PASSWORD_KEY = 'old_password'
NEW_PASSWORD_KEY = 'new_password1'
NEW_PASSWORD_CONFIRMATION_KEY = 'new_password2'

VALID_NEW_PASSWORD = 'blah'
INVALID_NEW_PASSWORD = 'b' 

class ChangePasswordTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.change_password_url = reverse('epic.core.views.change_password')
        self.login_url = reverse('django.contrib.auth.views.login')
        
    def tearDown(self):
        pass
    
    def testPage(self):
        """
        Test to make sure the change password page is visible for logged in
        users.
        """
        
        self.tryLogin(BOB_USERNAME)
        
        # Make certain the changepassword page is there.
        response = self.client.get(self.change_password_url)
        self.assertEqual(response.status_code, 200)
    
    def testPageNotLoggedInGet(self):
        """
        Test that non logged in users are redirected to login if they try to
        get the page.
        """
        response = self.client.get(self.change_password_url)
        # TODO: This should not just append the next variable.
        redirect_url = '%(base_url)s?next=%(next_url)s' % \
            {'base_url': self.login_url, 'next_url': self.change_password_url}
        self.assertRedirects(response, redirect_url, 302)
        
    def testPageNotLoggedInPost(self):
        """
        Test that non logged in users are redirected to login if they try to
        post to the page.
        """
        response = self.client.get(self.change_password_url)

        post_data = {
            OLD_PASSWORD_KEY: 'incorrectOldPassword',
            NEW_PASSWORD_KEY: VALID_NEW_PASSWORD,
            NEW_PASSWORD_CONFIRMATION_KEY: VALID_NEW_PASSWORD
        }
        
        response = self.client.post(self.change_password_url, post_data)
        # TODO: This should not just append the next variable.
        redirect_url = '%(base_url)s?next=%(next_url)s' % \
            {'base_url': self.login_url, 'next_url': self.change_password_url}
        self.assertRedirects(response, redirect_url, 302)
    
    def testFailToChangeMatchProblem(self):
        """
        Test that entering non-matching new passwords causes an error in the
        form.
        """
        self.tryLogin(BOB_USERNAME)
        
        post_data = {
            OLD_PASSWORD_KEY: BOB_PASSWORD,
            NEW_PASSWORD_KEY: INVALID_NEW_PASSWORD,
            NEW_PASSWORD_CONFIRMATION_KEY: VALID_NEW_PASSWORD
        }
        
        response = self.client.post(self.change_password_url, post_data)
        self.failUnless(response.status_code, 200)
        self.assertFormError(response,
                             CHANGE_PASSWORD_FORM_NAME,
                             NEW_PASSWORD_CONFIRMATION_KEY,
                             "The two password fields didn't match.")
    
    def testFailToChangeOldPasswordProblem(self):
        """
        Test that entering an incorrect old password causes an error in the
        form.
        """
        self.tryLogin(username=BOB_USERNAME, password=BOB_PASSWORD)
        
        post_data = {
            OLD_PASSWORD_KEY: 'incorrectOldPassword',
            NEW_PASSWORD_KEY: VALID_NEW_PASSWORD,
            NEW_PASSWORD_CONFIRMATION_KEY: VALID_NEW_PASSWORD
        }
        
        response = self.client.post(self.change_password_url, post_data)
        self.failUnless(response.status_code, 200)
        self.assertFormError(response,
                             CHANGE_PASSWORD_FORM_NAME,
                             OLD_PASSWORD_KEY,
                             'Your old password was entered incorrectly. ' + \
                                'Please enter it again.')
    
    def testChangePasswordSuccess(self):
        """
        Test that the form will actually change a user's password if used
        correctly.
        """
        self.tryLogin(username=BOB_USERNAME, password=BOB_PASSWORD)
        
        post_data = {
            OLD_PASSWORD_KEY: BOB_PASSWORD,
            NEW_PASSWORD_KEY: VALID_NEW_PASSWORD,
            NEW_PASSWORD_CONFIRMATION_KEY: VALID_NEW_PASSWORD
        }
        
        response = self.client.post(self.change_password_url, post_data)
        self.failUnless(response.status_code, 302)
        # This is set to match the redirect in core.views.change_password.
        redirect_url = reverse('epic.core.views.view_profile')
        self.assertRedirects(response, redirect_url)
        
        logout = self.client.logout()
        
        login = self.client.login(
            username=BOB_USERNAME, password=post_data[OLD_PASSWORD_KEY])
        self.failIf(login)
        
        self.tryLogin(username=BOB_USERNAME, 
                      password=post_data[NEW_PASSWORD_KEY])