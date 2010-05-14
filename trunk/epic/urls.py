from django.conf import settings
from django.conf.urls.defaults import *
from django.contrib import admin
from epic.core.util.view_utils import logged_view
from decorated_patterns import decorated_patterns


admin.autodiscover()

urlpatterns = decorated_patterns('', logged_view,
    (r'^$', 'epic.core.views.site_index'),
    (r'^terms_and_conditions', 'epic.core.views.terms_and_conditions'),
    (r'^privacy_policy', 'epic.core.views.privacy_policy'),
    url(r'^browse/$', 'epic.core.views.browse', name='browse'),
    (r'^stats/$', 'epic.core.views.stats'),
    (r'^about/$', 'epic.core.views.about'),
    (r'^register/$', 'epic.core.views.register'),
    (r'^activate/(?P<activation_key>.+?)/$', 'epic.core.views.activate'),
    (r'^login/$', 'django.contrib.auth.views.login', {'template_name': 'core/login.html'}),
    (r'^logout/$', 'epic.core.views.logout_view'),
    (r'^user/', include('epic.core.urls')),
    (r'^datasets/', include('epic.datasets.urls')),
    (r'^datarequests/', include('epic.datarequests.urls')),
    # TODO: Must change static media serving for security and performance reasons later on.
    (r'^files/(?P<path>.*)$', 'django.views.static.serve', {'document_root': settings.MEDIA_ROOT}),
    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    (r'^admin/(.*)', admin.site.root),
    (r'^comments/', include('epic.comments.urls')),
    (r'^tags/', include('epic.tags.urls')),
    (r'^geoloc/', include('epic.geoloc.urls')),
    (r'^projects/', include('epic.projects.urls')),
    (r'^categories/', include('epic.categories.urls')),
    (r'^authors/(?P<author_name>.+?)/$', 'epic.core.views.view_items_for_author'),
    (r'^search/', include('epic.search.urls')),
#    (r'^search2/', include('haystack.urls')),
)

if settings.DEBUG:
    # NOT SECURE NOT FOR POST_DEVELOPMENT!
    # http://docs.djangoproject.com/en/1.0/howto/static-files/
    urlpatterns += patterns('',
        (r'^media/core/(?P<path>.*)$',
         'django.views.static.serve',
         {'document_root': 'core/media/'}),
        (r'^media/projects/(?P<path>.*)$',
         'django.views.static.serve',
         {'document_root': 'projects/media/'}),
        (r'^media/search/(?P<path>.*)$',
         'django.views.static.serve',
         {'document_root': 'search/media/'}),
        (r'^media/tags/(?P<path>.*)$',
         'django.views.static.serve',
         {'document_root': 'tags/media/'}),
    )
