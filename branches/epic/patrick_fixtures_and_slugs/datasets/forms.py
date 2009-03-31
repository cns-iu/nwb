from django import forms
from django.forms import ModelForm
from django.forms.formsets import formset_factory

from epic.core.models import Item
from epic.core.util.multifile import MultiFileField
from epic.datasets.models import DataSet, RATING_SCALE
from epic.djangoratings.forms import RatingField


class NewDataSetForm(forms.Form):
	name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH)
	description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH, widget=forms.Textarea())
	files = MultiFileField(required=True)
	tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH, required=False)
	
class EditDataSetForm(forms.Form):
	name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH)
	description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH, widget=forms.Textarea())
	tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH, required=False)
	
class RatingDataSetForm(forms.Form):
	rating = RatingField(RATING_SCALE)

class TagDataSetForm(forms.Form):
	tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH, required=False)
	
class GeoLocationHiddenFieldForm(forms.Form):
	add_location = forms.CharField(required=False, widget=forms.HiddenInput)

class RemoveGeoLocationHiddenFieldForm(forms.Form):
	remove_location = forms.CharField(required=False, widget=forms.HiddenInput)
	
GeoLocationFormSet = formset_factory(GeoLocationHiddenFieldForm, extra=0)
RemoveGeoLocationFormSet = formset_factory(RemoveGeoLocationHiddenFieldForm, extra=0)
