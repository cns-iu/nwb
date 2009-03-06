from django import forms
from django.forms import ModelForm

from epic.datarequests.models import DataRequest

class DataRequestForm(ModelForm):
	class Meta:
		model = DataRequest
		exclude = ['creator', 'status']