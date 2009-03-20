from django.conf.urls.defaults import *

urlpatterns = patterns('epic.messages.views',
	(r'^$', 'index'),
	(r'^sent/(?P<sentmessage_id>\d+)/$', 'view_sent_message'),
	(r'^received/(?P<receivedmessage_id>\d+)/$', 'view_received_message'),
	(r'^received/(?P<in_reply_to_message_id>\d+)/reply/$', 'send_message'),
	(r'^new/$', 'send_message'),
	(r'^new/(?P<recipient_id>\d+)/$', 'send_message'),
	
)