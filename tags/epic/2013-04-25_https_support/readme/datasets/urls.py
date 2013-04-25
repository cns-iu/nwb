from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datasets.views',
    (r'^$', 'view_datasets'),
    (r'^(?P<item_id>\d+)/$', 'view_dataset'),
	(r'^list/(?P<user_id>.+)/$', 'view_user_dataset_list'),
    (r'^new/$', 'create_dataset'),
    (r'^(?P<item_id>\d+)/edit/$', 'edit_dataset'),
    (r'^(?P<item_id>\d+)/rate/$', 'rate_dataset'),
    (r'^(?P<item_id>\d+)/rate/(?P<input_rating>\d+)/$', 'rate_dataset_using_input_rating'),
    (r'^(?P<item_id>\d+)/add_tags/$', 'tag_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/$', 'view_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/edit/$', 'edit_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/rate/$', 'rate_dataset'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/rate/(?P<input_rating>\d+)/$', 'rate_dataset_using_input_rating'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/add_tags/$', 'tag_dataset'),
)