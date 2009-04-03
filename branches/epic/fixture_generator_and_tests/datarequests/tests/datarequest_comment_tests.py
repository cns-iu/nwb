from django.test import TestCase

from django.contrib.auth.models import User
from epic.comments.models import Comment
from epic.datarequests.models import DataRequest

class CommentOnDataRequestTestCase(TestCase):
	fixtures = [ "initial_data", "single_request" ]
	
	def setUp(self):
		self.data_request = DataRequest.objects.all()[0]
		
		self. post_comment_form_data = {
			"comment": "abcd"
		}
		
		self.user = User.objects.get(username="peebs")
		self.comment = Comment(posting_user=self.user, parent_item=self.data_request, contents="abcd")
	
	def tearDown(self):
		pass
	
	def testUserViewDataRequestAndNotLoggedIn(self):
		response = self.client.get("/datarequests/1/")
		self.failUnlessEqual(response.status_code, 200, "Error displaying datarequest!")
		
		self.assertContains(response, "You must be logged in to comment.", 1)
	
	def testUserViewDataRequestAndLoggedIn(self):
		# Log the user in.
		self.client.login(username="peebs", password="map")
		
		response = self.client.get("/datarequests/1/")
		self.failUnlessEqual(response.status_code, 200, "Error displaying datarequest!")
		
		self.assertContains(response, '<form action="/datarequests/1/comment/" method="POST">', 1)
	
	def testNotLoggedInUserPostComment(self):
		post_comment_response = self.client.post("/datarequests/1/comment/",
			self.post_comment_form_data)
		
		self.assertRedirects(post_comment_response, "/login/?next=/datarequests/1/comment/")
	
	def testLoggedInUserPostComment(self):
		# Make sure there are no prior comments.
		Comment.objects.all().delete()
		
		# Log a user in so we can post a test comment.
		self.client.login(username="peebs", password="map")
		
		# Post a test comment.
		post_comment_form_data = {
			"comment": "abcd"
		}
		
		post_comment_response = self.client.post("/datarequests/1/comment/",
			post_comment_form_data)
		
		# The posted-to URL should have redirected back here.
		self.assertRedirects(post_comment_response, "/datarequests/1/")
		
		# Verify that the comment is properly in the database.
		self.failUnlessEqual(Comment.objects.all()[0].contents, "abcd")
		
		# Delete the test comment.
		Comment.objects.all().delete()
	
	def testViewDataRequestWithNoComments(self):
		# Make sure there are no comments.
		Comment.objects.all().delete()
		
		response = self.client.get("/datarequests/1/")
		self.failUnlessEqual(response.status_code, 200, "Error displaying datarequest!")
		
		self.assertContains(response, "There are no comments yet.", 1)
	
	def testViewDataRequestWithAComment(self):
		# Make sure there are no prior comments.
		Comment.objects.all().delete()
		
		# Directly put a comment on the datarequest.
		self.comment.save()
		
		response = self.client.get("/datarequests/1/")
		self.failUnlessEqual(response.status_code, 200, "Error displaying datarequest!")
		
		# Make sure the posted comment is display.
		self.assertTrue(self.comment.contents in response.content)
		
		# Delete the test comment.
		Comment.objects.all().delete()
	
	def testNotLoggedInUserAccessCommentURL(self):
		get_datarequest_comment_page_response = self.client.get("/datarequests/1/comment/")
		self.assertRedirects(get_datarequest_comment_page_response, "/login/?next=/datarequests/1/comment/")
	
	def testLoggedInUserAccessCommentURL(self):
		self.client.login(username="peebs", password="map")
		
		get_datarequest_comment_page_response = self.client.get("/datarequests/1/comment/")
		self.assertRedirects(get_datarequest_comment_page_response, "/datarequests/1/")