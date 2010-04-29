import datetime
import log_settings
import logging
import random

from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
import epic.core.util.view_utils


# Tests core.util.logged_view.  See also log_settings.py.

class LogMessageTestCase(CustomTestCase):    
    def setUp(self):
        self.test_token = str(datetime.datetime.now())        
        logging.getLogger(self.__module__).info('Test log message: ' + self.test_token)
    
    def testTokenInLog(self):
        log_file = open(log_settings.FILE_PATH)
        self.assertTrue(self.test_token in log_file.read())

class SiteIndexViewLoggedTestCase(CustomTestCase):
  def setUp(self):
    self.view_name = 'site_index'
    self.url_extension = reverse('epic.core.views.%s' % self.view_name)
    self.client.get(self.url_extension)

  def testSiteIndexViewIsLogged(self):
    log_file = open(log_settings.FILE_PATH)
    most_recent_message = log_file.readlines()[-1]
    self.assertTrue('path="%s"' % self.url_extension in most_recent_message)
    self.assertTrue('view_name="%s"' % self.view_name in most_recent_message)

class GeolocViewLoggedTestCase(CustomTestCase):
  def setUp(self):
    self.view_name = 'geoloc_get_best_location'
    self.url_extension = reverse('epic.geoloc.views.%s' % self.view_name)
    self.client.get(self.url_extension)

  def testSiteIndexViewIsLogged(self):
    log_file = open(log_settings.FILE_PATH)
    most_recent_message = log_file.readlines()[-1]
    self.assertTrue('path="%s"' % self.url_extension in most_recent_message)
    self.assertTrue('view_name="%s"' % self.view_name in most_recent_message)

class ViewTimedeltaMicrosecondsNonnegativeTestCase(CustomTestCase):
    def setUp(self):
        self.client.get(reverse('epic.core.views.site_index'))

    def testViewTimedeltaMicrosecondsNonnegative(self):
        log_file = open(log_settings.FILE_PATH)
        most_recent_message = log_file.readlines()[-1]
        parts = most_recent_message.split()
        timedelta_parts = filter(lambda s: s.startswith('view_timedelta_microseconds='), parts)
        self.assertTrue(len(timedelta_parts) == 1)

        timedelta_entry = timedelta_parts[0]
        timedelta_value = float(timedelta_entry.split('=')[1].strip('"'))
        self.assertTrue(timedelta_value >= 0)


class UnknownHttpUserAgentTestCase(CustomTestCase):
    """Assuming Django's Client won't set HTTP_USER_AGENT in the request."""

    def setUp(self):
        self.client.get(reverse('epic.core.views.site_index'))

    def testViewTimedeltaMicrosecondsNonnegative(self):
        log_file = open(log_settings.FILE_PATH)
        most_recent_message = log_file.readlines()[-1]
        self.assertTrue(('HTTP_USER_AGENT="%s"' % epic.core.util.view_utils.UNKNOWN_LOG_VALUE) \
            in most_recent_message)
