from django.conf.urls.defaults import *


urlpatterns = patterns('epic.search.views',
    # All
    url(r'^form/$', 'get_search', name='form-search'),
    url(r'^all/$', 'search_all', kwargs={'query': ''}, name='all-empty-search'),
    url(r'^all/(?P<query>.+)/$', 'search_all', name='all-query-search'),
    # Data Requests
    url(r'data_requests/$',
        'search_data_requests',
        kwargs={'query': ''},
        name='data-requests-empty-search'),
    url(r'data_requests/(?P<query>.+)/$',
        'search_data_requests',
        name='data-requests-query-search'),
    # Datasets
    url(r'datasets/$',
        'search_datasets',
        kwargs={'query': ''},
        name='datasets-empty-search'),
    url(r'datasets/(?P<query>.+)/$',
        'search_datasets',
        name='datasets-query-search'),
    url(r'projects/$',
        'search_projects',
        kwargs={'query': ''},
        name='projects-empty-search'),
    url(r'projects/(?P<query>.+)/$',
        'search_projects',
        name='projects-query-search'),
    # Projects
)