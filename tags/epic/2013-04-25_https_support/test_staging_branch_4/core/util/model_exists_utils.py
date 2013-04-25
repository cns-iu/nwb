from django.contrib.auth.models import User


def user_exists(**kwargs):
    try:
        User.objects.get(**kwargs)

        return True
    except User.DoesNotExist:
        return False

def profile_exists(**kwargs):
    try:
        Profile.objects.get(**kwargs)

        return True
    except:
        return False
