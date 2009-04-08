from django.test import TestCase
from django.core.urlresolvers import reverse
from epic.datasets.models import DataSet
from base_tests import *

def setUp(self):
	self.item = DataRequest.objects.all()[0]
	
	self. post_comment_form_data = {
		"comment": "abcd"
	}
	
	self.user = User.objects.get(username="peebs")
	
	self.comment = Comment(posting_user=self.user,
						   parent_item=self.item,
						   contents="abcd")
	
	self.VIEW_URL = reverse("epic.datarequests.views.view_datarequest",
							kwargs={ "item_id": self.item.id, "slug": self.item.slug })
	
	self.POST_TO_COMMENT_URL = reverse("epic.datarequests.views.post_datarequest_comment",
									   kwargs={ "item_id": self.item.id, "slug": self.item.slug })
	
	self.ITEM_TYPE_STRING = "datarequest"

DataRequestCommentTestCase = create_comment_test_case(setUp,
													  [ "comments_just_users", "comments_datarequests" ])
