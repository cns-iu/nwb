import haystack

from haystack.query import SearchQuerySet


def perform_search(search_query_set, query, load_all):
    if query is not None and query != '':
        results = search_query_set.auto_query(query)

        if load_all:
            results = results.load_all()

        return results
    else:
        return []

def perform_search_for_item(item_type, query, load_all=True):
    query_set = SearchQuerySet().filter(type=item_type)
    results = perform_search(query_set, query, load_all)
    objects = [result.object.specific for result in results]

    return objects