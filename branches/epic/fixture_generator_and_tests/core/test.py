from django.core.urlresolvers import reverse
from django.test import TestCase


class CustomTestCase(TestCase):
    # TODO: Is there a way to have a single argument turn into a list of one
    # when passed to a method?
    def assertResponseStatus(self, success_codes, view, args=[], kwargs={}):
        self.assertResponseStatusSuccess(view, args, kwargs, success_codes)
        
    def assertResponseStatusNot(
            self, failure_codes, view, args=[], kwargs={}):
        self.assertResponseStatusNotFailure(view, args, kwargs, failure_codes)
        
    def assertResponseStatusSuccess(
            self, view, args=[], kwargs={}, success_codes=[200]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        if not response_status_code in success_codes:
            self.fail('%s not in %s' % (response_status_code, success_codes))

    def assertResponseStatusNotFailure(
            self, view, args=[], kwargs={}, failure_codes=[404, 500]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        if response_status_code in failure_codes:
            self.fail('%s in %s' % (response_status_code, failure_codes))
    
    def getStatusCode(self, view, args={}, kwargs={}):
         response = self.getResponseFromView(view, args, kwargs)
         return response.status_code
         
    def getResponseFromView(self, view, args=[], kwargs={}):
        view_url = reverse(view, args=args, kwargs=kwargs)
        return self.client.get(view_url)
    
    def tryLogin(self, username, password=None):
        if not password:
            login = self.client.login(username=username, password=username)
        else:
            login = self.client.login(username=username, password=password)
            
        if not login:
            raise CouldNotLoginException('Could not login as %s' % username)

    
    class CouldNotLoginException(Exception):
        def __init__(self, value):
            self.value = value
    
        def __str__(self):
            return repr(self.value)