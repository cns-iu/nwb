from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datasets.views',
    (r'^$', 'index'),
    (r'^(?P<dataset_id>\d+)/$', 'view_dataset'),
    (r'^(?P<item_id>\d+)/comment/$', 'post_dataset_comment'),
    (r'^new/$', 'create_dataset'),
    (r'^(?P<dataset_id>\d+)/edit/$', 'edit_dataset'),
    (r'^(?P<dataset_id>\d+)/rate/$', 'rate_dataset'),
    (r'^(?P<dataset_id>\d+)/rate/(?P<input_rating>\d+)/$', 'rate_dataset'),
    (r'^(?P<dataset_id>\d+)/add_tags/$', 'tag_dataset'),
    (r'^(?P<dataset_id>\d+)/(?P<slug>[-\w]+)/$', 'view_dataset'),
    (r'^(?P<dataset_id>\d+)/(?P<slug>[-\w]+)/edit/$', 'edit_dataset'),
    (r'^(?P<dataset_id>\d+)/(?P<slug>[-\w]+)/rate/$', 'rate_dataset'),
    (r'^(?P<dataset_id>\d+)/(?P<slug>[-\w]+)/rate/(?P<input_rating>\d+)/$', 'rate_dataset'),
    (r'^(?P<dataset_id>\d+)/(?P<slug>[-\w]+)/add_tags/$', 'tag_dataset'),
    
    
)