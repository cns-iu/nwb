from django import template

from epic.datasets.models import DataSet


register = template.Library()

@register.inclusion_tag('templatetags/dataset_list.html', takes_context=True)
def dataset_list(context, datasets):
    user = context['user']
    
    return {'datasets': datasets, 'user': user}

@register.inclusion_tag('templatetags/dataset_header.html',
                        takes_context=True)
def dataset_header(context, dataset, rating_allowed):
    user = context['user']
    if rating_allowed == 'rating_allowed':
        rating_allowed = True
    else:
        rating_allowed = False
    return {'dataset':dataset, 'user':user, 'rating_allowed': rating_allowed,}

@register.inclusion_tag('templatetags/recent_datasets.html')
def recent_datasets(limit=3):
    datasets = DataSet.objects.active().order_by('-created_at')[:limit]
    return {'datasets':datasets,}