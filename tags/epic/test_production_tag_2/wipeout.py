from django.db import connection
from django.db import transaction


cursor = connection.cursor()
cursor.execute('DROP SCHEMA public CASCADE')
cursor.execute('CREATE SCHEMA public')
transaction.commit_unless_managed()
