from epic.categories.models import Category
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project


#############
# category1 #
#############

def _create_category1():
    category1 = Category(name='Test Category1', description='Test Description1')
    category1.save()
    
    return category1

#############
# category2 #
#############

def _create_category2():
    category2 = Category(name='Test Category2', description='Test Description2')
    category2.save()
    
    return category2

############
# dataset1 #
############

def _create_dataset1(category):
    dataset1 = DataSet.objects.create(
        creator=bob,
        name='Dataset1',
        description='Dataset1 Description',
        slug='Dataset1',
        is_active=True)

    dataset1.categories.add(category) 
    dataset1.save()

    return dataset1

############
# dataset2 #
############

def _create_dataset2():
    dataset2 = DataSet.objects.create(
        creator=bob,
        name='Dataset2',
        description='Dataset2 Description',
        slug='Dataset2',
        is_active=True)
    
    return dataset2

############
# project1 #
############

def _create_project1(category):
    project1 = Project.objects.create(
        creator=bob,
        name='Project1',
        description='Project1 Description',
        
        slug='Project1',
        is_active=True)
    
    
    project1.categories.add(category)
    project1.save()
    
    return project1

############
# project2 #
############

def _create_project2():
    project2 = Project.objects.create(
        creator=bob,
        name='Project2',
        description='Project2 Description',
        slug='Project2',
        is_active=True)
    
    return project2

################
# datarequest1 #
################

def _create_datarequest1(category):
    datarequest1 = DataRequest.objects.create(
        creator=bob,
        name='Datarequest1',
        description='Datarequest1 Description',
        
        slug='Datarequest1',
        is_active=True)
    
    datarequest1.categories.add(category)
    datarequest1.save()
    
    return datarequest1

################
# datarequest2 #
################

def _create_datarequest2():
    datarequest2 = DataRequest.objects.create(
        creator=bob,
        name='Datarequest2',
        description='Datarequest2 Description',
        slug='Datarequest2',
        is_active=True)
    
    return datarequest2

######################################
# Generate the actual fixtures here. #
######################################

category1 = _create_category1()
category2 = _create_category2()
dataset1 = _create_dataset1(category1)
dataset2 = _create_dataset2()
project1 = _create_project1(category1)
project2 = _create_project2()
datarequest1 = _create_datarequest1(category1)
datarequest2 = _create_datarequest2()
