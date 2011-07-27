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

import random

from django.contrib.auth.models import User
from django.db import models
from django.template.defaultfilters import slugify
from django.utils.hashcompat import sha_constructor

from epic.categories.constants import NO_CATEGORY
from epic.categories.constants import NO_CATEGORY_DESCRIPTION
from epic.categories.models import Category
from epic.categories.models import default_category

from epic.core.util.model_exists_utils import profile_exists
from epic.core.util.postmarkup import PostMarkup

class Item(models.Model):
    MAX_ITEM_NAME_LENGTH = 256
    MAX_ITEM_DESCRIPTION_LENGTH = 16384
    MAX_ITEM_TAGS_LENGTH = 1024
    MAX_ITEM_INDIVIDUAL_TAG_LENGTH = 64
    
    creator = models.ForeignKey(User)
    name = models.CharField(max_length=MAX_ITEM_NAME_LENGTH)
    description = models.CharField(max_length=MAX_ITEM_DESCRIPTION_LENGTH)
    # TODO: Validate images placed in the BBCode.
    # No max_length is set for rendered_description because its contents
    # are always derived from description.
    # TODO: Actually the max_length likely defaults to some value.  It should
    # likely be set to the same as description.
    rendered_description = models.TextField(blank=True, null=True)
    tagless_description = models.TextField(blank=True, null=True)
    
    categories = models.ManyToManyField(Category, 
                                        related_name='categories', 
                                        blank=True)
    
    slug = models.SlugField(max_length=MAX_ITEM_NAME_LENGTH)
    created_at = models.DateTimeField(auto_now_add=True, db_index=True)
    is_active = models.BooleanField(default=False)
    
    #no @models.permalink because the .specific versions already do it.
    def get_absolute_url(self):
        return self.specific.get_absolute_url()
    
    # TODO: Fix this terrible hack.
    def is_dataset(self):
        return self.specific_name == 'DataSet'
    
    # TODO: Fix this terrible hack.
    def is_datarequest(self):
        return self.specific_name == 'DataRequest'
    
    # TODO: Fix this terrible hack.
    def is_project(self):
        return self.specific_name == 'Project'

    # TODO: Fix this terrible hack.
    def _specific_name(self):
        return type(self.specific).__name__

    # TODO: Fix this terrible hack.
    def _specific(self):
        possibilities = ['dataset', 'datarequest', 'project']

        for possibility in possibilities:
            if hasattr(self, possibility):
                return getattr(self, possibility)

        raise Exception('No subclass found for %s' % (self))

    specific_name = property(_specific_name)
    specific = property(_specific)

    def save(self, *args, **kwargs):
        self.rendered_description = self._render_description()
        self.tagless_description = self._strip_tags_from_description()
        self.slug = slugify(self.name)
        super(Item, self).save()

    def _render_description(self):
        markup_renderer = PostMarkup()
        markup_renderer.default_tags()
        
        return markup_renderer.render_to_html(self.description)
    
    def _strip_tags_from_description(self):
        markup_renderer = PostMarkup()
        markup_renderer.default_tags()
        
        return markup_renderer.strip_tags(self.description)

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
            activation_key = self._create_unused_activation_key(user)
            profile = Profile(user=user, activation_key=activation_key)

            try:
                profile.save()
            except:
                profile = user.get_profile()
        return profile

    def _create_unused_activation_key(self, user):
        activation_key = self._create_activation_key(user)

        while profile_exists(activation_key=activation_key):
            activation_key = self._create_activation_key(user)

        return activation_key

    def _create_activation_key(self, user):
        salt = sha_constructor(str(random.random())).hexdigest()[:5]
        activation_key = sha_constructor(salt + user.username).hexdigest()

        return activation_key

HAS_ACTIVATED_ACCOUNT_FIELD_NAME = 'has_activated_account'

class Profile(models.Model):
    MAX_USERNAME_LENGTH = 16
    MIN_USER_PASSWORD_LENGTH = 8
    MAX_USER_PASSWORD_LENGTH = 256
    MAX_USER_EMAIL_LENGTH = 256
    MAX_USER_PROFILE_LENGTH = 512
    MAX_FIRST_NAME_LENGTH = 30
    MAX_LAST_NAME_LENGTH = 30
    MAX_REGISTRATION_KEY_LENGTH = 50
    
    NULL_TITLE = '(No Name Set)'
    
    objects = ProfileManager()
    has_activated_account = models.BooleanField(default=False)
    user = models.ForeignKey(User, unique=True)
    affiliation = models.CharField(max_length=MAX_USER_PROFILE_LENGTH, blank=True)
    activation_key = models.CharField(max_length=MAX_REGISTRATION_KEY_LENGTH, null=True)
    
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
            full_title = self.short_title()
            
        return full_title
    
    def __unicode__(self):
        return self.full_title()
