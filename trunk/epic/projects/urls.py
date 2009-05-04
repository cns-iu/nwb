from django.conf.urls.defaults import *


urlpatterns = patterns('epic.projects.views',
    (r'^$', 'view_projects'),
    (r'^list/(?P<user_id>.+)/$', 'view_user_project_list'),
    (r'^new/$', 'create_project'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/$', 'view_project'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/edit/$', 'edit_project'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/confirm_delete/$', 'confirm_delete_project'),
    (r'^(?P<item_id>\d+)/(?P<slug>[-\w]+)/delete/$', 'delete_project'),
)