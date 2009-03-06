from django import forms
from django.forms import ModelForm

class ForgotUsernameForm(forms.Form):
	email = forms.EmailField()

class ForgotEmailForm(forms.Form):
	username = forms.CharField()

class ForgotPasswordForm(forms.Form):
	username_or_email = forms.CharField(label="Username or e-mail address")