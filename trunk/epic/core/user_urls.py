from django.conf.urls.defaults import *

urlpatterns = patterns('epic.core.user_views',
	(r'^$', 'index'),
)