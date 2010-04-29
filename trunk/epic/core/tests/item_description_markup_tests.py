from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.core.util.postmarkup import STRIP_TAGS_REPLACEMENT
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project


class ItemDescriptionMarkupTestCase(CustomTestCase):
    fixtures = ['core_markup_items']
    
    def setUp(self):
        self.datarequest1 = DataRequest.objects.all()[0]
        self.dataset1 = DataSet.objects.all()[0]
        self.project1 = Project.objects.all()[0]
        
        view_datarequest1_url_kwargs = {
            'item_id': self.datarequest1.id, 'slug': self.datarequest1.slug,
        }
        self.view_datarequest1_url = reverse(
            'epic.datarequests.views.view_datarequest', kwargs=view_datarequest1_url_kwargs)

        view_dataset1_url_kwargs = {'item_id': self.dataset1.id, 'slug': self.dataset1.slug,}
        self.view_dataset1_url = reverse(
            'epic.datasets.views.view_dataset', kwargs=view_dataset1_url_kwargs)

        view_project1_url_kwargs = {'item_id': self.project1.id, 'slug': self.project1.slug,}
        self.view_project1_url = reverse(
            'epic.projects.views.view_project',kwargs=view_project1_url_kwargs)

        self.view_browse_all_url = reverse('epic.core.views.browse')
        self.view_browse_datarequests_url = reverse('epic.datarequests.views.view_datarequests')
        self.view_browse_datasets_url = reverse('epic.datasets.views.view_datasets')
        self.view_browse_projects_url = reverse('epic.projects.views.view_projects')
    
    def testViewDataRequestPage(self):
        response = self.client.get(self.view_datarequest1_url)
        self.assertContains(response, self.datarequest1.rendered_description)
        self.assertNotContains(response, self.datarequest1.description)
    
    def testViewDataSetPage(self):
        response = self.client.get(self.view_dataset1_url)
        self.assertNotContains(response, self.dataset1.description)
        self.assertContains(response, self.dataset1.rendered_description)
    
    def testViewProjectPage(self):
        response = self.client.get(self.view_project1_url)
        self.assertNotContains(response, self.project1.description)
        self.assertContains(response, self.project1.rendered_description)
    
    def testBrowseAllPage(self):
        response = self.client.get(self.view_browse_all_url)
        self.assertContains(response, self._tagless_datarequest1_description())
        self.assertContains(response, self._tagless_dataset1_description())
        self.assertContains(response, self._tagless_project1_description())
        self.assertNotContains(response, self.datarequest1.description)
        self.assertNotContains(response, self.dataset1.description)
        self.assertNotContains(response, self.project1.description)
        self.assertNotContains(response, self.datarequest1.rendered_description)
        self.assertNotContains(response, self.dataset1.rendered_description)
        self.assertNotContains(response, self.project1.rendered_description)

    def testBrowseDataRequestsPage(self):
        response = self.client.get(self.view_browse_datarequests_url)
        self.assertContains(response, self._tagless_datarequest1_description())
        self.assertNotContains(response, self.datarequest1.description)
        self.assertNotContains(response, self.datarequest1.rendered_description)
    
    def testBrowseDataSetsPage(self):
        response = self.client.get(self.view_browse_datasets_url)
        self.assertContains(response, self._tagless_dataset1_description())
        self.assertNotContains(response, self.dataset1.description)
        self.assertNotContains(response, self.dataset1.rendered_description)
    
    def testBrowseProjectsPage(self):
        response = self.client.get(self.view_browse_projects_url)
        self.assertContains(response, self._tagless_project1_description())
        self.assertNotContains(response, self.project1.description)
        self.assertNotContains(response, self.project1.rendered_description)
    
    def testImageTag(self):
        img_url = 'http://www.google.com/intl/en_ALL/images/logo.gif'
        img_tag = '<img src="%s" />' % img_url

        response = self.client.get(self.view_dataset1_url)
        self.assertContains(response, img_tag, 1)
        self.assertContains(response, img_url, 1)
    
    def _tagless_datarequest1_description(self):
        tagless_description = '%(replacement)s%(name)s%(replacement)s' % \
            {'replacement': STRIP_TAGS_REPLACEMENT, 'name': self.datarequest1.name}
        
        return tagless_description
    
    def _tagless_dataset1_description(self):
        tagless_description = '%(replacement)s%(name)s%(replacement)s' % \
            {'replacement': STRIP_TAGS_REPLACEMENT, 'name': self.dataset1.name}
        
        return tagless_description
    
    def _tagless_project1_description(self):
        tagless_description = '%(replacement)s%(name)s%(replacement)s' % \
            {'replacement': STRIP_TAGS_REPLACEMENT, 'name': self.project1.name}
        
        return tagless_description
