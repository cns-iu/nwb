from django import forms
from django.forms import ModelForm

from epic.core.models import Item, Profile

class ForgotUsernameForm(forms.Form):
	email = forms.EmailField(max_length=Profile.MAX_USER_EMAIL_LENGTH)

class ForgotEmailForm(forms.Form):
	username = forms.CharField(max_length=Profile.MAX_USERNAME_LENGTH)

class ForgotPasswordForm(forms.Form):
	username_or_email = forms.CharField(max_length=Profile.MAX_USER_EMAIL_LENGTH, label="Username or e-mail address")