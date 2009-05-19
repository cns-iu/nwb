from django.contrib.auth.models import User
from django.db import models

from epic.categories.constants import NO_CATEGORY


class Category(models.Model):
    name = models.TextField()
    description = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True, db_index=True)
    
    class Meta:
        unique_together = (('name', 'description',),)
    
    def __unicode__(self):
        return '%s' % self.name
    
    def delete(self):
        if self.name == NO_CATEGORY:
            raise CannotDeleteNoCategoryException()
        
        no_category = Category.objects.get(name=NO_CATEGORY)
        self.item_set.update(category=no_category)
        super(Category, self).delete()

class CannotDeleteNoCategoryException(Exception):
    def __init__(self):
        self.value = 'You may not delete the "%s" category.' % NO_CATEGORY

    def __str__(self):
        return repr(self.value)
