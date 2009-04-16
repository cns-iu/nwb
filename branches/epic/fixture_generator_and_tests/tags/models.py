from django.contrib.auth.models import User
from django.db import connection, models, IntegrityError
from django.db.models.query import QuerySet

from epic.core.models import Item
from epic.tags.common import LOGARITHMIC, LINEAR, Tag
from epic.tags.utils import parse_tag_input, edit_string_for_tags

qn = connection.ops.quote_name

class TagManager(models.Manager):
    """
    This will replace the standard Tagging.objects to allow several Tagging related
    functions to be added.
    """

    def get_or_create(self, tag_name, item, user):
        """Get or create a tag given a tag(name), item and a user.
        Tags are a special case so this first check for the most specific tag first (tag, item, user), then
        it checks for a less specific version to return (tag, item) before finally creating the (tag, item, user)
        tagging if nothing else was found.  It returns the tagging and a boolean indicating if it was created.
        
        Arguments:
        tag_name -- the tag(name) for the tagging
        item -- the item that the tagging is connected to
        user -- the user doing the tagging
        
        """
        
        try:
            tag = Tagging.objects.get(tag=tag_name, item=item, user=user)
            created = False
        except Tagging.DoesNotExist:
            try:
                tag = Tagging.objects.get(tag=tag_name, item=item)
                created = False
            except Tagging.DoesNotExist:
                tag = Tagging.objects.create(tag=tag_name, item=item, user=user)
                created = True
        return tag, created
            

    def add_tags_and_return_added_tag_names(self, tag_names, item, user):
        """Add tags to the database and return the names of tags that were added.
        
        Arguments:
        tag_names -- the unparsed string representation of the tags to be added to the database
        item -- the item the tags are related to
        user -- the user adding the tags
        
        """
        
        parsed_tag_names = parse_tag_input(tag_names)
        
        added_tags = []
        for tag_name in parsed_tag_names:
            tag, created = Tagging.objects.get_or_create(tag_name=tag_name, item=item, user=user)
            if created:
                added_tags.append(tag.tag)
        return added_tags
            
    def update_tags(self, tag_names, item, user):
        """Add tags from the tag_names that are not already in the database and remove those that are in the
        database but are not in tag_names.
        
        Arguments:
        tag_names -- the unparsed string representation of the tags that should be the only ones attached to an item
        item -- the item the tagging is related to
        user -- the user adding the taggings
        
        """
        
        #clean the tag names
        clean_tag_names = parse_tag_input(tag_names)
 
        # Remove tags from the database that aren't in tag names.
        if user == item.creator:
            self.filter(item=item).exclude(tag__in=clean_tag_names).delete()
        else:
            self.filter(item=item, user=user).exclude(tag__in=clean_tag_names).delete()
        
        # Add new tags to the database.
        for tag in clean_tag_names:
            self.get_or_create(tag_name=tag, item=item, user=user)

    
    def get_tag_list(self, item, user):
        list_of_tags = [value['tag'] for value in self.filter(item=item, user=user).order_by('tag').values('tag')]
        
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
    
    def get_edit_string(self, item, user):
        if item.creator == user:
            list_of_tags = self.filter(item=item).order_by('tag')
        else:
            list_of_tags = self.filter(item=item, user=user).order_by('tag')
        
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
    user = models.ForeignKey(User)
    item = models.ForeignKey(Item)
    created_at = models.DateTimeField(auto_now_add=True)
                             
    class Meta:
        unique_together = (('tag', 'item'),)

    def __unicode__(self):
        return "%s tagged %s as %s" % (self.user.username, self.item, self.tag)
    
    def get_absolute_url(self):
        return Tagging.objects.get_url_for_tag(self.tag)
    
    def get_tag_url(self):
        return Tagging.objects.get_url_for_tag(self.tag)
    
    def remove(self):
        self.delete()
