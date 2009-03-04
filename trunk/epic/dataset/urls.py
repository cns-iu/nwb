from django.conf.urls.defaults import *

urlpatterns = patterns('epic_community_website.dataset.views',
    (r'^$', 'index'),
    (r'^(?P<dataset_id>\d+)/$', 'view_dataset'),
    (r'^(?P<dataset_id>\d+)/upload/$', 'upload'),
    (r'^new', 'new_dataset')
)