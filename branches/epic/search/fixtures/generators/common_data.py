from django.contrib.auth.models import User
from datarequests.models import DataRequest
from datasets.models import DataSet
from core.models import Profile

##############
# admin user #
##############

def _create_admin_user():
	admin_user = User.objects.create_user(username="admin",
		email="admin@epic.edu",
		password="admin")
	
	admin_user.save()
	
	return admin_user

def _create_admin_profile(admin_user):
	admin_profile = Profile.objects.for_user(admin_user)
	
	admin_profile.save()
	
	return admin_profile

def _create_admin():
	admin_user = _create_admin_user()
	admin_profile = _create_admin_profile(admin_user)
	
	return admin_user

############
# bob user #
############

def _create_bob_user():
	bob_user = User.objects.create_user(username="bob",
		email="bob@bob.com",
		password="bob")
	
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
# bob2 user #
#############

def _create_bob2_user():
	bob2_user = User.objects.create_user(username="bob2",
		email="bob2@bob.com",
		password="bob2")
	
	bob2_user.save()
	
	return bob2_user

def _create_bob2_profile(bob2_user):
	bob2_profile = Profile.objects.for_user(bob2_user)
	
	bob2_profile.save()
	
	return bob2_profile

def _create_bob2():
	bob2_user = _create_bob2_user()
	bob2_profile = _create_bob2_profile(bob2_user)
	
	return bob2_user

######################################
# Create the Canceled DataRequests.  #
######################################

def _create_c_datarequests():
	canceled_datarequest1 = DataRequest.objects.create(creator=bob, name='canceled_datarequest1', description='The first canceled datarequest', status='C', slug='canceled_datarequest1', is_active=True)
	canceled_datarequest2 = DataRequest.objects.create(creator=admin, name='canceled_datarequest2', description='The second canceled datarequest', status='C', slug='canceled_datarequest2', is_active=True)
	return

######################################
# Create the Fulfilled DataRequests. #
######################################

def _create_f_datarequests():
	fulfilled_datarequest1 = DataRequest.objects.create(creator=bob, name='fulfilled_datarequest1', description='The first fulfilled datarequest', status='F', slug='fulfilled_datarequest1', is_active=True)
	fulfilled_datarequest2 = DataRequest.objects.create(creator=admin, name='fulfilled_datarequest2', description='The second fulfilled datarequest', status='F', slug='fulfilled_datarequest2', is_active=True)
	return 

########################################
# Create the unfulfilled DataRequests. #
########################################

def _create_u_datarequests():
	unfulfilled_datarequest1 = DataRequest.objects.create(creator=bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U', slug='unfulfilled_datarequest1', is_active=True)
	unfulfilled_datarequest2 = DataRequest.objects.create(creator=admin, name='unfulfilled_datarequest2', description='The second unfulfilled datarequest', status='U', slug='unfulfilled_datarequest2', is_active=True)
	return

#######################
# Create the DataSets #
#######################

def _create_datasets():
	dataset1 = DataSet.objects.create(creator=bob, name='dataset1', description='this is the first dataset', slug='dataset1', is_active=True)
	dataset2 = DataSet.objects.create(creator=admin, name='dataset2', description='this is the second dataset', slug='dataset2', is_active=True)
	

######################################
# Generate the actual fixtures here. #
######################################

admin = _create_admin()
bob = _create_bob()
bob2 = _create_bob2()

_create_c_datarequests()
_create_f_datarequests()
_create_u_datarequests()

_create_datasets()