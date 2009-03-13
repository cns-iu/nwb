from django.db import models
from django.contrib.auth.models import User
from django.contrib.contenttypes.models import ContentType
from django.contrib.contenttypes import generic
from django.db import IntegrityError

from epic.core.models import Item
from epic.tags.utils import parse_tag_input, edit_string_for_tags

from django.db import connection, models
from django.db.models.query import QuerySet
qn = connection.ops.quote_name

class TagManager(models.Manager):
	"""
	This will replace the standard Tagging.objects to allow several Tagging related functions to be added
	"""
	use_for_related_fields = True

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
	
	def get_frequency_list(self):
		model = Tagging
		model_table = qn(model._meta.db_table)
		model_pk = '%s.%s' % (model_table, qn(model._meta.pk.column))
		query = """
			SELECT tag, COUNT(tag)
			FROM %s
			GROUP BY tag
			ORDER BY COUNT(tag) DESC
			""" % (model_table)
		cursor = connection.cursor()
		cursor.execute(query)
		count_tag = []
		for row in cursor.fetchall():
			count = row[1]
			tag_name = row[0]
			count_tag.append([count, tag_name])
		return count_tag
	
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
	
#TODO: Make it possible to view a tag(?? or not)
class Tagging(models.Model):
	"""
	This links a string called a 'tag' to a user and an item.
	"""
	objects = TagManager()
	
	tag = models.CharField(max_length=50)
	user = models.ForeignKey(User, related_name="tags")
	item = models.ForeignKey(Item, related_name="tags")
	created_at = models.DateTimeField(auto_now_add=True)
							 
	class Meta:
		unique_together = (('tag', 'item'),)

	def __unicode__(self):
		return "%s tagged %s as %s" % (self.user.username, self.item, self.tag)
	
	def get_absolute_url(self):
		return Tagging.objects.get_url_for_tag(self.tag)