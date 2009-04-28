from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.test import TestCase

from epic.comments.models import Comment
from epic.datarequests.models import DataRequest


COMMENT_KEY = 'comment'
FORM_KEY = 'form'
ITEM_ID_KEY = 'item_id'
SLUG_KEY = 'slug'

TEST_USER_USERNAME = 'peebs'
TEST_USER_PASSWORD = 'map'

def create_comment_test_case(_setUp, _fixtures):
    class CommentTestCase(TestCase):
        fixtures = _fixtures
        
        def setUp(self):
            _setUp(self)
            
            self.LOGIN_URL = reverse('django.contrib.auth.views.login')
            
            self.LOGIN_REDIRECT_URL = '%(base_url)s?next=%(next_url)s' % {
                'base_url': self.LOGIN_URL,
                'next_url': self.POST_TO_COMMENT_URL
            }
            
            self.ERROR_DISPLAYING_ITEM_TYPE = 'Error displaying %s!' % \
                self.ITEM_TYPE_STRING
        
        def tearDown(self):
            pass
        
        def testViewNotLoggedIn(self):
            response = self.client.get(self.VIEW_URL)
            
            self.failUnlessEqual(response.status_code,
                                 200,
                                 self.ERROR_DISPLAYING_ITEM_TYPE)
            
            self.assertContains(response,
                                'You must be logged in to comment.',
                                1)
        
        def testViewLoggedIn(self):
            self.client.login(username=TEST_USER_USERNAME,
                              password=TEST_USER_PASSWORD)
            
            # Go to the comment page.
            response = self.client.get(self.VIEW_URL)
            
            # Fail if the page is not there.
            self.failUnlessEqual(response.status_code,
                                 200,
                                 self.ERROR_DISPLAYING_ITEM_TYPE)
            
            # Fail if there's no form on the page.
            self.assertContains(response,
                                '<form action="%s" method="POST">' % \
                                    self.POST_TO_COMMENT_URL,
                                1)
        
        def testPostCommentNotLoggedIn(self):
            response = self.client.post(self.POST_TO_COMMENT_URL,
                                        self.post_comment_form_data)
            
            self.assertRedirects(response, self.LOGIN_REDIRECT_URL)
        
        def testPostCommentLoggedIn(self):
            # Make sure there are no prior comments.
            Comment.objects.all().delete()
            
            self.client.login(username=TEST_USER_USERNAME,
                              password=TEST_USER_PASSWORD)
            
            # Post a test comment.
            response = self.client.post(self.POST_TO_COMMENT_URL,
                                        self.post_comment_form_data)
            
            # The posted-to URL should have redirected back to the view page.
            self.assertRedirects(response, self.VIEW_URL)
            
            # Verify that the comment is properly in the database.
            self.failUnlessEqual(Comment.objects.all()[0].contents,
                                 self.post_comment_form_data[COMMENT_KEY])
        
        def testViewWithNoComments(self):
            # Make sure there are no comments.
            Comment.objects.all().delete()
            
            response = self.client.get(self.VIEW_URL)
            
            self.failUnlessEqual(response.status_code,
                                 200,
                                 self.ERROR_DISPLAYING_ITEM_TYPE)
            
            self.assertContains(response, 'There are no comments yet.', 1)
        
        def testViewWithAComment(self):
            self._make_our_comment_the_only_one()
            
            # Check if we are able to go to the page we're commenting to.
            
            response = self.client.get(self.VIEW_URL)
            self.failUnlessEqual(response.status_code,
                                 200,
                                 self.ERROR_DISPLAYING_ITEM_TYPE)
            
            # Make sure the posted comment is displayed
            self.assertTrue(self.comment.contents in response.content)
            
            # Delete the test comment.
            Comment.objects.all().delete()
        
        def testAccessPostCommentURLNotLoggedIn(self):
            response = self.client.get(self.POST_TO_COMMENT_URL)
            
            self.assertRedirects(response, self.LOGIN_REDIRECT_URL)
        
        def testAccessPostCommentURLLoggedIn(self):
            self.client.login(username=TEST_USER_USERNAME,
                              password=TEST_USER_PASSWORD)
            
            response = self.client.get(self.POST_TO_COMMENT_URL)
            self.assertRedirects(response, self.VIEW_URL)
        
        def testPostBlankComment(self):
            # This will only happen if the user is logged in, hence we only
            # need this one version of this test.
            
            self.client.login(username=TEST_USER_USERNAME,
                              password=TEST_USER_PASSWORD)
            
            blank_comment_form_data = {COMMENT_KEY: ''}
            response = self.client.post(self.POST_TO_COMMENT_URL,
                                        blank_comment_form_data)
            
            self.assertFormError(response,
                                 FORM_KEY,
                                 COMMENT_KEY,
                                 'This field is required.')
        
        def _make_our_comment_the_only_one(self):
            Comment.objects.all().delete()
            self.comment.save()
    
    return CommentTestCase
