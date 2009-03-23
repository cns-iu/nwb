"""
Core models: models that the entire EpiC project will (probably) use.

Let's just test out a few aspects of our models...

>>> user = User.objects.create_user("testuser22", "testing@epicproject.com", "testpassword")
>>> user
<User: testuser22>
>>> user.first_name
''
>>> user.last_name
''
>>> user.username
'testuser22'
>>> user.email
'testing@epicproject.com'
>>> item = Item(creator=user, name="test item", description="test description")
>>> item
<Item: Item object>
>>> item.creator
<User: testuser22>
>>> item.name
'test item'
>>> item.description
'test description'
>>> profile = Profile(user=user, affiliation="CNS Core")
>>> profile
<Profile: Profile object>
>>> profile.user
<User: testuser22>
>>> profile.affiliation
'CNS Core'
"""

from django.db import models
from django.contrib.auth.models import User

class Item(models.Model):
	MAX_ITEM_NAME_LENGTH = 256
	MAX_ITEM_DESCRIPTION_LENGTH = 16384
	MAX_ITEM_TAGS_LENGTH = 1024
	MAX_ITEM_INDIVIDUAL_TAG_LENGTH = 64
	
	creator = models.ForeignKey(User)
	name = models.CharField(max_length=MAX_ITEM_NAME_LENGTH)
	description = models.CharField(max_length=MAX_ITEM_DESCRIPTION_LENGTH)
	slug = models.SlugField()
	created_at = models.DateTimeField(auto_now_add=True, db_index=True)
									
	@models.permalink
	def get_absolute_url(self):
		return self.specific.get_absolute_url()
	
	# TODO: Fix this terrible hack
	def _specific(self):
		possibilities = ['dataset', 'datarequest']
		for possibility in possibilities:
			if hasattr(self, possibility):
				return getattr(self, possibility)
		raise Exception("No subclass found for %s" % (self))
	
	specific = property(_specific)
	
class ProfileManager(models.Manager):
	def for_user(self, user):
		try:
			profile = user.get_profile()
		except:
			profile = Profile(user=user)
			try:
				profile.save()
			except:
				profile = user.get_profile()
		return profile

class Profile(models.Model):
	MAX_USERNAME_LENGTH = 16
	MAX_USER_PASSWORD_LENGTH = 256
	MAX_USER_EMAIL_LENGTH = 256
	MAX_USER_PROFILE_LENGTH = 512

	objects = ProfileManager()
	user = models.ForeignKey(User, unique=True)
	affiliation = models.CharField(max_length=MAX_USER_PROFILE_LENGTH, blank=True)
