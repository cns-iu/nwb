from django.conf.urls.defaults import *

urlpatterns = patterns('epic.geoloc.views',
	(r'^get_best_location/$', 'geoloc_get_best_location'),
)