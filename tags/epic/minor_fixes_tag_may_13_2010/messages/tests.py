from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.messages.models import ReceivedMessage, SentMessage


def commonMessageSetUp(test_case):
	# Get the users from the fixtures
	test_case.user_bob = User.objects.get(username='bob')
	test_case.user_bob2 = User.objects.get(username='bob2')
	test_case.user_admin = User.objects.get(username='admin')
	
	# Get the messages from the fixtures
	test_case.first_received_message = ReceivedMessage.objects.get(recipient=test_case.user_bob, sender=test_case.user_admin, subject="m1r", message="this is the first received message", read=False, replied=False, deleted=False)
	test_case.first_sent_message = SentMessage.objects.get(recipient=test_case.user_bob, sender=test_case.user_admin, subject="m1s", message="this is the first sent message", deleted=False)
	test_case.second_received_message = ReceivedMessage.objects.get(recipient=test_case.user_admin, sender=test_case.user_admin, subject="m2r", message="this is the second received message", read=False, replied=False, deleted=False)
	test_case.second_sent_message = SentMessage.objects.get(recipient=test_case.user_admin, sender=test_case.user_admin, subject="m2s", message="this is the second sent message", deleted=False)
	

class UrlsTestCase(CustomTestCase):
	""" Test all the urls to make sure that the view for each works """
	
	fixtures = ['messages_just_users', 'messages_datasets', 'messages_messages']
	
	def setUp(self):
		commonMessageSetUp(self)
		
	def tearDown(self):
		pass
	
	def test_index(self):
		self.assertResponseStatusNotFailure('epic.messages.views.index', 
										kwargs={'user_id':self.user_bob.id,})
	
	def test_view_sent_message(self):
		self.assertResponseStatusNotFailure('epic.messages.views.view_sent_message', 
										kwargs={'user_id':self.user_bob.id,'sentmessage_id':self.first_sent_message.id,})

	def test_view_received_message(self):
		self.assertResponseStatusNotFailure('epic.messages.views.view_received_message', 
										kwargs={'user_id':self.user_bob.id,'receivedmessage_id':self.first_received_message.id,})
			
	def test_send_message(self):
		self.assertResponseStatusNotFailure('epic.messages.views.send_message', 
										kwargs={'user_id':self.user_bob.id,'in_reply_to_message_id':self.first_received_message.id,})
		self.assertResponseStatusNotFailure('epic.messages.views.send_message', 
										kwargs={'user_id':self.user_bob.id,})
		self.assertResponseStatusNotFailure('epic.messages.views.send_message', 
										kwargs={'user_id':self.user_bob.id,'recipient_id':self.first_received_message.recipient.id,})
		
	
class IndexTestCase(CustomTestCase):
	""" Test the view 'index' """
	
	fixtures = ['messages_just_users', 'messages_messages']
	
	def setUp(self):
		commonMessageSetUp(self)
		
	def tearDown(self):
		pass
	
	def testLoggedOut(self):
		self.assertResponseStatus([302], 'epic.messages.views.index', kwargs={'user_id':self.user_bob.id,},)

	def testLoggedInReceipient(self):
		self.tryLogin('bob')
		bob_response = self.getResponseFromView('epic.messages.views.index', kwargs={'user_id':self.user_bob.id,})

		# Bob should only see the first received message, but not the first sent message nor any part of the second message
		self.assertContains(bob_response, self.first_received_message.subject)
		self.assertNotContains(bob_response, self.first_sent_message.subject)
		self.assertNotContains(bob_response, self.second_received_message.subject)
		self.assertNotContains(bob_response, self.second_sent_message.subject)
		
	def testLoggedInSender(self):
		self.tryLogin('admin')
		admin_response = self.getResponseFromView('epic.messages.views.index', kwargs={'user_id':self.user_admin.id,})
		
		# Admin should not see the first received message, but should have everything else
		self.assertNotContains(admin_response, self.first_received_message.subject)
		self.assertContains(admin_response, self.first_sent_message.subject)
		self.assertContains(admin_response, self.second_received_message.subject)
		self.assertContains(admin_response, self.second_sent_message.subject)
		
	def testLoggedInOther(self):
		self.tryLogin('bob2')
		bob2_response = self.getResponseFromView('epic.messages.views.index', kwargs={'user_id':self.user_bob2.id,})

		# Bob2 should not see any messages
		self.assertNotContains(bob2_response, self.first_received_message.subject)
		self.assertNotContains(bob2_response, self.first_sent_message.subject)
		self.assertNotContains(bob2_response, self.second_received_message.subject)
		self.assertNotContains(bob2_response, self.second_sent_message.subject)
		
class ViewSentMessageTestCase(CustomTestCase):
	""" Test the view 'view_sent_message' """
	
	fixtures = ['messages_just_users', 'messages_messages']
	
	def setUp(self):
		commonMessageSetUp(self)
		
	def tearDown(self):
		pass
	
	def testLoggedOut(self):
		self.assertResponseStatus([302],
								'epic.messages.views.view_sent_message',
								kwargs={'user_id':self.user_bob.id,'sentmessage_id':self.first_sent_message.id,},
								)

	def testLoggedInRecipient(self):
		self.tryLogin('bob')
		bob_response = self.getResponseFromView('epic.messages.views.view_sent_message', kwargs={'user_id':self.user_bob.id,'sentmessage_id':self.first_sent_message.id,})

		# Bob should see the first sent message because he received it
		self.assertContains(bob_response, self.first_sent_message.subject)
	
	def testLoggedInSender(self):	
		self.tryLogin('admin')
		admin_response = self.getResponseFromView('epic.messages.views.view_sent_message', kwargs={'user_id':self.user_admin.id,'sentmessage_id':self.first_sent_message.id,})

		# Admin should see the first sent message because he sent it
		self.assertContains(admin_response, self.first_sent_message.subject)
		
	def testLoggedInOther(self):
		self.tryLogin('bob2')
		bob2_response = self.getResponseFromView('epic.messages.views.view_sent_message', kwargs={'user_id':self.user_bob2.id,'sentmessage_id':self.first_sent_message.id,})
		
		# Bob2 should not see the first sent message.  Instead he should be directed to his message index page
		bob2_redirect_url = reverse('epic.messages.views.index', kwargs={'user_id':self.user_bob2.id,})
		self.assertRedirects(bob2_response, bob2_redirect_url, 302)

class ViewReceivedMessageTestCase(CustomTestCase):
	""" Test the view view_received_message for messages """
	
	fixtures = ['messages_just_users', 'messages_messages']
	
	def setUp(self):
		commonMessageSetUp(self)
		
	def tearDown(self):
		pass
	
	def testLoggedOut(self):
		self.assertResponseStatus([302], 'epic.messages.views.view_received_message', kwargs={'user_id':self.user_bob.id,'receivedmessage_id':self.first_received_message.id,})
	
	def testLoggedInRecipient(self):
		self.tryLogin('bob')
		bob_response = self.getResponseFromView('epic.messages.views.view_received_message', kwargs={'user_id':self.user_bob.id,'receivedmessage_id':self.first_received_message.id,})
		
		# Bob should see the message because he is the recipient
		self.assertContains(bob_response, self.first_received_message.subject)
	
	def testLoggedInSender(self):
		self.tryLogin('admin')
		admin_response = self.getResponseFromView('epic.messages.views.view_received_message', kwargs={'user_id':self.user_admin.id,'receivedmessage_id':self.first_received_message.id,})
		
		# Admin should see the message because he is the sender
		self.assertContains(admin_response, self.first_received_message.subject)
		
	def testLoggedInOther(self):
		self.tryLogin('bob2')
		bob2_response = self.getResponseFromView('epic.messages.views.view_received_message', kwargs={'user_id':self.user_bob2.id,'receivedmessage_id':self.first_received_message.id,})
		
		# Bob2 should not see the first sent message.  Instead he should be directed to his message index page
		bob2_redirect_url = reverse('epic.messages.views.index', kwargs={'user_id':self.user_bob2.id,})
		self.assertRedirects(bob2_response, bob2_redirect_url, 302)

class SendMessageTestCase(CustomTestCase):
	""" Test the view send_message for messages """
	
	fixtures = ['messages_just_users', 'messages_messages']
	
	def setUp(self):
		commonMessageSetUp(self)
		
	def tearDown(self):
		pass
	
	def testLoggedOutSend(self):
		sender = self.user_bob
		recipient = self.user_admin
		send_url = reverse('epic.messages.views.send_message', kwargs={'user_id':sender.id,})		
		valid_post_data = {
					 'recipient': recipient.username,
                     'subject': 'm3',
                     'message': 'This is message number 3',
        }
		
		get_response = self.client.get(send_url)
		self.assertEqual(get_response.status_code, 302)
		
		post_response = self.client.post(send_url, valid_post_data)

		
		# Verify that no changes to the database were made
		
		for sent_message in SentMessage.objects.all():
			self.assertFalse(
							  (sent_message.sender == sender) and
							  (sent_message.recipient == recipient) and
							  (sent_message.subject == valid_post_data['subject']) and
							  (sent_message.message == valid_post_data['message'])
							)
			
		for received_message in ReceivedMessage.objects.all():
			self.assertFalse(    
							  (received_message.sender == sender) and
							  (received_message.recipient == recipient) and
							  (received_message.subject == valid_post_data['subject']) and
							  (received_message.message == valid_post_data['message'])
							)
	
	def testLoggedInSendBlank(self):
		sender = self.user_bob
		recipient = self.user_admin
		send_url = reverse('epic.messages.views.send_message', kwargs={'user_id':sender.id,})
		blank_post_data = {
					 'recipient': '',
                     'subject': '',
                     'message': '',
        }
	
		self.tryLogin('bob')

		get_response = self.client.get(send_url)
		self.assertEqual(get_response.status_code, 200)

		post_response = self.client.post(send_url, blank_post_data)
		self.failUnlessEqual(post_response.status_code, 200)
		
		self.assertFormError(post_response, 'form', 'recipient', 'This field is required.')
		self.assertFormError(post_response, 'form', 'subject', 'This field is required.')
		self.assertFormError(post_response, 'form', 'message', 'This field is required.')
		
	def testLoggedInSendInvalidUser(self):
		#===============================================================================
		#		Test the 'send_message' view without suppling a recipient ID but instead using an invalid username in the form
		#===============================================================================

		sender = self.user_bob
		recipient = self.user_admin
		send_url = reverse('epic.messages.views.send_message', kwargs={'user_id':sender.id,})
		invalid_user_post_data = {
					 'recipient': 'asdf 43qwt sdg ds',
                     'subject': 'm3',
                     'message': 'This is message number 3',
        }
		
		self.tryLogin('bob')

		get_response = self.client.get(send_url)
		self.assertEqual(get_response.status_code, 200)

		post_response = self.client.post(send_url, invalid_user_post_data)
		self.failUnlessEqual(post_response.status_code, 200)
		
		self.assertFormError(post_response, 'form', 'recipient', '%(recipient_username)s is not a valid username.' % ({'recipient_username':invalid_user_post_data['recipient'],}))

	def testLoggedInSendValid(self):
		sender = self.user_bob
		recipient = self.user_admin
		send_url = reverse('epic.messages.views.send_message', kwargs={'user_id':sender.id,})
		valid_post_data = {
					 'recipient': recipient.username,
                     'subject': 'm3',
                     'message': 'This is message number 3',
        }

		self.tryLogin('bob')

		get_response = self.client.get(send_url)
		self.assertEqual(get_response.status_code, 200)

		post_response = self.client.post(send_url, valid_post_data)
		self.failUnlessEqual(post_response.status_code, 302)
		
		# Verify the changes to the database
		SentMessage.objects.get(sender=sender, recipient=recipient, subject=valid_post_data['subject'], message=valid_post_data['message'])
		ReceivedMessage.objects.get(sender=sender, recipient=recipient, subject=valid_post_data['subject'], message=valid_post_data['message'])
	
	def testLoggedOutSentTo(self):
		#===============================================================================
		#		Test the 'send_message' view supplying a recipient id as a user who is not logged in.
		#===============================================================================
		
		sender = self.user_admin
		recipient = self.first_received_message.recipient
		send_to_url = reverse('epic.messages.views.send_message', kwargs={'user_id':self.user_admin.id,'recipient_id':recipient.id,})
		valid_post_data = {
                     'recipient': recipient.username,
                     'subject': 'm3',
                     'message': 'This is message number 3',
        }
		
		get_response = self.client.get(send_to_url)
		self.assertEqual(get_response.status_code, 302)
		
		post_response = self.client.post(send_to_url, valid_post_data)
		self.assertEqual(post_response.status_code, 302)

		# Verify that no changes to the database have been made
		
		for sent_message in SentMessage.objects.all():
			self.assertFalse(
							  (sent_message.sender == sender) and
							  (sent_message.recipient == recipient) and
							  (sent_message.subject == valid_post_data['subject']) and
							  (sent_message.message == valid_post_data['message'])
							)
			
		for received_message in ReceivedMessage.objects.all():
			self.assertFalse(    
							  (received_message.sender == sender) and
							  (received_message.recipient == recipient) and
							  (received_message.subject == valid_post_data['subject']) and
							  (received_message.message == valid_post_data['message'])
							)
		
	def testLoggedInSentToBlank(self):
		sender = self.user_admin
		recipient = self.first_received_message.recipient
		send_to_url = reverse('epic.messages.views.send_message', kwargs={'user_id':self.user_admin.id,'recipient_id':recipient.id,})
		blank_post_data = {
				     'recipient': '',
                     'subject': '',
                     'message': '',
        }
		
		self.tryLogin('admin')
		
		get_response = self.client.get(send_to_url)
		self.assertEqual(get_response.status_code, 200)
		self.assertContains(get_response, recipient.username)
		
		post_response = self.client.post(send_to_url, blank_post_data)
		self.failUnlessEqual(post_response.status_code, 200)
		
		self.assertFormError(post_response, 'form', 'recipient', 'This field is required.')
		self.assertFormError(post_response, 'form', 'subject', 'This field is required.')
		self.assertFormError(post_response, 'form', 'message', 'This field is required.')
		
		
	def testLoggedInSentToValid(self):
		sender = self.user_admin
		recipient = self.first_received_message.recipient
		send_to_url = reverse('epic.messages.views.send_message', kwargs={'user_id':self.user_admin.id,'recipient_id':recipient.id,})
		valid_post_data = {
                     'recipient': recipient.username,
                     'subject': 'm3',
                     'message': 'This is message number 3',
        }
		
		self.tryLogin('admin')

		get_response = self.client.get(send_to_url)
		self.assertEqual(get_response.status_code, 200)
		self.assertContains(get_response, recipient.username)

		post_response = self.client.post(send_to_url, valid_post_data)
		self.assertEqual(post_response.status_code, 302)
 
		# Verify changes to the database
		SentMessage.objects.get(sender=sender, recipient=recipient, subject=valid_post_data['subject'], message=valid_post_data['message'])
		ReceivedMessage.objects.get(sender=sender, recipient=recipient, subject=valid_post_data['subject'], message=valid_post_data['message'])
	
	def testLoggedOutReply(self):
		sender = self.first_received_message.recipient
		recipient = self.first_received_message.sender
		reply_url = reverse('epic.messages.views.send_message', kwargs={'user_id':sender.id,'in_reply_to_message_id':self.first_received_message.id,})
		valid_post_data = {
        	'recipient': recipient.username,
            'subject': 'm3',
            'message': 'This is message number 3',
        }
		
		get_response = self.client.get(reply_url)
		self.assertEqual(get_response.status_code, 302)
		
		post_response = self.client.post(reply_url, valid_post_data)
		self.assertEqual(post_response.status_code, 302)
		
		# Verify that no changes were made to the database
		
		for sent_message in SentMessage.objects.all():
			self.assertFalse(
							  (sent_message.sender == sender) and
							  (sent_message.recipient == recipient) and
							  (sent_message.subject, valid_post_data['subject']) and
							  (sent_message.message, valid_post_data['message'])
							)
			
		for received_message in ReceivedMessage.objects.all():
			self.assertFalse(    
							  (received_message.sender == sender) and
							  (received_message.recipient == recipient) and
							  (received_message.subject, valid_post_data['subject']) and
							  (received_message.message, valid_post_data['message'])
							)
			
	def testLoggedInReply(self):
		sender = self.first_received_message.recipient
		recipient = self.first_received_message.sender
		reply_url = reverse('epic.messages.views.send_message', kwargs={'user_id':sender.id,'in_reply_to_message_id':self.first_received_message.id,})
		valid_post_data = {
        	'recipient': recipient.username,
            'subject': 'm3',
            'message': 'This is message number 3',
        }
		
		self.tryLogin(sender.username)
		
		get_response = self.client.get(reply_url)
		self.assertEqual(get_response.status_code, 200)
		self.assertContains(get_response, 'RE:' + self.first_received_message.subject)

		post_response = self.client.post(reply_url, valid_post_data)
		self.failUnlessEqual(post_response.status_code, 302)
 
		# Check for database changes
		SentMessage.objects.get(sender=sender, recipient=recipient, subject=valid_post_data['subject'], message=valid_post_data['message'])
		ReceivedMessage.objects.get(sender=sender, recipient=recipient, subject=valid_post_data['subject'], message=valid_post_data['message'])

from epic.datasets.models import DataSet
class ContactUserLinkDataSetTestCase(CustomTestCase):
	
	fixtures = ['messages_just_users','messages_datasets', 'messages_messages']
	
	def setUp(self):
		commonMessageSetUp(self)
		self.first_data_set = DataSet.objects.get(creator=self.user_bob, 
                                                  name='dataset1', 
                                                  description='this is the first dataset')
 
	
	def tearDown(self):
		pass
	
	def testViewDataSetsLoggedOut(self):
		sender = self.user_admin
		recipient = self.first_data_set.creator
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datasets_url = reverse('epic.datasets.views.view_datasets')
		
		get_response = self.client.get(view_datasets_url)
		self.assertNotContains(get_response, contact_user_url)
	
	def testViewDataSetsLoggedInNotOwner(self):
		sender = self.user_admin
		recipient = self.first_data_set.creator
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datasets_url = reverse('epic.datasets.views.view_datasets')
		
		self.tryLogin(sender.username)
		
		get_response = self.client.get(view_datasets_url)
		self.assertContains(get_response, contact_user_url)
	
	def testViewDataSetsLoggedInOwner(self):
		sender = self.first_data_set.creator
		recipient = self.first_data_set.creator
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datasets_url = reverse('epic.datasets.views.view_datasets')
		
		self.tryLogin(sender.username)
		
		get_response = self.client.get(view_datasets_url)
		self.assertNotContains(get_response, contact_user_url)
	
	def testDataSetLoggedOut(self):
		sender = self.first_data_set.creator
		recipient = self.first_data_set.creator
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_dataset_url = reverse('epic.datasets.views.view_dataset', kwargs={'item_id':self.first_data_set.id, 'slug':self.first_data_set.slug,})
		
		get_response = self.client.get(view_dataset_url)
		self.assertNotContains(get_response, contact_user_url)
	
	def testDataSetLoggedInNotOwner(self):
		sender = self.user_admin
		recipient = self.first_data_set.creator
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_dataset_url = reverse('epic.datasets.views.view_dataset', kwargs={'item_id':self.first_data_set.id, 'slug':self.first_data_set.slug,})
				
		self.tryLogin(sender.username)
		
		get_response = self.client.get(view_dataset_url)
		self.assertContains(get_response, contact_user_url)
		
	def testDataSetLoggedInOwner(self):
		sender = self.first_data_set.creator
		recipient = self.first_data_set.creator
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_dataset_url = reverse('epic.datasets.views.view_dataset', kwargs={'item_id':self.first_data_set.id, 'slug':self.first_data_set.slug,})
				
		self.tryLogin(sender.username)
		
		get_response = self.client.get(view_dataset_url)
		self.assertNotContains(get_response, contact_user_url)
 
from epic.datarequests.models import DataRequest
class ContactUserLinkDataRequestTestCase(CustomTestCase):
	
	fixtures = ['messages_just_users','messages_datarequests', 'messages_messages']
	
	def setUp(self):
		commonMessageSetUp(self)
		self.first_data_request = DataRequest.objects.get(creator=self.user_bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U')
	
	def tearDown(self):
		pass
	
	def testViewDataRequestsLoggedOut(self):
		sender = self.user_admin
		recipient = self.first_data_request.creator
		
		contact_user_url = reverse("epic.messages.views.send_message", kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datarequests_url = reverse('epic.datarequests.views.view_datarequests')
		
		response = self.client.get(view_datarequests_url)
		self.assertNotContains(response, contact_user_url)
	
	def testViewDataRequestsLoggedInNotOwner(self):
		sender = self.user_admin
		recipient = self.first_data_request.creator
		
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datarequests_url = reverse('epic.datarequests.views.view_datarequests')
		
		self.tryLogin(sender.username)
		
		response = self.client.get(view_datarequests_url)
		self.assertContains(response, contact_user_url)
	
	def testViewDataRequestsLoggedInOwner(self):
		sender = self.first_data_request.creator
		recipient = self.first_data_request.creator
		
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datarequests_url = reverse('epic.datarequests.views.view_datarequests')
		
		self.tryLogin(sender.username)
		
		response = self.client.get(view_datarequests_url)
		self.assertNotContains(response, contact_user_url)
	
	def testViewDataRequestLoggedOut(self):
		sender = self.first_data_request.creator
		recipient = self.first_data_request.creator
		
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datarequest_url = reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':self.first_data_request.id, 'slug':self.first_data_request.slug,})

		response = self.client.get(view_datarequest_url)
		self.assertNotContains(response, contact_user_url)
	
	def testViewDataRequestLoggedInNotOwner(self):
		sender = self.user_admin
		recipient = self.first_data_request.creator
		
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datarequest_url = reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':self.first_data_request.id, 'slug':self.first_data_request.slug,})
				
		self.tryLogin(sender.username)
		
		response = self.client.get(view_datarequest_url)
		self.assertContains(response, contact_user_url)
	
	def testViewDataRequestLoggedInOwner(self):
		sender = self.first_data_request.creator
		recipient = self.first_data_request.creator
		
		contact_user_url = reverse("epic.messages.views.send_message", args=[], kwargs={'user_id':sender.id, 'recipient_id':recipient.id})
		view_datarequest_url = reverse('epic.datarequests.views.view_datarequest', kwargs={'item_id':self.first_data_request.id, 'slug':self.first_data_request.slug,})
				
		self.tryLogin(sender.username)
		
		response = self.client.get(view_datarequest_url)
		self.assertNotContains(response, contact_user_url)