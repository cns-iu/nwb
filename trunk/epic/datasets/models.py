from django.db import models
from django.contrib.auth.models import User

class Dataset(models.Model):
    owner = models.ForeignKey(User)
    title = models.CharField(max_length=500)
    description = models.TextField(max_length=5000)
    upload_date = models.DateTimeField('date uploaded')
    
    class Admin:
        pass
    
    def __unicode__(self):
        return "%s's Dataset '%s' uploaded %s" % (self.owner, self.title, self.upload_date)
    
    def get_absolute_url(self):
        return "/datasets/%i" % self.id
    
class File(models.Model):
    owner = models.ForeignKey(User)
    title = models.CharField(max_length=500)
    description = models.TextField(max_length=5000)
    upload_date = models.DateTimeField('date uploaded')
    file = models.FileField(upload_to="files")
    dataset = models.ForeignKey(Dataset)
    
    class Admin:
        pass
    
    def __unicode__(self):
        return "%s's file '%s'[%s] attached to %s" % (self.owner, self.title, self.file, self.dataset)