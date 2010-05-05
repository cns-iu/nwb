from haystack.indexes import *
from haystack import site
from epic.core.models import Item

class ItemIndex(SearchIndex):
	text = CharField(document=True, use_template=True)
	title = CharField(model_attr='name')
	description = CharField(model_attr='tagless_description')
	link = CharField(model_attr='get_absolute_url', indexed=False)
	

site.register(Item, ItemIndex)