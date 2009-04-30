"""
These are the DataSet and DataSetFile models.

"""
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.db import models

from epic.core.models import Item
from epic.core.util.customfilefield import CustomFileField
from epic.core.util.multifile import MultiFileField
from epic.djangoratings import RatingField
from epic.geoloc.models import GeoLoc


RATING_SCALE = [(n, str(n)) for n in range(1, 6)]

class DataSetManager(models.Manager):
    use_for_related_fields = True
    
    def active(self):
        return self.filter(is_active=True)


class DataSet(Item):
    """
    This is the DataSet model.  You work with it as follows:
     
    >>> user = User(username="bob23452345", password="blah")
    >>> user.save()
    >>> user
    <User: bob23452345>
    
    >>> dataset = DataSet(creator=user, name="Item #1", description="This is the first item")
    >>> dataset.save()
    
    """
    
    objects = DataSetManager()
    
    rating = RatingField(choices=RATING_SCALE)
    geolocations = models.ManyToManyField(GeoLoc, related_name='datasets', blank=True)
    
    previous_version = models.ForeignKey('self',related_name='previous', blank=True, null=True)
    next_version = models.ForeignKey('self', related_name='next', blank=True, null=True)
    
    #supposedly better to do this some other newer way where it's not nested
    class Admin:
        pass
    
    def __unicode__(self):
        return "Dataset %s" % (self.name)
    
    @models.permalink
    def get_absolute_url(self):
        if self.slug:
            kwargs = {'item_id':self.id, 'slug':self.slug,}
        else:
            kwargs = {'item_id':self.id,}
        return ("epic.datasets.views.view_dataset", [], kwargs)
    
    def get_add_tags_url(self):
        return reverse("epic.datasets.views.tag_dataset",
                       kwargs={ "item_id": self.id, "slug": self.slug })
    
class DataSetFile(models.Model):
    
    parent_dataset = models.ForeignKey(DataSet, related_name="files")
    file_contents = CustomFileField()
    uploaded_at = models.DateTimeField(auto_now_add=True, db_index=True)
    is_readme = models.BooleanField(default=False)

    class Admin:
        pass
    
    def __unicode__(self):
        return self.get_short_name()
       
    def get_short_name(self):
        #returns the non-path component of the file name (the real name)
        before_last_slash, slash, after_last_slash = self.file_contents.name.rpartition('/')
        short_name = after_last_slash
        return short_name
    
    def get_upload_to(self, attrname=None):
        return str("dataset_files/%d/" % self.parent_dataset.id)