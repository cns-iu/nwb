from django.db import models
from django.contrib.auth.models import User

class Profile(models.Model):
	user = models.ForeignKey(User)
	affiliation = models.CharField(max_length = 512)

class Item(models.Model):
	user = models.ForeignKey(User)
	name = models.CharField(max_length = 256)
	description = models.CharField(max_length = 1024)
	timestamp = models.DateTimeField("Date created")