from django.test import TestCase

from django.contrib.auth.models import User

class LoginoutTestCase(TestCase):
    fixtures = ['initial_users']
    
    def setUp(self):
        pass
    
    #works
    def testLoginSuccess(self):
        response = self.client.get('/login/')
        self.failUnlessEqual(response.status_code, 200, "Error reaching login!")
        response = self.client.post("/login/", data={'username': 'bob', 'password':'bob',})
        self.failUnlessEqual(response.status_code, 302, "Error logging in!")
        #TODO Once the follow=true actually works it should be used here.
        self.assertRedirects(response, '/')
        
    #(not tested?)
    def testLoginFailure(self):
        response = self.client.post("/login/", {'username': 'bob', 'password':'bob2',})
        self.failUnlessEqual(response.status_code, 200)
        response = self.client.post("/login/", {'username': 'bob', 'password':'bob2',})
        self.failUnless("Your username and password didn't match. Please try again." in response.content)
        
    #works
    def testLogout(self):
        response = self.client.get('/logout/')
        self.failUnlessEqual(response.status_code, 302)
        response = self.client.get('/')
        self.failUnless("login" in response.content)

class PasswordChangeTestCast(TestCase):
    fixtures = ['initial_users']
    
    def setUp(self):
        pass
    
    #error on test
    def testPage(self):
        #Login first
        login = self.client.login(username='bob', password='bob')
        self.failUnless(login, 'Could not login')
        #Make certain the changepassword page is there
        response = self.client.get('/user/change_password/')
        self.assertEqual(response.status_code, 200)
    
    #error on test
    def testPageNotLoggedIn(self):
        response = self.client.get('/user/change_password/')
        self.assertRedirects(response, '/login/?next=/user/change_password/')
    
    def testFailToChangeMatchProblem(self):
        login = self.client.login(username='bob', password='bob')
        self.failUnless(login, 'Could not login')
        post_data = {
                     'old_password': 'bob',
                     'new_password1': 'b',
                     'new_password2': 'blah'
        }
        response = self.client.post('/user/change_password/', post_data)
        self.failUnless(response.status_code, 200)
        self.failUnless("The two password fields didn&#39;t match." in response.content)
    
    def testFailToChangeOldPasswordProblem(self):
        login = self.client.login(username='bob', password='bob')
        self.failUnless(login, 'Could not login')
        post_data = {
                     'old_password': 'incorrectOldPassword',
                     'new_password1': 'blah',
                     'new_password2': 'blah'
        }
        response = self.client.post('/user/change_password/', post_data)
        self.failUnless(response.status_code, 200)
        self.failUnless("Your old password was entered incorrectly. Please enter it again." in response.content)
        
    #error on test
    def testChangePasswordSuccess(self):
        login = self.client.login(username='bob', password='bob')
        self.failUnless(login, 'Could not login')
        post_data = {
            'old_password': 'bob',
            'new_password1': 'blah',
            'new_password2': 'blah'
        }
        response = self.client.post('/user/change_password/', post_data)
        #Redirected if success.
        self.failUnless(response.status_code, 302)
        self.assertRedirects(response, '/user/')

class ProfileLinkTestCase(TestCase):
    fixtures = ['initial_users']
    
    def setUp(self):
        pass
    
    def testLinkForLoggedIn(self):
        login = self.client.login(username='bob', password='bob')
        self.failUnless(login, 'Could not login')
        response = self.client.get('/')
        self.failUnless('<a href="/user/">Profile</a>' in response.content)
    
    def testLinkForNotLoggedIn(self):
        response = self.client.get('/')
        self.failIf('<a href="/user/">Profile</a>' in response.content)

class ProfileDatasetTestCase(TestCase):
    fixtures = ['profile_datasets']
    
    def setUp(self):
        pass
    
    def testForNoDataSets(self):
        login = self.client.login(username='bob', password='bob')
        self.failUnless(login, 'Could not login')
        response = self.client.get('/user/')
        self.failUnless(response.status_code, 200)
        self.failIf("Your Datasets" in response.content)
        
    #fails
    def testForDataSets(self):
        login = self.client.login(username='bill', password='bill')
        self.failUnless(login, 'Could not login')
        response = self.client.get('/user/')
        self.failUnless(response.status_code, 200)
        self.failUnless("Your Datasets" in response.content, response.content)
        self.failUnless("edit" in response.content, response.content)
    
    def testForOnlyDataSets(self):
        login = self.client.login(username='bill', password='bill')
        self.failUnless(login, 'Could not login')
        response = self.client.get('/user/')
        self.failUnless(response.status_code, 200)
        #This is a datarequest object and should not be here
        self.failIf("Item 3" in response.content)

class ViewBasicUserProfileTestCase(TestCase):
	fixtures = ['initial_users']
	
	def setUp(self):
		pass
	
	def testViewProfileNotLoggedIn(self):
		user_response = self.client.get("/user/")
		self.failUnlessEqual(user_response.status_code, 302, "Did not redirect to login!")
	
	def testViewProfileLoggedIn(self):
		self.client.login(username="bob", password="bob")
		
		user_response = self.client.get("/user/")
		self.failUnlessEqual(user_response.status_code, 200, "Error reaching user!")
		
		self.failUnless("<h3>Displaying profile of  .</h3>" in user_response.content)