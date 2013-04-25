import datetime
import log_settings
import logging
import random

from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.core.util.view_utils import paginate


def terminal_pages(page):
	paginator = page.paginator
	page_range = paginator.page_range
	return [page_range[0], page_range[-1]]

class PaginateTestCase(CustomTestCase):    
    def testPerPageZero(self):
        self.assertRaises(ZeroDivisionError, paginate, range(20), {'page': 1}, per_page=0)
    
    def testPageZeroGivesATerminalPage(self):
    	page = paginate(range(20), {})
    	self.assertTrue(page.number in terminal_pages(page))
    
    def testPageOne(self):
    	page = paginate(range(20), {'page': '1'}, per_page=5)
    	self.assertEqual(page.number, 1)
    
    def testPageTwo(self):
    	page = paginate(range(20), {'page': '2'}, per_page=5)
    	self.assertEqual(page.number, 2)
    
    def testEmptyObjectListIsPageOne(self):
    	self.assertEqual(paginate([], {}).number, 1)
    	
    def testNegativePageGivesATerminalPage(self):
    	page = paginate(range(20), {'page': '-7'})
    	self.assertTrue(page.number in terminal_pages(page))
    
    def testNonIntPageGivesATerminalPage(self):
    	page = paginate(range(20), {'page': 'qwerty'})
    	self.assertTrue(page.number in terminal_pages(page))
    	
    def testLargePageGivesLastPage(self):
    	page = paginate(range(20), {'page': '1000000000000000'})
    	self.assertEqual(page.number, page.paginator.page_range[-1])