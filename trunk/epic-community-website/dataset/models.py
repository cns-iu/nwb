from django.db import models
from django.contrib.auth.models import User
from django.contrib.contenttypes.models import ContentType
from django.contrib.contenttypes import generic

class Dataset(models.Model):
    owner = models.ForeignKey(User)
    title = models.CharField(max_length=500)
    description = models.TextField(max_length=5000)
    upload_date = models.DateTimeField('date uploaded')
    
    class Admin:
        pass
    
class File(models.Model):
    owner = models.ForeignKey(User)
    title = models.CharField(max_length=500)
    description = models.TextField(max_length=5000)
    upload_date = models.DateTimeField('date uploaded')
    file = models.FileField(upload_to="files")
    
    content_type = models.ForeignKey(ContentType)
    object_id = models.PositiveIntegerField()
    content_object = generic.GenericForeignKey('content_type', 'object_id')
    
    class Admin:
        pass
    
    def __unicode__(self):
        return "%s's file '%s'[%s] attached to %s" % (self.owner, self.file, self.title, self.content_object)