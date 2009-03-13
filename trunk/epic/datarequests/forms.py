from django import forms
from django.forms import ModelForm

from epic.datarequests.models import DataRequest
from epic.core.models import Item

class NewDataRequestForm(ModelForm):
	description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH, widget=forms.Textarea())
	class Meta:
		model = DataRequest
		exclude = ['creator', 'status']