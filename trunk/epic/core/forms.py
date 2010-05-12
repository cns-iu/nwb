from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.views import login
from django.forms import ModelForm
from django.forms.util import ErrorList

from epic.categories.constants import NO_CATEGORY
from epic.categories.models import Category
from epic.categories.models import default_category
from epic.core.util.model_exists_utils import user_exists
from epic.core.models import Item
from epic.core.models import Profile


def email_address_already_used_message(email):
	return u"The email address '%s' was already registered to an account." % email
	
def password_must_not_match_username_message():
	return u"Your password may not match your username."

def confirm_password_does_not_match_message():
	return u'The two passwords you entered are not the same.  Enter your password and confirm it here.'

def password_too_short_message(password):
	return 'Ensure this value has at least %s characters (it has %s).' % \
		(Profile.MIN_USER_PASSWORD_LENGTH, len(password))

def username_already_used_message(username):
    return u"The username '%s' was already registered to an account." % username


class RegistrationForm(forms.Form):
    FIRST_AND_LAST_NAME_HELP_TEXT = 'Note: This may be displayed to other users on the site.'

    email = forms.EmailField(label='E-mail address')
    username = forms.CharField(
        label=("Username"),
        max_length=Profile.MAX_USERNAME_LENGTH,
        widget=forms.TextInput())
    password = forms.CharField(
        label=("Password"),
        min_length=Profile.MIN_USER_PASSWORD_LENGTH,
        max_length=Profile.MAX_USER_PASSWORD_LENGTH,
        widget=forms.PasswordInput())
    confirm_password = forms.CharField(
        label=("Confirm password"),
        min_length=Profile.MIN_USER_PASSWORD_LENGTH,
        max_length=Profile.MAX_USER_PASSWORD_LENGTH,
        widget=forms.PasswordInput())
    first_name = forms.CharField(
        label=("First name"),
        max_length=Profile.MAX_FIRST_NAME_LENGTH,
        required=False,
        help_text=FIRST_AND_LAST_NAME_HELP_TEXT)
    last_name = forms.CharField(
        label=("Last name"),
        max_length=Profile.MAX_LAST_NAME_LENGTH,
        required=False,
        help_text=FIRST_AND_LAST_NAME_HELP_TEXT)
    affiliation = forms.CharField(
        label=("Affiliation"),
        max_length=Profile.MAX_USER_PROFILE_LENGTH,
        required=False)

    def clean_email(self):
        cleaned_data = self.cleaned_data
        email = cleaned_data['email']

        if user_exists(email=email):
            message = email_address_already_used_message(email)
            self._errors['email'] = ErrorList([message])

        return email

    def clean_username(self):
        cleaned_data = self.cleaned_data
        username = cleaned_data['username']

        if user_exists(username=username):
            message = username_already_used_message(username)
            self._errors['username'] = ErrorList([message])

        return username

    def clean(self):
        cleaned_data = self.cleaned_data

        if 'username' in cleaned_data and 'password' in cleaned_data:
            username = cleaned_data['username']
            password = cleaned_data['password']

            if username == password:
                message = password_must_not_match_username_message()
                self._errors['password'] = ErrorList([message])

        if 'password' in cleaned_data and 'confirm_password' in cleaned_data:
            password = cleaned_data['password']
            confirm_password = cleaned_data['confirm_password']

            if confirm_password == password:
                cleaned_data['cleaned_password'] = password
            else:
                message = confirm_password_does_not_match_message()
                self._errors['confirm_password'] = ErrorList([message])

        return cleaned_data

class ForgotPasswordForm(forms.Form):
	username_or_email = forms.CharField(
        max_length=Profile.MAX_USER_EMAIL_LENGTH, label="Username or e-mail address")
	
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
	# This overwrites the email from the model.
    # It is important that email is NOT in the exclude list though...
	email = forms.EmailField(label='E-mail address')

	class Meta:
		model = User
		exclude = [
            'username',
            'password',
            'is_staff',
            'is_active',
            'is_superuser',
            'last_login',
            'date_joined',
            'groups',
            'user_permissions'
        ]

class ProfileForm(ModelForm):
	class Meta:
		model = Profile
		exclude = ['user', 'activation_key']
		
class ShortAuthenticationForm(forms.Form):
    username = forms.CharField(
        label="Username",
        initial="username",
        max_length=30,
        widget=forms.TextInput(attrs={'size': 11,}))
    password = forms.CharField(
        label=("Password"),
        widget=forms.PasswordInput(attrs={'size': 11}))

DESCRIPTION_HELP_TEXT = 'Format your text using these tags: ' + \
                        '<br />[b] ... [/b]  <strong> bold </strong> ' + \
                        '<br />[i] ... [/i] <i> italicized  </i>' + \
                        '<br />[s] ... [/s] <strike> strike out </strike> ' + \
                        '<br />[u] ... [/u] <u> underline </u> ' + \
                        '<br />[img]URL[/img] to display an image from URL'

class CategoryChoiceField(forms.ModelChoiceField):
    def __init__(self, *args, **kwargs):
        super(CategoryChoiceField, self).__init__(
            empty_label=None, queryset=Category.objects.all().order_by('name'))
    
    def clean(self, value):
        if value is None:
            no_category = default_category()
            
            return super(CategoryChoiceField, self).clean(no_category.id)
        
        return super(CategoryChoiceField, self).clean(value)
