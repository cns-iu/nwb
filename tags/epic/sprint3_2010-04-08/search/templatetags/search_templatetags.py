from django import template
from django.core.urlresolvers import reverse
from django.template import RequestContext


register = template.Library()

@register.inclusion_tag('templatetags/search_box.html')
def search_box(search_string=None):
    return {'search_string': search_string}
