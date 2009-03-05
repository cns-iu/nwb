from django.test import TestCase

from epic.datarequests.models import DataRequest

class DataRequestTestCase(TestCase):
	fixtures = ['initial_data', 'single_request']
	
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
