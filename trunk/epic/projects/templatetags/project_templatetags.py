from django import template

from epic.projects.models import Project


register = template.Library()

@register.inclusion_tag('templatetags/project_list.html', takes_context=True)
def project_list(context, projects, show_delete_buttons=False):
    user = context['user']
    
    return {
        'projects': projects,
        'show_delete_buttons': show_delete_buttons,
        'user': user,
    }

@register.inclusion_tag('templatetags/project_header.html',
                        takes_context=True)
def project_header(context, project, show_delete_buttons=False):
    user = context['user']
    
    return {
        'project': project,
        'user': user,
        'show_delete_buttons': show_delete_buttons,
    }
