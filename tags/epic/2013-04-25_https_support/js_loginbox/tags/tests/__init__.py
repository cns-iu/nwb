from doc_tests import *
from view_tests import *
from data_request_tests import *

class MagicTestCase(CustomTestCase):
	def testHttp(self):
		from urllib2 import urlopen
		import socket
		socket.setdefaulttimeout(20)
		urlopen("http://google.com", None)
		self.assertTrue(True)
