from django import forms
from django.forms import ModelForm

from epic.datasets.models import DataSet, RATING_SCALE
from epic.djangoratings.forms import RatingField

class NewDataSetForm(forms.Form):
    name = forms.CharField(max_length=256)
    description = forms.CharField(max_length=1024, widget=forms.Textarea())
    file  = forms.FileField()
    tags = forms.CharField(max_length=512)
    
class RatingDataSetForm(forms.Form):
	rating = RatingField(RATING_SCALE)

class TagDataSetForm(forms.Form):
	tags = forms.CharField(max_length=512)