from django.conf.urls.defaults import *

urlpatterns = patterns('epic.core.views',
    (r'^$', 'view_profile', {'user_id':None}),
	(r'^profile/(?P<user_id>\d+)/$', 'view_profile'),
	(r'^edit_profile/$', 'edit_profile'),
	(r'^forgot_password/$', 'forgot_password'),
	(r'^change_password/$', 'change_password'),
)
urlpatterns += patterns('',
	(r'^(?P<user_id>\d+)/messages/', include('epic.messages.urls')),
)
