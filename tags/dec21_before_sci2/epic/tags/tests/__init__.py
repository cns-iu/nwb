"""
>>> import os
>>> from django import forms
>>> from django.db.models import Q
>>> from epic.tags.models import Tagging
>>> from epic.tags.utils import parse_tag_input
>>> from epic.datasets.models import DataSet
>>> from django.contrib.auth.models import User

#############
# Utilities #
#############

# Tag input ###################################################################

# Simple space-delimited tags
>>> parse_tag_input('one')
[u'one']
>>> parse_tag_input('one two')
[u'one', u'two']
>>> parse_tag_input('one two three')
[u'one', u'three', u'two']
>>> parse_tag_input('one one two two')
[u'one', u'two']

# Comma-delimited multiple words - an unquoted comma in the input will trigger
# this.
>>> parse_tag_input(',one')
[u'one']
>>> parse_tag_input(',one two')
[u'one two']
>>> parse_tag_input(',one two three')
[u'one two three']
>>> parse_tag_input('a-one, a-two and a-three')
[u'a-one', u'a-two and a-three']

# Double-quoted multiple words - a completed quote will trigger this.
# Unclosed quotes are ignored.
>>> parse_tag_input('"one')
[u'one']
>>> parse_tag_input('"one two')
[u'one', u'two']
>>> parse_tag_input('"one two three')
[u'one', u'three', u'two']
>>> parse_tag_input('"one two"')
[u'one two']
>>> parse_tag_input('a-one "a-two and a-three"')
[u'a-one', u'a-two and a-three']

# No loose commas - split on spaces
>>> parse_tag_input('one two "thr,ee"')
[u'one', u'thr,ee', u'two']

# Loose commas - split on commas
>>> parse_tag_input('"one", two three')
[u'one', u'two three']

# Double quotes can contain commas
>>> parse_tag_input('a-one "a-two, and a-three"')
[u'a-one', u'a-two, and a-three']
>>> parse_tag_input('"two", one, one, two, "one"')
[u'one', u'two']

# Bad users! Naughty users!
>>> parse_tag_input(None)
[]
>>> parse_tag_input('')
[]
>>> parse_tag_input('"')
[]
>>> parse_tag_input('""')
[]
>>> parse_tag_input('"' * 7)
[]
>>> parse_tag_input(',,,,,,')
[]
>>> parse_tag_input('",",",",",",","')
[u',']
>>> parse_tag_input('a-one "a-two" and "a-three')
[u'a-one', u'a-three', u'a-two', u'and']

#############
#  Tagging  #
#############
>>> user1 = User.objects.create(username="bob55", password="bob55")
>>> user1
<User: bob55>
>>> user2 = User.objects.create(username="bob255", password="bob255")
>>> user2
<User: bob255>
>>> dataset1 = DataSet.objects.create(name="DS1", description="The first dataset item", creator=user1, is_active=True)
>>> dataset1
<DataSet: Dataset DS1>
>>> dataset2 = DataSet.objects.create(name="DS2", description="The second dataset item", creator=user1, is_active=True)
>>> dataset2
<DataSet: Dataset DS2>
>>> dataset3 = DataSet.objects.create(name="DS3", description="The third dataset item", creator=user2, is_active=True)
>>> dataset3
<DataSet: Dataset DS3>
>>> dataset3.tags.all()
[]
>>> Tagging.objects.update_tags("one two three", dataset3, user1)
>>> dataset3.tags.all()
[<Tagging: bob55 tagged Item object as one>, <Tagging: bob55 tagged Item object as three>, <Tagging: bob55 tagged Item object as two>]
>>> Tagging.objects.update_tags("one two three", dataset3, user2)
>>> Tagging.objects.update_tags("1, 2, 3 4 5", dataset2, user2)
>>> dataset2.tags.all()
[<Tagging: bob255 tagged Item object as 1>, <Tagging: bob255 tagged Item object as 2>, <Tagging: bob255 tagged Item object as 3 4 5>]
>>> Tagging.objects.update_tags("1, 2, 3 4 5, 7, 8, 9", dataset2, user1)
>>> dataset2.tags.all()
[<Tagging: bob255 tagged Item object as 1>, <Tagging: bob255 tagged Item object as 2>, <Tagging: bob255 tagged Item object as 3 4 5>, <Tagging: bob55 tagged Item object as 7>, <Tagging: bob55 tagged Item object as 8>, <Tagging: bob55 tagged Item object as 9>]
>>> Tagging.objects.update_tags("", dataset2, user2)
>>> dataset2.tags.all()
[<Tagging: bob55 tagged Item object as 7>, <Tagging: bob55 tagged Item object as 8>, <Tagging: bob55 tagged Item object as 9>]
"""

from doc_tests import *
from view_tests import *
from data_request_tests import *

