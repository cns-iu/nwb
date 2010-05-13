from django.contrib.auth.models import User
from django.core.urlresolvers import reverse

from epic.core.models import Profile
from epic.core.test import CustomTestCase
from epic.core.util.view_utils import *
from epic.datasets.models import DataSet
from epic.datasets.models import DataSetFile
from epic.projects.models import Project


class URLsTestCase(CustomTestCase):
    """ Test all the urls to make sure that the view for each works.
    """
    
    fixtures = ['projects_just_users', 'projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',)
    
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
        self.assertResponseStatusRedirect('epic.projects.views.create_project')
    
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
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset = DataSet.objects.get(
            creator=self.bob,
            name='dataset1',
            description='This is the first dataset',
            is_active=True)
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',)
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',)
        
        self.create_project_url = \
            reverse('epic.projects.views.create_project')
        
        self.invalid_post_data = {
            'name': '',
            'description': '',
            'project_datasets-INITIAL_FORMS': 1,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': '',
        }
        
        self.valid_post_data_without_dataset_url = {
            'name': 'This is a new project 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'project_datasets-INITIAL_FORMS': 0,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': '',
        }
        
        self.valid_post_data_with_invalid_dataset_url = {
            'name': 'This is a new project 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'project_datasets-INITIAL_FORMS': 1,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': 'asdf',
        }
        
        self.valid_post_data_with_valid_dataset_url = {
            'name': 'This is a new project 209u359hdfg',
            'description': '20y5hdfg ahi3hoh348t3948t5hsdfigh',
            'project_datasets-INITIAL_FORMS': 1,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': self.dataset.get_absolute_url(),
        }
    
    def testLoggedOut(self):
        post_data = self.valid_post_data_with_valid_dataset_url
        
        self._verify__get_from__create_project_url(True)
        self._verify__post_to__create_project_url(post_data, True)
        
        for project in Project.objects.all():
            self.assertNotEqual(project.name, post_data['name'])
            self.assertNotEqual(project.description, post_data['description'])
    
    def testPostInvalid(self):
        self.tryLogin('bob')

        self._verify__get_from__create_project_url()
        
        post_to__create_project_url__response = \
            self.client.post(self.create_project_url, self.invalid_post_data)
        
        self.assertStatusCodeIsASuccess(
            post_to__create_project_url__response.status_code)
        
        self.assertContains(post_to__create_project_url__response,
                            'This field is required.',
                            2)
    
    def testPostValidWithoutDataSetURL(self):
        self.tryLogin('bob')
        
        post_data = self.valid_post_data_without_dataset_url
        
        self._verify__get_from__create_project_url()
        self._verify__post_to__create_project_url(post_data, True)
        
        try:
            project = Project.objects.get(
                name=post_data['name'],
                description=post_data['description'])
        except Project.DoesNotExist:
            self.fail()
        
        self.assertFalse(self.dataset in project.datasets.all())
    
    def testPostValidWithInvalidDataSetURL(self):
        self.tryLogin('bob')
        
        post_data = self.valid_post_data_with_invalid_dataset_url
        
        self._verify__get_from__create_project_url()
        self._verify__post_to__create_project_url(post_data, False)
        
        project_created = False
        
        try:
            project = Project.objects.get(
                name=post_data['name'],
                description=post_data['description'])
            
            project_created = True
        except Project.DoesNotExist:
            pass
        
        self.assertFalse(project_created)
    
    def testPostValidWithValidDataSetURL(self):
        self.tryLogin('bob')
        
        post_data = self.valid_post_data_with_valid_dataset_url
        
        self._verify__get_from__create_project_url()
        self._verify__post_to__create_project_url(post_data, True)
        
        try:
            project = Project.objects.get(
                name=post_data['name'],
                description=post_data['description'])
        except Project.DoesNotExist:
            self.fail()
        
        self.assertTrue(self.dataset in project.datasets.all())
    
    def _verify__get_from__create_project_url(
            self, response_should_be_redirect=False):
        response = self.client.get(self.create_project_url)
        
        if response_should_be_redirect:
            self.assertStatusCodeIsARedirect(response.status_code)
        else:
            self.assertStatusCodeIsASuccess(response.status_code)
    
    def _verify__post_to__create_project_url(
            self, post_data, response_should_be_redirect=False):
        response = self.client.post(self.create_project_url, post_data)
        
        if response_should_be_redirect:
            self.assertStatusCodeIsARedirect(response.status_code)
        else:
            self.assertStatusCodeIsASuccess(response.status_code)

class EditProjectTestCase(CustomTestCase):
    """ Test the edit_project view.
    """
    
    fixtures = ['projects_just_users', 'projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset = DataSet.objects.get(
            creator=self.bob,
            name='dataset1',
            description='This is the first dataset',
            is_active=True)
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',)
        
        self.project2 = Project.objects.create(
            creator=self.admin,
            name='project2',
            description='This is the second project',)
        
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
        
        self.invalid_post_data = {
            'name': '',
            'description': '',
            'project_datasets-INITIAL_FORMS': 1,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': '',
        }
        
        self.valid_post_data_without_dataset_url = {
            'name': 'New project name',
            'description': 'New project description',
            'project_datasets-INITIAL_FORMS': 0,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': '',
        }
        
        self.valid_post_data_with_invalid_dataset_url = {
            'name': 'New project name',
            'description': 'New project description',
            'project_datasets-INITIAL_FORMS': 1,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': 'asdf',
        }
        
        self.valid_post_data_with_valid_dataset_url = {
            'name': 'New project name',
            'description': 'New project description',
            'project_datasets-INITIAL_FORMS': 1,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': self.dataset.get_absolute_url(),
        }
    
    def testLoggedOut(self):
        get_response = self.client.get(self.edit_project1_url)
        self.assertStatusCodeIsARedirect(get_response.status_code)
        
        post_data = self.valid_post_data_with_valid_dataset_url
        
        post_response = self.client.post(self.edit_project1_url, post_data)
        self.assertStatusCodeIsARedirect(post_response.status_code)
        
        for project in Project.objects.all():
            self.assertNotEqual(project.name, post_data['name'])
            self.assertNotEqual(project.description, post_data['description'])
    
    def testNotOwner(self):
        self.tryLogin('bob')
        
        post_data = self.valid_post_data_with_valid_dataset_url
        
        get_response = self.client.get(self.edit_project2_url)
        self.assertStatusCodeIsARedirect(get_response.status_code)
        
        post_response = self.client.post(self.edit_project2_url, post_data)
        self.assertStatusCodeIsARedirect(post_response.status_code)
        
        for project in Project.objects.all():
            self.assertNotEqual(project.name, post_data['name'])
            self.assertNotEqual(project.description, post_data['description'])
    
    def testPostInvalid(self):
        self.tryLogin('bob')

        self._verify__get_from__edit_project_url(self.edit_project1_url)
        
        response = \
            self.client.post(self.edit_project1_url, self.invalid_post_data)
        self.assertStatusCodeIsASuccess(response.status_code)
        self.assertContains(response, 'This field is required.', 2)
    
    def testPostValidWithoutDataSetURL(self):
        self.tryLogin('bob')
        
        post_data = self.valid_post_data_without_dataset_url
        
        self._verify__get_from__edit_project_url(self.edit_project1_url)
        self._verify__post_to__edit_project_url(
            post_data, self.edit_project1_url, True)
        
        try:
            project = Project.objects.get(
                name=post_data['name'],
                description=post_data['description'])
        except Project.DoesNotExist:
            self.fail()
        
        self.assertFalse(self.dataset in project.datasets.all())
    
    def testPostValidWithInvalidDataSetURL(self):
        self.tryLogin('bob')
        
        post_data = self.valid_post_data_with_invalid_dataset_url
        
        self._verify__get_from__edit_project_url(self.edit_project1_url)
        self._verify__post_to__edit_project_url(
            post_data, self.edit_project1_url, False)
        
        project_edited = False
        
        try:
            project = Project.objects.get(
                name=post_data['name'],
                description=post_data['description'])
            
            project_edited = True
        except Project.DoesNotExist:
            pass
        
        self.assertFalse(project_edited)
    
    def testPostValidWithValidDataSetURL(self):
        self.tryLogin('bob')
        
        post_data = self.valid_post_data_with_valid_dataset_url
        
        self._verify__get_from__edit_project_url(self.edit_project1_url)
        self._verify__post_to__edit_project_url(
            post_data, self.edit_project1_url, True)
        
        try:
            project = Project.objects.get(
                name=post_data['name'],
                description=post_data['description'])
        except Project.DoesNotExist:
            self.fail()
        
        self.assertTrue(self.dataset in project.datasets.all())
    
    def _verify__get_from__edit_project_url(
            self, edit_project_url, response_should_be_redirect=False):
        response = self.client.get(edit_project_url)
        
        if response_should_be_redirect:
            self.assertStatusCodeIsARedirect(response.status_code)
        else:
            self.assertStatusCodeIsASuccess(response.status_code)
    
    def _verify__post_to__edit_project_url(self,
                                           post_data,
                                           edit_project_url,
                                           response_should_be_redirect=False):
        response = self.client.post(edit_project_url, post_data)
        
        if response_should_be_redirect:
            self.assertStatusCodeIsARedirect(response.status_code)
        else:
            self.assertStatusCodeIsASuccess(response.status_code)

class DeleteDatasetFromProjectTestCase(CustomTestCase):
    fixtures = ['projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.dataset = DataSet.objects.get(
            creator=self.bob,
            name='dataset1',
            description='This is the first dataset',)
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',)
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',)
        
        self.edit_project1_url = reverse(
            'epic.projects.views.edit_project',
            kwargs={'item_id': self.project1.id, 'slug': self.project1.slug,})
        
        self.view_project2_url = reverse('epic.projects.views.view_project',
                                         kwargs={'item_id': self.project2.id,
                                                 'slug': self.project2.slug,})
        self.edit_project2_url = reverse(
            'epic.projects.views.edit_project',
            kwargs={'item_id': self.project2.id, 'slug': self.project2.slug,})

        self.post_data = {
            'name': self.project1.name,
            'description': self.project1.description,
            'project_datasets-INITIAL_FORMS': 0,
            'project_datasets-TOTAL_FORMS': 0,
            'project_datasets-0-dataset_url': '',
        }
    
    def testDatasetNotInProject(self):
    	#TODO: What is this doing? (it's not clear, after thinking about it for a minute)
        self.tryLogin('bob')
        
        get_response1 = \
            self.client.get(self.edit_project1_url)
        
        post_data = {
            'name': self.project1.name,
            'description': self.project1.description,
            'project_datasets-INITIAL_FORMS': 0,
            'project_datasets-TOTAL_FORMS': 0,
        }
        
        post_response = \
            self.client.post(self.edit_project1_url, post_data)
        
        get_response2 = \
            self.client.get(self.edit_project1_url)
            
        self.assertEqual(get_response1.content,
                         get_response2.content)
    
    def testActuallyDeleteDatasetFromProject(self):
        self.tryLogin('admin')
        
        get_response = \
            self.client.get(self.view_project2_url)
            
        self.assertContains(get_response, self.dataset.name)
        
        post_data = {
            'name': self.project1.name,
            'description': self.project1.description,
            'project_datasets-INITIAL_FORMS': 0,
            'project_datasets-TOTAL_FORMS': 1,
            'project_datasets-0-dataset_url': '',
        }
        
        
        post_response = \
            self.client.post(self.edit_project2_url, post_data)
            
        get_response = \
            self.client.get(self.view_project2_url)
        
        self.assertNotContains(get_response, self.dataset.name)
    
    def _form_edit_project_post_data_with_remove_dataset(self, dataset):
        post_data = self.post_data.copy()
        remove_dataset__html_element_name = 'remove-dataset-%s' % dataset.id
        post_data[remove_dataset__html_element_name] = 'on'
        
        return post_data

class DeleteProjectTestCase(CustomTestCase):
    fixtures = ['projects_projects']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',)
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',)
        
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
        self.tryLogin('bob')
        
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
        self.tryLogin('admin')
        
        response = self.client.get(self.confirm_delete_project1_url)
        self.assertStatusCodeIsARedirect(response.status_code)
    
    def testConfirmationPageOwnerLoggedIn(self):
        self.tryLogin('bob')
        
        response = self.client.get(self.confirm_delete_project1_url)
        self.assertStatusCodeIsASuccess(response.status_code)
        
        self.assertContains(response, self.project1.name)
    
    def testCancelDelete(self):
        self.tryLogin('bob')
        
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
        self.tryLogin('admin')
        
        response = self.client.get(self.delete_project1_url)
        self.assertStatusCodeIsARedirect(response.status_code)
    
    def testConfirmDeleteOwnerLoggedIn(self):
        self.tryLogin('bob')
        
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
        self.bob = User.objects.get(username='bob')
        self.bobs_profile = Profile.objects.for_user(user=self.bob)
        
        self.admin = User.objects.get(username='admin')
        self.admins_profile = Profile.objects.for_user(user=self.admin)
        
        self.bill = User.objects.get(username='bill')
        self.bills_profile = Profile.objects.for_user(user=self.bill)
        
        self.project1 = Project.objects.get(
            creator=self.bob,
            name='project1',
            description='This is the first project',)
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',)
        
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
        
        self.tag = 'testing'
        
        testing_tag_url_reverse_kwargs = {
            'tag_name': self.tag,
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
        
        self.tryLogin('bob')
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
        
        self.tryLogin('bob')
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
        
        self.tryLogin('bob')
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
        
        self.tryLogin('admin')
        response = self.client.get(self.view_bobs_projects_url)
        
        self.assertNotContains(response, 'There are no projects.')
        self.assertContains(response, self.project1.name)
        
        edit_project1_link = '<a href="%s"' % self.edit_project1_url
        delete_project1_link = '<a href="%s"' % self.delete_project1_url
        self.assertNotContains(response, edit_project1_link)
        self.assertNotContains(response, delete_project1_link)
    
    def testUsersProjectsOwnerLoggedIn(self):
        # Test browsing Projects via a user and logged in.
        
        self.tryLogin('bob')
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
        
        self.tryLogin('admin')
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
        
        self.tryLogin('bill')
        response = self.client.get(self.view_bills_profile_url)
        
        fake_projects_listing = 'Your Projects'
        real_projects_listing = 'You have not created any projects.'
        self.assertNotContains(response, fake_projects_listing)
        self.assertContains(response, real_projects_listing)
    
    def testProjectsInUsersProfileNotOwner(self):
        # Test browsing Projects via a user's profile and not the
        # profile's user, and there are projects.
        
        self.tryLogin('admin')
        response = self.client.get(self.view_bobs_profile_url)
        
        fake_projects_listing = '%s has not created any projects.' % \
            self.bobs_profile.full_title()
        real_projects_listing = 'Projects'
        self.assertNotContains(response, fake_projects_listing)
        self.assertContains(response, real_projects_listing)
    
    def testProjectsInUsersProfileOwnerLoggedIn(self):
        # Test browsing Projects via a user's profile and the profile's user,
        # and there are projects.
        
        self.tryLogin('bob')
        response = self.client.get(self.view_bobs_profile_url)
        
        fake_projects_listing = 'You have not created any projects.'
        real_projects_listing = 'Your Projects'
        self.assertNotContains(response, fake_projects_listing)
        self.assertContains(response, real_projects_listing)
    
    def testProjectsInGenericTagResultSet(self):
        # Test browsing Projects via tags--Projects in a generic tag
        # result set.
        
        response = self.client.get(self.testing_tag_all_items_url)
        
        self.assertContains(response, 'Tag > %s' % self.tag)
        self.assertNotContains(response, self.project1.name)
        self.assertContains(response, self.project2.name)
        self.assertContains(response, self.project2_dataset.name)
    
    def testProjectsInSpecificTagResultSet(self):
        # Test browsing Projects via tags--Projects in Project-specific
        # tag result set.
        
        response = self.client.get(self.testing_tag_projects_url)
        
        self.assertContains(response, 'Tag > %s' % self.tag)
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
            description='This is the first project',)
        
        self.project2 = Project.objects.create(
            creator=self.admin,
            name='project2',
            description='This is the second project',)
        
        self.view_projects_url = reverse('epic.projects.views.view_projects')

    def testLoggedOut(self):
        response = self.client.get(self.view_projects_url)
        self.assertEqual(response.status_code, 200)
        
        for project in Project.objects.active():
            self.assertContains(response, project.name)
        
    def testLoggedInNotOwner(self):
        self.tryLogin('admin')
        
        response = self.client.get(self.view_projects_url)
        self.assertEqual(response.status_code, 200)
        
        for project in Project.objects.active():
            self.assertContains(response, project.name)
    
    def testLoggedInOwner(self):
        self.tryLogin('bob')
        
        response = self.client.get(self.view_projects_url)
        self.assertEqual(response.status_code, 200)
        
        for project in Project.objects.active():
            self.assertContains(response, project.name)

def save_n_projects(user, n):
    for i in range(n):
    	project = Project(creator=user)
    	project.name = 'test_project_name_' + str(i)
    	project.is_active = True
    	project.save()

class ViewProjectsFitExactlyOnePageTestCase(CustomTestCase):
    fixtures = ['projects_just_users']

    def setUp(self):
    	from views import PER_PAGE
    	save_n_projects(User.objects.get(username="bob"), PER_PAGE)
    
    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.projects.views.view_projects'))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for project in Project.objects.all():
    		self.assertContains(response, project.get_absolute_url())
    	
class ViewProjectsPartialPageTestCase(CustomTestCase):
    fixtures = ['projects_just_users']

    def setUp(self):
    	from views import PER_PAGE
    	save_n_projects(User.objects.get(username="bob"), PER_PAGE - 1)
    
    def testPaginated(self):    	
    	response = self.client.get(reverse('epic.projects.views.view_projects'))

    	self.assertNotContains(response, '<div class="pagination"')
    	
    	for project in Project.objects.all():
    		self.assertContains(response, project.get_absolute_url())
    	
class ViewProjectsTwoPagesTestCase(CustomTestCase):
    fixtures = ['projects_just_users']

    def setUp(self):
    	from views import PER_PAGE
    	self.per_page = PER_PAGE
    	save_n_projects(User.objects.get(username="bob"), 2 * self.per_page)
    
    def testFirstPage(self):    	
    	response = self.client.get(reverse('epic.projects.views.view_projects'))

    	self.assertContains(response, '<div class="pagination"')
    	self.assertContains(response, 'href="?page=2">2')
    	self.assertContains(response, 'href="?page=2">next')
    	self.assertContains(response, 'href="?page=2">last')    	
    	self.assertNotContains(response, 'href="?page=1"')

    	projects = Project.objects.active().order_by('-created_at')
    	on_first_page, on_second_page = projects[:self.per_page], projects[self.per_page:]
    	
    	for project in on_first_page:
    		self.assertContains(response, project.get_absolute_url())
    	
    	for project in on_second_page:
    		self.assertNotContains(response, project.get_absolute_url())
    
    def testSecondPage(self):
    	response = self.client.get(reverse('epic.projects.views.view_projects'), {'page': '2'})
    	
    	self.assertContains(response, '<div class="pagination"')
    	self.assertContains(response, 'href="?page=1">1')
    	self.assertNotContains(response, 'href="?page=2"')

    	projects = Project.objects.active().order_by('-created_at')
    	on_first_page, on_second_page = projects[:self.per_page], projects[self.per_page:]
    	
    	for project in on_first_page:
    		self.assertNotContains(response, project.get_absolute_url())
    	
    	for project in on_second_page:
    		self.assertContains(response, project.get_absolute_url())


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
            is_active=True)
        
        self.project2 = Project.objects.get(
            creator=self.admin,
            name='project2',
            description='This is the second project',
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
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')
        
        self.project1 = Project.objects.get(
            creator=self.bob, 
            name='project1',
            description='This is the first project',)
        
        self.project2 = Project.objects.create(
            creator=self.admin,
            name='project2',
            description='This is the second project',)
        
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

class DownloadAll(CustomTestCase):
    """ Test that downloading all the dataset fiels for a project works
        TODO: Actually open the zip file that is received
    """
    
    fixtures = ['projects_just_users']
    
    def setUp(self):
        self.bob = User.objects.get(username='bob')
        self.admin = User.objects.get(username='admin')   
        
        self.dataset = DataSet.objects.create(
            creator=self.bob, 
            name='asdfn83tn54yj', 
            description='this is the first dataset',
            is_active=True)
        
        self.datasetfile = DataSetFile.objects.create(
            parent_dataset=self.dataset,
            file_contents='asdfhnasdf')
        
    def testInactive(self):
        # Inactive projects should not allow you to download anything.
        project = Project.objects.create(creator=self.bob,
                                         name='asdf89234t6',
                                         description='asn0894jnnagnasd fasd',
                                         is_active=False)
        view_project_url = get_item_url(project, 
                                        'epic.projects.views.view_project')
        
        download_all_url = get_item_url(project,
                                        'epic.projects.views.download_all')
        self.tryLogin('bob')
        
        response = self.client.get(download_all_url)
        self.assertRedirects(response, view_project_url)

    def testActiveLoggedOut(self):
        # Logged out users should be redirected (to login).
        project = Project.objects.create(creator=self.bob,
                                         name='asdf89234t6',
                                         description='asn0894jnnagnasd fasd',
                                         is_active=True)
        
        download_all_url = get_item_url(project,
                                        'epic.projects.views.download_all')
         
        response = self.client.get(download_all_url)
        self.assertEqual(response.status_code, 302)
    
    def testActiveLoggedIn(self):
        # Logged in users should get the zip file
        self.tryLogin('bob')
        project = Project.objects.create(creator=self.bob,
                                         name='asdf89234t6',
                                         description='asn0894jnnagnasd fasd',
                                         is_active=True)
        
        download_all_url = get_item_url(project,
                                        'epic.projects.views.download_all')
         
        response = self.client.get(download_all_url)
        self.assertEqual(response['Content-Type'], 'application/zip')
