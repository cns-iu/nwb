import logging
import logging.handlers

FILEPATH = 'server.log'
FORMAT_STRING = '%(asctime)s - %(name)s - %(levelname)s - %(message)s'


def setup():
	root_logger = logging.getLogger('')
	root_logger.setLevel(logging.DEBUG)
	
    # TODO Or perhaps SysLogHandler, if we can get IT to set it up?
	console = logging.handlers.RotatingFileHandler(FILEPATH, maxBytes=50000, backupCount=3)
	console.setFormatter(logging.Formatter(FORMAT_STRING))
	root_logger.addHandler(console)


# Apparently, for some historical reason, Django imports settings.py multiple times.
# Without special consideration, this would mean multiple calls to setup().
# We use dynamic attribute setting on the logging module
# to try to guard against any further setup()s after the first.
if not hasattr(logging, "safe_setup_complete"):
	logging.safe_setup_complete = False
def safeSetup():
	if logging.safe_setup_complete:
		return

	setup()

	logging.safe_setup_complete = True