from django.conf.urls.defaults import *

urlpatterns = patterns('epic.datarequests.views',
    (r'^$', 'index'),
    (r'^(?P<datarequest_id>\d+)/$', 'view_datarequest'),
    #(r'^new', 'new_datarequest')
)