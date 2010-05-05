from django.core.urlresolvers import reverse

from epic.comments.tests.base_tests import *
from epic.core.test import CustomTestCase
from epic.datasets.models import DataSet


def setUp(self):
    self.item = DataSet.objects.active()[0]
    self.comment_posting_form_data = {'comment': 'abcd'}
    self.user = User.objects.get(username='mark')
    self.comment = Comment(posting_user=self.user, parent_item=self.item, contents='abcd')
    
    kwargs_for_url_reverses = {'item_id': self.item.id, 'slug': self.item.slug}
    self.view_url = reverse('epic.datasets.views.view_dataset', kwargs=kwargs_for_url_reverses)
    self.post_to_comment_url = reverse(
        'epic.comments.views.post_comment', kwargs=kwargs_for_url_reverses)
    self.item_type_string = 'dataset'

dataset_comment_test_case_fixtures = ['comments_just_users', 'comments_datasets']
DataSetCommentTestCase = create_comment_test_case(
    setUp, dataset_comment_test_case_fixtures)
DataSetCommentTestCase.__name__ = "DataSetCommentTestCase"
