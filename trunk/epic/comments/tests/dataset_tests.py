from django.core.urlresolvers import reverse
from epic.core.test import CustomTestCase

from epic.comments.tests.base_tests import *
from epic.datasets.models import DataSet


def setUp(self):
    self.item = DataSet.objects.all()[0]
    
    self.post_comment_form_data = {
        COMMENT_KEY: 'abcd'
    }
    
    self.user = User.objects.get(username='peebs')
    
    self.comment = Comment(posting_user=self.user,
                           parent_item=self.item,
                           contents='abcd')
    
    kwargs_for_url_reverses = {
        ITEM_ID_KEY: self.item.id,
        SLUG_KEY: self.item.slug
    }
    
    self.VIEW_URL = reverse('epic.datasets.views.view_dataset',
                            kwargs=kwargs_for_url_reverses)
    
    self.POST_TO_COMMENT_URL = reverse(
        'epic.comments.views.post_comment',
        kwargs=kwargs_for_url_reverses)
    
    self.ITEM_TYPE_STRING = 'dataset'

dataset_comment_test_case_fixtures = [
    'comments_just_users',
    'comments_datasets'
]

DataSetCommentTestCase = create_comment_test_case(
    setUp, dataset_comment_test_case_fixtures)