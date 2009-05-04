from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.test import CustomTestCase
from epic.core.util.view_utils import *
from epic.datasets.models import DataSet
from epic.projects.models import Project


ADMIN_USERNAME = 'admin'
ADMIN_PASSWORD = 'admin'

BOB_USERNAME = 'bob'
BOB_PASSWORD = 'bob'

class URLsTestCase(CustomTestCase):
    """ Test all the urls to make sure that the view for each works.
    """
    
    fixtures = ['projects_just_users', 'projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username=BOB_USERNAME)
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',
            slug='project1')
    
    def test_view_projects(self):
        self.assertResponseStatusNotFailure(
            'epic.projects.views.view_projects')
        
    def test_view_project(self):
        view_project_url = \
            get_item_url(self.project1, 'epic.projects.views.view_project')
        
        response = self.client.get(view_project_url)
        self.assertStatusCodeIsNotAFailure(response.status_code)
        
    def test_view_user_project_list(self):
        self.assertResponseStatusNotFailure(
            'epic.projects.views.view_user_project_list',
            kwargs={'user_id': self.bob.id,})
        
    def test_create_project_not_logged_in(self):
        self.assertResponseStatusRedirect(
            'epic.projects.views.create_project')
    
    def test_create_project_logged_in(self):
        pass
        
    def test_edit_project(self):
        edit_project_url = \
            get_item_url(self.project1, 'epic.projects.views.edit_project')
        
        response = self.client.get(edit_project_url)
        self.assertStatusCodeIsNotAFailure(response.status_code)

#class ViewProjectsTestCase(CustomTestCase):
#    """ Test the view_projects view.
#    """
#    
#    fixtures = ['projects_just_users', 'projects_projects']
#    
#    def setUp(self):
#        self.bob = User.objects.get(username='bob')
#        self.admin = User.objects.get(username='admin')
#        
#        self.project1 = Project.objects.get(
#            creator=self.bob,
#            name='project1',
#            description='This is the first project',
#            slug='project1')
#        
#        self.project2 = Project.objects.create(
#            creator=self.admin,
#            name='project2',
#            description='This is the second project',
#            slug='project2')
#        
#        self.view_projects_url = reverse('epic.projects.views.view_projects')
#    
#    def testLoggedOut(self):
#        response = self.client.get(self.view_projects_url)
#        self.assertEqual(response.status_code, 200)
#        
#        for project in Project.objects.all():
#            self.assertContains(response, project.name)
#        
#    def testLoggedInNotOwner(self):
#        self.tryLogin(ADMIN_USERNAME)
#        
#        response = self.client.get(self.view_projects_url)
#        self.assertEqual(response.status_code, 200)
#        
#        for project in Project.objects.all():
#            self.assertContains(response, project.name)
#    
#    def testLoggedInOwner(self):
#        self.tryLogin(BOB_USERNAME)
#        
#        response = self.client.get(self.view_projects_url)
#        self.assertEqual(response.status_code, 200)
#        
#        for project in Project.objects.all():
#            self.assertContains(response, project.name)
#    
#class ViewProjectTestCase(CustomTestCase):
#    """ Test the view_project view.
#    """
#    
#    fixtures = ['projects_just_users', 'projects_projects']
#    
#    def setUp(self):
#        self.bob = User.objects.get(username='bob')
#        self.admin = User.objects.get(username='admin')
#        
#        self.project1 = Project.objects.get(
#            creator=self.bob,
#            name='project1',
#            description='This is the first project',
#            slug='project1')
#        
#        self.project2 = Project.objects.create(
#            creator=self.admin,
#            name='project2',
#            description='This is the second project',
#            slug='project2')
#        
#        self.view_project1_url = \
#            get_item_url(self.project1, 'epic.projects.views.view_project')
#        
#        self.view_project2_url = \
#            get_item_url(self.project2, 'epic.projects.views.view_project')
#    
#    def testLoggedOut(self):
#        response = self.client.get(self.view_project1_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project1.name)
#        self.assertContains(response, self.project1.description)
#    
#        response = self.client.get(self.view_project2_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project2.name)
#        self.assertContains(response, self.project2.description)
#        
#    def testLoggedInNotOwner(self):
#        self.tryLogin(username='admin', password='admin')
#    
#        response = self.client.get(self.view_project1_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project1.name)
#        self.assertContains(response, self.project1.description)
#    
#        response = self.client.get(self.view_project2_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project2.name)
#        self.assertContains(response, self.project2.description)
#        
#    def testLoggedInOwner(self):
#        self.tryLogin(username='bob', password='bob')
#        
#        response = self.client.get(self.view_project1_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project1.name)
#        self.assertContains(response, self.project1.description)
#    
#        response = self.client.get(self.view_project2_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project2.name)
#        self.assertContains(response, self.project2.description)
#
#class ViewUserProjectListTestCase(CustomTestCase):
#    """ Test the view_user_project_list view.
#    """
#    
#    fixtures = ['projects_just_users', 'projects_projects']
#    
#    def setUp(self):
#        self.bob = User.objects.get(username=BOB_USERNAME)
#        self.admin = User.objects.get(username=ADMIN_USERNAME)
#        
#        self.project1 = Project.objects.get(
#            creator=self.bob, 
#            name='project1',
#            description='This is the first project',
#            slug='project1')
#        
#        self.project2 = Project.objects.create(
#            creator=self.admin,
#            name='project2',
#            description='This is the second project',
#            slug='project2')
#        
#        self.view_user_project_list_url = reverse(
#            'epic.projects.views.view_user_project_list',
#            kwargs={'user_id': self.bob.id,})
#    
#    def testLoggedOut(self):
#        response = self.client.get(self.view_user_project_list_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project1.name)
#        self.assertContains(response, self.project1.description)
#        
#        self.assertNotContains(response, self.project2.name)
#        self.assertNotContains(response, self.project2.description)
#    
#    def testLoggedInNotOwner(self):
#        self.tryLogin(username='admin', password='admin')
#        
#        response = self.client.get(self.view_user_project_list_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project1.name)
#        self.assertContains(response, self.project1.description)
#        
#        self.assertNotContains(response, self.project2.name)
#        self.assertNotContains(response, self.project2.name)
#        
#    def testLoggedInOwner(self):
#        self.tryLogin(username='bob', password='bob')
#    
#        response = self.client.get(self.view_user_project_list_url)
#        self.assertEqual(response.status_code, 200)
#        
#        self.assertContains(response, self.project1.name)
#        self.assertContains(response, self.project1.description)
#        
#        self.assertNotContains(response, self.project2.name)
#        self.assertNotContains(response, self.project2.name)
        
class CreateProjectTestCase(CustomTestCase):
    """ Test the create_project view.
    """
    
    fixtures = ['projects_just_users', 'projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username=BOB_USERNAME)
        self.admin = User.objects.get(username=ADMIN_USERNAME)
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',
            slug='project1')
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',
            slug='project2')
        
        self.create_project_url = \
            reverse('epic.projects.views.create_project')
        
        self.invalid_post_data = {
            'name': '',
            'description': '',
        }
        
        self.valid_post_data = {
            'name': 'This is a new project 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
        }
    
    def testLoggedOut(self):
        self._verify__get_from__create_project_url(True)
        self._verify__post_to__create_project_url(self.valid_post_data, True)
        
        for project in Project.objects.all():
            self.assertNotEqual(project.name,
                                self.valid_post_data['name'])
            
            self.assertNotEqual(project.description,
                                self.valid_post_data['description'])
    
    def testPostInvalid(self):
        self.tryLogin(BOB_USERNAME)

        self._verify__get_from__create_project_url()
        
        post_to__create_project_url__response = \
            self.client.post(self.create_project_url, self.invalid_post_data)
        
        self.assertStatusCodeIsASuccess(
            post_to__create_project_url__response.status_code)
        
        self.assertContains(post_to__create_project_url__response,
                            'This field is required.',
                            2)
    
    def testPostValid(self):
        self.tryLogin(BOB_USERNAME)
        
        self._verify__get_from__create_project_url()
        self._verify__post_to__create_project_url(self.valid_post_data, True)
        
        project = Project.objects.get(
            name=self.valid_post_data['name'],
            description=self.valid_post_data['description'])
    
    def _verify__get_from__create_project_url(
            self, response_should_be_redirect=False):
        get_from__create_project_url__response = \
            self.client.get(self.create_project_url)
        
        if response_should_be_redirect:
            self.assertStatusCodeIsARedirect(
                get_from__create_project_url__response.status_code)
        else:
            self.assertStatusCodeIsASuccess(
                get_from__create_project_url__response.status_code)
    
    def _verify__post_to__create_project_url(
            self, post_data, response_should_be_redirect=False):
        post_to__create_project_url__response = self.client.post(
            self.create_project_url, post_data)
        
        if response_should_be_redirect:
            self.assertStatusCodeIsARedirect(
                post_to__create_project_url__response.status_code)
        else:
            self.assertStatusCodeIsASuccess(
                post_to__create_project_url__response.status_code)

class EditProjectTestCase(CustomTestCase):
    """ Test the edit_project view.
    """
    
    fixtures = ['projects_just_users', 'projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username=BOB_USERNAME)
        self.admin = User.objects.get(username=ADMIN_USERNAME)
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',
            slug='project1')
        
        self.project2 = Project.objects.create(
            creator=self.admin,
            name='project2',
            description='This is the second project',
            slug='project2')
        
        project1_url_reverse_kwargs = {
            'item_id': self.project1.id,
            'slug': self.project1.slug,
        }
        
        project2_url_reverse_kwargs = {
            'item_id': self.project2.id,
            'slug': self.project2.slug,
        }
        
        self.view_project1_url = reverse(
            'epic.projects.views.view_project',
            kwargs=project1_url_reverse_kwargs)
        
        self.view_project2_url = reverse(
            'epic.projects.views.view_project',
            kwargs=project2_url_reverse_kwargs)
        
        self.edit_project1_url = reverse(
            'epic.projects.views.edit_project',
            kwargs=project1_url_reverse_kwargs)
        
        self.edit_project2_url = reverse(
            'epic.projects.views.edit_project',
            kwargs=project2_url_reverse_kwargs)

        self.post_data = {
            'name': '3456 345y,th[-k-0dfgh0 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
        }
    
    def testLoggedOut(self):
        get_from__edit_project1_url__response = \
            self.client.get(self.edit_project1_url)
        self.assertStatusCodeIsARedirect(
            get_from__edit_project1_url__response.status_code)
        
        post_to__project1_url__response = \
            self.client.post(self.edit_project1_url, self.post_data)
        self.assertStatusCodeIsARedirect(
            post_to__project1_url__response.status_code)
        
        for project in Project.objects.all():
            self.assertNotEqual(project.name, self.post_data['name'])
            self.assertNotEqual(project.description,
                                self.post_data['description'])
    
    def testNotOwnerLoggedIn(self):
        self.tryLogin(BOB_USERNAME)
        
        get_from__edit_project2_url__response = \
            self.client.get(self.edit_project2_url)
        self.assertStatusCodeIsARedirect(
            get_from__edit_project2_url__response.status_code)
        
        post_to__edit_project2_url__response = \
            self.client.post(self.edit_project2_url, self.post_data)
        self.assertStatusCodeIsARedirect(
            post_to__edit_project2_url__response.status_code)
        
        for project in Project.objects.all():
            self.assertNotEqual(project.name, self.post_data['name'])
            self.assertNotEqual(project.description,
                                self.post_data['description'])
    
    def testOwnerLoggedIn(self):
        self.tryLogin(BOB_USERNAME)
        
        get_from__edit_project2_url__response = \
            self.client.get(self.edit_project1_url)
        self.assertStatusCodeIsASuccess(
            get_from__edit_project2_url__response.status_code)
        
        post_to__edit_project2_url__response = \
            self.client.post(self.edit_project1_url, self.post_data)
        self.assertEqual(
            post_to__edit_project2_url__response.status_code, 200)
        
        project = Project.objects.get(
            name=self.post_data['name'],
            description=self.post_data['description'])
    
    def testSaveAndContinueEditing(self):
        # Verify that the "Save and Continue Editing" button takes the user
        # right back to the edit page and the new displaying of the edit page
        # has the properly-updated content.
        
        self.tryLogin(BOB_USERNAME);
        
        get_from__edit_project1_url__response = \
            self.client.get(self.edit_project1_url)
        self.assertStatusCodeIsASuccess(
            get_from__edit_project1_url__response.status_code)
        
        post_data_with_save_and_continue_editing_button = \
            self._form_edit_project_post_data_with_specific_submit_button(
                'save_and_continue_editing', 'Save and Continue Editing')
        
        post_to__edit_project1_url__response = \
            self.client.post(self.edit_project1_url,
                             post_data_with_save_and_continue_editing_button)
        self.assertStatusCodeIsASuccess(
            post_to__edit_project1_url__response.status_code)
        
        self.assertContains(post_to__edit_project1_url__response,
                            post_data_with_save_and_continue_editing_button['name'])
        self.assertContains(post_to__edit_project1_url__response,
                            post_data_with_save_and_continue_editing_button['description'])
    
    def testSaveAndFinishEditing(self):
        # Verify that the "Save and Finish Editing" button takes the user
        # to the project page and the updated project is displayed correctly.
        
        self.tryLogin(BOB_USERNAME)
        
        get_from__edit_project1_url__response = \
            self.client.get(self.edit_project1_url)
        self.assertStatusCodeIsASuccess(
            get_from__edit_project1_url__response.status_code)
        
        post_data_with_save_and_finish_editing_button = \
            self._form_edit_project_post_data_with_specific_submit_button(
                'save_and_finish_editing', 'Save and Finish Editing')
        post_to__edit_project1_url__response = \
            self.client.post(self.edit_project1_url,
                             post_data_with_save_and_finish_editing_button)
        self.assertStatusCodeIsARedirect(
            post_to__edit_project1_url__response.status_code)
        
        get_view_project1_response = self.client.get(self.view_project1_url)
        
        self.assertContains(get_view_project1_response,
                            post_data_with_save_and_finish_editing_button['name'])
        self.assertContains(get_view_project1_response,
                            post_data_with_save_and_finish_editing_button['description'])
    
    def _form_edit_project_post_data_with_specific_submit_button(
            self, submit_button_name, submit_button_value):
        post_data = self.post_data.copy()
        post_data[submit_button_name] = submit_button_name
        
        return post_data

class AddDatasetsToProjectTestCase(CustomTestCase):
    fixtures = ['projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username=BOB_USERNAME)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob,
            name='dataset1',
            description='This is the first dataset',
            slug='project1')
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',
            slug='project1')
        
        project1_url_reverse_kwargs = {
            'item_id': self.project1.id,
            'slug': self.project1.slug,
        }
        
        self.view_project1_url = reverse(
            'epic.projects.views.view_project',
            kwargs=project1_url_reverse_kwargs)
        
        self.edit_project1_url = reverse(
            'epic.projects.views.edit_project',
            kwargs=project1_url_reverse_kwargs)

        self.post_data = {
            'name': self.project1.name,
            'description': self.project1.description,
            'dataset_url': '',
        }
    
    def testEmptyURL(self):
        self.tryLogin(BOB_USERNAME)
        
        get_from__edit_project_url__response = \
            self.client.get(self.edit_project1_url)
        
        edit_project_post_data = \
            self._form_edit_project_post_data_with_add_dataset_url('')
        post_to__edit_project_url__response = \
            self.client.post(self.edit_project1_url, edit_project_post_data)
        
        self.assertEqual(get_from__edit_project_url__response.content,
                         post_to__edit_project_url__response.content)
    
    def testURLIsNotDataset(self):
        self.tryLogin(BOB_USERNAME)
        
        url_that_is_not_a_dataset = 'http://www.google.com/'
        edit_project_post_data = \
            self._form_edit_project_post_data_with_add_dataset_url(
                url_that_is_not_a_dataset)
        post_to__edit_project_url__response = \
            self.client.post(self.edit_project1_url, edit_project_post_data)
        
        text_that_should_be_in_response = \
            '%s does not refer to a valid dataset.' % \
                url_that_is_not_a_dataset
        self.assertContains(post_to__edit_project_url__response,
                            text_that_should_be_in_response)
    
    def testAddDatasetToProjectSuccessfully(self):
        self.tryLogin(BOB_USERNAME)
        
        edit_project_post_data = \
            self._form_edit_project_post_data_with_add_dataset_url(
                self.dataset.get_absolute_url())
        post_to__edit_project_url__response = \
            self.client.post(self.edit_project1_url, edit_project_post_data)
        
        remove_dataset_from_project_string = \
            "Remove dataset '%s'?" % self.dataset.name
        self.assertContains(post_to__edit_project_url__response,
                            remove_dataset_from_project_string)
        
        get_view_project_response = self.client.get(self.view_project1_url)
        self.assertContains(get_view_project_response, self.dataset.name)
    
    def testProjectAlreadyHasDataset(self):
        self.tryLogin(BOB_USERNAME)
        
        edit_project_post_data = \
            self._form_edit_project_post_data_with_add_dataset_url(
                self.dataset.get_absolute_url())
        post_to__edit_project_url__response = \
            self.client.post(self.edit_project1_url, edit_project_post_data)
        post_to__edit_project_url_again__response = \
            self.client.post(self.edit_project1_url, edit_project_post_data)
        
        self.assertEqual(post_to__edit_project_url__response.content,
                         post_to__edit_project_url_again__response.content)
    
    def _form_edit_project_post_data_with_add_dataset_url(self, url):
        post_data = self.post_data.copy()
        post_data['dataset_url'] = url
        
        return post_data

class DeleteDatasetFromProjectTestCase(CustomTestCase):
    fixtures = ['projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username=BOB_USERNAME)
        self.admin = User.objects.get(username=ADMIN_USERNAME)
        
        self.dataset = DataSet.objects.get(
            creator=self.bob,
            name='dataset1',
            description='This is the first dataset',
            slug='project1')
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',
            slug='project1')
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',
            slug='project2')
        
        self.edit_project1_url = reverse(
            'epic.projects.views.edit_project',
            kwargs={'item_id': self.project1.id, 'slug': self.project1.slug,})
        
        self.edit_project2_url = reverse(
            'epic.projects.views.edit_project',
            kwargs={'item_id': self.project2.id, 'slug': self.project2.slug,})

        self.post_data = {
            'name': self.project1.name,
            'description': self.project1.description,
            'dataset_url': '',
        }
    
    def testDatasetNotInProject(self):
        self.tryLogin(BOB_USERNAME)
        
        get_from__edit_project_url__response = \
            self.client.get(self.edit_project1_url)
        
        edit_project_post_data = \
            self._form_edit_project_post_data_with_remove_dataset(
                self.dataset)
        post_to__edit_project_url__response = \
            self.client.post(self.edit_project1_url, edit_project_post_data)
        
        self.assertEqual(get_from__edit_project_url__response.content,
                         post_to__edit_project_url__response.content)
    
    def testActuallyDeleteDatasetFromProject(self):
        self.tryLogin(ADMIN_USERNAME)
        
        get_from__edit_project_url_pre_delete__response = \
            self.client.get(self.edit_project2_url)
        
        remove_dataset_from_project_string = \
            "Remove dataset '%s'?" % self.dataset.name
        self.assertContains(get_from__edit_project_url_pre_delete__response,
                            remove_dataset_from_project_string)
        
        edit_project_post_data = \
            self._form_edit_project_post_data_with_remove_dataset(
                self.dataset)
        post_to__edit_project_url__response = \
            self.client.post(self.edit_project2_url, edit_project_post_data)
        get_from__edit_project_url_post_delete__response = \
            self.client.get(self.edit_project2_url)
        
        self.assertNotContains(
            get_from__edit_project_url_post_delete__response,
            remove_dataset_from_project_string)
    
    def _form_edit_project_post_data_with_remove_dataset(self, dataset):
        post_data = self.post_data.copy()
        remove_dataset__html_element_name = 'remove-dataset-%s' % dataset.id
        post_data[remove_dataset__html_element_name] = 'on'
        
        return post_data

class DeleteProjectTestCase(CustomTestCase):
    fixtures = ['projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username=BOB_USERNAME)
        self.admin = User.objects.get(username=ADMIN_USERNAME)
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',
            slug='project1')
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',
            slug='project2')
        
        project1_url_reverse_kwargs = {
            'item_id': self.project1.id,
            'slug': self.project1.slug,
        }
        
        self.edit_project1_url = reverse(
            'epic.projects.views.edit_project',
            kwargs=project1_url_reverse_kwargs)
        
        self.view_project1_url = reverse(
            'epic.projects.views.view_project',
            kwargs=project1_url_reverse_kwargs)
        
        self.confirm_delete_project1_url = reverse(
            'epic.projects.views.confirm_delete_project',
            kwargs=project1_url_reverse_kwargs)
        
        self.delete_project1_url = reverse(
            'epic.projects.views.delete_project',
            kwargs=project1_url_reverse_kwargs)
        
        self.view_profile_url = reverse('epic.core.views.view_profile')
    
    def testLinks(self):
        self.tryLogin(BOB_USERNAME)
        
        confirm_delete_project1_link = \
            '<a href="%s"' % self.confirm_delete_project1_url
        
        view_profile_response = self.client.get(self.view_profile_url)
        self.assertContains(view_profile_response,
                            confirm_delete_project1_link)
        
        edit_project1_response = self.client.get(self.edit_project1_url)
        self.assertContains(edit_project1_response,
                            confirm_delete_project1_link)
    
    def testConfirmationPageLoggedOut(self):
        response = self.client.get(self.confirm_delete_project1_url)
        self.assertStatusCodeIsARedirect(response.status_code)
    
    def testConfirmationPageNotOwnerLoggedIn(self):
        self.tryLogin(ADMIN_USERNAME)
        
        response = self.client.get(self.confirm_delete_project1_url)
        self.assertStatusCodeIsARedirect(response.status_code)
    
    def testConfirmationPageOwnerLoggedIn(self):
        self.tryLogin(BOB_USERNAME)
        
        response = self.client.get(self.confirm_delete_project1_url)
        self.assertStatusCodeIsASuccess(response.status_code)
        
        self.assertContains(response, self.project1.name)
    
    def testCancelDelete(self):
        self.tryLogin(BOB_USERNAME)
        
        confirm_delete_project1_response = \
            self.client.get(self.confirm_delete_project1_url)
        self.assertStatusCodeIsASuccess(
            confirm_delete_project1_response.status_code)
        
        cancel_delete_project1_link = \
            '<a href="%s"' % self.view_project1_url
        self.assertContains(confirm_delete_project1_response,
                            cancel_delete_project1_link)
        
        view_project1_response = self.client.get(self.view_project1_url)
        self.assertStatusCodeIsASuccess(view_project1_response.status_code)
    
    def testConfirmDeleteLoggedOut(self):
        response = self.client.get(self.delete_project1_url)
        self.assertStatusCodeIsARedirect(response.status_code)
    
    def testConfirmDeleteNotOwnerLoggedIn(self):
        self.tryLogin(ADMIN_USERNAME)
        
        response = self.client.get(self.delete_project1_url)
        self.assertStatusCodeIsARedirect(response.status_code)
    
    def testConfirmDeleteOwnerLoggedIn(self):
        self.tryLogin(BOB_USERNAME)
        
        confirm_delete_project1_response = \
            self.client.get(self.confirm_delete_project1_url)
        self.assertStatusCodeIsASuccess(
            confirm_delete_project1_response.status_code)
        
        delete_project1_link = \
            '<a href="%s"' % self.delete_project1_url
        self.assertContains(confirm_delete_project1_response,
            delete_project1_link)
        
        delete_project1_response = self.client.post(self.delete_project1_url)
        self.assertStatusCodeIsARedirect(delete_project1_response.status_code)
        
        view_project1_response = self.client.get(self.view_project1_url)
        self.assertStatusCodeIsAFailure(view_project1_response.status_code)