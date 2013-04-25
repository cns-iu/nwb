from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase


class ProfileLinkTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.profile_url = reverse('epic.core.views.view_profile')
    
    def testLinkForLoggedIn(self):
        self.tryLogin(self.bob.username)

        response = self.client.get('/')
        self.profile_url = reverse(
            'epic.core.views.view_profile', kwargs={'user_id': self.bob.id})
        self.assertContains(
            response, 'href="%(profile_link)s"' % {'profile_link': self.profile_url})
    
    def testLinkForNotLoggedIn(self):
        response = self.client.get('/')
        self.assertNotContains(
            response, 'href="%(profile_link)s"' % {'profile_link': self.profile_url})
