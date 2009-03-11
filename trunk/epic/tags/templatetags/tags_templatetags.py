from django import template

register = template.Library()

@register.inclusion_tag('templatetags/list_item_tags.html')
def list_item_tags(item, item_name):
	tags = item.tags.get_tag_list()
	return {'tags':tags, 'item_name':item_name}