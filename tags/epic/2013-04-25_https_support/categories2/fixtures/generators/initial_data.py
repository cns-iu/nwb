from django.contrib.auth.models import User

from epic.core.models import Profile


User.objects.create_superuser('super', 'super@gmail.com', 'super')
super = User.objects.get(username='super')
super.first_name = 'Super'
super.last_name = 'User'
super.save()

super_profile = Profile.objects.for_user(user=super)


chintan = User.objects.create_user(username='chintan',
                                   email='chintan@gmail.com',
                                   password='chintan')
chintan.first_name = 'Chintan'
chintan.last_name = 'Tank'
chintan.save()

chintan_profile = Profile.objects.for_user(user=chintan)


david = User.objects.create_user(username='david',
                                 email='david@gmail.com',
                                 password='david')
david.first_name = 'David'
david.last_name = 'Coe'
david.save()

david_profile = Profile.objects.for_user(user=david)


elisha = User.objects.create_user(username='elisha',
                                  email='elisha@gmail.com',
                                  password='elisha')
elisha.first_name = 'David'
elisha.last_name = 'Coe'
elisha.save()

elisha_profile = Profile.objects.for_user(user=elisha)


micah = User.objects.create_user(username='micah',
                                 email='micah@gmail.com',
                                 password='micah')
micah.first_name = 'micah'
micah.last_name = 'micah'
micah.save()

micah_profile = Profile.objects.for_user(user=micah)


patrick = User.objects.create_user(username='patrick',
                                   email='patrick@gmail.com',
                                   password='patrick')
patrick.first_name = 'Patrick'
patrick.last_name = 'Phillips'
patrick.save()

patrick_profile = Profile.objects.for_user(user=patrick)


russell = User.objects.create_user(username='russell',
                                   email='russell@gmail.com',
                                   password='russell')
russell.first_name = 'Russell'
russell.last_name = 'Duhon'
russell.save()

russell_profile = Profile.objects.for_user(user=russell)



alex = User.objects.create_user(username='alex',
                                email='alex@gmail.com',
                                password='alex')
alex.first_name = 'Alessandro'
alex.last_name = 'Vespignani'
alex.save()

alex_profile = Profile.objects.for_user(user=alex)


bruno = User.objects.create_user(username='bruno',
                                 email='bruno@gmail.com',
                                 password='bruno')
bruno.first_name = 'Bruno'
bruno.last_name = 'Goncalves'
bruno.save()

bruno_profile = Profile.objects.for_user(user=bruno)


jim = User.objects.create_user(username='jim',
                               email='jim@gmail.com',
                               password='jim')
jim.first_name = 'Jim'
jim.last_name = 'Sherman'
jim.save()

jim_profile = Profile.objects.for_user(user=jim)


katy = User.objects.create_user(username='katy',
                                email='katy@gmail.com',
                                password='katy')
katy.first_name = 'Katy'
katy.last_name = 'Borner'
katy.save()

katy_profile = Profile.objects.for_user(user=katy)
