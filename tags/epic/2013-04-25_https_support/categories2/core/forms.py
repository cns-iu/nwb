from django import forms
from django.contrib.auth.models import User
from django.forms import ModelForm
from django.forms.util import ErrorList

from epic.categories.models import Category
from epic.core.models import Item
from epic.core.models import Profile


class ForgotPasswordForm(forms.Form):
	username_or_email = forms.CharField(max_length=Profile.MAX_USER_EMAIL_LENGTH, label="Username or e-mail address")
	
	def clean(self):
		try:
			cleaned_data = self.cleaned_data
			username_or_email = cleaned_data['username_or_email']
		except:
			return self.cleaned_data
		
		# TODO: abstract this out
		if username_or_email.count('@') > 0:
			try:
				user = User.objects.get(email=username_or_email)
				cleaned_data['user'] = user
			except:
				msg = u"There is no user registered with the email address '%(email)s'." % {'email': username_or_email,}
				self._errors['username_or_email'] = ErrorList([msg])
				del cleaned_data['username_or_email']
		else:
			try:
				user = User.objects.get(username=username_or_email)
				cleaned_data['user'] = user
			except:
				msg = u"'%(username)s' is not a valid username." % {'username':username_or_email,}
				self._errors['username_or_email'] = ErrorList([msg])
				del cleaned_data['username_or_email']
			
		return cleaned_data
	
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
		
class ShortAuthenticationForm(forms.Form):
    username = forms.CharField(label=("Username"), max_length=30, widget=forms.TextInput(attrs={'size': 11}));
    password = forms.CharField(label=("Password"), widget=forms.PasswordInput(attrs={'size': 11}));

class CategoryChoiceField(forms.ModelChoiceField):
    def __init__(self, *args, **kwargs):
        super(CategoryChoiceField, self).__init__(
            queryset=Category.objects.all(),
            empty_label='(No Category)',
            required=False)
