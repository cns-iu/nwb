from django import template
from epic.datasets.models import DataSet

register = template.Library()

@register.inclusion_tag('templatetags/list_datasets.html', takes_context=True)
def list_datasets(context, datasets):
	user = context['user']
	return {'datasets':datasets, 'user':user}

@register.inclusion_tag("templatetags/show_edit_dataset_metadata_form.html")
def show_edit_dataset_metadata_form(dataset, edit_dataset_metadata_form):
	return {
		"dataset": dataset,
		"edit_dataset_metadata_form": edit_dataset_metadata_form
	}

@register.inclusion_tag("templatetags/dataset_title_view_ratebox_datainfo.html", takes_context=True)
def show_dataset_title_view_ratebox_datainfo(context, dataset):
	user = context['user']
	return {'dataset':dataset, 'user':user}

@register.inclusion_tag("templatetags/dataset_title_vote_ratebox_datainfo.html", takes_context=True)
def show_dataset_title_vote_ratebox_datainfo(context, dataset):
	user = context['user']
	return {'dataset':dataset, 'user':user}