from django import forms
from django.forms import ModelForm

from epic.messages.models import Message

class NewMessageForm(ModelForm):
	message = forms.CharField(max_length=Message.MAX_MESSAGE_LENGTH, widget=forms.Textarea())
	class Meta:
		model = Message
		exclude = ['to_user', 'from_user']
	