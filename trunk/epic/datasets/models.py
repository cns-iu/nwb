"""
These are the DataSet and DataSetFile models.

"""
from epic.core.models import Item

from django.db import models
from django.contrib.auth.models import User


class DataSet(Item):
    """
    This is the DataSet model.  You work with it as follows:
     
    >>> user = User(username="bob", password="blah")
    >>> user.save()
    >>> user
    <User: bob>
    
    >>> dataset = DataSet(creator=user, name="Item #1", description="This is the first item")
    >>> dataset.save()
    
    """
    
    #supposedly better to do this some other newer way where it's not nested
    class Admin:
        pass
    
    def __unicode__(self):
        return "Dataset %s created at %s" % (self.name, self.created_at)
    
    @models.permalink
    def get_absolute_url(self):
        return ("epic.datasets.views.view_dataset", [self.id])
    
class DataSetFile(models.Model):
    parent_dataset = models.ForeignKey(DataSet, related_name="files")
    file = models.FileField(upload_to="files")
    
    class Admin:
        pass

    def __unicode__(self):
        return "%s created at %s" % (self.file, self.created_at)
    