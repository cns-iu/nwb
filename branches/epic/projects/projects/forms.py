import re

from django import forms
from django.forms.util import ValidationError
from django.forms.formsets import formset_factory

from epic.core.models import Item
from epic.datasets.models import DataSet


class NewProjectForm(forms.Form):
    name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH)
    description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
                                  widget=forms.Textarea())

class EditProjectForm(forms.Form):
    name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH)
    description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
                                  widget=forms.Textarea())

class AddDatasetToProjectForm(forms.Form):
    dataset_url = \
        forms.CharField(label='Add Dataset From These URLs', required=False)
    
    def clean_dataset_url(self):
        cleaned_data = self.cleaned_data
        dataset_url = cleaned_data['dataset_url']
        
        if not dataset_url:
            cleaned_data['dataset'] = None
            
            return cleaned_data
        
        dataset_id = _parse_out_dataset_id(dataset_url)
        
        try:
            dataset = DataSet.objects.get(pk=dataset_id)
        except Exception, dataset_not_found_exception:
            raise ValidationError(
                '%s does not refer to a valid dataset.' % dataset_url)
        
        cleaned_data['dataset'] = dataset
        
        return cleaned_data

class RemoveDatasetFromProjectForm(forms.Form):
    dataset_id = forms.BooleanField(label='Dataset', required=False)

AddDatasetToProjectFormSet = formset_factory(AddDatasetToProjectForm, extra=1)
# TODO: Use this formset instead of my own crap?
RemoveDatasetFromProjectFormSet = \
    formset_factory(RemoveDatasetFromProjectForm, extra=0)

def _parse_out_dataset_id(dataset_url):
    dataset_id = _parse_out_item_id_of_type(dataset_url, 'dataset')
    
    return dataset_id

def _parse_out_item_id_of_type(item_url, item_type):
    assumed_application_base_url_scheme = '%ss' % item_type
    matching_expression = \
        r'/%s/(?P<item_id>\d+)' % assumed_application_base_url_scheme
    compiled_matching_expression = re.compile(matching_expression)
    match = compiled_matching_expression.search(item_url)
    
    if match is None:
        return None
    
    return match.group('item_id')