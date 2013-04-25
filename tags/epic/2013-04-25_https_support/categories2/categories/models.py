from django.contrib.auth.models import User
from django.db import models


class Category(models.Model):
    name = models.TextField()
    description = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True, db_index=True)
    
    class Meta:
        unique_together = (('name', 'description',),)
    
    def __unicode__(self):
        return '%s' % self.name
