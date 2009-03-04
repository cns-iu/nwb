from django.db import models
from epic.core.models import Item

REQUEST_STATUS = (
    ('U' , 'unfulfilled'),
    ('F' , 'fulfilled'),
    ('C' , 'canceled'),
)

class DataRequest(models.Model):
    item = models.OneToOneField(Item, primary_key=true)
    status = models.CharField(max_length=1, choices=REQUEST_STATUS)
