from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect

from epic.settings import DEACTIVATED_ACCOUNT_VIEW

#TODO: get rid of this in favor of the appropriate user_passes_test calls
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
