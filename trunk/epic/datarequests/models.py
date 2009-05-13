"""
These are the data request models. You work with them as follows:

>>> data_request = DataRequest()
>>> data_request.get_status_display()
u'unfulfilled'

"""

from django.db import models

from epic.core.models import Item


REQUEST_STATUS = (
    ('U' , 'unfulfilled'),
    ('F' , 'fulfilled'),
    ('C' , 'canceled'),
)

class DataRequestManager(models.Manager):
    def unfulfilled(self):
        return self.get_query_set().filter(status='U')
    def fulfilled(self):
        return self.get_query_set().filter(status='F')
    def canceled(self):
        return self.get_query_set().filter(status='C')
    def active(self):
        return self.filter(is_active=True)

class DataRequest(Item):
    """
    A data request.
    
    >>> data_request = DataRequest()
    >>> data_request.get_status_display()
    u'unfulfilled'
    >>> data_request.fulfill()
    >>> data_request.get_status_display()
    u'fulfilled'
    """
    objects = DataRequestManager()
    status = models.CharField(max_length=1, choices=REQUEST_STATUS, db_index=True, default='U')
    fulfilling_item = models.ForeignKey(Item, related_name='fulfilled_requests', null=True, blank=True)
    
    def fulfill(self):
        self.status = 'F'
        
    def cancel(self):
        self.status = 'C'
        
    @models.permalink
    def get_absolute_url(self):
        return ('epic.datarequests.views.view_datarequest',
                [],
                {'item_id': self.id, 'slug': self.slug})
    
    def __unicode__(self):
        return '%s %s %s' % (self.status, self.name, self.creator)
