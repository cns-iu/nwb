from django.conf.urls.defaults import *

urlpatterns = patterns('epic.core.user_views',
	(r'^$', 'index'),
	(r'^change_password/$', 'change_password')
)
