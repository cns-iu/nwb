from django.db import models
from django.contrib.auth.models import User

class Message(models.Model):
	MAX_SUBJECT_LENGTH = 128
	MAX_MESSAGE_LENGTH = 16384
	to_user = models.ForeignKey(User, related_name="incomming_messages")
	from_user = models.ForeignKey(User, related_name="outgoing_messages")
	subject = models.CharField(max_length=128)
	message = models.CharField(max_length=16384)
	created_at = models.DateTimeField(auto_now_add=True, db_index=True)
	
	def __unicode__(self):
		return '%s from %s to %s' % (self.subject, self.from_user, self.to_user)
	
	@models.permalink
	def get_absolute_url(self):
		return ('epic.messages.views.view_message', [], {'message_id':self.id,})