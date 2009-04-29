from django.conf.urls.defaults import *

urlpatterns = patterns('epic.search.views',
    (r'^$', 'search'),
)