from django.contrib.auth.models import User
from django.core import mail
from django.core.urlresolvers import reverse

from epic.core.forms import email_address_already_used_message, password_must_not_match_username_message, password_too_short_message, confirm_password_does_not_match_message, username_already_used_message
from epic.core.models import Profile
from epic.core.test import *
from epic.core.util.model_exists_utils import *
from epic.core.views import REGISTRATION_EMAIL_SUBJECT
from epic.core.views import REGISTRATION_FORM_NAME
from epic.core.views import form_email_about_registration
from epic.settings import LOGIN_REDIRECT_URL


BOB_USERNAME = 'bob'
BOB_PASSWORD = 'bob'
BOB_EMAIL = 'bob@bob.com'

NEW_USER_INVALID_EMAIL = 'newuser'
NEW_USER_ALREADY_REGISTERED_EMAIL = BOB_EMAIL
NEW_USER_VALID_EMAIL = 'newuser@email.com'

NEW_USER_ALREADY_REGISTERED_USERNAME = BOB_USERNAME
NEW_USER_VALID_USERNAME = 'newuser'

NEW_USER_INVALID_PASSWORD = 'abcdefgh'
NEW_USER_TOO_SHORT_OF_PASSWORD = 'pass'
NEW_USER_VALID_PASSWORD = 'password'

NEW_USER_INVALID_CONFIRM_PASSWORD = '%s%s' % (NEW_USER_VALID_PASSWORD, NEW_USER_VALID_PASSWORD)
NEW_USER_VALID_CONFIRM_PASSWORD = NEW_USER_VALID_PASSWORD

NEW_USER_FIRST_NAME = 'New'
NEW_USER_LAST_NAME = 'User'
NEW_USER_AFFILIATION = 'CNS'

EMAIL_FIELD_NAME = 'email'
USERNAME_FIELD_NAME = 'username'
PASSWORD_FIELD_NAME = 'password'
CONFIRM_PASSWORD_FIELD_NAME = 'confirm_password'

class RegistrationTestCase(CustomTestCase):
    fixtures = ['core_just_users']

    def setUp(self):
        self.login_url = reverse('django.contrib.auth.views.login')
        self.logout_url = reverse('epic.core.views.logout_view')
        self.register_url = reverse('epic.core.views.register')

    def testAlreadyLoggedIn(self):
        """Test a user trying to access the login page when already logged in."""

        log_user_in(self.login_url, BOB_USERNAME, BOB_PASSWORD, self)

        response = self.client.get(self.register_url)
        self.assertRedirects(response, LOGIN_REDIRECT_URL)

    def testGetPage(self):
        """Test a GET operation on the registration page."""

        self.assertResponseStatusSuccess(view='epic.core.views.register')

    def testEmailAddressField(self):
        """Test email address being blank, invalid, and already used."""

        blank_email_response = self._register()
        self.assertFormError(
            blank_email_response,
            REGISTRATION_FORM_NAME,
            EMAIL_FIELD_NAME,
            'This field is required.')

        invalid_email_response = self._register(email=NEW_USER_INVALID_EMAIL)
        self.assertFormError(
            invalid_email_response,
            REGISTRATION_FORM_NAME,
            EMAIL_FIELD_NAME,
            'Enter a valid e-mail address.')

        email_already_used_response = self._register(email=NEW_USER_ALREADY_REGISTERED_EMAIL)
        self.assertFormError(
            email_already_used_response,
            REGISTRATION_FORM_NAME,
            EMAIL_FIELD_NAME,
            email_address_already_used_message(NEW_USER_ALREADY_REGISTERED_EMAIL))

    def testUsernameField(self):
        """Test username being blank and already used."""

        blank_username_response = self._register()
        self.assertFormError(
            blank_username_response,
            REGISTRATION_FORM_NAME,
            USERNAME_FIELD_NAME,
            'This field is required.')

        username_already_used_response = self._register(
            username=NEW_USER_ALREADY_REGISTERED_USERNAME)
        self.assertFormError(
            username_already_used_response,
            REGISTRATION_FORM_NAME,
            USERNAME_FIELD_NAME,
            username_already_used_message(NEW_USER_ALREADY_REGISTERED_USERNAME))

    def testPasswordField(self):
        """Test password being blank, invalid, and too short."""

        blank_password_response = self._register()
        self.assertFormError(
            blank_password_response,
            REGISTRATION_FORM_NAME,
            PASSWORD_FIELD_NAME,
            'This field is required.')

        invalid_password_response = self._register(
            username=NEW_USER_INVALID_PASSWORD,
            password=NEW_USER_INVALID_PASSWORD)
        self.assertFormError(
            invalid_password_response,
            REGISTRATION_FORM_NAME,
            PASSWORD_FIELD_NAME,
            password_must_not_match_username_message())

        password_too_short_response = self._register(password=NEW_USER_TOO_SHORT_OF_PASSWORD)
        self.assertFormError(
            password_too_short_response,
            REGISTRATION_FORM_NAME,
            PASSWORD_FIELD_NAME,
            password_too_short_message(NEW_USER_TOO_SHORT_OF_PASSWORD))

    def testConfirmPasswordField(self):
        """Test confirm password being blank and not matching password."""

        blank_confirm_password_response = self._register()
        self.assertFormError(
            blank_confirm_password_response,
            REGISTRATION_FORM_NAME,
            CONFIRM_PASSWORD_FIELD_NAME,
            'This field is required.')
        
        confirm_password_not_matching_response = self._register(
            password=NEW_USER_VALID_PASSWORD,
            confirm_password=NEW_USER_INVALID_CONFIRM_PASSWORD)
        self.assertFormError(
            confirm_password_not_matching_response,
            REGISTRATION_FORM_NAME,
            CONFIRM_PASSWORD_FIELD_NAME,
            confirm_password_does_not_match_message())

    def testAllRequiredFieldsValidWithoutOptionalFields(self):
        """Test the successful registration of an account, without optional fields."""

        response_with_optional_fields = self._register(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            password=NEW_USER_VALID_PASSWORD,
            confirm_password=NEW_USER_VALID_CONFIRM_PASSWORD)
        user = User.objects.get(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            first_name='',
            last_name='')
        profile = Profile.objects.for_user(user)
        self.assertEquals(profile.affiliation, '')

        # Make sure the user is active so we can log it in.
        user.is_active = True
        user.save()
        self.failUnless(self.client.login(
            username=NEW_USER_VALID_USERNAME, password=NEW_USER_VALID_PASSWORD))

        # Clean up.
        user.delete()
        profile.delete()

    def testAllRequiredFieldsValidWithOptionalFields(self):
        """Test the successful registration of an account, with optional fields."""

        response_with_optional_fields = self._register(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            password=NEW_USER_VALID_PASSWORD,
            confirm_password=NEW_USER_VALID_CONFIRM_PASSWORD,
            first_name=NEW_USER_FIRST_NAME,
            last_name=NEW_USER_LAST_NAME,
            affiliation=NEW_USER_AFFILIATION)
        user = User.objects.get(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            first_name=NEW_USER_FIRST_NAME,
            last_name=NEW_USER_LAST_NAME)
        profile = Profile.objects.for_user(user)
        self.assertEquals(profile.affiliation, NEW_USER_AFFILIATION)

        # Make sure the user is active so we can log it in.
        user.is_active = True
        user.save()
        self.failUnless(self.client.login(
            username=NEW_USER_VALID_USERNAME, password=NEW_USER_VALID_PASSWORD))

        # Clean up.
        user.delete()
        profile.delete()

    def testRegistrationEmail(self):
        """Test the sending and rendering of a registration email."""

        response_with_optional_fields = self._register(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            password=NEW_USER_VALID_PASSWORD,
            confirm_password=NEW_USER_VALID_CONFIRM_PASSWORD)
        user = User.objects.get(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            first_name='',
            last_name='')
        profile = Profile.objects.for_user(user)

        self.assertEquals(len(mail.outbox), 1)
        self.assertEquals(mail.outbox[0].subject, REGISTRATION_EMAIL_SUBJECT)
        email_body = form_email_about_registration(RequestFactory().request(), user, profile)
        self.assertEquals(mail.outbox[0].body, email_body)
        self.assertNotContains(response_with_optional_fields, "<a href=3D'")

        # Clean up.
        user.delete()
        profile.delete()

    def testLogInAfterRegisteringWithoutActivation(self):
        """Test unactivated accounts."""

        response_with_optional_fields = self._register(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            password=NEW_USER_VALID_PASSWORD,
            confirm_password=NEW_USER_VALID_CONFIRM_PASSWORD)
        user = User.objects.get(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            first_name='',
            last_name='')
        profile = Profile.objects.for_user(user)

        self.failIf(self.client.login(
            username=NEW_USER_VALID_USERNAME, password=NEW_USER_VALID_PASSWORD))

        # Clean up.
        user.delete()
        profile.delete()

    def testLogInAfterRegisteringWithActivation(self):
        """Test activated accounts."""

        response_with_optional_fields = self._register(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            password=NEW_USER_VALID_PASSWORD,
            confirm_password=NEW_USER_VALID_CONFIRM_PASSWORD)
        user = User.objects.get(
            email=NEW_USER_VALID_EMAIL,
            username=NEW_USER_VALID_USERNAME,
            first_name='',
            last_name='')
        profile = Profile.objects.for_user(user)
        
        activation_url = reverse(
            'epic.core.views.activate', kwargs={'activation_key': profile.activation_key})
        activation_response = self.client.get(activation_url)

        self.failUnless(self.client.login(
            username=NEW_USER_VALID_USERNAME, password=NEW_USER_VALID_PASSWORD))

        # Clean up.
        user.delete()
        profile.delete()

    def testInvalidActivationKey(self):
        """Test an invalid activation URL."""

        self.assertResponseStatusFailure(
            view='epic.core.views.activate', kwargs={'activation_key': 'invalid_activation_key'})

    def testLogInToDeactivatedAccount(self):
        """Test a user with an inactive account trying to login."""
        # TODO: This maybe shouldn't go in here.  (Move in to log_in_and_out_tests?)

        bob = User.objects.get(username=BOB_USERNAME)
        previous_is_active = bob.is_active
        bob.is_active = False
        bob.save()

        self.failIf(self.client.login(username=BOB_USERNAME, password=BOB_PASSWORD))

        bob.is_active = previous_is_active
        bob.save()

    def testAccessLoginRequiredPageWithDeactivatedAccount(self):
        """Test a user with an inactive account trying to access a login_required page."""
        # TODO: This maybe shouldn't go in here.  (Move in to log_in_and_out_tests?)

        bob = User.objects.get(username=BOB_USERNAME)

        self.failUnless(self.client.login(username=BOB_USERNAME, password=BOB_PASSWORD))

        previous_is_active = bob.is_active
        bob.is_active = False
        bob.save()

        self.assertResponseStatusRedirect(
            'epic.core.views.view_profile', kwargs={'user_id': bob.id})

        bob.is_active = previous_is_active
        bob.save()

    def _register(
            self,
            email='',
            username='',
            password='',
            confirm_password='',
            first_name='',
            last_name='',
            affiliation=''):
        post_data = {
            'email': email,
            'username': username,
            'password': password,
            'confirm_password': confirm_password,
            'first_name': first_name,
            'last_name': last_name,
            'affiliation': affiliation,
        }

        return self.client.post(self.register_url, post_data)
