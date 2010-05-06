from django.contrib.auth.models import User

from epic.categories.constants import NO_CATEGORY
from epic.categories.models import Category
from epic.core.models import Profile


# DO NOT REMOVE THIS.  IT WILL KILL THE SITE!
no_category = Category.objects.create(
    name=NO_CATEGORY, description='Items that do not fall under any other category')
no_category.save()
