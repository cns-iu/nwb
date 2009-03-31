from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datarequests.views',
    (r'^$', 'view_datarequests'),
    (r'^new/$', 'new_datarequest'),
    (r'^(?P<item_id>\d+)/$', 'view_datarequest'),
    (r'^(?P<item_id>\d+)/comment/$', 'post_datarequest_comment'),
    (r'^(?P<item_id>\d+)/edit/$', 'edit_datarequest'),
    (r'^(?P<item_id>\d+)/cancel/$', 'cancel_datarequest'),
    (r'^(?P<item_id>\d+)/fulfill/$', 'fulfill_datarequest'),
)