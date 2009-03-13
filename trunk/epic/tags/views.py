from epic.core.models import Item
from epic.tags.models import Tagging

from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404

def index(request):
    count_tag_list = Tagging.objects.get_frequency_list()
    return render_to_response('tags/index.html', {'user':request.user, 'count_tag_list':count_tag_list})

def view_items_for_tag(request, tag_name):
	tags = Tagging.objects.filter(tag=tag_name)
	return render_to_response('tags/tag_view.html', {'user':request.user, 'tags':tags, 'tag_name':tag_name})
