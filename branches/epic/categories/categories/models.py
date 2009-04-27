from django.contrib.auth.models import User
from django.db import models

class Category(models.Model):

    owning_user = models.ForeignKey(User)
    name = models.TextField()
    description = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True, db_index=True)
    
    class Admin:
        pass
    
    class Meta:
        unique_together = (('name', 'description',),)
    
    def __unicode__(self):
        return '%s' % self.name
