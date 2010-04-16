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

def decorated_patterns(prefix, func, *args):
    '''
    Notice this only goes to depth 1 on include()d URLconfs.
    '''
    
    result = patterns(prefix, *args)
    if func:
        for p in result:
            if isinstance(p, RegexURLPattern):
                p.__class__ = DecoratedURLPattern
                p._decorate_with = func
            elif isinstance(p, RegexURLResolver):
                for pp in p._get_url_patterns():
                    if isinstance(pp, RegexURLPattern):
                        pp.__class__ = DecoratedURLPattern
                        pp._decorate_with = func
    
    return result