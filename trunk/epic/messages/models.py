from django.db import models
from django.contrib.auth.models import User

class Message(models.Model):
	MAX_SUBJECT_LENGTH = 128
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
	
	#TODO: this is stupid, but the reverse didn't work for the permalink.  FIX THIS!
	def get_absolute_url(self):
		return ('/user/%s/messages/received/%s/' % (self.recipient.id, self.id))
	
class SentMessage(Message):
	deleted = models.BooleanField()
	
	#TODO: this is stupid, but the reverse didn't work for the permalink.  FIX THIS!
	def get_absolute_url(self):
		return ('/user/%s/messages/sent/%s/' % (self.sender.id, self.id))
	