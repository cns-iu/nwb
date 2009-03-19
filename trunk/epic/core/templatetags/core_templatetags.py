from django import template
from django.template import RequestContext
from django.contrib.auth.forms import AuthenticationForm
	
from epic.datasets.models import DataSet

register = template.Library()

@register.inclusion_tag('core/login_box.html')
def login_box():
	form = AuthenticationForm()
	return {'login_form':form}