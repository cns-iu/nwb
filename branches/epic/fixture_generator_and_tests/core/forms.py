from django import forms
from django.contrib.auth.models import User
from django.forms import ModelForm
from django.forms.util import ErrorList
from django.utils.translation import ugettext_lazy

from epic.core.models import Item, Profile

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
		
#TODO: This is a copy and paste of Django's AuthenticationForm, with a few tweaks. Hopefully we can make this extend AuthenticationForm or something instead
class ShortAuthenticationForm(forms.Form):
    """
    Base class for authenticating users. Extend this to get a form that accepts
    username/password logins.
    """
    username = forms.CharField(label=("Username"), max_length=30, widget=forms.TextInput(attrs={'size': 11}));
    password = forms.CharField(label=("Password"), widget=forms.PasswordInput(attrs={'size': 11}));

    def __init__(self, request=None, *args, **kwargs):
        """
        If request is passed in, the form will validate that cookies are
        enabled. Note that the request (a HttpRequest object) must have set a
        cookie with the key TEST_COOKIE_NAME and value TEST_COOKIE_VALUE before
        running this validation.
        """
        self.request = request
        self.user_cache = None
        super(ShortAuthenticationForm, self).__init__(*args, **kwargs)

    def clean(self):
        username = self.cleaned_data.get('username')
        password = self.cleaned_data.get('password')

        if username and password:
            self.user_cache = authenticate(username=username, password=password)
            if self.user_cache is None:
            	# TODO: is the variable _ actually defined anywhere?
                raise forms.ValidationError(_("Please enter a correct username and password. Note that both fields are case-sensitive."))
            elif not self.user_cache.is_active:
                raise forms.ValidationError(_("This account is inactive."))

        # TODO: determine whether this should move to its own method.
        if self.request:
            if not self.request.session.test_cookie_worked():
                raise forms.ValidationError(_("Your Web browser doesn't appear to have cookies enabled. Cookies are required for logging in."))

        return self.cleaned_data

    def get_user_id(self):
        if self.user_cache:
            return self.user_cache.id
        return None

    def get_user(self):
        return self.user_cache