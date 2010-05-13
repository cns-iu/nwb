from django.contrib.auth.models import User
from django.core.management import call_command
from django.core.urlresolvers import reverse

from haystack.management.commands.rebuild_index import Command as RebuildCommand

from epic.core.models import Profile
from epic.core.test import CustomTestCase


#class SearchTestCase(CustomTestCase):
#    fixtures = ['demo_data']
#
#    def setUp(self):
#        print "setUp"
#        call_command('test_setup')
#
#    def tearDown(self):
#        print "tearDown"
#        call_command('test_teardown')
#
#    def testEmptyQuery(self):
#        self.assertTrue(False)
#
#    def testAllPage(self):
#        self.assertTrue(False)
#
#    def testDataRequestPage(self):
#        self.assertTrue(False)
#
#    def testDatasetsPage(self):
#        self.assertTrue(False)
#
#    def testProjectPage(self):
#        self.assertTrue(False)
