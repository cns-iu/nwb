from django.conf.urls.defaults import *

urlpatterns = patterns('epic.tags.views',
    (r'^$', 'index'),
    (r'^delete_tag/$', 'delete_tag'),
    (r'^add_tags_and_return_successful_tag_names/$', 'add_tags_and_return_successful_tag_names'),
    (r'^(?P<tag_name>.+)/$', 'view_items_for_tag'),
)