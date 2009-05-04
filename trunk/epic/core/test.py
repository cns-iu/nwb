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
    
    # Why did we use camel casing for the rest of these methods?
    # This is Python!
    def assertResponseStatusRedirect(
            self, view, args=[], kwargs={}, status_codes=[302]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        
        self.assertStatusCodeIsARedirect(response_status_code, status_codes)
    
    def assertResponseStatusSuccess(
            self, view, args=[], kwargs={}, success_codes=[200]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        
        self.assertStatusCodeIsASuccess(response_status_code, success_codes)
    
    def assertResponseStatusFailure(
            self, view, args=[], kwargs={}, failure_codes=[404, 500]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        
        self.assertStatusCodeIsAFailure(response_status_code, failure_codes)

    def assertResponseStatusNotFailure(
            self, view, args=[], kwargs={}, failure_codes=[404, 500]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        
        self.assertStatusCodeIsNotAFailure(
            response_status_code, failure_codes)
    
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
    
    def assertStatusCodeIsASuccess(self, status_code, success_codes=[200]):
        self._assertStatusCodeIsOneOf(status_code, success_codes)
    
    def assertStatusCodeIsAFailure(
            self, status_code, failure_codes=[404, 500]):
        self._assertStatusCodeIsOneOf(status_code, failure_codes)
    
    def assertStatusCodeIsNotAFailure(
            self, status_code, failure_codes=[404, 500]):
        self._assertStatusCodeIsNotOneOf(status_code, failure_codes)
    
    def assertStatusCodeIsARedirect(
            self, status_code, redirect_codes=[302]):
        self._assertStatusCodeIsOneOf(status_code, redirect_codes)
    
    def _assertStatusCodeIsOneOf(self, status_code, codes):
        if status_code not in codes:
            self.fail('%s not in %s' % (status_code, codes))
    
    def _assertStatusCodeIsNotOneOf(self, status_code, codes):
        if status_code in codes:
            self.fail('%s in %s' % (status_code, codes))
    
    class CouldNotLoginException(Exception):
        def __init__(self, value):
            self.value = value
    
        def __str__(self):
            return repr(self.value)