from django.conf.urls.defaults import *

# Uncomment the next two lines to enable the admin:
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
    (r'^media/(?P<path>.*)$', 'django.views.static.serve',
     {'document_root': '/tmp/django_uploads'}),
    # Example:
    # (r'^epic_community_website/', include('epic_community_website.foo.urls')),

    # Uncomment the admin/doc line below and add 'django.contrib.admindocs' 
    # to INSTALLED_APPS to enable admin documentation:
     (r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
     (r'^admin/(.*)', admin.site.root),
)
