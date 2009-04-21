from django import template
from django.contrib.auth.forms import AuthenticationForm
from django.template import RequestContext

from epic.core.forms import ShortAuthenticationForm    
from epic.datasets.models import DataSet


register = template.Library()

@register.inclusion_tag('core/login_box.html')
def login_box():
    form = ShortAuthenticationForm()
    
    return {'login_form': form}

@register.inclusion_tag('templatetags/include_form_as_table.html')
def include_form_as_table(form, style_required=False):
    return {'form': form, 'style_required': style_required,}

