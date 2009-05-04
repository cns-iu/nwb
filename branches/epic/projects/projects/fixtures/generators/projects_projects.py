from epic.datasets.models import DataSet
from epic.projects.models import Project
from epic.tags.models import Tagging


###################
# Create project1 #
###################

def _create_project1():
    project1 = Project.objects.create(
        creator=bob,
        name='project1',
        description='This is the first project',
        slug='project1',
        is_active=True)
    
    return project1

###################
# Create project2 #
###################

def _create_project2_dataset():
    dataset = DataSet.objects.create(
        creator=bob,
        name='dataset1',
        description='This is the first dataset',
        slug='dataset1',
        is_active=True)
    
    bob_tags = 'lol testing for teh win'
    
    Tagging.objects.add_tags_and_return_added_tag_names(
        bob_tags, item=dataset, user=bob)
    
    return dataset

def _create_project2():
    project_dataset = _create_project2_dataset()
    
    project2 = Project.objects.create(
        creator=admin,
        name='project2',
        description='This is the second project',
        slug='project2',
        is_active=True)
    
    project2.datasets.add(project_dataset)
    
    return project2

#######################
# Create the Projects #
#######################

def _create_projects():
    project1 = _create_project1()
    project2 = _create_project2()

######################################
# Generate the actual fixtures here. #
######################################

_create_projects()