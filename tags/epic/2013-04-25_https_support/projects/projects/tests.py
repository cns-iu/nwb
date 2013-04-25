from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.models import Profile
from epic.core.test import CustomTestCase
from epic.core.util.view_utils import *
from epic.datasets.models import DataSet
from epic.projects.models import Project


ADMIN_USERNAME = 'admin'
ADMIN_PASSWORD = 'admin'

BOB_USERNAME = 'bob'
BOB_PASSWORD = 'bob'

BILL_USERNAME = 'bill'
BILL_PASSWORD = 'bill'

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
        # Verify that posting data using the "Save and Continue Editing"
        # button takes the user back to the edit page and the newly displayed
        # edit page has the posted content.
        
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
            slug='dataset1')
        
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
            slug='dataset1')
        
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
        self.assertStatusCodeIsASuccess(view_project1_response.status_code)
        self.assertContains(view_project1_response,
                            'This project is not available.')

class BrowseProjectsTestCase(CustomTestCase):
    """ Test the browsing of projects.
    """
    
    fixtures = ['projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username=BOB_USERNAME)
        self.bobs_profile = Profile.objects.for_user(user=self.bob)
        
        self.admin = User.objects.get(username=ADMIN_USERNAME)
        self.admins_profile = Profile.objects.for_user(user=self.admin)
        
        self.bill = User.objects.get(username=BILL_USERNAME)
        self.bills_profile = Profile.objects.for_user(user=self.bill)
        
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
        
        self.project2_dataset = self.project2.datasets.all()[0]
        
        view_bob_kwargs = {
            'user_id': self.bob.id,
        }
        
        view_admin_kwargs = {
            'user_id': self.admin.id,
        }
        
        view_bill_kwargs = {
            'user_id': self.bill.id,
        }
        
        self.view_bobs_projects_url = reverse(
            'epic.projects.views.view_user_project_list',
            kwargs=view_bob_kwargs)
        
        self.view_bobs_profile_url = reverse(
            'epic.core.views.view_profile',
            kwargs=view_bob_kwargs)
        
        
        self.view_admins_projects_url = reverse(
            'epic.projects.views.view_user_project_list',
            kwargs=view_admin_kwargs)
        
        self.view_admins_profile_url = reverse(
            'epic.core.views.view_profile',
            kwargs=view_admin_kwargs)
        
        
        self.view_bills_projects_url = reverse(
            'epic.projects.views.view_user_project_list',
            kwargs=view_bill_kwargs)
        
        self.view_bills_profile_url = reverse(
            'epic.core.views.view_profile',
            kwargs=view_bill_kwargs)
        
        
        self.browse_url = reverse('epic.core.views.browse')
        self.browse_projects_url = \
            reverse('epic.projects.views.view_projects')
        
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
        
        self.delete_project1_url = reverse(
            'epic.projects.views.confirm_delete_project',
            kwargs=project1_url_reverse_kwargs)
        
        testing_tag_url_reverse_kwargs = {
            'tag_name': 'testing',
        }
        
        self.testing_tag_all_items_url = reverse(
            'epic.tags.views.view_items_for_tag',
            kwargs=testing_tag_url_reverse_kwargs)
        
        self.testing_tag_projects_url = reverse(
            'epic.tags.views.view_projects_for_tag',
            kwargs=testing_tag_url_reverse_kwargs)
    
    def testBrowseLinkInSubMenuLoggedOut(self):
        # Test that the Projects option shows up with
        # (All, Datasets, Data Requests) when logged out.
        
        response = self.client.get(self.browse_url)
        
        browse_projects_link = '<a href="%s"' % self.browse_projects_url
        self.assertContains(response, browse_projects_link)
    
    def testBrowseLinkInSubMenuLoggedIn(self):
        # Test that the Projects option shows up with
        # (All, Datasets, Data Requests) when logged in.
        
        self.tryLogin(BOB_USERNAME)
        response = self.client.get(self.browse_url)
        
        browse_projects_link = '<a href="%s"' % self.browse_projects_url
        self.assertContains(response, browse_projects_link)
    
    def testWhenNoneExistLoggedOut(self):
        # Test browsing Projects when there are no projects and logged out.
        
        Project.objects.all().delete()
        
        response = self.client.get(self.browse_projects_url)
        
        self.assertContains(response, 'There are no projects.')
    
    def testWhenNoneExistLoggedIn(self):
        # Test browsing projects when there are no projects and logged in.
        
        Project.objects.all().delete()
        
        self.tryLogin(BOB_USERNAME)
        response = self.client.get(self.browse_projects_url)
        
        self.assertContains(response, 'There are no projects.')
    
    def testWhenTheyExistLoggedOut(self):
        # Test browsing Projects when there are projects and logged out.
        
        response = self.client.get(self.browse_projects_url)
        
        self.assertNotContains(response, 'There are no projects.')
        self.assertContains(response, self.project1.name)
        
        edit_project1_link = '<a href="%s"' % self.edit_project1_url
        delete_project1_link = '<a href="%s"' % self.delete_project1_url
        self.assertNotContains(response, edit_project1_link)
        self.assertNotContains(response, delete_project1_link)
    
    def testWhenTheyExistLoggedIn(self):
        # Test browsing Projects when there are projects and logged in.
        
        self.tryLogin(BOB_USERNAME)
        response = self.client.get(self.browse_projects_url)
        
        self.assertNotContains(response, 'There are no projects.')
        self.assertContains(response, self.project1.name)
        
        edit_project1_link = '<a href="%s"' % self.edit_project1_url
        delete_project1_link = '<a href="%s"' % self.delete_project1_url
        self.assertContains(response, edit_project1_link)
        self.assertNotContains(response, delete_project1_link)
    
    def testUsersProjectsLoggedOut(self):
        # Test browsing Projects via a user and logged out.
        
        response = self.client.get(self.view_bobs_projects_url)
        
        self.assertNotContains(response, 'There are no projects.')
        self.assertContains(response, self.project1.name)
        
        edit_project1_link = '<a href="%s"' % self.edit_project1_url
        delete_project1_link = '<a href="%s"' % self.delete_project1_url
        self.assertNotContains(response, edit_project1_link)
        self.assertNotContains(response, delete_project1_link)
    
    def testUsersProjectsNotOwnerLoggedIn(self):
        # Test browsing Projects via a user and logged in.
        
        self.tryLogin(ADMIN_USERNAME)
        response = self.client.get(self.view_bobs_projects_url)
        
        self.assertNotContains(response, 'There are no projects.')
        self.assertContains(response, self.project1.name)
        
        edit_project1_link = '<a href="%s"' % self.edit_project1_url
        delete_project1_link = '<a href="%s"' % self.delete_project1_url
        self.assertNotContains(response, edit_project1_link)
        self.assertNotContains(response, delete_project1_link)
    
    def testUsersProjectsOwnerLoggedIn(self):
        # Test browsing Projects via a user and logged in.
        
        self.tryLogin(BOB_USERNAME)
        response = self.client.get(self.view_bobs_projects_url)
        
        self.assertNotContains(response, 'There are no projects.')
        self.assertContains(response, self.project1.name)
        
        edit_project1_link = '<a href="%s"' % self.edit_project1_url
        delete_project1_link = '<a href="%s"' % self.delete_project1_url
        self.assertContains(response, edit_project1_link)
        self.assertContains(response, delete_project1_link)
    
    def testNoProjectsInUsersProfileNotOwner(self):
        # Test browsing Projects via a user's profile and not the profile's
        # user, and there are no projects.
        
        self.tryLogin(ADMIN_USERNAME)
        response = self.client.get(self.view_bills_profile_url)
        
        fake_projects_listing = \
            '%s Projects' % self.bills_profile.full_title()
        real_projects_listing = '%s has not created any projects.' % \
            self.admins_profile.full_title()
        self.assertNotContains(response, fake_projects_listing)
        self.assertContains(response, real_projects_listing)
    
    def testNoProjectsInUsersProfileOwner(self):
        # Test browsing Projects via a user's profile and not the profile's
        # user, and there are no projects.
        
        self.tryLogin(BILL_USERNAME)
        response = self.client.get(self.view_bills_profile_url)
        
        fake_projects_listing = 'Your Projects'
        real_projects_listing = 'You have not created any projects.'
        self.assertNotContains(response, fake_projects_listing)
        self.assertContains(response, real_projects_listing)
    
    def testProjectsInUsersProfileNotOwner(self):
        # Test browsing Projects via a user's profile and not the
        # profile's user, and there are projects.
        
        self.tryLogin(ADMIN_USERNAME)
        response = self.client.get(self.view_bobs_profile_url)
        
        fake_projects_listing = '%s has not created any projects.' % \
            self.bobs_profile.full_title()
        real_projects_listing = '%s Projects' % self.bobs_profile.full_title()
        self.assertNotContains(response, fake_projects_listing)
        self.assertContains(response, real_projects_listing)
    
    def testProjectsInUsersProfileOwnerLoggedIn(self):
        # Test browsing Projects via a user's profile and the profile's user,
        # and there are projects.
        
        self.tryLogin(BOB_USERNAME)
        response = self.client.get(self.view_bobs_profile_url)
        
        fake_projects_listing = 'You have not created any projects.'
        real_projects_listing = 'Your Projects'
        self.assertNotContains(response, fake_projects_listing)
        self.assertContains(response, real_projects_listing)
    
    def testProjectsInGenericTagResultSet(self):
        # Test browsing Projects via tags--Projects in a generic tag
        # result set.
        
        response = self.client.get(self.testing_tag_all_items_url)
        
        self.assertContains(response, 'Items tagged as testing')
        self.assertNotContains(response, self.project1.name)
        self.assertContains(response, self.project2.name)
        self.assertContains(response, self.project2_dataset.name)
    
    def testProjectsInSpecificTagResultSet(self):
        # Test browsing Projects via tags--Projects in Project-specific
        # tag result set.
        
        response = self.client.get(self.testing_tag_projects_url)
        
        self.assertContains(response, 'Items tagged as testing')
        self.assertNotContains(response, self.project1.name)
        self.assertContains(response, self.project2.name)
        self.assertNotContains(response, self.project2_dataset.name)

class ViewProjectsTestCase(CustomTestCase):
    """ Test the view_projects view.
    """
    
    fixtures = ['projects_just_users', 'projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
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
        
        self.view_projects_url = reverse('epic.projects.views.view_projects')
    
    def testLoggedOut(self):
        response = self.client.get(self.view_projects_url)
        self.assertEqual(response.status_code, 200)
        
        for project in Project.objects.active():
            self.assertContains(response, project.name)
        
    def testLoggedInNotOwner(self):
        self.tryLogin(ADMIN_USERNAME)
        
        response = self.client.get(self.view_projects_url)
        self.assertEqual(response.status_code, 200)
        
        for project in Project.objects.active():
            self.assertContains(response, project.name)
    
    def testLoggedInOwner(self):
        self.tryLogin(BOB_USERNAME)
        
        response = self.client.get(self.view_projects_url)
        self.assertEqual(response.status_code, 200)
        
        for project in Project.objects.active():
            self.assertContains(response, project.name)
    
class ViewProjectTestCase(CustomTestCase):
    """ Test the view_project view.
    """
    
    fixtures = ['projects_just_users', 'projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',
            slug='project1',
            is_active=True)
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',
            slug='project2',
            is_active=True)
        
        self.view_project1_url = \
            get_item_url(self.project1, 'epic.projects.views.view_project')
        
        self.view_project2_url = \
            get_item_url(self.project2, 'epic.projects.views.view_project')
    
    def testLoggedOut(self):
        response = self.client.get(self.view_project1_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project1.name)
        self.assertContains(response, self.project1.description)
    
        response = self.client.get(self.view_project2_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project2.name)
        self.assertContains(response, self.project2.description)
        
    def testLoggedInNotOwner(self):
        self.tryLogin(username='admin', password='admin')
    
        response = self.client.get(self.view_project1_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project1.name)
        self.assertContains(response, self.project1.description)
    
        response = self.client.get(self.view_project2_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project2.name)
        self.assertContains(response, self.project2.description)
        
    def testLoggedInOwner(self):
        self.tryLogin(username='bob', password='bob')
        
        response = self.client.get(self.view_project1_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project1.name)
        self.assertContains(response, self.project1.description)
    
        response = self.client.get(self.view_project2_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project2.name)
        self.assertContains(response, self.project2.description)

class ViewUserProjectListTestCase(CustomTestCase):
    """ Test the view_user_project_list view.
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
        
        self.view_user_project_list_url = reverse(
            'epic.projects.views.view_user_project_list',
            kwargs={'user_id': self.bob.id,})
    
    def testLoggedOut(self):
        response = self.client.get(self.view_user_project_list_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project1.name)
        self.assertContains(response, self.project1.description)
        
        self.assertNotContains(response, self.project2.name)
        self.assertNotContains(response, self.project2.description)
    
    def testLoggedInNotOwner(self):
        self.tryLogin(username='admin', password='admin')
        
        response = self.client.get(self.view_user_project_list_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project1.name)
        self.assertContains(response, self.project1.description)
        
        self.assertNotContains(response, self.project2.name)
        self.assertNotContains(response, self.project2.name)
        
    def testLoggedInOwner(self):
        self.tryLogin(username='bob', password='bob')
    
        response = self.client.get(self.view_user_project_list_url)
        self.assertEqual(response.status_code, 200)
        
        self.assertContains(response, self.project1.name)
        self.assertContains(response, self.project1.description)
        
        self.assertNotContains(response, self.project2.name)
        self.assertNotContains(response, self.project2.name)
