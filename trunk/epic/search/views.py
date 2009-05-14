import urllib

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User, UserManager
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render_to_response, get_object_or_404, get_list_or_404
from django.template import RequestContext, Context, loader
from django.utils import simplejson

from epic.core.models import Item

def search(request):
    
    responseData = {}
    
    search_param = 'search_string'
    search_string = None
    
    if search_param in request.POST:
        search_string = request.POST[search_param]
    elif search_param in request.GET:
        search_string = request.GET[search_param]
    
    if search_string:
        results = []
        print search_string
        raw = urllib.urlopen('http://epic.slis.indiana.edu/fake.json')
        js_object = simplejson.loads(raw.read())

        for result in js_object['result']:
            item_id = result['item_id']
            try:
                item = Item.objects.get(pk=item_id)
                results.append({'item':item, 'score': result['score']})
            except Item.DoesNotExist:
                results = None
    else:
        results = None
        
    return render_to_response('search/view_search_results.html', 
                              {'results': results},
                              context_instance=RequestContext(request))