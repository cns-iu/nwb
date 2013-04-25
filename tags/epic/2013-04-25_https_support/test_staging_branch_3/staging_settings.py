from base_settings import *
import log_settings


from deploy_settings import *


GOOGLE_KEY = \
    'ABQIAAAAPID1hqHQtPywH_7NchOzFBQEETqECKGW5nJg3svHFwfIy2F2ChTIqOldboWGioscwT9XwAtYygHHPQ'

DEBUG = True

log_settings.safeSetup('dev_stage_logging.conf')
