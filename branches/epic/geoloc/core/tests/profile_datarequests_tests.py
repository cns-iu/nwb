from django.test import TestCase

from django.contrib.auth.models import User

from epic.datarequests.models import DataRequest

class ProfileDataRequestTestCase(TestCase):
	
	fixtures = ['profile_datarequests']
	
	def setUp(self):
		pass

	def testForNoDataRequests(self):
		''' Test if data requests are published even though not belonging to that user. Bob has no data requests. '''
		
		login = self.client.login(username='bob', password='bob')
		self.assertTrue(login)
		response = self.client.get('/user/')
		self.failUnless(response.status_code, 200)
		self.failIf("Your Data Requests" in response.content)

	def testForDataRequests(self):
		''' Test if a User's Data Request is shown in that User's profile. In this case Bill has 4 data requests. '''
		login = self.client.login(username='bill', password='bill')
		self.assertTrue(login)
		
		user = User.objects.get(username='bill')
		datarequest = DataRequest(creator = user, 
									name = 'Inbuilt Data request Object with Status U', 
									description = 'Inbuilt Data request Object with Status U for Bill',
									status = 'U'
								)
		datarequest.save()
		
		datarequest = DataRequest(creator = user, 
									name = 'Inbuilt Data request Object with Status F', 
									description = 'Inbuilt Data request Object with Status F for Bill',
									status = 'F'
								)
		datarequest.save()
		
		response = self.client.get('/user/')

		self.failUnless(response.status_code, 200)
		self.failUnless("Your Data Requests" in response.content, response.content)
		self.failUnless("edit" in response.content, response.content)

	def testForNoCancelledDataRequests(self):
		''' Test if canceled data requests are published for the logged in user. In this case,
 		Bill has 1 canceled data request which should not be published. '''
		login = self.client.login(username='bill', password='bill')
		self.assertTrue(login)

		user = User.objects.get(username='bill')
		datarequest = DataRequest(creator = user, 
									name = 'Inbuilt Data request Object with Status C <- should not be shown', 
									description = 'Inbuilt Data request Object with Status C for Bill',
									status = 'C'
								)
		datarequest.save()
		
		response = self.client.get('/user/')
		
		self.failUnless(response.status_code, 200)
		self.failUnless("Your Data Requests" in response.content, response.content)
		self.failIf("Status C" in response.content, response.content)
		self.failUnless("edit" in response.content, response.content)
