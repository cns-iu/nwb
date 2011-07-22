from django import template
from django.core.urlresolvers import reverse
from django.template import RequestContext


register = template.Library()

@register.inclusion_tag('templatetags/search_box.html', takes_context=True)
def search_box(context, search_string=None):
    return {'search_string': search_string, 'search_box': context['search_box'],}

@register.inclusion_tag('templatetags/submenu.html')
def search_submenu(query, active=None):
    if query is None or query == '':
        all_url = reverse('all-empty-search')
        data_requests_url = reverse('data-requests-empty-search')
        datasets_url = reverse('datasets-empty-search')
        projects_url = reverse('projects-empty-search')
    else:
        all_url = reverse('all-query-search', kwargs={'query': query,})
        data_requests_url = reverse('data-requests-query-search', kwargs={'query': query,})
        datasets_url = reverse('datasets-query-search', kwargs={'query': query,})
        projects_url = reverse('projects-query-search', kwargs={'query': query,})

    return {
        'active': active,
        'header': "Limit search results to",
#        'all_url': reverse('search', kwargs={'query': query,}),
        'all_url': all_url,
#        'data_requests_url': reverse('browse-data-requests',),
        'data_requests_url': data_requests_url,
#        'datasets_url': reverse('browse-datasets',),
        'datasets_url': datasets_url,
#        'projects_url': reverse('browse-projects',),
        'projects_url': projects_url,
    }
