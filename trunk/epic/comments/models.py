"""Comment models: models relating to comments (on things)."""

from django.contrib.auth.models import User
from django.db import models

from epic.core.models import Item


class Comment(models.Model):
    """
    
    Usage as follows:
    >>> from epic.datasets.models import DataSet
    >>> from epic.comments.models import Comment
    >>> user = User.objects.create_user( \
        'testuser1337', 'leethax0r@epic.edu', '3y3r1337')
    >>> user.save()
    >>> dataset = DataSet( \
        creator=user, name='Item #1', description='This is the first item')
    >>> dataset.save()
    >>> comment = Comment(posting_user=user, \
                          parent_item=dataset, \
                          contents='3y3 4m t0t4lly h4x0ring j00r sit3!')
    >>> comment.save()
    >>> comment
    <Comment: 3y3 4m t0t4lly h4x0ring j00r sit3!>
    >>> dataset.comments.count()
    1
    >>> dataset.comments.all()[0]
    <Comment: 3y3 4m t0t4lly h4x0ring j00r sit3!>
    >>> dataset.comments.all()[0] == comment
    True
    """
    
    posting_user = models.ForeignKey(User)
    parent_item = models.ForeignKey(Item, related_name='comments')
    contents = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True, db_index=True)
    
    class Admin:
        pass
    
    def __unicode__(self):
        return '%s' % (self.contents)
