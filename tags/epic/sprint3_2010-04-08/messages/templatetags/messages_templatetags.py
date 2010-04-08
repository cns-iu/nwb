from django import template
from django.contrib.auth.models import User
from django.shortcuts import get_object_or_404

from epic.messages.models import ReceivedMessage


register = template.Library()

@register.simple_tag
def num_unread_messages(user):
	unread_messages = ReceivedMessage.objects.filter(recipient=user,
                                                     read=False)
    
	return unread_messages.count()