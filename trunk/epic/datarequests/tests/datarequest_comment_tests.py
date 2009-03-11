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
		
		pass
	
	def tearDown(self):
		pass
	
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
		
		self.assertRedirects(post_comment_response, "/datarequests/1/")
		
		self.failUnlessEqual(Comment.objects.all()[0].contents, "abcd")
		
		Comment.objects.all().delete()
	
	def testViewdatarequestWithNoCommentsAndNotLoggedIn(self):
		# Make sure there are no comments.
		Comment.objects.all().delete()
		
		get_datarequest_page_response = self.client.get("/datarequests/1/")
		self.failUnlessEqual(get_datarequest_page_response.status_code, 200, "Error displaying datarequest!")
		
		self.failUnless('Comments:\r\n</h3>\r\n\r\n<table border="1">\r\n\t\r\n</table>' in get_datarequest_page_response.content,
			"The comments should be empty!")
		
		self.failUnless("You must be logged in to comment." in get_datarequest_page_response.content,
			"The user should have to be logged in to even see the post comment form!")
	
	def testViewdatarequestWithACommentAndNotLoggedIn(self):
		# Make sure there are no prior comments.
		Comment.objects.all().delete()
		
		# Directly put a comment on the datarequest.
		self.comment.save()
		
		get_datarequest_page_response = self.client.get("/datarequests/1/")
		self.failUnlessEqual(get_datarequest_page_response.status_code, 200, "Error displaying datarequest!")
		
		# Make sure the posted comment is display.
		self.failUnless("<td>\r\n\t\t\t\tPosted by: peebs\r\n\t\t\t</td>\r\n\t\t\t\r\n\t\t\t<td>\r\n\t\t\t\tabcd\r\n\t\t\t</td>\r\n\t\t</tr>\r\n\t\r\n</table>" in get_datarequest_page_response.content,
			"There should be a single comment, and it should have been posted by peebs!")
		
		self.failUnless("You must be logged in to comment." in get_datarequest_page_response.content,
			"The user should have to be logged in to even see the post comment form!")
		
		# Delete the test comment.
		Comment.objects.all().delete()
	
	def testViewdatarequestWithNoCommentsAndLoggedIn(self):
		# Make sure there are no comments.
		Comment.objects.all().delete()
		
		self.client.login(username="peebs", password="map")
		
		get_datarequest_page_response = self.client.get("/datarequests/1/")
		self.failUnlessEqual(get_datarequest_page_response.status_code, 200, "Error displaying datarequest!")
		
		self.failUnless('Comments:\r\n</h3>\r\n\r\n<table border="1">\r\n\t\r\n</table>' in get_datarequest_page_response.content,
			"The comments should be empty!")
		
		self.failUnless('<form action="/datarequests/1/comment/" method="POST">' in get_datarequest_page_response.content,
			"Logged in users should be able to see the post comment form!")
	
	def testNotLoggedInUserAccessCommentURL(self):
		get_datarequest_comment_page_response = self.client.get("/datarequests/1/comment/")
		self.assertRedirects(get_datarequest_comment_page_response, "/login/?next=/datarequests/1/comment/")
	
	def testLoggedInUserAccessCommentURL(self):
		self.client.login(username="peebs", password="map")
		
		get_datarequest_comment_page_response = self.client.get("/datarequests/1/comment/")
		self.assertRedirects(get_datarequest_comment_page_response, "/datarequests/1/")