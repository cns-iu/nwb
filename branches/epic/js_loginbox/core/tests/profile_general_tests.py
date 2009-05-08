from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.models import Profile
from epic.core.test import CustomTestCase


class ProfileTestCase(CustomTestCase):
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.user_with_full_profile = User.objects.get(username='peebs')
        self.user_with_empty_profile = User.objects.get(username='bob')
        self.user_with_no_profile = self._create_noprofile_user()
        
        self.full_profile = Profile.objects.for_user(self.user_with_full_profile)
        self.empty_profile = Profile.objects.for_user(self.user_with_empty_profile)
        
    def _create_noprofile_user(self):
        mrempty_user = User.objects.create_user(username='mrnoprofile',
            email='mrnoprofile@sample.com',
            password='mrnoprofile')
    
        mrempty_user.first_name = 'Melvin'
        mrempty_user.last_name = 'NoProfile'
        mrempty_user.save()
        
        #(notice that we're not creating a profile here)
    
        return mrempty_user

    def testGettingAnExistingProfile(self):
        # If a user's profile is already created,
        # we can get it.
        
        profile = Profile.objects.for_user(self.user_with_full_profile)
        self.assertExists(profile)
        
    def testGettingANewProfile(self):
        # If a user does not yet have a profile,
        # when we try to get it (in the proper way), it returns a new profile.
        
        new_profile = Profile.objects.for_user(self.user_with_empty_profile)
        self.assertExists(new_profile)
        
    def testShortTitle(self):
        # If you have a full profile,
        # you should return something for your short title,
        # and it SHOULD NOT contain your affiliation.
        
        short_title = self.full_profile.short_title()
        self.assertExists(short_title)
        self.assertFalse(self.full_profile.affiliation in short_title)
    
    def testFullTitle(self):
        # If you have a full profile,
        # you should return something for your full title,
        # and it SHOULD contain your affiliation.
        
        full_title = self.full_profile.full_title()
        self.assertExists(full_title)
        self.assertTrue(self.full_profile.affiliation in full_title)
        
    def testEmptyProfileTitles(self):
        # If you have an empty profile,
        # You should return the 'null title' for your short and full titles.
        
        short_title = self.empty_profile.short_title()
        self.assertEquals(short_title, Profile.NULL_TITLE)
        full_title = self.empty_profile.full_title()
        self.assertEquals(full_title, Profile.NULL_TITLE)
        
        
