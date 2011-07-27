import re

from django import forms
from django.forms.formsets import formset_factory
from django.forms.util import ValidationError

from epic.core.forms import CategoryChoiceField
from epic.core.forms import DESCRIPTION_HELP_TEXT, MAX_TEXT_INPUT_DISPLAY_SIZE
from epic.core.models import Item
from epic.datasets.models import DataSet


class ProjectForm(forms.Form):
    name_attrs = {
        'size': MAX_TEXT_INPUT_DISPLAY_SIZE,
        'onFocus': 'ClearField(this)',
    }
    name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH, 
                           widget=forms.TextInput(attrs=name_attrs))
    # TODO: Include help_text to talk about BBCode. 
    # This will probably involve displaying the entire Project forms manually.)
    description = forms.CharField(
        max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
        widget=forms.Textarea())
#    description = forms.CharField(
#        max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
#        widget=forms.Textarea(),
#        help_text=DESCRIPTION_HELP_TEXT)
    category = CategoryChoiceField()

class ProjectDataSetForm(forms.Form):
    dataset_url = forms.CharField(label='Dataset URL', required=False)
    
    def clean_dataset_url(self):
        cleaned_data = self.cleaned_data
        dataset_url = cleaned_data['dataset_url']
        
        if not dataset_url:
            return ''
        
        dataset_id = _parse_out_dataset_id(dataset_url)
        
        try:
            dataset = DataSet.objects.get(pk=dataset_id)
        except DataSet.DoesNotExist:
            raise ValidationError(
                '%s does not refer to a valid dataset.' % dataset_url)
        
        cleaned_data['dataset'] = dataset
        
        return dataset_url

ProjectDataSetFormSet = formset_factory(ProjectDataSetForm, extra=1)

def _parse_out_dataset_id(dataset_url):
    dataset_id = _parse_out_item_id_of_type(dataset_url, 'datasets')
    
    return dataset_id

#def _parse_out_algorithm_id(alrogithm_url):
#    algorithm_id = _parse_out_item_id_of_type(algorithm_url, 'algorithms')
#    
#    return algorithm_id

def _parse_out_item_id_of_type(item_url, app_name):
    expression = r'.*?/%s/(?P<item_id>\d+)/.*?' % app_name 
    pattern = re.compile(expression)
    match = pattern.search(item_url)
    
    if match is None:
        return None
    
    return match.group('item_id')
