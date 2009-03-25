from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datasets.views',
    (r'^$', 'index'),
    (r'^(?P<item_id>\d+)/$', 'view_dataset'),
    (r'^(?P<item_id>\d+)/comment/$', 'post_dataset_comment'),
    (r'^new/$', 'create_dataset'),
    (r'^(?P<item_id>\d+)/edit/$', 'edit_dataset'),
    (r'^(?P<item_id>\d+)/rate/$', 'rate_dataset'),
    (r'^(?P<item_id>\d+)/rate/(?P<input_rating>\d+)/$', 'rate_dataset'),
    (r'^(?P<item_id>\d+)/add_tags/$', 'tag_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/$', 'view_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/edit/$', 'edit_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/rate/$', 'rate_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/rate/(?P<input_rating>\d+)/$', 'rate_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/add_tags/$', 'tag_dataset'),  
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/add_geoloc/$', 'add_location_to_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/remove_geoloc/$', 'remove_location_from_dataset'),
)