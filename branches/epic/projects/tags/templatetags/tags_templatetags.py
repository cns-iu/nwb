from django import template
from epic.tags.models import Tagging
from epic.tags.utils import LOGARITHMIC, LINEAR
from epic.tags.models import Tagging

register = template.Library()

@register.inclusion_tag('templatetags/list_item_tags.html')
def list_item_tags(item, user, show_addtag=None):
	tags = Tagging.objects.filter(item=item)
	return { 'tags': tags, 'user': user, 'item': item, 'show_addtag':show_addtag, }

@register.inclusion_tag("templatetags/show_tag_cloud.html")
def show_tag_cloud(steps=10, distribution="logarithmic", min_count=None):
	if distribution == "logarithmic":
		tag_cloud_distribution = LOGARITHMIC
	elif distribution == "linear":
		tag_cloud_distribution = LINEAR
	else:
		raise template.TemplateSyntaxError, "The third parameter of show_tag_cloud must either be 'logarithmic' (default) or 'linear'."
	
	tags = Tagging.objects.tag_cloud(steps=steps,
		distribution=tag_cloud_distribution,
		min_count=min_count)
	
	return { "tags": tags }