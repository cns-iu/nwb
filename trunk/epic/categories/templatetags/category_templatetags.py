from django import template

from epic.categories.models import Category


register = template.Library()

@register.inclusion_tag('templatetags/categories_box.html')
def categories_box(categories=None):
    if categories is None:
        categories = Category.objects.all()
    
    return {'categories': categories}

@register.inclusion_tag('templatetags/category_link.html')
def category_link(item):
    return {'item': item}