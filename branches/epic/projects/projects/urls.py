from django.conf.urls.defaults import *


urlpatterns = patterns('epic.projects.views',
    (r'^$', 'view_projects'),
    (r'^list/(?P<user_id>.+)/$', 'view_user_project_list'),
    (r'^new/$', 'create_project'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/$', 'view_project'),
    (r'^(?P<item_id>\d+)/view-(?P<slug>[-\w]+)/edit/$', 'edit_project'),
)