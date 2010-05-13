from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.models import Profile
from epic.core.test import CustomTestCase


class ViewEditProfilePageCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.edit_profile_url = reverse('epic.core.views.edit_profile')
        self.login_url = reverse('django.contrib.auth.views.login')
    
    def testLoggedOutView(self):
        """ Verify that logged out users can't reach the edit profile page.
        """
        
        response = self.client.get(self.edit_profile_url)
        # TODO: This should not set next this way.
        redirect_url = '%(base_url)s?next=%(next_url)s' % \
            {'base_url': self.login_url, 'next_url': self.edit_profile_url}
        self.assertRedirects(response, redirect_url, 302)
        
    def testLoggedInView(self):
        """ Verify that logged in users can reach the edit profile page.
        """ 
        
        # Log in and view the edit page
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.edit_profile_url)
        self.assertEqual(response.status_code, 200)
        # Check that the correct stuff is on the page
        self.assertContains(response, 'First name')
        self.assertContains(response, 'Last name')
        self.assertContains(response, 'E-mail address')
        self.assertContains(response, 'affiliation')
        
class ActionEditProfilePageCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.edit_profile_url = reverse('epic.core.views.edit_profile')
        self.login_url = reverse('django.contrib.auth.views.login')
    
    def testLoggedOutEdit(self):
        """ Make sure that logged out users can't edit the data.
        """

        # The changes to the data.
        post_data = {
            'first_name': 'Bob',
            'last_name': "L'Admin",
            'email': 'bob@admin.com',
            'affiliation': 'Harvard',
        }
        # Attempt to edit the dataset.
        response = self.client.get(self.edit_profile_url)
        redirect_url = '%(base_url)s?next=%(next_url)s' % \
            {'base_url': self.login_url, 'next_url': self.edit_profile_url}
        self.assertRedirects(response, redirect_url, 302)
        
    def testLoggedInEdit(self):
        """ Verify that only the creator can edit data.
        """
        
        # Get the objects to be used for this test.
        user = User.objects.get(username='bob')
        profile = Profile.objects.for_user(user)
        
        # Save the original values.
        user_original_email = user.email
        user_original_first_name = user.first_name
        user_original_last_name = user.last_name
        user_original_affiliation = profile.affiliation

        self.tryLogin(username='bob', password='bob')

        # The changes to the data.
        post_data = {
            'first_name': 'Bob',
            'last_name': "L'Admin",
            'email': 'bob@admin.com',
            'affiliation': 'Harvard',
        }
        # Edit the dataset.
        response = self.client.post(self.edit_profile_url, post_data)
        self.assertEqual(response.status_code, 302)
        
        # Get the objects to be used for this test again
        user = User.objects.get(username='bob')
        profile = Profile.objects.for_user(user)
        
        # Get the new values
        user_new_email = user.email
        user_new_first_name = user.first_name
        user_new_last_name = user.last_name
        user_new_affiliation = profile.affiliation
        
        # Make sure the values changed
        self.assertNotEqual(user_new_affiliation, user_original_affiliation)
        self.assertNotEqual(user_new_email, user_original_email)
        self.assertNotEqual(user_new_first_name, user_original_first_name)
        self.assertNotEqual(user_new_last_name, user_original_last_name)
        
        # Make sure the values changed correctly
        self.assertEqual(user_new_affiliation, post_data['affiliation'])
        self.assertEqual(user_new_email, post_data['email'])
        self.assertEqual(user_new_first_name, post_data['first_name'])
        self.assertEqual(user_new_last_name, post_data['last_name'])
