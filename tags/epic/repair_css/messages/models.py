from django.db import models
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

class Message(models.Model):
	MAX_SUBJECT_LENGTH = 256
	MAX_MESSAGE_LENGTH = 16384
	
	recipient = models.ForeignKey(User, related_name="recipient")
	sender = models.ForeignKey(User, related_name="sender")
	subject = models.CharField(max_length=MAX_SUBJECT_LENGTH)
	message = models.CharField(max_length=MAX_MESSAGE_LENGTH)
	created_at = models.DateTimeField(auto_now_add=True, db_index=True)
	
	def __unicode__(self):
		return '%s from %s to %s' % (self.subject, self.sender, self.recipient)

class ReceivedMessage(Message):
	read = models.BooleanField()
	replied = models.BooleanField()
	deleted = models.BooleanField()
	
	def get_absolute_url(self):
		return reverse('epic.messages.views.view_received_message', kwargs={'user_id':self.recipient.id, 'receivedmessage_id':self.id})
	
class SentMessage(Message):
	deleted = models.BooleanField()

	def get_absolute_url(self):
		return reverse('epic.messages.views.view_sent_message', kwargs={'user_id':self.sender.id, 'sentmessage_id':self.id})		
	