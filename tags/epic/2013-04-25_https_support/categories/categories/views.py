from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext

from epic.categories.models import Category
from epic.core.models import Item

def display_category(request, cat_id):
    category = get_object_or_404(Category, pk=cat_id)
    items = Item.objects.filter(categories=category)
    return render_to_response('categories/display_category.html',
                              {'category': category,
                               'items': items})