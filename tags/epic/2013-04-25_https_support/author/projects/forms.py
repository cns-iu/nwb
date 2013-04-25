import re

from django import forms
from django.forms.util import ValidationError
from django.forms.formsets import formset_factory

from epic.core.models import Item
from epic.datasets.models import DataSet


class ProjectForm(forms.Form):
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
        except DataSet.DoesNotExist:
            raise ValidationError(
                '%s does not refer to a valid dataset.' % dataset_url)
        
        cleaned_data['dataset'] = dataset
        
        return cleaned_data

class RemoveDatasetFromProjectForm(forms.Form):
    dataset_id = forms.BooleanField(label='Dataset', required=False)

# TODO: Actually use these for adding multiple datasets to projects at a time.
AddDatasetToProjectFormSet = formset_factory(AddDatasetToProjectForm, extra=1)
RemoveDatasetFromProjectFormSet = \
    formset_factory(RemoveDatasetFromProjectForm, extra=0)

def _parse_out_dataset_id(dataset_url):
    dataset_id = _parse_out_item_id_of_type(dataset_url, 'datasets')
    
    return dataset_id

#def _parse_out_algorithm_id(alrogithm_url):
#    algorithm_id = _parse_out_item_id_of_type(algorithm_url, 'algorithms')
#    
#    return algorithm_id

def _parse_out_item_id_of_type(item_url, item_type):
    expression = r'/(?P<app_name>.+?)/(?P<item_id>\d+)/'
    pattern = re.compile(expression)
    match = pattern.search(item_url)
    
    if match is None:
        return None
    
    app_name = match.group('app_name')
    
    if app_name == item_type:
        return match.group('item_id')
    
    return None
