from base_settings import *
import log_settings

from database_credentials import *

DATABASE_ENGINE = 'postgresql_psycopg2'
DATABASE_NAME = 'epic_web'
DATABASE_PORT = ''
#DATABASE_OPTIONS = {'sslmode': 'require'}

MEDIA_ROOT = '/home/epic_website/epic_data/'

csrfProtect()

log_settings.safeSetup('/home/epic_website/epic_data/server.log')


TEMPLATE_DIRS = (
	'/home/epic_website/epic_code/epic/templates',
)
