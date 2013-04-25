from django import template

from epic.categories.models import Category


register = template.Library()

@register.inclusion_tag('templatetags/categories_box.html')
def categories_box(categories=None):
    if categories is None:
        categories = Category.objects.all()
    
    return {'categories': categories}

@register.inclusion_tag('templatetags/category_listing.html')
def category_listing(item):
    return {'item': item}