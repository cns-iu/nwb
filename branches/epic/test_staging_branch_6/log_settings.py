import logging
import logging.handlers
import logging.config


FILE_PATH = 'server.log'
FORMAT_STRING = '%(asctime)s - %(name)s - %(levelname)s - %(message)s'


# Apparently, for some historical reason, Django imports settings.py multiple times.
# Without special consideration, this would mean multiple calls to setup().
# We use dynamic attribute setting on the logging module
# to try to guard against any further setup()s after the first.
if not hasattr(logging, "safe_setup_complete"):
	logging.safe_setup_complete = False

def safeSetup(config):
	if logging.safe_setup_complete:
		return
    
	logging.config.fileConfig(config)

	logging.safe_setup_complete = True
