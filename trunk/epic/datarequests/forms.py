from django import forms

from epic.datarequests.models import DataRequest

class NewDataRequestForm(forms.Form):
	item_name = forms.CharField(max_length=256)
	item_description = forms.CharField(max_length=1024, widget=forms.Textarea())