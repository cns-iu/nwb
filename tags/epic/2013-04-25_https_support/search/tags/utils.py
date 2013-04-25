import math
import types

from django.db.models.query import QuerySet
from django.utils.encoding import force_unicode
from django.utils.translation import ugettext as _

from common import LOGARITHMIC, LINEAR, Tag

# Huge thanks go to http://code.google.com/p/django-tagging/
# under MIT license found here: http://www.opensource.org/licenses/mit-license.php

def parse_tag_input(raw_input):
	"""
	Parses tag input, with multiple word input being activated and
	delineated by commas and double quotes. Quotes take precedence, so
	they may contain commas.
	
	Returns a sorted list of unique tag names.
	"""
	if not raw_input:
		return []
	
	input = force_unicode(raw_input)
	
	# Special case - if there are no commas or double quotes in the
	# input, we don't *do* a recall... I mean, we know we only need to
	# split on spaces.
	if u',' not in input and u'"' not in input:
		words = list(set(split_strip(input, u' ')))
		words.sort()
		
		return words
	
	words = []
	buffer = []
	# Defer splitting of non-quoted sections until we know if there are
	# any unquoted commas.
	to_be_split = []
	saw_loose_comma = False
	open_quote = False
	input_iterator = iter(input)
	
	try:
		while 1:
			c = input_iterator.next()
			
			if c == u'"':
				if buffer:
					to_be_split.append(u''.join(buffer))
					buffer = []
				
				# Find the matching quote
				open_quote = True
				c = input_iterator.next()
				
				while c != u'"':
					buffer.append(c)
					c = input_iterator.next()
				
				if buffer:
					word = u''.join(buffer).strip()
					
					if word:
						words.append(word)
					
					buffer = []
				
				open_quote = False
			else:
				if not saw_loose_comma and c == u',':
					saw_loose_comma = True
				
				buffer.append(c)
	except StopIteration:
		# If we were parsing an open quote which was never closed treat
		# the buffer as unquoted.
		if buffer:
			if open_quote and u',' in buffer:
				saw_loose_comma = True
			
			to_be_split.append(u''.join(buffer))
	if to_be_split:
		if saw_loose_comma:
			delimiter = u','
		else:
			delimiter = u' '
		
		for chunk in to_be_split:
			words.extend(split_strip(chunk, delimiter))
	
	words = list(set(words))
	words.sort()
	
	return words

def split_strip(input, delimiter=u','):
	"""
	Splits ``input`` on ``delimiter``, stripping each resulting string
	and returning a list of non-empty strings.
	"""
	if not input:
		return []

	words = [w.strip() for w in input.split(delimiter)]
	
	return [w for w in words if w]
   
def edit_string_for_tags(taggings):
	"""
	Given list of ``Tagging`` instances, creates a string representation of
	the list suitable for editing by the user, such that submitting the
	given string representation back without changing it will give the
	same list of tags.

	Tag names which contain commas will be double quoted.

	If any tag name which isn't being quoted contains whitespace, the
	resulting string of tag names will be comma-delimited, otherwise
	it will be space-delimited.
	"""
	names = []
	use_commas = False
	
	for tagging in taggings:
		name = tagging.tag
		
		if u',' in name:
			names.append('"%s"' % name)
			
			continue
		elif u' ' in name:
			if not use_commas:
				use_commas = True
		
		names.append(name)
	
	if use_commas:
		glue = u', '
	else:
		glue = u' '
	
	return glue.join(names)


# Copied from
# http://www.google.com/codesearch/p?hl=en#0rOnU6goYOY/trunk/tagging/utils.py&q=cloud%20package:http://django-tagging\.googlecode\.com
# for tag clouds.

def _calculate_thresholds(min_weight, max_weight, steps):
	"""
	>>> _calculate_thresholds(0, 0, 1)
	[0.0]
	>>> _calculate_thresholds(0, 0, 10)
	[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
	>>> _calculate_thresholds(0, 10, 5)
	[2.0, 4.0, 6.0, 8.0, 10.0]
	"""
	
	delta = (max_weight - min_weight) / float(steps)
	
	return [min_weight + i * delta for i in range(1, steps + 1)]

def _calculate_tag_weight(weight, max_weight, distribution):
	"""
	Logarithmic tag weight calculation is based on code from the
	`Tag Cloud`_ plugin for Mephisto, by Sven Fuchs.
	
	.. _`Tag Cloud`: http://www.artweb-design.de/projects/mephisto-plugin-tag-cloud
	
	>>> _calculate_tag_weight(1, 15, 1)
	0.0
	>>> _calculate_tag_weight(1, 15, 2)
	1
	>>> _calculate_tag_weight(3, 15, 1)
	6.0852580662331937
	>>> _calculate_tag_weight(3, 15, 2)
	3
	"""
	
	if distribution == LINEAR or max_weight == 1:
		return weight
	elif distribution == LOGARITHMIC:
		return math.log(weight) * max_weight / math.log(max_weight)
	
	raise ValueError(_('Invalid distribution algorithm specified: %s.') % distribution)

def _calculate_tag_cloud_weight_min_and_max(tags):
	"""
	>>> from tags.common import Tag
	>>> tag1 = Tag('tag1', 1)
	>>> tag2 = Tag('tag2', 5)
	>>> tag3 = Tag('tag3', 50)
	>>> tags = [tag3, tag1, tag2]
	>>> _calculate_tag_cloud_weight_min_and_max(tags)
	[1.0, 50.0]
	"""
	
	tags_counts = [tag.count for tag in tags]
	
	return [float(min(tags_counts)), float(max(tags_counts))]

def _annotate_tag_with_font_size(original_tag, tag_weight, thresholds):
	"""
	>>> from tags.common import Tag
	>>> tag1 = Tag('tag1', 11.52)
	>>> thresholds = calculate_thresholds(0, 50, 10)
	>>> thresholds
	[5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0, 45.0, 50.0]
	>>> annotated_tag = _annotate_tag_with_font_size(tag1, 50.0, thresholds)
	>>> annotated_tag.font_size
	3
	"""
	
	annotated_tag = Tag(original_tag.tag_name, original_tag.count)
	
	# Default the font_size to the tag's count.
	annotated_tag.font_size = annotated_tag.count
	
	for ii in range(len(thresholds)):
		if tag_weight <= thresholds[ii]:
			annotated_tag.font_size = int(thresholds[ii]) + 1
			
			break
	
	return annotated_tag

# TODO: Figure out why Python sucks so much.
# (For some reason, we have to pass in Tagging as model, since it complains if we
# try to import Tagging from models here.  WTF mates?)
def calculate_cloud(model, steps=5, distribution=LOGARITHMIC):
	"""
	Add a ``font_size`` attribute to each tag according to the
	frequency of its use, as indicated by its ``count``
	attribute.
	
	``steps`` defines the range of font sizes - ``font_size`` will
	be an integer between 1 and ``steps`` (inclusive).
	
	``distribution`` defines the type of font size distribution
	algorithm which will be used - logarithmic or linear. It must be
	one of ``tags.utils.LOGARITHMIC`` or ``tags.utils.LINEAR``.
	
	>>> import datetime
	>>> date = datetime.date(1984, 3, 15)
	>>> date
	datetime.date(1984, 3, 15)
	>>> from django.contrib.auth.models import User
	>>> from datasets.models import DataSet
	>>> from tags.common import Tag
	>>> user = User.objects.create_user("billy", "billybob@gmail.com", "thornton")
	>>> user
	<User: billy>
	>>> data_set = DataSet(creator=user, name="Data Set", description="Description!", created_at=date)
	>>> data_set
	<DataSet: Dataset Data Set>
	>>> data_set.save()
	>>> tags = "tag1 tag2 tag3"
	>>> data_set.update_tags(tags, user=user)
	>>> data_set.tags.all()
	[<Tagging: billy tagged Item object as tag1>, <Tagging: billy tagged Item object as tag2>, <Tagging: billy tagged Item as tag3>]
	>>> from tags.models import Tagging
	>>> tag_cloud_tags = calculate_cloud(Tagging)
	>>> len(tag_cloud_tags)
	3
	>>> for tag in tag_cloud_tags:
	...  type(tag)
	...  tag.font_size
	...  tag.tag_name
	... 
	<type 'instance'>
	1
	u'tag1'
	<type 'instance'>
	1
	u'tag2'
	<type 'instance'>
	1
	u'tag3'
	"""
	
	original_tags = model.objects.get_tags()
	annotated_tags = []
	
	if len(original_tags) > 0:
		min_weight, max_weight = _calculate_tag_cloud_weight_min_and_max(original_tags)
		
		thresholds = _calculate_thresholds(min_weight, max_weight, steps)
		
		for original_tag in original_tags:
			tag_weight = _calculate_tag_weight(original_tag.count, max_weight, distribution)
			
			annotated_tag = _annotate_tag_with_font_size(original_tag,
				tag_weight,
				thresholds)
			
			annotated_tags.append(annotated_tag)
	
	return annotated_tags
