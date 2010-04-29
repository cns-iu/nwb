from base_settings import *
import log_settings


DATABASE_ENGINE = 'postgresql_psycopg2'
DATABASE_NAME = 'epic_web'
DATABASE_USER = 'epic_appuser'
DATABASE_PASSWORD = 'du3T9Y'
DATABASE_HOST = 'cns-dbdev.slis.indiana.edu'
DATABASE_PORT = ''
#DATABASE_OPTIONS = {'sslmode': 'require'}

GOOGLE_KEY = \
    'ABQIAAAAPID1hqHQtPywH_7NchOzFBQaeT_z_s7ky1aQpddgjZAedvZ1kBQhCnJYwofdlXqWAQDTT8TbD7FrGA'
MEDIA_ROOT = '/home/epic_website/epic_data/'

DEBUG = True

csrfProtect()

log_settings.safeSetup('/home/epic_website/epic_data/server.log')

TEMPLATE_DIRS = (
	'/home/epic_website/epic_code/epic/templates',
)