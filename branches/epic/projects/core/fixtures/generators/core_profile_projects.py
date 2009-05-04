from epic.projects.models import Project


############
# project1 #
############

def _create_project1():
    project1 = Project(creator=bob,
                       name='Test Project1',
                       description='Description of Test Project1',
                       slug='test-project1',
                       is_active=True)
    
    project1.save()
    
    return project1

######################################
# Generate the actual fixtures here. #
######################################

project1 = _create_project1()
