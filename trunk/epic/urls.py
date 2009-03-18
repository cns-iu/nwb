from django.conf.urls.defaults import *
from django.conf import settings
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    (r'^$', 'epic.core.views.index'),
    (r'^login/$', 'django.contrib.auth.views.login', {'template_name': 'core/login.html'}),
    (r'^logout/$', 'epic.core.views.logout_view'),
    (r'^user/', include('epic.core.user_urls')),
    (r'^datasets/', include('epic.datasets.urls')),
    (r'^datarequests/', include('epic.datarequests.urls')),
    #TODO: must change static media serving for security and performance reasons later on
    (r'^media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': 'uploaded_files/'}),
    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    (r'^admin/(.*)', admin.site.root),
    (r'^comments/', include('epic.comments.urls')),
    (r'^tags/', include('epic.tags.urls')),
)

if settings.DEBUG:
	# NOT SECURE NOT FOR POST_DEVELOPMENT!
	# http://docs.djangoproject.com/en/1.0/howto/static-files/
	urlpatterns += patterns('',
		(r'^core_media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': 'core/media/'}),					
	)