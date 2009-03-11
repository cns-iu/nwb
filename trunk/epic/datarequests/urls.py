from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datarequests.views',
    (r'^$', 'index'),
    (r'^new/$', 'new_datarequest'),
    (r'^(?P<datarequest_id>\d+)/$', 'view_datarequest'),
    (r'^(?P<item_id>\d+)/comment/$', 'post_datarequest_comment'),
    (r'^(?P<datarequest_id>\d+)/edit/$', 'edit_datarequest'),
    (r'^(?P<datarequest_id>\d+)/cancel/$', 'cancel_datarequest'),
    (r'^(?P<datarequest_id>\d+)/fulfill/$', 'fulfill_datarequest'),
)