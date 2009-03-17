from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.contrib.auth.decorators import login_required
from django.core.mail import send_mail

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
	return render_to_response('messages/index.html', {'user':user, 'sent_messages':sent_messages, 'received_messages':received_messages})

@login_required
def view_sent_message(request, user_id, sentmessage_id):
	user = request.user
	user_from_id = get_object_or_404(User, pk=user_id)
	sent_message = get_object_or_404(SentMessage, pk=sentmessage_id)
	
	# Sent user to their page if they try to find another user's page...
	if (user != user_from_id):
		print "Users didn't match, sending to correct page."
		return HttpResponseRedirect(reverse('epic.messages.views.view_sent_message', kwargs={'user_id':user.id, 'sentmessage_id':sentmessage_id}))

	# Sent the user to the index if they are not the sender or receiver of this message
	if (user.id != sent_message.sender.id) and (user.id != sent_message.recipient.id):
		print "User shouldn't be allowed to view this message: %s != (%s | %s)" % (user.id, sent_message.sender.id, sent_message.recipient.id)
		return HttpResponseRedirect(reverse('epic.messages.views.index', kwargs={'user_id':user.id,}))
	
	return render_to_response('messages/view_sent_message.html', {'user':user, 'sent_message':sent_message,})

@login_required	
def view_received_message(request, user_id, receivedmessage_id):
	user = request.user
	user_from_id = get_object_or_404(User, pk=user_id)
	received_message = get_object_or_404(ReceivedMessage, pk=receivedmessage_id)
	
	# Sent user to their page if they try to find another user's page...
	if (user != user_from_id):
		print "Users didn't match, sending to correct page."
		return HttpResponseRedirect(reverse('epic.messages.views.view_received_message', kwargs={'user_id':user.id, 'receivedmessage_id':receivedmessage_id}))

	# Sent the user to the index if they are not the sender or receiver of this message
	if (user.id != received_message.sender.id) and (user.id != received_message.recipient.id):
		print "User shouldn't be allowed to view this message: %s != (%s | %s)" % (user.id, received_message.sender.id, received_message.recipient.id)
		return HttpResponseRedirect(reverse('epic.messages.views.index', kwargs={'user_id':user.id,}))
	
	received_message.read = True
	received_message.save()
	return render_to_response('messages/view_received_message.html', {'user':user, 'received_message':received_message,})

@login_required	
def reply_received_message(request, user_id, receivedmessage_id):
	user = request.user
	user_from_id = get_object_or_404(User, pk=user_id)
	received_message = get_object_or_404(ReceivedMessage, pk=receivedmessage_id)

	# Sent user to their page if they try to find another user's page...
	if (user != user_from_id):
		print "Users didn't match, sending to correct page."
		return HttpResponseRedirect(reverse('epic.messages.views.reply_received_message', kwargs={'user_id':user.id, 'receivedmessage_id':receivedmessage_id}))

	# Sent the user to the index if they are not the sender or receiver of this message
	if (user.id != received_message.sender.id) and (user.id != received_message.recipient.id):
		print "User shouldn't be allowed to view this message: %s != (%s | %s)" % (user.id, received_message.sender.id, received_message.recipient.id)
		return HttpResponseRedirect(reverse('epic.messages.views.index', kwargs={'user_id':user.id,}))
	
	if request.method != 'POST':
		form = NewMessageForm(initial={'recipient':received_message.sender.username,'subject':'RE:%s' % (received_message.subject), 'message':'------\n%s said:\n%s' % (received_message.sender, received_message.message)})
		return render_to_response('messages/reply_message.html', {'form':form, 'user':request.user,})
	else:
		form = NewMessageForm(request.POST)
		if form.is_valid():
			subject = form.cleaned_data['subject']
			message = form.cleaned_data['message']
			recipient_name = form.cleaned_data['recipient']
			recipient = User.objects.get(username=recipient_name)
			sender = user
			new_received_message = ReceivedMessage.objects.create(recipient=recipient, sender=sender, subject=subject, message=message, read=False, replied=False, deleted=False)
			new_sent_message = SentMessage.objects.create(recipient=recipient, sender=sender, subject=subject, message=message, deleted=False)
			
			email_subject = "New mail at EpiC from %s" % (new_received_message.sender.username)
			#TODO: Set the get_absolute_url to actually return the domain (www.epic.org or what not)
			email_message = "%s has sent you a message:\n\n-----------\n%s\n-----------\n\nTo view this email or reply please visit %s\n" % (new_received_message.sender.username, new_received_message.message, new_received_message.get_absolute_url())
			send_mail(email_subject, email_message, 'email@epic.com', [recipient.email])
			
			received_message.replied=True
			received_message.save()
			
			return HttpResponseRedirect(reverse('epic.messages.views.view_sent_message', kwargs={'user_id':sender.id, 'sentmessage_id':new_sent_message.id,}))
		else:
			print "invalid reply form"
			return render_to_response('messages/reply_message.html', {'form':form, 'user':request.user,})
	
@login_required
def create_new_message(request, user_id):
	sender = request.user
	user_from_id = get_object_or_404(User, pk=user_id)
	
	# Sent user to their page if they try to find another user's page...
	if (sender != user_from_id):
		print "Users didn't match, sending to correct page."
		return HttpResponseRedirect(reverse('epic.messages.views.create_new_message', kwargs={'user_id':user.id}))
	
	if request.method != 'POST':
		form = NewMessageForm()
		return render_to_response('messages/new_message.html', {'form':form, 'user':request.user,})
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
			send_mail(email_subject, email_message, 'email@epic.com', [recipient.email])
			return HttpResponseRedirect(reverse('epic.messages.views.view_sent_message', kwargs={'user_id':sender.id, 'sentmessage_id':new_sent_message.id,}))
		else:
			return render_to_response('messages/new_message.html', {'form':form, 'user':request.user,})