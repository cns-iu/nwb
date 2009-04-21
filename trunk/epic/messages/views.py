from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.mail import send_mail
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext

from epic.messages.models import Message, SentMessage, ReceivedMessage
from epic.messages.forms import NewMessageForm

@login_required
def index(request, user_id):
	user = request.user
	user_from_id = get_object_or_404(User, pk=user_id)
	
	# Sent user to their page if they try to find another user's page...
	if (user != user_from_id):
		return HttpResponseRedirect(reverse('epic.messages.views.index', kwargs={'user_id':user.id,}))
	
	# Grab the messages this user would want
	sent_messages = SentMessage.objects.filter(sender=user).order_by('-created_at')
	received_messages = ReceivedMessage.objects.filter(recipient=user).order_by('-created_at')
	return render_to_response('messages/index.html', 
							  {'sent_messages':sent_messages, 'received_messages':received_messages}, context_instance=RequestContext(request))

@login_required
def view_sent_message(request, user_id, sentmessage_id):
	user = request.user
	user_from_id = get_object_or_404(User, pk=user_id)
	sent_message = get_object_or_404(SentMessage, pk=sentmessage_id)
	
	# Sent user to their page if they try to find another user's page...
	if (user != user_from_id):
		#print "Users didn't match, sending to correct page."
		return HttpResponseRedirect(reverse('epic.messages.views.view_sent_message', kwargs={'user_id':user.id, 'sentmessage_id':sentmessage_id}))

	# Sent the user to the index if they are not the sender or receiver of this message
	if (user.id != sent_message.sender.id) and (user.id != sent_message.recipient.id):
		#print "User shouldn't be allowed to view this message: %s != (%s | %s)" % (user.id, sent_message.sender.id, sent_message.recipient.id)
		return HttpResponseRedirect(reverse('epic.messages.views.index', kwargs={'user_id':user.id,}))
	
	return render_to_response('messages/view_sent_message.html', {'sent_message':sent_message,}, context_instance=RequestContext(request))

@login_required	
def view_received_message(request, user_id, receivedmessage_id):
	user = request.user
	user_from_id = get_object_or_404(User, pk=user_id)
	received_message = get_object_or_404(ReceivedMessage, pk=receivedmessage_id)
	
	# Sent user to their page if they try to find another user's page...
	if (user != user_from_id):
		#print "Users didn't match, sending to correct page."
		return HttpResponseRedirect(reverse('epic.messages.views.view_received_message', kwargs={'user_id':user.id, 'receivedmessage_id':receivedmessage_id}))

	# Sent the user to the index if they are not the sender or receiver of this message
	if (user.id != received_message.sender.id) and (user.id != received_message.recipient.id):
		#print "User shouldn't be allowed to view this message: %s != (%s | %s)" % (user.id, received_message.sender.id, received_message.recipient.id)
		return HttpResponseRedirect(reverse('epic.messages.views.index', kwargs={'user_id':user.id,}))
	
	received_message.read = True
	received_message.save()
	return render_to_response('messages/view_received_message.html', {'received_message':received_message,}, context_instance=RequestContext(request))
	
@login_required
def send_message(request, user_id, recipient_id=None, in_reply_to_message_id=None):
	sender = request.user
	user_from_id = get_object_or_404(User, pk=user_id)
	
	# Sent user to their page if they try to find another user's page...
	if (sender != user_from_id):
		#print "Users didn't match, sending to correct page."
		return HttpResponseRedirect(reverse('epic.messages.views.send_message', kwargs={'user_id':user.id}))
	
	if request.method != 'POST':
		# If this is a reply, fill in the subject, recipient and message
		if in_reply_to_message_id is not None:
			in_reply_to_message=get_object_or_404(ReceivedMessage, pk=in_reply_to_message_id)
			form = NewMessageForm(initial={'recipient':in_reply_to_message.sender.username,
										   'subject':'RE:%s' % (in_reply_to_message.subject), 
										   'message':'------\n%s said:\n%s' % (in_reply_to_message.sender, in_reply_to_message.message)})
			return render_to_response('messages/send_message.html', {'form':form,}, context_instance=RequestContext(request))
		# If the recipient was supplied by id, fill in the username for the user
		elif recipient_id is not None:
			recipient_from_id = get_object_or_404(User, pk=recipient_id)
			form = NewMessageForm(initial={'recipient':recipient_from_id.username,})
		else:
			form = NewMessageForm()
		return render_to_response('messages/send_message.html', {'form':form,}, context_instance=RequestContext(request))
	else:
		form = NewMessageForm(request.POST)
		if form.is_valid():
			subject = form.cleaned_data['subject']
			message = form.cleaned_data['message']
			recipient_name = form.cleaned_data['recipient']
			recipient = User.objects.get(username=recipient_name)
			new_received_message = ReceivedMessage.objects.create(recipient=recipient, sender=sender, subject=subject, message=message, read=False, replied=False, deleted=False)
			new_sent_message = SentMessage.objects.create(recipient=recipient, sender=sender, subject=subject, message=message, deleted=False)
			
			email_subject = "New mail at EpiC from %s" % (new_received_message.sender.username)
			#TODO: Set the get_absolute_url to actually return the domain (www.epic.org or what not)
			email_message = "%s has sent you a message:\n\n-----------\n%s\n-----------\n\nTo view this email or reply please visit %s\n" % (new_received_message.sender.username, new_received_message.message, new_received_message.get_absolute_url())
			send_mail(email_subject, email_message, 'no-reply@epic.edu', [recipient.email])
			return HttpResponseRedirect(reverse('epic.messages.views.view_sent_message', kwargs={'user_id':sender.id, 'sentmessage_id':new_sent_message.id,}))
		else:
			return render_to_response('messages/send_message.html', {'form':form,}, context_instance=RequestContext(request))