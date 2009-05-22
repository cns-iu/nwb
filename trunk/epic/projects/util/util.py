from epic.projects.models import Project


def get_projects_containing_datasets(datasets):
    projects = Project.objects.filter(datasets__in=datasets).distinct()
    
    return projects
