from base_settings import *

DATABASE_ENGINE = 'postgresql_psycopg2'
DATABASE_NAME = 'epic_web'
DATABASE_USER = 'epic_appuser'
DATABASE_PASSWORD = 'du3T9Y'
DATABASE_HOST = 'cns-dbdev.slis.indiana.edu'
DATABASE_PORT = ''
DATABASE_OPTIONS = {'sslmode': 'require'}

GOOGLE_KEY = 'ABQIAAAAXvJKaXXv-aFZ9QFPDtLf2RRYbDcT7AU1sqlHKk7y1uR7IcGNVBRPMeVhk8y3ZgqAMdZmpULqIktKzw'
MEDIA_ROOT = '/home/epic_website/epic_data/'

DEBUG = False

csrfProtect()

import log_settings
log_settings.safeSetup('/home/epic_website/epic_data/server.log')

