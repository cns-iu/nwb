from django.conf.urls.defaults import *
urlpatterns = patterns('epic.core.views',
	(r'^$', 'view_profile'),
	(r'^edit_profile/$', 'edit_profile'),
	(r'^forgotusername/$', 'forgot_username'),
	(r'^forgotemail/$', 'forgot_email'),
	(r'^forgotpassword/$', 'forgot_password'),
	(r'^change_password/$', 'change_password'),
)
urlpatterns += patterns('',
	(r'^(?P<user_id>\d+)/messages/', include('epic.messages.urls')),
)