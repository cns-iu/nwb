from epic.core.test import CustomTestCase

from epic.datasets.models import DataSet
from epic.datasets.models import DataSetFile
from django.utils.datastructures import MultiValueDict
from django.core.urlresolvers import reverse
from django.conf import settings
import os

class RatingTestCase(CustomTestCase):
	fixtures = ['common_data']
	
	def setUp(self):
		self.data_set = DataSet.objects.active()[0]
		
	def tearDown(self):
		pass
	
	def testRatingView(self):
		response = self.client.get('/datasets/')
		self.failUnlessEqual(response.status_code, 200, "Error listing datasets!")
		
		totalVotes = "Votes"
		numberVotes = "<span id=\"votes-value\">0</span>"
		
		self.failUnless(totalVotes in response.content, "There should be unrated data specifically the token 'Total Votes:'")
		self.failUnless(numberVotes in response.content, "There should be unrated data specifically the token '0' for votes")
		
		response = self.client.post("/login/", {'username': 'bob', 'password':'bob',})
		response = self.client.get('/datasets/')
		
		self.failUnlessEqual(response.status_code, 200, "Error listing datasets!")
		
		self.failUnless(totalVotes in response.content, "There should be unrated data specifically the token 'Total Votes:'")
		self.failUnless(numberVotes in response.content, "There should be unrated data specifically the token '0' for votes")
	
	def testRating(self):
		from django.test.client import Client
		#TODO remove once we use a version that is past r9847
		# http://code.djangoproject.com/changeset/9847
		# http://code.djangoproject.com/ticket/8551
		c = Client(REMOTE_ADDR='127.0.0.1')
		
		# Make sure there is no rating
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)

		avgVotes = "Average"
		averageValue = "<span id=\"average-value\">No</span>"
		
		self.failUnless(avgVotes in response.content, "There should be unrated data specifically the token 'Avg:'")
		self.failUnless(averageValue in response.content, "There should be unrated data specifically the token 'No' for votes")
		
		# Rate
		rate_location = dataset_location + "rate/3/"
		response = c.get(rate_location)
		self.failUnlessEqual(response.status_code, 302)
		
		# Make sure there still no rating because anon people can't rate
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)
		
		self.failUnless(avgVotes in response.content, "There should be unrated data specifically the token 'Avg:'")
		self.failUnless(averageValue in response.content, "There should be unrated data specifically the token 'None' for votes")
		
		# Log in
		login = c.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		# Rate
		rate_location = dataset_location + "rate/3/"
		response = c.get(rate_location)
		self.failUnlessEqual(response.status_code, 200)
		
		# Make sure bob's rating counted
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)
		
		averageValue = "<span id=\"average-value\">3.0</span>"
		self.failUnless(avgVotes in response.content, "There should be rated data specifically the token 'Avg:'")
		self.failUnless(averageValue in response.content, "There should be rated data specifically the token '3' for votes")
		
		# Log in as bob2
		login = c.login(username='bob2', password='bob2')
		self.failUnless(login, 'Could not login')
		
		# Rate
		rate_location = dataset_location + "rate/5/"
		response = c.get(rate_location)
		self.failUnlessEqual(response.status_code, 200)
		
		# Make sure bob2's rating counted
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)
		
		averageValue = "<span id=\"average-value\">4.0</span>"
		
		self.failUnless(avgVotes in response.content, "There should be rated data specifically the token 'Avg:'")
		self.failUnless(averageValue in response.content, "There should be rated data specifically the token '4' for votes")
		
		# Rate AGAIN!
		rate_location = dataset_location + "rate/5/"
		response = c.get(rate_location)
		self.failUnlessEqual(response.status_code, 200)
		
		# Make sure bob2's second rating didn't count
		dataset_location = '/datasets/%s/' % (self.data_set.id)
		response = c.get(dataset_location)

		self.failUnless(avgVotes in response.content, "There should be rated data specifically the token 'Avg:'")
		self.failUnless(averageValue in response.content, "There should be rated data specifically the token '4' for votes")