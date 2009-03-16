from django.test import TestCase

from django.contrib.auth.models import User
from epic.messages.models import Message

class ViewTests(TestCase):
	fixtures = ['messages']
	
	def setUp(self):
		pass
	
	def tearDown(self):
		pass
	
	def testIndex(self):
		# Verify that login is required
		response = self.client.get('/user/messages/')
		self.assertEqual(response.status_code, 302)
		# Log in
		login = self.client.login(username='peebs', password='map')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/messages/')
		self.assertEqual(response.status_code, 200)
		self.assertTrue('<a href="/user/messages/1/">m1</a>' in response.content, response.content)
		self.assertTrue('<a href="/user/messages/2/">m2</a>' in response.content, response.content)
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/messages/')
		self.assertEqual(response.status_code, 200)
		self.assertFalse('<a href="/user/messages/1/">m1</a>' in response.content, response.content)
		self.assertFalse('<a href="/user/messages/2/">m2</a>' in response.content, response.content)
		self.assertFalse('<a href="/user/messages/3/">m3</a>' in response.content, response.content)
	def testSendMessage(self):
		# Verify that login is required
		response = self.client.get('/user/messages/')
		self.assertEqual(response.status_code, 302)
		# Log in
		login = self.client.login(username='peebs', password='map')
		self.failUnless(login, 'Could not login')
		# Goto create new message page
		response = self.client.get('/user/messages/new/1/')
		self.assertEqual(response.status_code, 200)
		self.assertTrue('Send a message to admin' in response.content)
		post_data = {
                     'subject': 'm3',
                     'message': 'This is message number 3',
        }
		response = self.client.post('/user/messages/new/1/', post_data)
		self.failUnlessEqual(response.status_code, 302)
		# Log in as admin and check for the new message
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/messages/')
		self.assertEqual(response.status_code, 200)
		self.assertFalse('<a href="/user/messages/1/">m1</a>' in response.content, response.content)
		self.assertFalse('<a href="/user/messages/2/">m2</a>' in response.content, response.content)
		self.assertTrue('<a href="/user/messages/3/">m3</a>' in response.content, response.content)
	def testViewMessage(self):
		# Create the third message
		Message.objects.create(to_user=User.objects.get(username='peebs'), from_user=User.objects.get(username='bob'), subject="m3", message="This is message number three.")
		# Make sure that you must be logged in to view.
		response = self.client.get('/user/messages/')
		self.assertEqual(response.status_code, 302)
		# Make sure that you can see allowed messages if you are logged in
		login = self.client.login(username='peebs', password='map')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/messages/3/')
		self.failUnlessEqual(response.status_code, 200)
		self.assertTrue('This is message number three.' in response.content)
		# Verify that someone who did not send or receive the message can't see it
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/messages/3/')
		#TODO: when using upgraded django, follow this redirect and verify you are sent to /user/messages/
		self.failUnlessEqual(response.status_code, 302)
		# Make object four
		Message.objects.create(to_user=User.objects.get(username='peebs'), from_user=User.objects.get(username='admin'), subject="m4", message="This is message number four.")
		# Verify that both the sender and the receiver can view the message.
		login = self.client.login(username='admin', password='admin')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/messages/4/')
		self.assertTrue('This is message number four.' in response.content)
		login = self.client.login(username='peebs', password='map')
		self.failUnless(login, 'Could not login')
		response = self.client.get('/user/messages/4/')
		self.assertTrue('This is message number four.' in response.content)
	def testMessageSent(self):
		# Check that the page exists.  Beyond that, who cares?
		response = self.client.get('/user/messages/message_sent/')
		self.assertEqual(response.status_code, 200)