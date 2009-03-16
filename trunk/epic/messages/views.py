from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.contrib.auth.decorators import login_required
from django.core.mail import send_mail

from epic.messages.models import Message
from epic.messages.forms import NewMessageForm

@login_required
def index(request):
	user = request.user
	sent_messages = Message.objects.filter(from_user=user)
	received_messages = Message.objects.filter(to_user=user)
	return render_to_response('messages/index.html', {'user':user, 'sent_messages':sent_messages, 'received_messages':received_messages})

def message_sent(request):
	return render_to_response('messages/message_sent.html', {'user':request.user,})

@login_required
def new_message(request, to_user_id):
	to_user = get_object_or_404(User, pk=to_user_id)
	from_user = request.user
	if request.method != 'POST':
		form = NewMessageForm()
		return render_to_response('messages/new_message.html', {'form':form, 'user':request.user,'to_user_name':to_user.username,})
	else:
		form = NewMessageForm(request.POST)
		if form.is_valid():
			subject = form.cleaned_data['subject']
			message = form.cleaned_data['message']
			new_message = Message.objects.create(to_user=to_user, from_user=from_user, subject=subject, message=message)
			subject = "New mail at EpiC"
			#TODO: Set the get_absolute_url to actually return the domain (www.epic.org or what not)
			email_message = "%s has sent you a message:\n\n-----------\n%s\n-----------\n\nTo view this email or reply please visit %s\n" % (new_message.from_user.username, new_message.message, new_message.get_absolute_url())
			send_mail(subject, email_message, 'email@epic.com', [to_user.email])
			return HttpResponseRedirect(reverse('epic.messages.views.message_sent'))
		else:
			return render_to_response('messages/new_message.html', {'form':form, 'user':request.user, 'to_user_name':to_user.username,})
		
@login_required
def view_message(request, message_id):
	user = request.user
	message = get_object_or_404(Message, pk=message_id)
	if (message.to_user == user) or (message.from_user == user):
		reply_form = NewMessageForm(initial={'subject': 'RE:%s' % (message.subject), 'message': ' \n--------------\n%s wrote:\n%s' % (message.from_user, message.message)})
		return render_to_response('messages/view_message.html', {'user':request.user, 'message':message, 'reply_form':reply_form, 'to_user':message.from_user if user != message.from_user else message.to_user})
	else:
		#print "User %s was not %s or %s" % (user, message.to_user, message.from_user)
		return HttpResponseRedirect(reverse('epic.messages.views.index'))