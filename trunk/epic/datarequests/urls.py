from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datarequests.views',
    (r'^$', 'view_datarequests'),
    (r'^new/$', 'new_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/$', 'view_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/comment/$', 'post_datarequest_comment'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/edit/$', 'edit_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/cancel/$', 'cancel_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/fulfill/$', 'fulfill_datarequest'),
)