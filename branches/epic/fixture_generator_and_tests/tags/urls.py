from django.conf.urls.defaults import *

urlpatterns = patterns('epic.tags.views',
    (r'^$', 'index'),
    (r'^delete_tag/$', 'delete_tag'),
    (r'^(?P<tag_name>.+)/$', 'view_items_for_tag'),
)