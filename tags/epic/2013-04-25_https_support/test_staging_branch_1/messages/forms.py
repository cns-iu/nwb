from django import forms
from django.forms import ModelForm
from django.forms.util import ErrorList

from epic.messages.models import Message
from django.contrib.auth.models import User

class NewMessageForm(ModelForm):
	recipient = forms.CharField(max_length=1024)
	message = forms.CharField(max_length=Message.MAX_MESSAGE_LENGTH, widget=forms.Textarea())
	
	class Meta:
		model = Message
		exclude = ['sender', 'recipient']
		
	def clean_recipient(self):
		recipient = self.cleaned_data['recipient']
		try:
			User.objects.get(username=recipient)
		except:
			raise forms.ValidationError("%s is not a valid username." % (recipient))
		return recipient

# TODO: not do this.			 
NewMessageForm.base_fields.keyOrder = ["recipient"] + NewMessageForm.base_fields.keyOrder[:-1] 