"""
Core models: models that the entire EpiC project will (probably) use.

Let's just test out a few aspects of our models...

>>> user = User.objects.create_user("testuser", "testing@epicproject.com", "testpassword")
>>> user
<User: testuser>
>>> user.first_name
''
>>> user.last_name
''
>>> user.username
'testuser'
>>> user.email
'testing@epicproject.com'
>>> item = Item(creator=user, name="test item", description="test description")
>>> item
<Item: Item object>
>>> item.creator
<User: testuser>
>>> item.name
'test item'
>>> item.description
'test description'
>>> profile = Profile(user=user, affiliation="CNS Core")
>>> profile
<Profile: Profile object>
>>> profile.user
<User: testuser>
>>> profile.affiliation
'CNS Core'
"""

from django.db import models
from django.contrib.auth.models import User

class Item(models.Model):
	creator = models.ForeignKey(User)
	name = models.CharField(max_length=256)
	description = models.TextField()
	created_at = models.DateTimeField(auto_now_add=True, db_index=True)

class Profile(models.Model):
	user = models.ForeignKey(User, unique=True)
	affiliation = models.CharField(max_length=512)
