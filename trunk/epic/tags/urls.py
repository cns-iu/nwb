from django.conf.urls.defaults import *

urlpatterns = patterns('epic.tags.views',
    (r'^$', 'index'),
    (r'^delete_tag/$', 'delete_tag'),
    (r'^add_tags_and_return_successful_tag_names/$', 'add_tags_and_return_successful_tag_names'),
    url(
        r'^(?P<tag_name>.+)/datarequests/$',
        'view_datarequests_for_tag',
        name='view-data-requests-for-tag'),
    url(r'^(?P<tag_name>.+)/datasets/$', 'view_datasets_for_tag', name='view-datasets-for-tag'),
    url(r'^(?P<tag_name>.+)/projects/$', 'view_projects_for_tag', name='view-projects-for-tag'),
    # This has to be last!
    url(r'^(?P<tag_name>.+)/$', 'view_items_for_tag', name='view-items-for-tag'),
)