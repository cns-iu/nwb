import re

from django import forms
from django.forms.formsets import formset_factory
from django.forms.util import ErrorList
from django.forms.util import ValidationError

from epic.core.models import Item
from epic.datasets.models import DataSet


class ProjectForm(forms.Form):
    name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH)
    description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
                                  widget=forms.Textarea())
class ProjectDataSetForm(forms.Form):
    dataset_url = forms.CharField(label='Dataset URL', required=False)
    
    def clean_dataset_url(self):
        cleaned_data = self.cleaned_data
        dataset_url = cleaned_data['dataset_url']

        dataset_id = _parse_out_dataset_id(dataset_url)
        
        try:
            dataset = DataSet.objects.get(pk=dataset_id)
        except DataSet.DoesNotExist:
            raise ValidationError(
                '%s does not refer to a valid dataset.' % dataset_url)
        
        cleaned_data['dataset'] = dataset
        
        return cleaned_data

ProjectDataSetFormSet = formset_factory(ProjectDataSetForm, extra=1)

def _parse_out_dataset_id(dataset_url):
    dataset_id = _parse_out_item_id_of_type(dataset_url, 'datasets')
    
    return dataset_id
    
def _parse_out_item_id_of_type(item_url, item_type):
    pattern = re.compile(r'^.*?/'+item_type+'/(?P<item_id>\d+)/.*')
    match = pattern.match(item_url)
    if match is not None:
        dict = match.groupdict()
        if 'item_id' in dict:
            return dict['item_id']
    return None
    