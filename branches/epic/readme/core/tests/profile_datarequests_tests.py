from epic.core.test import CustomTestCase
from django.core.urlresolvers import reverse
from django.contrib.auth.models import User
from epic.datarequests.models import DataRequest

class ProfileDataRequestTestCase(CustomTestCase):
	
	fixtures = ['core_profile_datarequests']
	
	def setUp(self):
		self.profile_url = reverse('epic.core.views.view_profile')

	def tearDown(self):
		pass
	
	def testForNoDataRequests(self):
		""" Test if data requests are published even though not belonging to that user. Bob has no data requests. """
		
		self.tryLogin(username='bob', password='bob')
		
		response = self.client.get(self.profile_url)
		self.failUnless(response.status_code, 200)
		self.failIf("Your Data Requests" in response.content)

	def testForDataRequests(self):
		""" Test if a User's Data Request is shown in that User's profile. In this case Bill has 4 data requests. """
		self.tryLogin(username='bill', password='bill')
		
		user = User.objects.get(username='bill')
		datarequest = DataRequest(creator = user, 
									name = 'Inbuilt Data request Object with Status U', 
									description = 'Inbuilt Data request Object with Status U for Bill',
									status = 'U',
									slug = 'whatever'
								)
		datarequest.save()
		
		datarequest = DataRequest(creator = user, 
									name = 'Inbuilt Data request Object with Status F', 
									description = 'Inbuilt Data request Object with Status F for Bill',
									status = 'F',
									slug = 'whatever'
								)
		datarequest.save()
		
		response = self.client.get(self.profile_url)

		self.failUnless(response.status_code, 200)
		self.failUnless("Your Data Requests" in response.content, response.content)
		self.failUnless("edit" in response.content, response.content)

	def testForNoCanceledDataRequests(self):
		""" Test if canceled data requests are published for the logged in user. In this case,
 		Bill has 1 canceled data request which should not be published. """
		self.tryLogin(username='bill', password='bill')

		user = User.objects.get(username='bill')
		datarequest = DataRequest(creator = user, 
									name = 'Inbuilt Data request Object with Status C <- should not be shown', 
									description = 'Inbuilt Data request Object with Status C for Bill',
									status = 'C',
									slug = 'whatever'
								)
		datarequest.save()
		
		response = self.client.get(self.profile_url)
		
		self.failUnless(response.status_code, 200)
		self.failUnless("Your Data Requests" in response.content, response.content)
		self.failIf("Status C" in response.content, response.content)
		self.failUnless("edit" in response.content, response.content)
