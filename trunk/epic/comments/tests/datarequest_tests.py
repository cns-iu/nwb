from django.core.urlresolvers import reverse
from epic.core.test import CustomTestCase

from epic.comments.tests.base_tests import *
from epic.datasets.models import DataSet


def setUp(self):
    self.item = DataRequest.objects.active()[0]
    
    self. post_comment_form_data = {
        COMMENT_KEY: 'abcd'
    }
    
    self.user = User.objects.get(username=TEST_USER_USERNAME)
    
    self.comment = Comment(posting_user=self.user,
                           parent_item=self.item,
                           contents='abcd')
    
    kwargs_for_url_reverses = {
        ITEM_ID_KEY: self.item.id,
        SLUG_KEY: self.item.slug
    }
    
    self.VIEW_URL = reverse('epic.datarequests.views.view_datarequest',
                            kwargs=kwargs_for_url_reverses)
    
    self.POST_TO_COMMENT_URL = reverse(
        'epic.comments.views.post_comment',
        kwargs=kwargs_for_url_reverses)
    
    self.ITEM_TYPE_STRING = 'datarequest'

datarequest_comment_test_case_fixtures = [
    'comments_just_users',
    'comments_datarequests'
]

DataRequestCommentTestCase = create_comment_test_case(
    setUp, datarequest_comment_test_case_fixtures)
