from django.test import TestCase

from django.contrib.auth.models import User
from epic.comments.models import Comment
from epic.datasets.models import DataSet

class CommentOnDataSetTestCase(TestCase):
	fixtures = [ "initial_data", "single_dataset" ]
	
	def setUp(self):
		self.data_set = DataSet.objects.all()[0]
		
		self. post_comment_form_data = {
			"comment": "abcd"
		}
		
		self.user = User.objects.get(username="peebs")
		self.comment = Comment(posting_user=self.user, parent_item=self.data_set, contents="abcd")
		
		pass
	
	def tearDown(self):
		pass
	
	def testNotLoggedInUserPostComment(self):
		post_comment_response = self.client.post("/datasets/1/comment/",
			self.post_comment_form_data)
		
		self.assertRedirects(post_comment_response, "/login/?next=/datasets/1/comment/")
	
	def testLoggedInUserPostComment(self):
		# Make sure there are no prior comments.
		Comment.objects.all().delete()
		
		# Log a user in so we can post a test comment.
		self.client.login(username="peebs", password="map")
		
		# Post a test comment.
		post_comment_form_data = {
			"comment": "abcd"
		}
		
		post_comment_response = self.client.post("/datasets/1/comment/",
			post_comment_form_data)
		
		self.assertRedirects(post_comment_response, "/datasets/1/")
		
		self.failUnlessEqual(Comment.objects.all()[0].contents, "abcd")
		
		Comment.objects.all().delete()
	
	def testViewDataSetWithNoCommentsAndNotLoggedIn(self):
		# Make sure there are no comments.
		Comment.objects.all().delete()
		
		get_dataset_page_response = self.client.get("/datasets/1/")
		self.failUnlessEqual(get_dataset_page_response.status_code, 200, "Error displaying dataset!")
		
		self.failUnless('Comments:\r\n</h3>\r\n\r\n<table border="1">\r\n\t\r\n</table>' in get_dataset_page_response.content,
			"The comments should be empty!")
		
		self.failUnless("You must be logged in to comment." in get_dataset_page_response.content,
			"The user should have to be logged in to even see the post comment form!")
	
	def testViewDataSetWithACommentAndNotLoggedIn(self):
		# Make sure there are no prior comments.
		Comment.objects.all().delete()
		
		# Directly put a comment on the dataset.
		self.comment.save()
		
		get_dataset_page_response = self.client.get("/datasets/1/")
		self.failUnlessEqual(get_dataset_page_response.status_code, 200, "Error displaying dataset!")
		
		# Make sure the posted comment is display.
		self.failUnless("<td>\r\n\t\t\t\tPosted by: peebs\r\n\t\t\t</td>\r\n\t\t\t\r\n\t\t\t<td>\r\n\t\t\t\tabcd\r\n\t\t\t</td>\r\n\t\t</tr>\r\n\t\r\n</table>" in get_dataset_page_response.content,
			"There should be a single comment, and it should have been posted by peebs!")
		
		self.failUnless("You must be logged in to comment." in get_dataset_page_response.content,
			"The user should have to be logged in to even see the post comment form!")
		
		# Delete the test comment.
		Comment.objects.all().delete()
	
	def testViewDataSetWithNoCommentsAndLoggedIn(self):
		# Make sure there are no comments.
		Comment.objects.all().delete()
		
		self.client.login(username="peebs", password="map")
		
		get_dataset_page_response = self.client.get("/datasets/1/")
		self.failUnlessEqual(get_dataset_page_response.status_code, 200, "Error displaying dataset!")
		
		self.failUnless('Comments:\r\n</h3>\r\n\r\n<table border="1">\r\n\t\r\n</table>' in get_dataset_page_response.content,
			"The comments should be empty!")
		
		self.failUnless('<form action="/datasets/1/comment/" method="POST">' in get_dataset_page_response.content,
			"Logged in users should be able to see the post comment form!")
	
	def testNotLoggedInUserAccessCommentURL(self):
		get_dataset_comment_page_response = self.client.get("/datasets/1/comment/")
		self.assertRedirects(get_dataset_comment_page_response, "/login/?next=/datasets/1/comment/")
	
	def testLoggedInUserAccessCommentURL(self):
		self.client.login(username="peebs", password="map")
		
		get_dataset_comment_page_response = self.client.get("/datasets/1/comment/")
		self.assertRedirects(get_dataset_comment_page_response, "/datasets/1/")