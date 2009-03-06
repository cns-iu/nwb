from django.test import TestCase

from epic.datarequests.models import DataRequest

class DataRequestTestCase(TestCase):
	fixtures = ['initial_data', 'single_request', 'initial_users']
	
	def setUp(self):
		self.data_request = DataRequest.objects.unfulfilled()[0]
	
	def testUnfulfilled(self):
		self.assertEqual(1, DataRequest.objects.unfulfilled().count())
		self.data_request.cancel()
		self.data_request.save()
		self.assertEqual(0, DataRequest.objects.unfulfilled().count())
	
	def testFulfilled(self):
		self.assertEqual(0, DataRequest.objects.fulfilled().count())
		self.data_request.fulfill()
		self.data_request.save()
		self.assertEqual(1, DataRequest.objects.fulfilled().count())
	
	def testCanceled(self):
		self.assertEqual(0, DataRequest.objects.canceled().count())
		self.data_request.cancel()
		self.data_request.save()
		for data_request in DataRequest.objects.canceled():
			self.assertEqual('canceled', data_request.get_status_display())
	
	def testLifeCycle(self):
		data_request = DataRequest(creator=self.data_request.creator, name="Amazing request", description="Spectacular request indeed")
		data_request.save()
		self.assertEqual(2, DataRequest.objects.unfulfilled().count())
		data_request.fulfill()
		data_request.save()
		self.assertEqual(1, DataRequest.objects.unfulfilled().count())
		self.assertEqual(1, DataRequest.objects.fulfilled().count())
		self.assertEqual(data_request.name, DataRequest.objects.fulfilled()[0].name)

	def testIndex(self):
		data_request = DataRequest(creator=self.data_request.creator, name="Amazing request", description="Spectacular request indeed")
		data_request.save()
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200, "Error listing Data Requests!")
		self.failUnless("Amazing request" in response.content)
		
	def testDataRequestsPage(self):
		data_request = DataRequest(creator=self.data_request.creator, name="Amazing request", description="Spectacular request indeed")
		data_request.save()
		datarequest_location = '/datarequests/%s/' % (data_request.id)
		response = self.client.get(datarequest_location)
		self.failUnlessEqual(response.status_code, 200, "Error viewing Data Requests!")
		self.failUnless("Spectacular request indeed" in response.content)
	
	def test404NonExistant(self):
		response = self.client.get('/datarequests/10000000000/')
		self.failUnlessEqual(response.status_code , 404)
	
	def testAddDataRequestPageNotLoggedIn(self):
		response = self.client.get('/datarequests/new/')
		self.failUnlessEqual(response.status_code, 302)
	
	def testAddDataRequestPageLoggedIn(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
        
		response = self.client.get('/datarequests/new/')
		self.failUnlessEqual(response.status_code, 200)
	
	def testAddDataRequest(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
        
		response = self.client.get('/datarequests/new/')
		self.failUnlessEqual(response.status_code, 200)
		
		post_data = {
                     'item_name': 'This is a new datarequest that is asdf983205',
                     'item_description': 'Jump, Jump, Jump Around!',
        }
		
		response = self.client.post('/datarequests/new/', post_data)
		self.failUnlessEqual(response.status_code, 302)
		response = self.client.get('/datarequests/')
		self.failUnless("asdf983205" in response.content)
	
	def testStatusExistsAtIndex(self):
		data_request = DataRequest(creator=self.data_request.creator, name="Amazing request", description="Spectacular request indeed", status='F')
		data_request.save()
		datarequest_location = '/datarequests/%s/' % (data_request.id)
		response = self.client.get(datarequest_location)
		self.failUnlessEqual(response.status_code, 200, "Error viewing Data Requests!")
		self.failUnless("Spectacular request indeed" in response.content)
		self.failUnless("fulfilled" in response.content)
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200, "Error listing Data Requests!")
		self.failUnless("fulfilled" in response.content)
	
	def testCanceledNotViewableAtIndex(self):
		data_request = DataRequest(creator=self.data_request.creator, name="Amazing request", description="Spectacular request indeed", status='C')
		data_request.save()
		response = self.client.get('/datarequests/')
		self.failIf("canceled" in response.content)