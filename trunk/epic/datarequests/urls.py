from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datarequests.views',
    (r'^$', 'view_datarequests'),
    (r'^new/$', 'new_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/$', 'view_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/edit/$', 'edit_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/cancel/$', 'cancel_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/fulfill/$', 'fulfill_datarequest'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/fulfill/(?P<fulfilling_item_id>\d+)/$', 'fulfill_datarequest'),
    (r'^choose/(?P<fulfilling_item_id>\d+)/$', 'choose_fulfilling_item')
)