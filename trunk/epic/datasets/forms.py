from django import forms
from django.forms import ModelForm

from epic.datasets.models import DataSet

class NewDataSetForm(forms.Form):
    item_name = forms.CharField(max_length=256)
    item_description = forms.CharField(max_length=1024, widget=forms.Textarea())
    file  = forms.FileField()