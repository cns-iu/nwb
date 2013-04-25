from epic.projects.models import Project


def get_projects_containing_datasets(datasets):
    projects = Project.objects.filter(datasets__in=datasets)
    
    return projects
#    
#    project_sets = [dataset.projects.all() for dataset in datasets]
#    
#    
#    
#    for project_set in project_sets:
#        projects = 
#    project_sets = [set(dataset.projects.all()) for dataset in datasets]
#    projects = set([])
#    
#    for project_set in project_sets:
#        projects = projects.union(project_set)
#    
#    return list(projects)