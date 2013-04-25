from django import template

from epic.projects.models import Project


register = template.Library()

@register.inclusion_tag('templatetags/list_projects.html', takes_context=True)
def list_projects(context, projects, show_delete_buttons=False):
    user = context['user']
    
    return {
        'projects': projects,
        'show_delete_buttons': show_delete_buttons,
        'user': user,
    }

@register.inclusion_tag('templatetags/show_project_header.html',
                        takes_context=True)
def show_project_header(context, project, show_delete_buttons=False):
    user = context['user']
    
    return {
        'project': project,
        'user': user,
        'show_delete_buttons': show_delete_buttons,
    }

# TODO: This functionality is not finished, and thus this is never called.
# Delete it?
@register.inclusion_tag('templatetags/add_dataset_to_project__widget.html')
def add_dataset_to_project__widget(add_dataset_to_project_formset):
    return {'add_dataset_to_project_formset': add_dataset_to_project_formset,}
