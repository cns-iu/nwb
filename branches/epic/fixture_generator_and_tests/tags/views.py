from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext

from epic.core.models import Item
from epic.tags.models import Tagging

def index(request):
    count_tag_list = Tagging.objects.get_tags()
    return render_to_response('tags/index.html', {'tags':count_tag_list}, context_instance=RequestContext(request))

def view_items_for_tag(request, tag_name):
	tags = Tagging.objects.filter(tag=tag_name)
	datasets = []
	for tag in tags:
		datasets.append(tag.item.specific)
	return render_to_response('tags/tag_view.html', {'tags':tags, 'tag_name':tag_name, 'datasets':datasets}, context_instance=RequestContext(request))
