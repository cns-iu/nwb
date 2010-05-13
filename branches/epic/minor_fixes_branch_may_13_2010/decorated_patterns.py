from django.core.urlresolvers import RegexURLPattern, RegexURLResolver
from django.conf.urls.defaults import patterns


# Credit to miracle2k and timbroder at djangosnippets.org
# http://www.djangosnippets.org/snippets/532/

class DecoratedURLPattern(RegexURLPattern):
    def resolve(self, *args, **kwargs):
        result = RegexURLPattern.resolve(self, *args, **kwargs)

        if result:
            result = list(result)
            result[0] = self._decorate_with(result[0])

        return result

def decorate_pattern_tree(pattern_bits, decorator):
    for p in pattern_bits:
        if isinstance(p, RegexURLPattern):
            p.__class__ = DecoratedURLPattern
            p._decorate_with = decorator
        # Recurse where necessary to decorate include()d patterns also.
        elif isinstance(p, RegexURLResolver):
            decorate_pattern_tree(p._get_url_patterns(), decorator)

def decorated_patterns(prefix, decorator, *args):
    result = patterns(prefix, *args)

    if decorator:
        decorate_pattern_tree(result, decorator)
    
    return result