from django.conf.urls.defaults import *

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
	(r'^$', 'epic.core.views.index'),
	(r'^login/$', 'django.contrib.auth.views.login', {'template_name': 'core/login.html'}),
	(r'^logout/$', 'epic.core.views.logout_view'),
	(r'^user/', include('epic.core.user_urls')),
	(r'^datasets/', include('epic.datasets.urls')),
	(r'^datarequests/', include('epic.datarequests.urls')),
	(r'^admin/doc/', include('django.contrib.admindocs.urls')),
	(r'^admin/(.*)', admin.site.root),
	(r'^comments/', include('epic.comments.urls')),
	# TODO: Must change static media serving for security and performance reasons later on.
    (r'^media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': '/tmp/django_uploads'}),
)
