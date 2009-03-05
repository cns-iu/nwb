from django.db import models
from django.contrib.auth.models import User

class Profile(models.Model):
	user = models.ForeignKey(User, unique=True)
	affiliation = models.CharField(max_length=512)

class Item(models.Model):
	creator = models.ForeignKey(User)
	name = models.CharField(max_length=256)
	description = models.TextField()
	created_at = models.DateTimeField(auto_now_add=True, db_index=True)
