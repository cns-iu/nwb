from base_settings import *
import log_settings


log_settings.safeSetup('local_logging.conf')
TEST_RUNNER='xmlrunner.extra.djangotestrunner.run_tests'
