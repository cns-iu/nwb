from base_settings import *
import log_settings

try:
	from database_credentials import *
except:
	print "No Database Credentials found"
	pass


DATABASE_ENGINE = 'postgresql_psycopg2'
DATABASE_NAME = 'epic_web'
DATABASE_PORT = '5432'
DATABASE_OPTIONS = {'sslmode': 'require'}

MEDIA_ROOT = '/home/epic_website/epic_data/'

#csrfProtect()



TEMPLATE_DIRS = (
	'/home/epic_website/epic_code/epic/templates',
)

EMAIL_HOST = 'localhost'
EMAIL_PORT = '25'

HAYSTACK_WHOOSH_PATH = '/tmp/whoosh'
