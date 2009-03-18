from django import template
from epic.tags.models import Tagging

register = template.Library()

@register.inclusion_tag('templatetags/list_item_tags.html')
def list_item_tags(item):
	tags = item.tags.all()
	return {'tags':tags}

@register.simple_tag
def get_tag_url(tag_name):
	return Tagging.objects.get_url_for_tag(tag_name)