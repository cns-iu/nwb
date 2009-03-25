from django import template
from epic.datasets.models import DataSet

register = template.Library()

@register.inclusion_tag('templatetags/list_datasets.html', takes_context=True)
def list_datasets(context, datasets):
	user = context['user']
	return {'datasets':datasets, 'user':user}

@register.inclusion_tag("templatetags/dataset_title_ratebox_and_datainfo.html", takes_context=True)
def show_dataset_title_ratebox_and_datainfo(context, dataset):
	user = context['user']
	return {'dataset':dataset, 'user':user}