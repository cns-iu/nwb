from django.conf.urls.defaults import *

urlpatterns = patterns('epic.categories.views',
    (r'(?P<cat_id>\d+)/$', 'display_category'),
)
