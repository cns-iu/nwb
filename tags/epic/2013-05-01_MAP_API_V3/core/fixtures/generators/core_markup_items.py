from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project


def _create_item_of_type(item_type, item_type_string, unique_identifier):
    unique_item_name_string = '%s%s' % (item_type_string, unique_identifier)
    description = \
        ('This is the description for the item [b]%(item_name)s[/b] ' + \
         'of type [blink]%(item_type)s[/blink]' + \
         '[img]https://www.google.com/intl/en_ALL/images/logo.gif[/img]') % \
            {'item_name': unique_item_name_string, 'item_type': item_type_string}
    
    item = item_type(
        creator=bob, name=unique_item_name_string, description=description, is_active=True)
    
    return item

################
# datarequest1 #
################

def _create_datarequest1():
    datarequest1 = _create_item_of_type(DataRequest, 'DataRequest', 1)
    datarequest1.status = 'U'
    datarequest1.save()
    
    return datarequest1

############
# dataset1 #
############

def _create_dataset1():
    dataset1 = _create_item_of_type(DataSet, 'DataSet', 1)
    dataset1.save()
    
    return dataset1

############
# project1 #
############

def _create_project1():
    project1 = _create_item_of_type(Project, 'Project', 1)
    project1.save()
    
    return project1

######################################
# Generate the actual fixtures here. #
######################################

datarequest1 = _create_datarequest1()
dataset1 = _create_dataset1()
project1 = _create_project1()
