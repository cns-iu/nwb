from django.test import TestCase
from django.core.urlresolvers import reverse
from django.contrib.auth.models import User
from epic.comments.models import Comment
from epic.datarequests.models import DataRequest

def create_comment_test_case(_setUp, _fixtures):
	class CommentTestCase(TestCase):
		fixtures = _fixtures
		
		def setUp(self):
			_setUp(self)
			
			self.LOGIN_URL = reverse("django.contrib.auth.views.login")
			
			self.LOGIN_REDIRECT_URL = "%(base_url)s?next=%(next_url)s" % {
				"base_url": self.LOGIN_URL,
				"next_url": self.POST_TO_COMMENT_URL
			}
		
		def tearDown(self):
			pass
		
		def testViewNotLoggedIn(self):
			response = self.client.get(self.VIEW_URL)
			
			self.failUnlessEqual(response.status_code,
								 200,
								 "Error displaying %s!" % self.ITEM_TYPE_STRING)
			
			self.assertContains(response, "You must be logged in to comment.", 1)
		
		def testViewLoggedIn(self):
			# Log in.
			self.client.login(username="peebs", password="map")
			
			# Go to the comment page.
			response = self.client.get(self.VIEW_URL)
			
			# Fail if the page is not there.
			self.failUnlessEqual(response.status_code,
								 200,
								 "Error displaying %s!" % self.ITEM_TYPE_STRING)
			
			# Fail if there's no form on the page.
			self.assertContains(response,
								'<form action="%s" method="POST">' % self.POST_TO_COMMENT_URL,
								1)
		
		def testPostCommentNotLoggedIn(self):
			response = self.client.post(self.POST_TO_COMMENT_URL,
										self.post_comment_form_data)
			
			self.assertRedirects(response, self.LOGIN_REDIRECT_URL)
		
		def testPostCommentLoggedIn(self):
			# Make sure there are no prior comments.
			Comment.objects.all().delete()
			
			# Log in.
			self.client.login(username="peebs", password="map")
			
			# Post a test comment.
			response = self.client.post(self.POST_TO_COMMENT_URL,
										self.post_comment_form_data)
			
			# The posted-to URL should have redirected back to the view page.
			self.assertRedirects(response, self.VIEW_URL)
			
			# Verify that the comment is properly in the database.
			self.failUnlessEqual(Comment.objects.all()[0].contents,
								 self.post_comment_form_data["comment"])
		
		def testViewWithNoComments(self):
			# Make sure there are no comments.
			Comment.objects.all().delete()
			
			response = self.client.get(self.VIEW_URL)
			
			self.failUnlessEqual(response.status_code,
								 200,
								 "Error displaying %s!" % self.ITEM_TYPE_STRING)
			
			self.assertContains(response, "There are no comments yet.", 1)
		
		def testViewWithAComment(self):
			# Make sure there are no prior comments.
			Comment.objects.all().delete()
			
			# Directly put a comment on the datarequest.
			self.comment.save()
			
			#Check if we are able to go to the page we're commenting to.
			
			response = self.client.get(self.VIEW_URL)
			self.failUnlessEqual(response.status_code,
								 200,
								 "Error displaying %s!" % self.ITEM_TYPE_STRING)
			
			# Make sure the posted comment is displayed
			self.assertTrue(self.comment.contents in response.content)
			
			# Delete the test comment.
			Comment.objects.all().delete()
		
		def testAccessPostCommentURLNotLoggedIn(self):
			response = self.client.get(self.POST_TO_COMMENT_URL)
			
			self.assertRedirects(response, self.LOGIN_REDIRECT_URL)
		
		def testAccessPostCommentURLLoggedIn(self):
			self.client.login(username="peebs", password="map")
			
			response = self.client.get(self.POST_TO_COMMENT_URL)
			self.assertRedirects(response, self.VIEW_URL)
		
		def testPostBlankComment(self):
			# This will only happen if the user is logged in, hence we only
			# need this one version of this test.
			
			self.client.login(username="peebs", password="map")
			
			blank_comment_form_data = { "comment": "" }
			response = self.client.post(self.POST_TO_COMMENT_URL,
										blank_comment_form_data)
			
			self.assertFormError(response,
								 "form",
								 "comment",
								 "This field is required.")
	
	return CommentTestCase
