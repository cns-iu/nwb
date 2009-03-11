from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datasets.views',
    (r'^$', 'index'),
    (r'^(?P<dataset_id>\d+)/$', 'view_dataset'),
    (r'^(?P<item_id>\d+)/comment/$', 'post_dataset_comment'),
    (r'^new', 'create_dataset'),
    (r'^(?P<dataset_id>\d+)/rate/$', 'rate_dataset'),
    (r'^(?P<dataset_id>\d+)/rate/(?P<input_rating>\d+)/$', 'rate_dataset'),
)