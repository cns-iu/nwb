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
    is_active = models.BooleanField(default=False)
    
    @models.permalink
    def get_absolute_url(self):
        return self.specific.get_absolute_url()
    
    # TODO: Fix this terrible hack
    def is_dataset(self):
        return type(self.specific).__name__ == 'DataSet'
    
    # TODO: Fix this terrible hack
    def is_datarequest(self):
        return type(self.specific).__name__ == 'DataRequest'
    
    # TODO: Fix this terrible hack
    def _specific(self):
        possibilities = ['dataset', 'datarequest', 'project']
        for possibility in possibilities:
            if hasattr(self, possibility):
                return getattr(self, possibility)
        raise Exception('No subclass found for %s' % (self))
    
    specific = property(_specific)

class Author(models.Model):
    items = models.ManyToManyField(Item, blank=True, null=True, related_name="authors")
    author = models.CharField(max_length=100)
    
    def __unicode__(self):
        return "%s" % self.author
    
class AcademicReference(models.Model):
    item = models.ForeignKey(Item, related_name="references")
    reference = models.CharField(max_length=1000)
    
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
    
    NULL_TITLE = '(Invalid Name)'
    
    objects = ProfileManager()
    user = models.ForeignKey(User, unique=True)
    affiliation = models.CharField(max_length=MAX_USER_PROFILE_LENGTH,
                                   blank=True)
    
    def short_title(self):
        if self.user.first_name and self.user.last_name:
            short_title = self.user.first_name + " " + self.user.last_name
        else:
            short_title = self.NULL_TITLE
            
        return short_title
        
    def full_title(self):
        if self.user.first_name and self.user.last_name and self.affiliation:
            full_title = self.user.first_name + " " + self.user.last_name + \
                ", " + self.affiliation
        else:
            full_title = self.NULL_TITLE
            
        return full_title
    
    def __unicode__(self):
        return self.full_title()
