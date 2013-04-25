from django.conf.urls.defaults import *


urlpatterns = patterns('epic.categories.views',
    (r'^all/$', 'view_categories'),
    (r'^(?P<category_id>\d+)/$', 'view_items_for_category'),
    (r'^(?P<category_id>\d+)/datasets/$', 'view_datasets_for_category'),
    (r'^(?P<category_id>\d+)/projects/$', 'view_projects_for_category'),
    (r'^(?P<category_id>\d+)/datarequests/$',
     'view_datarequests_for_category'),
)
