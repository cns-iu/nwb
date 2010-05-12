from django import forms


class SearchBoxForm(forms.Form):
    q = forms.CharField(label='', widget=forms.TextInput(attrs={'size': 17}))
