import os
import sys

from django.core.management.base import BaseCommand

from util_classes.Application import Application


RELATIVE_FIXTURES_PATH = 'fixtures'
RELATIVE_GENERATORS_PATH = 'generators'

INIT_MODULE_FILE_NAME = '__init__.py'

class Command(BaseCommand):
    help = 'Runs Fixture Generators and outputs appropriate fixtures.'
    args = '[Appname ...]'
    
    requires_model_validation = True
    
    def handle(self, *application_labels, **options):
        applications = self._generate_applications(application_labels)
        self._generate_fixtures_from_applications(applications)
    
    # TODO: Add comment that describes our strategy for finding applications
    # (if user specified, then X, otherwise Y)
    # Our notion of an Application is any directory that has a subdirectory
    # called fixtures that has a subdirectory called generators.
    # If the user specified a list of application labels, we use that as the
    # set of Applications (or directories) to search in for fixture
    # generators.  If the user specified no application labels, we treat any
    # subdirectory as a potential Application.
    # Error messages are only printed if the user specified application
    # labels because a subdirectory that we find otherwise may not necessarily
    # be an application.
    def _generate_applications(self, application_labels):
        application_names, user_did_supply_application_names = \
            self._determine_application_names(application_labels)
        
        # TODO: What about generating fixtures for things other than
        # applications?  (Maybe a renaming is in order).
        applications = self._generate_applications_from_application_names(
            application_names, print_invalid_name_warnings=user_did_supply_application_names)
        
        return applications
    
    def _generate_fixtures_from_applications(self, applications):
        if len(applications) != 0:
            for application in applications:
                application.generate_and_output_fixtures()
        else:
            error_message = \
                'No applications with fixture generators have been ' + \
                'specified.  No fixtures have been created.'
            print error_message
    
    def _determine_application_names(self, application_labels):
        num_application_labels = len(application_labels)
        
        if num_application_labels > 0:
            application_names = application_labels
            user_did_supply_application_names = True
        else:
            application_names = self._get_application_names()
            user_did_supply_application_names = False
        
        #this is part of making sure we always do initial_data first
        application_names = list(application_names)
        if '.' in application_names:
            application_names.remove('.')
            application_names.insert(0, '.')
        
        return application_names, user_did_supply_application_names
    
    def _generate_applications_from_application_names(
            self, application_names, print_invalid_name_warnings=True):
        applications = []
        
        for application_name in application_names:
            application_path = _create_application_path(application_name)
            fixtures_path = _create_fixtures_path(application_path)
            generators_path = _create_generators_path(fixtures_path)
        
            if _validate_application(
                    application_name,
                    application_path,
                    fixtures_path,
                    generators_path,
                    print_invalid_name_warnings):
                new_application = Application(application_name, fixtures_path, generators_path)
                applications.append(new_application)

        return applications
    
    def _get_application_names(self):
        directory_listing = _get_current_directory_listing()
        application_names = []
        
        for path in directory_listing:
            self._append_path_as_application_name_if_appropriate(
                path, application_names)
        
        # Also append the current directory as an 'application'.
        application_names.append('.')
        
        return application_names
    
    def _append_path_as_application_name_if_appropriate(self, path, application_names):
        if os.path.isdir(path):
            application_names.append(path)

def _get_current_directory_listing():
    directory_path = os.getcwd()
    directory_listing = os.listdir(directory_path)
    
    return directory_listing

def _validate_application_path(application_path, application_name, print_invalid_name_warnings):
    if not os.path.isdir(application_path):
        if print_invalid_name_warnings:
            warning_message_template = \
                ('The application path %s either does not exist or is ' + \
                 'not a valid path.  Cannot add application "%s".')
            print warning_message_template % (application_path, application_name)
        
        return False
    
    return True

def _validate_fixtures_path(fixtures_path, application_name, print_invalid_name_warnings):
    if not os.path.exists(fixtures_path) or not os.path.isdir(fixtures_path):
        if print_invalid_name_warnings:
            warning_message_template = \
                ('The fixtures path %s either does not exist or is ' + \
                 'not a valid path.  Cannot add application "%s".')
            print warning_message_template % (fixtures_path, application_name)
        
        return False
    
    return True

def _validate_generators_path(generators_path, application_name, print_invalid_name_warnings):
    if not os.path.exists(generators_path) or not os.path.isdir(generators_path):
        if print_invalid_name_warnings:
            warning_message_template = \
                ('The generators path %s either does not exist or is ' + \
                 'not a valid path.  Cannot add application "%s".')
            print warning_message_template % (generators_path, application_name)
        
        return False
    
    return True

def _validate_application(
        application_name,
        application_path,
        fixtures_path,
        generators_path,
        print_invalid_name_warnings):
    # Verify that the application name is a directory, and print a message to the user if not.
    if not _validate_application_path(
            application_path, application_name, print_invalid_name_warnings):
        return False
    
    # The application name is at least a directory, so now verify that it contains a
    # fixtures/ directory.
    if not _validate_fixtures_path(fixtures_path, application_name, print_invalid_name_warnings):
        return False
            
    # The fixtures directory exists within the application directory, so now verify that the
    # generators/ directory exists within it.
    if not _validate_generators_path(
            generators_path, application_name, print_invalid_name_warnings):
        return False
    
    return True

def _create_application_path(application_name):
    absolute_path = os.getcwd()
    application_path = os.path.join(absolute_path, application_name)
    
    return application_path

def _create_fixtures_path(application_path):
    fixtures_path = os.path.join(application_path, RELATIVE_FIXTURES_PATH)
    
    return fixtures_path

def _create_generators_path(fixtures_path):
    generators_path = os.path.join(fixtures_path, RELATIVE_GENERATORS_PATH)
    
    return generators_path
