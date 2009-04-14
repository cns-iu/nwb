from django.db import models
from django.contrib.auth.models import User

from django.db import models
from django.contrib.auth.models import User
from django.db import IntegrityError

from epic.core.models import Item
from epic.tags.utils import parse_tag_input, edit_string_for_tags
from common import LOGARITHMIC, LINEAR, Tag

from django.db import connection, models
from django.db.models.query import QuerySet
qn = connection.ops.quote_name

from epic.core.models import Item

class TagManager(models.Manager):
    """
    This will replace the standard Tagging.objects to allow several Tagging related
    functions to be added.
    """
    
    use_for_related_fields = True
    
    def delete_tag(self, tag_name, tagged_item):
        print self.get(item=tagged_item, tag=tag_name)
        tag_to_delete = self.get(item=tagged_item, tag=tag_name)
        tag_to_delete.delete()
            
    def update_tags(self, tag_names, item=None, user=None):
        if item is None and user is None:
            raise Exception("User and item must not be none together. That deletes all tags.")
        
        filters = {}
        
        if item is not None:
            filters['item'] = item
        
        if user is not None:
            filters['user'] = user
        
        clean_tag_names = parse_tag_input(tag_names)
        self.filter(**filters).exclude(tag__in=clean_tag_names).delete()
        current_tags = [value['tag'] for value in self.filter(**filters).values('tag')]
        
        for tag in clean_tag_names:
            if tag not in current_tags:
                try:
                    self.get_or_create(tag=tag, **filters)
                except IntegrityError:
                    pass
    
    def get_tag_list(self, item=None, user=None):
        filters = {}
        
        if item is not None:
            filters['item'] = item
        
        if user is not None:
            filters['user'] = user
        
        list_of_tags = [value['tag'] for value in self.filter(**filters).order_by('tag').values('tag')]
        
        return ", ".join(list_of_tags)
    
    def get_tags(self):
        model = Tagging
        model_table = qn(model._meta.db_table)
        model_pk = '%s.%s' % (model_table, qn(model._meta.pk.column))
        
        # TODO: created_at isn't necessarily the CORRECT created_at.
        # If we want to color-code tags by date created, we should make sure
        # we're getting the CORRECT created_at every time (i.e. the first/earliest one).
        query = """
            SELECT tag, COUNT(tag)
            FROM %s
            GROUP BY tag
            ORDER BY COUNT(tag) DESC
            """ % (model_table)
        
        cursor = connection.cursor()
        cursor.execute(query)
        tags = []
        
        for row in cursor.fetchall():
            tag_name = row[0]
            count = row[1]
            tag = Tag(tag_name, count)
            
            tags.append(tag)
        
        return tags
    
    def get_edit_string(self, item=None, user=None):
        filters = {}
        
        if item is not None:
            filters['item'] = item
        
        if user is not None:
            filters['user'] = user
        
        list_of_tags = self.filter(**filters).order_by('tag')
        
        return edit_string_for_tags(list_of_tags)
    
    @models.permalink
    def get_url_for_tag(self, tag_name):
        return ('epic.tags.views.view_items_for_tag', [], {'tag_name':tag_name})
    
    def tag_cloud(self, steps=4, distribution=LOGARITHMIC, filters=None, min_count=None):
        """
        Obtain a list of tags associated with instances of the given
        Model, giving each tag a ``count`` attribute indicating how
        many times it has been used and a ``font_size`` attribute for
        use in displaying a tag cloud.

        ``steps`` defines the range of font sizes - ``font_size`` will
        be an integer between 1 and ``steps`` (inclusive).

        ``distribution`` defines the type of font size distribution
        algorithm which will be used - logarithmic or linear. It must
        be either ``common.LOGARITHMIC`` or
        ``common.LINEAR``.

        To limit the tags displayed in the cloud to those associated
        with a subset of the Model's instances, pass a dictionary of
        field lookups to be applied to the given Model as the
        ``filters`` argument.

        To limit the tags displayed in the cloud to those with a
        ``count`` greater than or equal to ``min_count``, pass a value
        for the ``min_count`` argument.
        """
        
        from utils import calculate_cloud
        
        return calculate_cloud(Tagging, steps, distribution)
    
#TODO: Make it possible to view a tag(?? or not)
class Tagging(models.Model):
    """
    This links a string called a 'tag' to a user and an item.
    """
    objects = TagManager()
    
    tag = models.CharField(max_length=Item.MAX_ITEM_INDIVIDUAL_TAG_LENGTH)
    user = models.ForeignKey(User, related_name="tags")
    item = models.ForeignKey(Item, related_name="tags")
    created_at = models.DateTimeField(auto_now_add=True)
                             
    class Meta:
        unique_together = (('tag', 'item'),)

    def __unicode__(self):
        return "%s tagged %s as %s" % (self.user.username, self.item, self.tag)
    
    def get_absolute_url(self):
        return Tagging.objects.get_url_for_tag(self.tag)
    
    def get_tag_url(self):
        return Tagging.objects.get_url_for_tag(self.tag)
