from django.test import TestCase

from epic.datarequests.models import DataRequest
from django.contrib.auth.models import User
from django.template import TemplateDoesNotExist

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
	
	def testCancelled(self):
		self.assertEqual(0, DataRequest.objects.cancelled().count())
		self.data_request.cancel()
		self.data_request.save()
		for data_request in DataRequest.objects.cancelled():
			self.assertEqual('cancelled', data_request.get_status_display())
	
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
	
	def test404NonExistent(self):
		try:
			response = self.client.get('/datarequests/10000000000/')
			self.failUnlessEqual(response.status_code , 404)
		except TemplateDoesNotExist:
			pass
			
	
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
                     'name': 'This is a new datarequest that is asdf983205',
                     'description': 'Jump, Jump, Jump Around!',
                     'status': 'U',
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
	
	def testCancelledNotViewableAtIndex(self):
		data_request = DataRequest(creator=self.data_request.creator, name="Amazing request", description="Spectacular request indeed", status='C')
		data_request.save()
		response = self.client.get('/datarequests/')
		self.failIf("cancelled" in response.content)
		
class DataRequestCancelEditFulfillIndexTestCase(TestCase):
	fixtures = ['initial_users']
	
	def testDataRequestIndexUnOwned(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob2', password='bob2')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failIf('Edit</a>' in response.content, response.content)
		self.failIf('Cancel</a>' in response.content)
		self.failIf('Fulfill</a>' in response.content)
	
	def testDataRequestIndexUnOwnedCancelled(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='C')
		data_request.save()
		login = self.client.login(username='bob2', password='bob2')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failIf('Amazing request' in response.content, response.content)
		
	def testDataRequestIndexOwned(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob2', password='bob2')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
	
	def testDataRequestIndexOwnedCancelled(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='C')
		data_request.save()
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failIf('Amazing request' in response.content, response.content)
	
	def testDataRequestIndexOwnedFulfilled(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='F')
		data_request.save()
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless('Amazing request' in response.content, response.content)
		self.failIf('Edit</a>' in response.content, response.content)
		self.failIf('Cancel</a>' in response.content)
		self.failIf('Fulfill</a>' in response.content)
	
	def testDataRequestIndexOwnedUnFulfilled(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless('Amazing request' in response.content, response.content)

class DataRequestCancelEditFulfillPageTestCase(TestCase):
	fixtures = ['initial_users']
	
	def testDataRequestPageUnOwned(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob2', password='bob2')
		self.failUnless(login, 'Could not login')
		datarequesturl = '/datarequests/%s/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless('Amazing request' in response.content, response.content)
		self.failIf('Edit</a>' in response.content, response.content)
		self.failIf('Cancel</a>' in response.content)
	
	def testDataRequestPageOwnedU(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		datarequesturl = '/datarequests/%s/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless('Amazing request' in response.content, response.content)
		self.failUnless('Edit</a>' in response.content, response.content)
		self.failUnless('Cancel</a>' in response.content)
		self.failUnless('Fulfilled</a>' in response.content, response.content)
	
	def testDataRequestPageOwnedF(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='F')
		data_request.save()
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		datarequesturl = '/datarequests/%s/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless('Amazing request' in response.content, response.content)
		self.failIf('Edit</a>' in response.content, response.content)
		self.failIf('Cancel</a>' in response.content)
		self.failIf('Fulfilled</a>' in response.content)
	
	def testDataRequestPageOwnedC(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='C')
		data_request.save()
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		datarequesturl = '/datarequests/%s/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless('Amazing request' in response.content, response.content)
		self.failIf('Edit</a>' in response.content, response.content)
		self.failIf('Cancel</a>' in response.content)
		self.failIf('Fulfilled</a>' in response.content)
		
class DataRequestCancelEditFulfillActionsTestCase(TestCase):
	fixtures = ['initial_users']
	
	def testDataRequestCancelUnOwned(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob2', password='bob2')
		self.failUnless(login, 'Could not login')
		datarequesturl = '/datarequests/%s/cancel/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 302)
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless("Amazing request" in response.content)
		
	def testDataRequestCancelOwned(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		datarequesturl = '/datarequests/%s/cancel/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 302)
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failIf("Amazing request" in response.content)
	
	def testDataRequestFulfillUnOwned(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob2', password='bob2')
		self.failUnless(login, 'Could not login')
		datarequesturl = '/datarequests/%s/fulfill/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 302)
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless("Amazing request" in response.content)
		
	def testDataRequestFulfillOwned(self):
		creator = User.objects.get(username="bob")
		data_request = DataRequest(creator=creator, name="Amazing request", description="Spectacular request indeed", status='U')
		data_request.save()
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		datarequesturl = '/datarequests/%s/fulfill/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 302)
		response = self.client.get('/datarequests/')
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless("Amazing request" in response.content)
		datarequesturl = '/datarequests/%s/' % (data_request.id)
		response = self.client.get(datarequesturl)
		self.failUnlessEqual(response.status_code, 200)
		self.failUnless('Amazing request' in response.content, response.content)
		self.failIf('Edit</a>' in response.content, response.content)
		self.failIf('Cancel</a>' in response.content)
		self.failIf('Fulfilled</a>' in response.content)
		