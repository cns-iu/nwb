from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect

from epic.settings import DEACTIVATED_ACCOUNT_VIEW


def active_user_required(actual_view_function, redirect_url=DEACTIVATED_ACCOUNT_VIEW):
    def _decorate(view_function):
        def _view(request, *args, **kwargs):
            if not request.user.is_active:
                if not redirect_url:
                    return HttpResponseRedirect(reverse(''))
                else:
                    return HttpResponseRedirect(reverse(redirect_url))
            else:
                return actual_view_function(request, *args, **kwargs)

        _view.__name__ = view_function.__name__
        _view.__dict__ = view_function.__dict__
        _view.__doc__ = view_function.__doc__

        return _view

    if actual_view_function is None:
        return _decorate
    else:
        return _decorate(actual_view_function)
    
def admin_user_required(actual_view_function, redirect_url="epic.core.views.about"):
    '''A Staff or a Superuser both can be considered as having admin rights. TODO: Verify
    if my definition is correct, if not, then change the name of the decorator.'''
    def _decorate(view_function):
        def _view(request, *args, **kwargs):
            if request.user.is_staff or request.user.is_superuser:
                return actual_view_function(request, *args, **kwargs)
            else:
                if redirect_url:
                    return HttpResponseRedirect(reverse(redirect_url))
                else:
                    return HttpResponseRedirect(reverse(''))

        _view.__name__ = view_function.__name__
        _view.__dict__ = view_function.__dict__
        _view.__doc__ = view_function.__doc__

        return _view

    if actual_view_function is None:
        return _decorate
    else:
        return _decorate(actual_view_function)