from django.conf.urls.defaults import *

urlpatterns = patterns('epic.core.user_views',
	(r'^$', 'index'),
	(r'^forgotusername/$', 'forgot_username'),
	(r'^forgotemail/$', 'forgot_email'),
	(r'^forgotpassword/$', 'forgot_password'),
	(r'^change_password/$', 'change_password')
)
urlpatterns += patterns('epic.messages.views',
	(r'^messages/$', 'index'),
	(r'^messages/message_sent/$', 'message_sent'),
	(r'^messages/new/(?P<to_user_id>\d+)/$', 'new_message'),
	(r'^messages/(?P<message_id>\d+)/$', 'view_message'),
)