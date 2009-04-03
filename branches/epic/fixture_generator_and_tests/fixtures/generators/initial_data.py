from django.contrib.auth.models import User
from core.models import Profile

##############
# admin user #
##############

def _create_admin_user():
	admin_user = User.objects.create_user(username="admin",
		email="admin@example.com",
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


######################################
# Generate the actual fixtures here. #
######################################

admin = _create_admin()
