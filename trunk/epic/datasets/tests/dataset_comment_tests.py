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
	
	def tearDown(self):
		pass
	
	def testUserViewDataSetAndNotLoggedIn(self):
		get_dataset_page_response = self.client.get("/datasets/1/")
		self.failUnlessEqual(get_dataset_page_response.status_code, 200, "Error displaying dataset!")
		
		self.assertContains(get_dataset_page_response, "You must be logged in to comment.", 1)
	
	def testUserViewDataSetAndLoggedIn(self):
		# Log the user in.
		self.client.login(username="peebs", password="map")
		
		get_dataset_page_response = self.client.get("/datasets/1/")
		self.failUnlessEqual(get_dataset_page_response.status_code, 200, "Error displaying dataset!")
		
		self.assertContains(get_dataset_page_response, '<form action="/datasets/1/comment/" method="POST">', 1)
	
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
		
		# The posted-to URL should have redirected back here.
		self.assertRedirects(post_comment_response, "/datasets/1/")
		
		# Verify that the comment is properly in the database.
		self.failUnlessEqual(Comment.objects.all()[0].contents, "abcd")
		
		# Delete the test comment.
		Comment.objects.all().delete()
	
	def testViewDataSetWithNoComments(self):
		# Make sure there are no comments.
		Comment.objects.all().delete()
		
		get_dataset_page_response = self.client.get("/datasets/1/")
		self.failUnlessEqual(get_dataset_page_response.status_code, 200, "Error displaying dataset!")
		
		self.assertContains(get_dataset_page_response, "There are no comments yet.", 1)
	
	def testViewDataSetWithAComment(self):
		# Make sure there are no prior comments.
		Comment.objects.all().delete()
		
		# Directly put a comment on the dataset.
		self.comment.save()
		
		get_dataset_page_response = self.client.get("/datasets/1/")
		self.failUnlessEqual(get_dataset_page_response.status_code, 200, "Error displaying dataset!")
		
		# Make sure the posted comment is display.
		self.assertContains(get_dataset_page_response, "Posted by:", 1)
		self.assertContains(get_dataset_page_response, "Posted by: peebs", 1)
		self.assertContains(get_dataset_page_response, "abcd", 1)
		
		# Delete the test comment.
		Comment.objects.all().delete()
	
	def testNotLoggedInUserAccessCommentURL(self):
		get_dataset_comment_page_response = self.client.get("/datasets/1/comment/")
		self.assertRedirects(get_dataset_comment_page_response, "/login/?next=/datasets/1/comment/")
	
	def testLoggedInUserAccessCommentURL(self):
		self.client.login(username="peebs", password="map")
		
		get_dataset_comment_page_response = self.client.get("/datasets/1/comment/")
		self.assertRedirects(get_dataset_comment_page_response, "/datasets/1/")