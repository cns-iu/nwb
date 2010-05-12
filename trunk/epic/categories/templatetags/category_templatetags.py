from django import template
from django.core.urlresolvers import reverse

from epic.categories.models import Category


register = template.Library()

@register.inclusion_tag('templatetags/categories_box.html')
def categories_box(categories=None):
    if categories is None:
        categories = Category.objects.all().order_by('name')
    
    return {'categories': categories}

@register.inclusion_tag('templatetags/category_link.html')
def category_link(item):
    return {'item': item}

@register.inclusion_tag('templatetags/submenu.html')
def category_submenu(category, active=None):
    return {
        'active': active,
        'header': "Browse by category in",
        'all_url': reverse(
            'view-items-for-category', kwargs={'category_id': category.id}),
        'data_requests_url': reverse(
            'view-datarequests-for-category', kwargs={'category_id': category.id}),
        'datasets_url': reverse(
            'view-datasets-for-category', kwargs={'category_id': category.id}),
        'projects_url': reverse(
            'view-projects-for-category', kwargs={'category_id': category.id}),
    }
