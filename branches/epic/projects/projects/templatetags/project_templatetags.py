from django import template

from epic.projects.models import Project


register = template.Library()

@register.inclusion_tag('templatetags/list_projects.html', takes_context=True)
def list_projects(context, projects):
    user = context['user']
    
    return {'projects': projects, 'user': user}

@register.inclusion_tag('templatetags/show_project_header.html',
                        takes_context=True)
def show_project_header(context, project):
    user = context['user']
    
    return {'project': project, 'user': user}
