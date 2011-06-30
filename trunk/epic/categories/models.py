from django.contrib.auth.models import User
from django.db import models
from django.template.defaultfilters import slugify

from epic.categories.constants import NO_CATEGORY
from epic.categories.constants import NO_CATEGORY_DESCRIPTION


class Category(models.Model):
    name = models.CharField(max_length=256, unique=True)
    description = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True, db_index=True)
    slug = models.SlugField()
        
    def __unicode__(self):
        return '%s' % self.name
    
    def delete(self):
        if self.name == NO_CATEGORY:
            raise CannotDeleteNoCategoryException()
        
        no_category = default_category()
        self.item_set.update(category=no_category)
        super(Category, self).delete()
        
    def save(self, *args, **kwargs):
        self.slug = slugify(self.name)
        super(Category, self).save()

class CannotDeleteNoCategoryException(Exception):
    def __init__(self):
        self.value = 'You may not delete the "%s" category.' % NO_CATEGORY

    def __str__(self):
        return repr(self.value)



def default_category():
    # The second result is whether or not a new object was created.
    return Category.objects.get_or_create(
        name=NO_CATEGORY, defaults={'description': NO_CATEGORY_DESCRIPTION})[0]
