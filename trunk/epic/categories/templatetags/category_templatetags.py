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
            'view_items_for_category', kwargs={'category_id': category.slug}),
        'data_requests_url': reverse(
            'view_datarequests_for_category', kwargs={'category_id': category.slug}),
        'datasets_url': reverse(
            'view_datasets_for_category', kwargs={'category_id': category.slug}),
        'projects_url': reverse(
            'view_projects_for_category', kwargs={'category_id': category.slug}),
    }
