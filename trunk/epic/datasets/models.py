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
        return "Dataset: %s owned by %s" % (self.title, self.owner)
    
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
        return "File: %s[%s] owned by %s attached to %s" % (self.title, self.file, self.owner, self.dataset)
    