from django import forms
from django.forms import ModelForm

from epic.datarequests.models import DataRequest
from epic.core.models import Item

class DataRequestForm(ModelForm):
	name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH, widget=forms.TextInput(attrs={'size':42 , 'onfocus': "MPClearField(this)"}))
	description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH, widget=forms.Textarea(attrs={'rows':6, 'cols':42, 'onfocus': "MPClearField(this)"}))
		
	class Meta:
		model = DataRequest
		exclude = ['creator', 'status', 'slug']