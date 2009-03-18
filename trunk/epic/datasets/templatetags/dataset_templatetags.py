from django import template
from epic.datasets.models import DataSet

register = template.Library()

@register.inclusion_tag('templatetags/list_datasets.html')
def list_datasets(datasets):
	return {'datasets':datasets}

@register.inclusion_tag("templatetags/show_edit_dataset_metadata_form.html")
def show_edit_dataset_metadata_form(dataset, edit_dataset_metadata_form):
	return {
		"dataset": dataset,
		"edit_dataset_metadata_form": edit_dataset_metadata_form
	}