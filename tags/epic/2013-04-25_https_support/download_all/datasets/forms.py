from django import forms
from django.forms import ModelForm
from django.forms.formsets import formset_factory

from epic.core.models import Item
from epic.core.util.multifile import MultiFileField
from epic.datasets.models import DataSet, RATING_SCALE
from epic.djangoratings.forms import RatingField
    
class EditDataSetForm(forms.Form):
    name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH)
    description = forms.CharField(max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH, widget=forms.Textarea())
    tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH, required=False)

class NewDataSetForm(EditDataSetForm):
    help_text = """A "readme.txt" is required.  If one is not directly provided, 
                it must be in a compressed file that is directly added."""
    files = MultiFileField(required=True, help_text=help_text)
    
class UploadReadMeForm(forms.Form):
    readme = forms.FileField(required=True)
    
class RatingDataSetForm(forms.Form):
    rating = RatingField(RATING_SCALE)

class TagDataSetForm(forms.Form):
    # TODO: Seriously write this.  An example would be good. tag1, tag 3 is equivalent to tag1 "tag 3"?
    help_text = """
                Type your tags in the input field.
                To remove your existing tags from the dataset, remove the tags from the field.
                Tags are comma or space separated.
                Double quotes can be used to allow multiword tags.
                """
    tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH, required=False, help_text=help_text)
    
class GeoLocationHiddenFieldForm(forms.Form):
    add_location = forms.CharField(required=False, widget=forms.HiddenInput)

class RemoveGeoLocationHiddenFieldForm(forms.Form):
    remove_location = forms.CharField(required=False, widget=forms.HiddenInput)
    
GeoLocationFormSet = formset_factory(GeoLocationHiddenFieldForm, extra=0)
RemoveGeoLocationFormSet = formset_factory(RemoveGeoLocationHiddenFieldForm, extra=0)