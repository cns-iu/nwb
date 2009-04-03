from django.contrib.auth.models import User

user_peebs = User.objects.create_user(username="peebs", email="markispeebs@gmail.com", password="map")