from epic.core.models import Item

from django.db import models
from django.contrib.auth.models import User


class DataSet(models.Model):
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
    