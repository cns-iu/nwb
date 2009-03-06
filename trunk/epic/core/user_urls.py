from django.conf.urls.defaults import *

urlpatterns = patterns('epic.core.user_views',
	(r'^$', 'index'),
	(r'^forgotusername/$', 'forgot_username'),
	(r'^forgotemail/$', 'forgot_email'),
	(r'^forgotpassword/$', 'forgot_password'),
	(r'^change_password/$', 'change_password')
)
