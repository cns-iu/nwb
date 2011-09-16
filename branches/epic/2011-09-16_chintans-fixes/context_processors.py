#from haystack.forms import SearchForm
from epic.search.forms import *


def search_box(request):
    if 'q' in request.GET:
        query = request.GET.get('q')
    else:
        query = ''

    form = SearchBoxForm(initial={'q': query})
#    form = SearchForm(request)

    return {'search_box': form}