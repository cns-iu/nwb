from django.contrib.auth.models import User
from core.models import Profile


##############
# admin user #
##############

def _create_admin_user():
	admin_user = User.objects.create_user(
        username="admin", email="admin@epic.edu", password="admin")
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


##############
# mark user #
##############

def _create_mark_user():
	mark_user = User.objects.create_user(username="mark", email="mark@gmail.com", password="mark")
	mark_user.first_name = "Mark"
	mark_user.last_name = "Price"
	mark_user.save()
	
	return mark_user

def _create_mark_profile(mark_user):
	mark_profile = Profile.objects.for_user(mark_user)
	mark_profile.affiliation = 'CNS'
	mark_profile.save()
	
	return mark_profile

def _create_mark():
	mark_user = _create_mark_user()
	mark_profile = _create_mark_profile(mark_user)
	
	return mark_user


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


#############
# bill user #
#############

def _create_bill_user():
	bill_user = User.objects.create_user(username="bill",
		email="bill@bill.com",
		password="bill")
	
	bill_user.save()
	
	return bill_user

def _create_bill_profile(bill_user):
	bill_profile = Profile.objects.for_user(bill_user)
	
	bill_profile.save()
	
	return bill_profile

def _create_bill():
	bill_user = _create_bill_user()
	bill_profile = _create_bill_profile(bill_user)
	
	return bill_user


######################################
# Generate the actual fixtures here. #
######################################

mark = _create_mark()
bob = _create_bob()
bob2 = _create_bob2()
bill = _create_bill()
