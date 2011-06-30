from django.conf.urls.defaults import *


urlpatterns = patterns('epic.categories.views',
    (r'^all/$', 'view_categories'),
    url(r'^(?P<category_id>[\w|-]+)/$', 'view_items_for_category', name='view-items-for-category'),
    url(
        r'^(?P<category_id>[\w|-]+)/datarequests/$',
        'view_datarequests_for_category',
        name='view-datarequests-for-category'),
    url(
        r'^(?P<category_id>[\w|-]+)/datasets/$',
        'view_datasets_for_category',
        name='view-datasets-for-category'),
    url(
        r'^(?P<category_id>[\w|-]+)/projects/$',
        'view_projects_for_category',
        name='view-projects-for-category'),
)
