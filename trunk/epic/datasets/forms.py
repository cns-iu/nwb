from django import forms
from django.forms import ModelForm

from epic.datasets.models import DataSet, RATING_SCALE
from epic.djangoratings.forms import RatingField

from epic.core.models import Item

class NewDataSetForm(forms.Form):
	name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH)
	description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH, widget=forms.Textarea())
	file  = forms.FileField()
	tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH, required=False)

class EditDataSetForm(forms.Form):
	name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH)
	description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH, widget=forms.Textarea())
	tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH, required=False)
    
class RatingDataSetForm(forms.Form):
	rating = RatingField(RATING_SCALE)

class TagDataSetForm(forms.Form):
	tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH, required=False)