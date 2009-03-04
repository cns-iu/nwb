from django import forms
from django.forms import ModelForm

from epic.datasets.models import Dataset



class UploadFileForm(forms.Form):
    title = forms.CharField(max_length=500)
    description = forms.CharField(max_length=5000, widget=forms.Textarea)
    file  = forms.FileField()
    
class NewDatasetForm(ModelForm):
    class Meta:
        model = Dataset
        exclude = ['upload_date', 'owner']