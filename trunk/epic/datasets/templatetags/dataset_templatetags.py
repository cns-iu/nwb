from django import template
from epic.datasets.models import DataSet

register = template.Library()

@register.inclusion_tag('templatetags/list_datasets.html', takes_context=True)
def list_datasets(context):
	datasets = DataSet.objects.all().order_by('-created_at')
	return {'datasets':datasets}