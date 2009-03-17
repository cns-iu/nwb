from django.conf.urls.defaults import *

urlpatterns = patterns('epic.messages.views',
	(r'^$', 'index'),
	(r'^sent/(?P<sentmessage_id>\d+)/$', 'view_sent_message'),
	(r'^received/(?P<receivedmessage_id>\d+)/$', 'view_received_message'),
	(r'^received/(?P<receivedmessage_id>\d+)/reply/$', 'reply_received_message'),
	(r'^new/$', 'create_new_message'),
)