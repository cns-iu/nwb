from django import forms
from django.forms import ModelForm
from django.contrib.auth.models import User
from django.utils.translation import ugettext_lazy

from epic.core.models import Item, Profile

class ForgotUsernameForm(forms.Form):
	email = forms.EmailField(max_length=Profile.MAX_USER_EMAIL_LENGTH)

class ForgotEmailForm(forms.Form):
	username = forms.CharField(max_length=Profile.MAX_USERNAME_LENGTH)

class ForgotPasswordForm(forms.Form):
	username_or_email = forms.CharField(max_length=Profile.MAX_USER_EMAIL_LENGTH, label="Username or e-mail address")

class UserForm(ModelForm):
	# this overwrites the email from the model.  It is important that email is NOT in the exclude list though...
	email = forms.EmailField(label='E-mail address')
	class Meta:
		model = User
		exclude = ['username', 'password', 'is_staff', 'is_active', 'is_superuser', 'last_login', 'date_joined', 'groups', 'user_permissions']

class ProfileForm(ModelForm):
	class Meta:
		model = Profile
		exclude = ['user']