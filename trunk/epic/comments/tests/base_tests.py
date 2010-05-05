from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.comments.models import Comment
from epic.core.test import CustomTestCase


def create_comment_test_case(_setUp, _fixtures):
    class CommentTestCase(CustomTestCase):
        fixtures = _fixtures
        
        def setUp(self):
            _setUp(self)
            
            self.login_url = reverse('django.contrib.auth.views.login')
            
            self.login_redirect_url = '%(base_url)s?next=%(next_url)s' % {
                'base_url': self.login_url, 'next_url': self.post_to_comment_url
            }
            
            self.error_displaying_item_type = 'Error displaying %s!' % self.item_type_string
        
        def tearDown(self):
            pass
        
        def testViewNotLoggedIn(self):
            response = self.client.get(self.view_url)
            self.failUnlessEqual(response.status_code, 200, self.error_displaying_item_type)
            self.assertContains(response, 'You must be logged in to comment.', 1)

        def testViewLoggedIn(self):
            self.tryLogin('mark', 'mark')
            
            # Go to the comment page.
            response = self.client.get(self.view_url)
            # Fail if the page is not there.
            self.failUnlessEqual(response.status_code, 200, self.error_displaying_item_type)
            # Fail if there's no form on the page.
            self.assertContains(
                response, '<form action="%s" method="POST">' % self.post_to_comment_url, 1)
        
        def testPostCommentNotLoggedIn(self):
            response = self.client.post(self.post_to_comment_url, self.comment_posting_form_data)
            self.assertRedirects(response, self.login_redirect_url)
        
        def testPostCommentLoggedIn(self):
            # Make sure there are no prior comments.
            Comment.objects.all().delete()
            
            self.tryLogin('mark', 'mark')
            
            # Post a test comment.
            response = self.client.post(self.post_to_comment_url, self.comment_posting_form_data)
            # The posted-to URL should have redirected back to the view page.
            self.assertRedirects(response, self.view_url)
            # Verify that the comment is properly in the database.
            self.failUnlessEqual(
                Comment.objects.all()[0].contents, self.comment_posting_form_data['comment'])

        
        def testViewWithNoComments(self):
            # Make sure there are no comments.
            Comment.objects.all().delete()
            
            response = self.client.get(self.view_url)
            self.failUnlessEqual(response.status_code, 200, self.error_displaying_item_type)
            self.assertContains(response, 'There are no comments yet.', 1)
        
        def testViewWithAComment(self):
            self._make_our_comment_the_only_one()
            
            # Check if we are able to go to the page we're commenting to.
            response = self.client.get(self.view_url)
            self.failUnlessEqual(response.status_code, 200, self.error_displaying_item_type)
            # Make sure the posted comment is displayed
            self.assertTrue(self.comment.contents in response.content)
            
            # Delete the test comment.
            Comment.objects.all().delete()
        
        def testAccessPostCommentURLNotLoggedIn(self):
            response = self.client.get(self.post_to_comment_url)
            self.assertRedirects(response, self.login_redirect_url)
        
        def testAccessPostCommentURLLoggedIn(self):
            self.tryLogin('mark', 'mark')

            response = self.client.get(self.post_to_comment_url)
            self.assertRedirects(response, self.view_url)
        
        def testPostBlankComment(self):
            # This will only happen if the user is logged in, hence we only
            # need this one version of this test.
            
            self.tryLogin('mark', 'mark')
            
            blank_comment_form_data = {'comment': ''}
            response = self.client.post(self.post_to_comment_url, blank_comment_form_data)
            # TODO: Currently it redirects on errors, but should *maybe* show.
            # the user what was wrong with the form.
            self.assertEqual(response.status_code, 302)
        
        def _make_our_comment_the_only_one(self):
            Comment.objects.all().delete()
            self.comment.save()

    return CommentTestCase
