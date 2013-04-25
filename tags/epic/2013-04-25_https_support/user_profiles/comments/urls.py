from django.conf.urls.defaults import *


urlpatterns = patterns('epic.comments.views',
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/$', 'post_comment'),
)