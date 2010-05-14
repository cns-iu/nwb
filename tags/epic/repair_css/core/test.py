from django.contrib.auth.models import User
from django.core.handlers.wsgi import WSGIRequest
from django.core.urlresolvers import reverse
from django.test import Client
from django.test import TestCase


class CustomTestCase(TestCase):
    def assertExists(self, object):
        self.assertTrue(object)
        
    # TODO: Is there a way to have a single argument turn into a list of one
    # when passed to a method?
    
    def assertResponseStatus(self, success_codes, view, args=[], kwargs={}):
        self.assertResponseStatusSuccess(view, args, kwargs, success_codes)
        
    def assertResponseStatusNot(self, failure_codes, view, args=[], kwargs={}):
        self.assertResponseStatusNotFailure(view, args, kwargs, failure_codes)
    
    # Why did we use camel casing for the rest of these methods?
    # This is Python!
    def assertResponseStatusRedirect(self, view, args=[], kwargs={}, status_codes=[302]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        self.assertStatusCodeIsARedirect(response_status_code, status_codes)
    
    def assertResponseStatusSuccess(self, view, args=[], kwargs={}, success_codes=[200]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        self.assertStatusCodeIsASuccess(response_status_code, success_codes)
    
    def assertResponseStatusFailure(self, view, args=[], kwargs={}, failure_codes=[404, 500]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        self.assertStatusCodeIsAFailure(response_status_code, failure_codes)

    def assertResponseStatusNotFailure(self, view, args=[], kwargs={}, failure_codes=[404, 500]):
        response_status_code = self.getStatusCode(view, args, kwargs)
        self.assertStatusCodeIsNotAFailure(response_status_code, failure_codes)
    
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
    
    def assertStatusCodeIsAFailure(self, status_code, failure_codes=[404, 500]):
        self._assertStatusCodeIsOneOf(status_code, failure_codes)
    
    def assertStatusCodeIsNotAFailure(self, status_code, failure_codes=[404, 500]):
        self._assertStatusCodeIsNotOneOf(status_code, failure_codes)
    
    def assertStatusCodeIsARedirect(self, status_code, redirect_codes=[302]):
        self._assertStatusCodeIsOneOf(status_code, redirect_codes)
    
    def failIfNot(self, condition):
        self.failIf(not condition)
    
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

class RequestFactory(Client):
    """
    Class that lets you create mock Request objects for use in testing.
    
    Usage:
    
    rf = RequestFactory()
    get_request = rf.get('/hello/')
    post_request = rf.post('/submit/', {'foo': 'bar'})
    
    This class re-uses the django.test.client.Client interface, docs here:
    http://www.djangoproject.com/documentation/testing/#the-test-client
    
    Once you have a request object you can pass it to any view function, 
    just as if that view had been hooked up using a URLconf.
    
    """
    def request(self, **request):
        """
        Similar to parent class, but returns the request object as soon as it
        has created it.
        """
        environment = {
            'HTTP_COOKIE': self.cookies,
            'PATH_INFO': '/',
            'QUERY_STRING': '',
            'REQUEST_METHOD': 'GET',
            'SCRIPT_NAME': '',
            'SERVER_NAME': 'testserver',
            'SERVER_PORT': 80,
            'SERVER_PROTOCOL': 'HTTP/1.1',
        }
        environment.update(self.defaults)
        environment.update(request)

        return WSGIRequest(environment)

def log_user_in(login_url, username, password, test_case):
    post_data = {'username': username, 'password': password}

    return test_case.client.post(login_url, post_data)
