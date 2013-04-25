from django import forms
from django.forms import ModelForm

from epic.core.forms import CategoryChoiceField
from epic.core.models import Item
from epic.datarequests.models import DataRequest


class DataRequestForm(ModelForm):
    name_attrs = {
        'size': 42,
        'onFocus': 'MPClearField(this)',
    }
    
    name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH,
                           widget=forms.TextInput(attrs=name_attrs))
    
    description_attrs = {
        'rows': 6,
        'cols': 42,
        'onFocus': 'MPClearField(this)',
    }
    
    description = forms.CharField(
        max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
        widget=forms.Textarea(attrs=description_attrs))
    
    category = CategoryChoiceField()
    
    tags_attrs = {
        'size': 42,
        'onFocus': 'MPClearField(this)',
    }
    
    tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH,
                           required=False,
                           widget=forms.TextInput(attrs=tags_attrs))
        
    class Meta:
        model = DataRequest
        exclude = \
            ['creator', 'rendered_description', 'status', 'slug', 'is_active']
