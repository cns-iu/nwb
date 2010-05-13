from django import forms
from django.forms import ModelForm
from django.forms.formsets import formset_factory

from epic.core.forms import CategoryChoiceField
from epic.core.forms import DESCRIPTION_HELP_TEXT
from epic.core.models import AcademicReference
from epic.core.models import Author
from epic.core.models import Item
from epic.core.util.multifile import MultiFileField, MultiFileInput
from epic.datasets.models import DataSet
from epic.datasets.models import RATING_SCALE
from epic.djangoratings.forms import RatingField


class EditDataSetForm(forms.Form):
    name = forms.CharField(max_length=Item.MAX_ITEM_NAME_LENGTH, widget=forms.TextInput(attrs={'size': 50}))
    description = forms.CharField(
        max_length=Item.MAX_ITEM_DESCRIPTION_LENGTH,
        widget=forms.Textarea(),
        help_text=DESCRIPTION_HELP_TEXT)
    
    category = CategoryChoiceField()
    tags = forms.CharField(max_length=Item.MAX_ITEM_TAGS_LENGTH,
                           required=False,
                           widget=forms.TextInput(attrs={'size': 50}))

class NewDataSetForm(EditDataSetForm):
    def __init__(self, user, *args, **kwargs):
        super(NewDataSetForm, self).__init__(*args, **kwargs)
        self.fields['previous_version'].queryset = DataSet.objects.active().filter(creator=user)
        
    help_text = """Specify that this dataset is a newer version of a dataset
                    that you have previously added.  Please only use this as a
                    way to correct errors from past datasets."""
    previous_version = forms.ModelChoiceField(
        queryset=DataSet.objects.none(),
        empty_label='(No Previous Version)',
        required=False,
        help_text=help_text)
      
    help_text = """A "readme.txt" is required if you choose to upload files.  If one is not directly
                   provided, it must be in a compressed file that is directly added."""
    files = MultiFileField(
        required=False, help_text=help_text, widget=MultiFileInput(attrs={'size': 40}))
    
class UploadReadMeForm(forms.Form):
    readme = forms.FileField(required=False)

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
    tags = forms.CharField(
        max_length=Item.MAX_ITEM_TAGS_LENGTH,
        required=False, 
        help_text=help_text,
        widget=forms.TextInput(attrs={'size': 50}))
    
class GeoLocationHiddenFieldForm(forms.Form):
    add_location = forms.CharField(required=False, widget=forms.HiddenInput)

class RemoveGeoLocationHiddenFieldForm(forms.Form):
    remove_location = forms.CharField(required=False, widget=forms.HiddenInput)
    
GeoLocationFormSet = formset_factory(GeoLocationHiddenFieldForm, extra=0)
RemoveGeoLocationFormSet = formset_factory(RemoveGeoLocationHiddenFieldForm, extra=0)

class AcademicReferenceForm(ModelForm):
    reference = forms.CharField(required=False,
                                widget=forms.TextInput(attrs={'size': 50}))
    
    EMPTY_REFERENCE_ERROR_MESSAGE = 'References must contain more than whitespace.'
    
    def clean_reference(self):
        reference = self.cleaned_data['reference']
        
        # Strip will remove all whitespace so if there is anything left, the
        #    field was not left 'blank'
        if reference.strip():
            pass
        else:
            raise forms.ValidationError(self.EMPTY_REFERENCE_ERROR_MESSAGE)
        
        return reference
    
    class Meta:
        model = AcademicReference
        exclude = ['item']
        
AcademicReferenceFormSet = formset_factory(AcademicReferenceForm, extra=1)

class AuthorForm(ModelForm):
    author = forms.CharField(required=False, widget=forms.TextInput(attrs={'size': 50}))
    
    EMPTY_AUTHOR_ERROR_MESSAGE = 'Authors must contain more than whitespace.'
    
    def clean_author(self):
        author = self.cleaned_data['author']
        
        if author.strip():
            pass
        else:
            raise forms.ValidationError(self.EMPTY_AUTHOR_ERROR_MESSAGE)
    
        return author
    
    class Meta:
        model = Author
        exclude = ['items']
        
AuthorFormSet = formset_factory(AuthorForm, extra=1)
