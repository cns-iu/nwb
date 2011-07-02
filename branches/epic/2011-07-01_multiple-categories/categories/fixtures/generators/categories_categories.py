from django.contrib.auth.models import User
from epic.core.models import Profile
from epic.categories.models import Category
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project

############
# bob user #
############

def _create_bob_user():
    bob_user = User.objects.create_user(username='bob', email='bob@bob.com', password='bob')
    bob_user.save()
    
    return bob_user

def _create_bob_profile(bob_user):
    bob_profile = Profile.objects.for_user(bob_user)
    bob_profile.save()
    
    return bob_profile

def _create_bob():
    bob_user = _create_bob_user()
    bob_profile = _create_bob_profile(bob_user)
    
    return bob_user

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

def _create_dataset1(creator, category):
    dataset1 = DataSet.objects.create(
        creator=creator,
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

def _create_dataset2(creator):
    dataset2 = DataSet.objects.create(
        creator=creator,
        name='Dataset2',
        description='Dataset2 Description',
        slug='Dataset2',
        is_active=True)
    
    return dataset2

############
# project1 #
############

def _create_project1(creator, category):
    project1 = Project.objects.create(
        creator=creator,
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

def _create_project2(creator):
    project2 = Project.objects.create(
        creator=creator,
        name='Project2',
        description='Project2 Description',
        slug='Project2',
        is_active=True)
    
    return project2

################
# datarequest1 #
################

def _create_datarequest1(creator, category):
    datarequest1 = DataRequest.objects.create(
        creator=creator,
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

def _create_datarequest2(creator):
    datarequest2 = DataRequest.objects.create(
        creator=creator,
        name='Datarequest2',
        description='Datarequest2 Description',
        slug='Datarequest2',
        is_active=True)
    
    return datarequest2

######################################
# Generate the actual fixtures here. #
######################################

bob = _create_bob()
category1 = _create_category1()
category2 = _create_category2()
dataset1 = _create_dataset1(bob, category1)
dataset2 = _create_dataset2(bob)
project1 = _create_project1(bob, category1)
project2 = _create_project2(bob)
datarequest1 = _create_datarequest1(bob, category1)
datarequest2 = _create_datarequest2(bob)
