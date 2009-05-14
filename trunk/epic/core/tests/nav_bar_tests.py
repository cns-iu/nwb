from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase


BOB_USERNAME = 'bob'
BOB_PASSWORD = 'bob'

class NavBarLinkHighlightsTestCase(CustomTestCase):
    """ If the user is on a page that corresponds to a link in the nav bar,
    that link should be highlighted (in the nav bar).  All other links in the
    nav bar should not be highlighted.
    """
    
    fixtures = ['core_just_users']
    
    def setUp(self):
        self.BROWSE_VIEW_URL = reverse('epic.core.views.browse')
        self.UPLOAD_VIEW_URL = reverse('epic.datasets.views.create_dataset')
        self.REQUEST_VIEW_URL = \
            reverse('epic.datarequests.views.new_datarequest')
        self.ABOUT_VIEW_URL = reverse('epic.core.views.about')
    
    def testNoLinksHighlighted(self):
        site_index__view_function__name = 'epic.core.views.site_index'
        site_index_view_url = reverse(site_index__view_function__name)
        response = self.client.get(site_index_view_url)
        
        self.assertNotContains(response, 'pageon')
    
    def testBrowsePage(self):
        response = self.client.get(self.BROWSE_VIEW_URL)
        
        self._testThatPageHasCorrectLinkHighlights(response, 'browse')
    
    def testUploadPage(self):
        self.tryLogin(BOB_USERNAME)
        
        response = self.client.get(self.UPLOAD_VIEW_URL)
        
        self._testThatPageHasCorrectLinkHighlights(response, 'upload')
    
    def testRequestPage(self):
        self.tryLogin(BOB_USERNAME)
        
        response = self.client.get(self.REQUEST_VIEW_URL)
        
        self._testThatPageHasCorrectLinkHighlights(response, 'request')
    
    def testAboutPage(self):
        self.tryLogin(BOB_USERNAME)
        
        response = self.client.get(self.ABOUT_VIEW_URL)
        
        self._testThatPageHasCorrectLinkHighlights(response, 'about')
    
    def _testThatPageHasCorrectLinkHighlights(
            self, response, link_to_be_highlighted=None):
        # link_classes is the set of HTML/CSS classes for the link (anchor tag
        # HTML) elements in the nav bar.
        link_classes = self._form_link_classes(link_to_be_highlighted.lower())
        
        # Form the link (anchor tag) HTML based on where the links should link
        # to (the view URL) and the links' HTML/CSS classes.
        
        browse_link_html = self._form_link_html(self.BROWSE_VIEW_URL,
                                                link_classes['browse'])
        
        upload_link_html = self._form_link_html(self.UPLOAD_VIEW_URL,
                                                link_classes['upload'])
        
        request_link_html = self._form_link_html(self.REQUEST_VIEW_URL,
                                                link_classes['request'])
        
        about_link_html = self._form_link_html(self.ABOUT_VIEW_URL,
                                                link_classes['about'])
        
        # The response should contain the link HTML we just formed.
        
        self.assertContains(response, browse_link_html)
        self.assertContains(response, upload_link_html)
        self.assertContains(response, request_link_html)
        self.assertContains(response, about_link_html)
    
    def _form_link_classes(self, link_to_be_highlighted=None):
        link_classes = {
            'browse': 'pageoff',
            'upload': 'pageoff',
            'request': 'pageoff',
            'about': 'pageoff'
        }
        
        if link_to_be_highlighted is not None:
            link_classes[link_to_be_highlighted] = 'pageon'
        
        return link_classes
    
    def _form_link_html(self, view_url, link_class):
        link_html = '<a href="%s" class="%s">' % (view_url, link_class)
        
        return link_html
