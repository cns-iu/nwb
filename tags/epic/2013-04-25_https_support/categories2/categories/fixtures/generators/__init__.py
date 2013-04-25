from django.contrib.auth.models import User

from epic.core.models import Profile


############
# bob user #
############

def _create_bob_user():
    bob_user = User.objects.create_user(username='bob',
        email='bob@bob.com',
        password='bob')
    
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


######################################
# Generate the actual fixtures here. #
######################################

bob = _create_bob()