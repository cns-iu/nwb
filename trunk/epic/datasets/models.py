"""
These are the DataSet and DataSetFile models.

"""
from epic.core.models import Item

from django.db import models
from django.contrib.auth.models import User


class DataSet(models.Model):
    """
    This is the DataSet model.  You work with it as follows:
    
    >>> user = User(username="bob", password="blah")
    >>> user.save()
    >>> user
    <User: bob>
    
    >>> from datetime import datetime
    >>> item = Item(creator=user, name="Item #1", description="This is the first item", created_at=datetime.now())
    >>> item
    <Item: Item object>
    >>> item.save()
    
    >>> dataset = DataSet(item=item, timestamp=datetime.now())
    >>> dataset.save()
    >>> dataset.item
    <Item: Item object>
    """
    item = models.ForeignKey(Item)
    timestamp = models.DateTimeField('timestamp')
    
    class Admin:
        pass
    
    def __unicode__(self):
        return "Dataset %s created at %s" % (self.item, self.timestamp)
    
    def get_absolute_url(self):
        return "/datasets/%i" % self.id
    
class DataSetFile(models.Model):
    dataset = models.ForeignKey(DataSet)
    file = models.FileField(upload_to="files")
    timestamp = models.DateTimeField('timestamp')
    
    class Admin:
        pass

    def __unicode__(self):
        return "%s created at %s" % (self.file, self.timestamp)
    