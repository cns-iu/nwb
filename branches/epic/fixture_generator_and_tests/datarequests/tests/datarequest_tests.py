from django.test import TestCase
from django.core.urlresolvers import reverse
from epic.datarequests.models import DataRequest
from django.contrib.auth.models import User
from django.template import TemplateDoesNotExist

class DataRequestTestCase(TestCase):
	""" Test that the model for datarequests is working correctly """
	
	fixtures = ['just_users', 'datarequests']
	
	def setUp(self):
		self.admin = User.objects.get(username='admin')
		self.bob = User.objects.get(username='bob')
		
		self.drC1 = DataRequest.objects.get(creator=self.bob, name='drC1', description='The first canceled datarequest', status='C')
		self.drC2 = DataRequest.objects.get(creator=self.admin, name='drC2', description='The second canceled datarequest', status='C')
		self.drC3 = DataRequest.objects.get(creator=self.bob, name='drC3', description='The third canceled datarequest', status='C')
		self.drC4 = DataRequest.objects.get(creator=self.admin, name='drC4', description='The fourth canceled datarequest', status='C')
		self.drC5 = DataRequest.objects.get(creator=self.bob, name='drC5', description='The fifth canceled datarequest', status='C')
		
		self.drF1 = DataRequest.objects.get(creator=self.bob, name='drF1', description='The first fulfilled datarequest', status='F')
		self.drF2 = DataRequest.objects.get(creator=self.admin, name='drF2', description='The second fulfilled datarequest', status='F')
		self.drF3 = DataRequest.objects.get(creator=self.bob, name='drF3', description='The third fulfilled datarequest', status='F')
		self.drF4 = DataRequest.objects.get(creator=self.admin, name='drF4', description='The fourth fulfilled datarequest', status='F')
		self.drF5 = DataRequest.objects.get(creator=self.bob, name='drF5', description='The fifth fulfilled datarequest', status='F')
		self.drF6 = DataRequest.objects.get(creator=self.admin, name='drF6', description='The sixth fulfilled datarequest', status='F')
	
		self.drU1 = DataRequest.objects.get(creator=self.bob, name='drU1', description='The first unfulfilled datarequest', status='U')
		self.drU2 = DataRequest.objects.get(creator=self.admin, name='drU2', description='The second unfulfilled datarequest', status='U')
		self.drU3 = DataRequest.objects.get(creator=self.bob, name='drU3', description='The third unfulfilled datarequest', status='U')
		self.drU4 = DataRequest.objects.get(creator=self.admin, name='drU4', description='The fourth unfulfilled datarequest', status='U')
	
	def tearDown(self):
		pass
	
	def testUnfulfilled(self):
		self.assertEqual(DataRequest.objects.unfulfilled().count(), 4)
		self.drU1.cancel()
		self.drU1.save()
		self.assertEqual(DataRequest.objects.unfulfilled().count(), 3)
		for dr in DataRequest.objects.unfulfilled():
			self.assertEqual('unfulfilled', dr.get_status_display())
		
	def testFulfilled(self):
		self.assertEqual(DataRequest.objects.fulfilled().count(), 6)
		self.drU1.fulfill()
		self.drU1.save()
		self.assertEqual(DataRequest.objects.fulfilled().count(), 7)
		for dr in DataRequest.objects.fulfilled():
			self.assertEqual('fulfilled', dr.get_status_display())
	
	def testCanceled(self):
		self.assertEqual(DataRequest.objects.canceled().count(), 5)
		self.drU1.cancel()
		self.drU1.save()
		self.assertEqual(DataRequest.objects.canceled().count(), 6)
		for dr in DataRequest.objects.canceled():
			self.assertEqual('canceled', dr.get_status_display())
	
	def testLifeCycle(self):
		self.assertEqual(DataRequest.objects.unfulfilled().count(), 4)
		dr = DataRequest.objects.create(creator=self.bob, name="Amazing request", description="this is the description")
		self.assertEqual(DataRequest.objects.unfulfilled().count(), 5)
		dr.fulfill()
		dr.save()
		self.assertEqual(DataRequest.objects.unfulfilled().count(), 4)
		self.assertEqual(DataRequest.objects.fulfilled().count(), 7)
		self.assertTrue(dr in DataRequest.objects.fulfilled())
		
class UrlsTestCase(TestCase):
	""" Test all the urls to make sure that the view for each works """
	
	fixtures = ['just_users', 'datarequests'] # TODO: I just need one datarequest and a user
	
	def setUp(self):
		self.bob = User.objects.get(username='bob')
		self.dr1 = DataRequest.objects.get(creator=self.bob, name='drU1', description='The first unfulfilled datarequest', status='U')
	
	def tearDown(self):
		pass
	
	def testViewDataRequests(self):
		url = reverse('epic.datarequests.views.view_datarequests')
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 200)
		
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 200)
	
	def testNewDataReqeusts(self):
		url = reverse('epic.datarequests.views.new_datarequest')
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 302)
		
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 200)
		
	def testCommentDataReqeusts(self):
		url = reverse('epic.datarequests.views.post_datarequest_comment', kwargs={'item_id':self.dr1.id})
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 302)
		
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 302)
		
	def testEditDataReqeusts(self):
		url = reverse('epic.datarequests.views.edit_datarequest', kwargs={'item_id':self.dr1.id})
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 302)
		
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 200)
		
	def testCancelDataRequests(self):
		url = reverse('epic.datarequests.views.cancel_datarequest', kwargs={'item_id':self.dr1.id})
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 302)
		
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 302)
		
	def testFulfillDataReqeusts(self):
		url = reverse('epic.datarequests.views.fulfill_datarequest', kwargs={'item_id':self.dr1.id})
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 302)
		
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(url)
		self.failUnlessEqual(response.status_code, 302)
		
class ViewDatarequestsTestCase(TestCase):
	""" Test the view datarequests page/view """
	
	fixtures = ['just_users', 'datarequests']
	
	def setUp(self):
		self.bob = User.objects.get(username="bob")
		self.admin = User.objects.get(username="admin")
		
		dr1 = DataRequest.objects.get(creator=self.bob, name='drU1', description='The first unfulfilled datarequest', status='U')
	
		self.index_url = reverse('epic.datarequests.views.view_datarequests')
		self.edit_url = reverse('epic.datarequests.views.edit_datarequest', kwargs={'item_id': dr1.id,})
		self.cancel_url = reverse('epic.datarequests.views.cancel_datarequest', kwargs={'item_id': dr1.id,})
		self.fulfill_url = reverse('epic.datarequests.views.fulfill_datarequest', kwargs={'item_id': dr1.id,})
		
	def tearDown(self):
		pass
	
	def testIndexLoggedOut(self):
		response = self.client.get(self.index_url)
		self.assertEqual(response.status_code, 200)
		
		for dr in DataRequest.objects.fulfilled():
			self.assertContains(response, dr.name)
		
		for dr in DataRequest.objects.unfulfilled():
			self.assertContains(response, dr.name)
		
		for dr in DataRequest.objects.canceled():
			self.assertNotContains(response, dr.name)
			
		self.assertNotContains(response, self.edit_url)
		self.assertNotContains(response, self.cancel_url)
		self.assertNotContains(response, self.fulfill_url)
			
	def testIndexUnOwned(self):
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(self.index_url)
		self.assertEqual(response.status_code, 200)
		
		for dr in DataRequest.objects.fulfilled():
			self.assertContains(response, dr.name)
		
		for dr in DataRequest.objects.unfulfilled():
			self.assertContains(response, dr.name)
		
		for dr in DataRequest.objects.canceled():
			self.assertNotContains(response, dr.name)
			
		self.assertNotContains(response, self.edit_url)
		self.assertNotContains(response, self.cancel_url)
		self.assertNotContains(response, self.fulfill_url)
			
	def testIndexOwned(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(self.index_url)
		self.assertEqual(response.status_code, 200)
		
		for dr in DataRequest.objects.fulfilled():
			self.assertContains(response, dr.name)
		
		for dr in DataRequest.objects.unfulfilled():
			self.assertContains(response, dr.name)
		
		for dr in DataRequest.objects.canceled():
			self.assertNotContains(response, dr.name)
			
		self.assertContains(response, self.edit_url)
		self.assertContains(response, self.cancel_url)
		self.assertContains(response, self.fulfill_url)
		
class ViewDatarequestTestCase(TestCase):
	""" Test the view datarequest page/view """
	
	fixtures = ['just_users', 'datarequests']
	
	def setUp(self):
		self.bob = User.objects.get(username="bob")
		self.admin = User.objects.get(username="admin")
		
		dr1 = DataRequest.objects.get(creator=self.bob, name='drU1', description='The first unfulfilled datarequest', status='U')
	
		self.view_url = reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id': dr1.id,})
		self.edit_url = reverse('epic.datarequests.views.edit_datarequest', kwargs={'item_id': dr1.id,})
		self.cancel_url = reverse('epic.datarequests.views.cancel_datarequest', kwargs={'item_id': dr1.id,})
		self.fulfill_url = reverse('epic.datarequests.views.fulfill_datarequest', kwargs={'item_id': dr1.id,})
		
	def tearDown(self):
		pass
	
	def testLoggedOut(self):
		response = self.client.get(self.view_url)
		self.assertEqual(response.status_code, 200)
		
		self.assertNotContains(response, self.edit_url)
		self.assertNotContains(response, self.cancel_url)
		self.assertNotContains(response, self.fulfill_url)
	
	def testLoggedInNotOwner(self):
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(self.view_url)
		self.assertEqual(response.status_code, 200)
		
		self.assertNotContains(response, self.edit_url)
		self.assertNotContains(response, self.cancel_url)
		self.assertNotContains(response, self.fulfill_url)
	
	def testLoggedInOwner(self):
		login = self.client.login(username='bob', password='bob')
		self.failUnless(login, 'Could not login')
		
		response = self.client.get(self.view_url)
		self.assertEqual(response.status_code, 200)
		
		self.assertContains(response, self.edit_url)
		self.assertContains(response, self.cancel_url)
		self.assertContains(response, self.fulfill_url)