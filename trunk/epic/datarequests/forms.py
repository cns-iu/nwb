from django import forms
from django.forms import ModelForm

from epic.core.forms import CategoryChoiceField
from epic.core.forms import DESCRIPTION_HELP_TEXT
from epic.core.models import Item
from epic.datarequests.models import DataRequest
from epic.core.util.multicategory import MultiCategoryField

class DataRequestForm(ModelForm):
    name_attrs = {
        'size': 42,
        'onFocus': 'ClearField(this)',
    }
    
    name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH,
                           widget=forms.TextInput(attrs=name_attrs))
    
    description_attrs = {
        'rows': 6,
        'cols': 42,
        'onFocus': 'ClearField(this)',
    }
    
    # TODO: Include help_text to talk about BBCode. 
    # This will probably involve displaying the entire DataRequest forms
    # manually.
    description = forms.CharField(
        max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
        widget=forms.Textarea(attrs=description_attrs))
#    description = forms.CharField(
#        max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
#        widget=forms.Textarea(attrs=description_attrs),
#        help_text=DESCRIPTION_HELP_TEXT)
    
    category = CategoryChoiceField()
#    category = MultiCategoryField()
    
    
    tags_attrs = {
        'size': 42,
        'onFocus': 'ClearField(this)',
    }
    
    tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH,
                           required=False,
                           widget=forms.TextInput(attrs=tags_attrs))
        
    class Meta:
        model = DataRequest
        exclude = [
            'creator',
#            Using the custom Field for categories instead of the default CategoryChoiceField
#            which takes care of cleanup & custom GUI.
            'categories',
            'rendered_description',
            'tagless_description',
            'status',
            'slug',
            'is_active',
            'fulfilling_item',
        ]
