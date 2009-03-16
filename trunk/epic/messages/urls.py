from django.conf.urls.defaults import *

# TODO: have this url file imported
# We want these in users_urls.py I guess so that /user/messages goes here, but having users_urls.py import this file breaks the permalink get_absolute_url for the Messsage Model....
##############################
#urlpatterns = patterns('epic.messages.views',
#	(r'^$', 'index'),
#	(r'^message_sent/$', 'message_sent'),
#	(r'^new/(?P<to_user_id>\d+)/$', 'new_message'),
#	(r'^(?P<message_id>\d+)/$', 'view_message'),
#)