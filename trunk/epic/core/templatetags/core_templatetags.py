from django import template
from django.contrib.auth.forms import AuthenticationForm
from django.core.urlresolvers import reverse
from django.template import RequestContext

from epic.core.forms import ShortAuthenticationForm
from epic.core.models import Profile 
from epic.datasets.models import DataSet


NAV_BAR_LINK_NAMES = ('browse', 'share', 'request', 'about')

register = template.Library()


@register.inclusion_tag('templatetags/pagination.html', takes_context=True)
def pagination(context, page=None, page_radius=5):
    page_index_start = max(page.number - page_radius, 1)
    page_index_finish = min(page.number + page_radius, page.paginator.num_pages)
    page_range = range(page_index_start, page_index_finish + 1)
    
    return {'page': page,
		    'page_index_start': page_index_start,
		    'page_index_finish': page_index_finish,
		    'page_range': page_range,
		    'user': context['user']}

@register.inclusion_tag('core/login_box.html')
def login_box():
    form = ShortAuthenticationForm()
    
    return {'login_form': form}

@register.inclusion_tag('templatetags/form_as_table.html')
def form_as_table(form, style_required=False):
    return {'form': form, 'style_required': style_required,}

@register.inclusion_tag('templatetags/nav_bar.html')
def nav_bar(link_to_be_highlighted=None):
    if link_to_be_highlighted is None:
        return {'link_to_be_highlighted': None}
    
    if link_to_be_highlighted.lower() not in NAV_BAR_LINK_NAMES:
        raise Exception('nav_bar expects one of the following: %s' % NAV_BAR_LINK_NAMES)
    
    return {'link_to_be_highlighted': link_to_be_highlighted}

@register.simple_tag
def nav_bar_link(current_link, link_to_be_highlighted, view_function_name):
    """ This is a simple tag, which means it's returning the actual HTML for
    the nav bar link to be placed in the template calling it.
    """
    
    if link_to_be_highlighted is None or current_link.lower() != link_to_be_highlighted.lower():
        # TODO: pageoff class (have Elisha style this up real pretty).
        link_class = 'pageoff'
    else:
        link_class = 'pageon'
    
    view_url = reverse(view_function_name, kwargs={})
    nav_bar_link_html_data = {
        'view_url': view_url, 'link_class': link_class, 'current_link': current_link
    }
    nav_bar_link_html = '<a href="%(view_url)s" class="%(link_class)s">%(current_link)s</a>' % \
        nav_bar_link_html_data
    
    return nav_bar_link_html

@register.inclusion_tag('templatetags/submenu.html')
def browse_submenu(active=None):
    return {
        'active': active,
        'header': "Browse in",
        'all_url': reverse('browse',),
        'data_requests_url': reverse('browse-data-requests',),
        'datasets_url': reverse('browse-datasets',),
        'projects_url': reverse('browse-projects',),
    }

@register.inclusion_tag('templatetags/user_title.html')
def user_title(user, show_affiliation=None):
    if user.first_name and user.last_name:
        name = user.first_name + " " + user.last_name  
    else:
        name = Profile.objects.for_user(user).NULL_TITLE
        
    if show_affiliation:
        affiliation = Profile.objects.for_user(user).affiliation
    else:
        affiliation = None
        
    return {'name': name, 'affiliation': affiliation, 'user': user}
