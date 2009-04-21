import os
import sys

from django.core.management.base import BaseCommand


RELATIVE_FIXTURES_PATH = 'fixtures'
RELATIVE_GENERATORS_PATH = 'generators'

INIT_MODULE_FILE_NAME = '__init__.py'

DJANGO_DUMP_DATA_INTO_FIXTURE_COMMAND = 'dumpdata'
FIXTURE_FILE_NAME_EXTENSION = 'json'

FIXTURE_FILE_WRITE_OPTIONS = 'w'

class Command(BaseCommand):
    help = 'Runs Fixture Generators and outputs appropriate fixtures.'
    args = '[Appname ...]'
    
    requires_model_validation = True
    
    def handle(self, *application_labels, **options):
        applications = self._generate_applications(application_labels)
        self._generate_fixtures_from_applications(applications)
    
    #TODO: Add comment that describes our strategy for finding applications (if user specified, then X, otherwise Y)
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
            application_names,
            print_invalid_name_warnings=user_did_supply_application_names)
        
        return applications
    
    def _generate_fixtures_from_applications(self, applications):
        if len(applications) != 0:
            for application in applications:
                application.generate_and_output_fixtures()
        else:
            print 'No applications with fixture generators have been ' + \
                'specified.  No fixtures have been created.'
    
    def _determine_application_names(self, application_labels):
        num_application_labels = len(application_labels)
        
        if num_application_labels > 0:
            application_names = application_labels
            user_did_supply_application_names = True
        else:
            application_names = self._get_application_names()
            user_did_supply_application_names = False
        
        return application_names, user_did_supply_application_names
    
    def _generate_applications_from_application_names(
            self,
            application_names,
            print_invalid_name_warnings=True):
        filtered_applications = []
        
        #TODO: take in a list and output a list (maybe we can eliminate a layer of functions here)
        for application_name in application_names:
            self._try_creating_application_and_appending_it(
                application_name,
                filtered_applications,
                print_invalid_name_warnings)
        
        return filtered_applications
    
    def _get_application_names(self):
        directory_listing = _get_current_directory_listing()
        application_names = []
        
        for path in directory_listing:
            self._append_path_as_application_name_if_appropriate(
                path, application_names)
        
        # Also append the current directory as an 'application'.
        application_names.append('.')
        
        return application_names
    
    def _try_creating_application_and_appending_it(
            self,
            application_name,
            applications,
            print_invalid_name_warnings):
        application_path = _create_application_path(application_name)
        fixtures_path = _create_fixtures_path(application_path)
        generators_path = _create_generators_path(fixtures_path)
        
        if _validate_application(application_name,
                                 application_path,
                                 fixtures_path,
                                 generators_path,
                                 print_invalid_name_warnings):
            # This application passed all of our filters, so add it to the
            # list of application names that we're returning.
            applications.append(
                Application(application_name, fixtures_path, generators_path))
    
    def _append_path_as_application_name_if_appropriate(
            self, path, application_names):
        if os.path.isdir(path):
            application_names.append(path)

#TODO: Put this in a separate file, in util_classes
#TODO: Write a short explanation of this class (what is it going to be useful for?)
class Application(object):
    def __init__(self, application_name, fixtures_path, generators_path):
        self.application_name = application_name
        self.fixtures_path = fixtures_path
        self.generators_path = generators_path
        self._fixture_generators = self._gather_fixture_generators()
    
    def __str__(self):
        #TODO: Names need to be at the level of intent
        user_readable_string_dictionary = {
            'application_name': self.application_name,
            'fixtures_path': self.fixtures_path,
            'generators_path': self.generators_path
        }
        
        user_readable_string = \
                'Name: "%(application_name)s", ' + \
                'Fixtures Path: "%(fixtures_path)s", ' + \
                'Generators Path: "%(generators_path)s"' % \
            user_readable_string_dictionary
        
        return user_readable_string
    
    def generate_and_output_fixtures(self):
        print 'Generating fixtures for application "%s".' % \
            self.application_name
        
        self._execute_all_fixture_generators()
        
        print ''
    
    #TODO: Needs a short, high-level description of our strategy for gathering fixture generators
    def _gather_fixture_generators(self):
        directory_listing = os.listdir(self.generators_path)
        init_file_name = self._determine_init_file_name(directory_listing)
        fixture_generators = []
        
        #TODO: Try passing the entire list as an argument, and also returning a list (should be cleaner)
        for listing in directory_listing:
            self._append_listing_as_fixture_generator_if_appropriate(
                listing, init_file_name, fixture_generators)
        
        return fixture_generators
    
    def _execute_all_fixture_generators(self):
        for fixture_generator in self._fixture_generators:
            fixture_generator.execute(self.fixtures_path,
                                      self.generators_path)
    
    def _determine_init_file_name(self, directory_listing):
        # Check if the init file (__init__.py) exists as a valid init file in
        # the generators directory.
        if self._init_module_is_valid(directory_listing):
            return INIT_MODULE_FILE_NAME
        
        return None
    
    def _append_listing_as_fixture_generator_if_appropriate(
            self, listing, init_file_name, fixture_generators):
        absolute_file_name = os.path.join(self.generators_path, listing)
        
        # If this listing is not the init module (__init__.py), split it
        # by '.' (to see if it's a Python module).
        # Also, this listing should exist and should be a file.
        if _is_valid_file_and_not_init_module(absolute_file_name):
            split_file_name = listing.split('.')
            
            if _file_is_valid_python_module(split_file_name):
                reformed_module_name = '.'.join(split_file_name[: -1])
                
                absolute_init_file_name = \
                    os.path.join(self.generators_path, init_file_name)
                
                new_fixture_generator = FixtureGenerator(
                    reformed_module_name, absolute_init_file_name)
                
                fixture_generators.append(new_fixture_generator)
    
    def _init_module_is_valid(self, directory_listing):
        absolute_init_module_file_name = \
            os.path.join(self.generators_path, INIT_MODULE_FILE_NAME)
        
        return _file_is_valid_and_in_directory_listing(
            absolute_init_module_file_name, directory_listing)

#TODO: Move to another file in util_classes
class FixtureGenerator(object):
    def __init__(self, generator_module_name, init_file_name=None):
        self.generator_module_name = generator_module_name
        self.init_file_name = init_file_name
    
        #TODO: Add comments (either at the top here, or in high-level summary style throughout code)
    def execute(self, fixtures_path, generators_path):
        # TODO: Maybe better way to do the return arguments?
        #TODO: Why are we setting up a temporary database?
        original_database_name = _setup_temporary_database()
        
        #TODO: move these input arguments into the constructor
        self._try_to_generate_fixture(fixtures_path, generators_path)
        
        _destroy_temporary_database_and_restore_original(
            original_database_name)
    
    def _reconstruct_generator_file_name(self, generators_path):
        generator_file_name = '%s.py' % self.generator_module_name
        
        return os.path.join(generators_path, generator_file_name)
    
    def _construct_fixture_file_name(self, fixtures_path):
        absolute_base_filename = \
            os.path.join(fixtures_path, self.generator_module_name)
        
        return '%s.%s' % (absolute_base_filename, FIXTURE_FILE_NAME_EXTENSION)
    
    def _run_appropriate_modules(self,
                                 generator_file_name,
                                 current_globals):
        # If an init module was specified, execute it.
        if self.init_file_name:
            execfile(self.init_file_name, current_globals)
        
        # Finally, execute our fixture generator.
        execfile(generator_file_name, current_globals)
    
    def _try_to_generate_fixture(self, fixtures_path, generators_path):
        try:
            # Get the current 'snapshot' of globals.
            current_globals = globals()
            
            #TODO: WHY am I doing any of this? (this is always important)
            generator_file_name = \
                self._reconstruct_generator_file_name(generators_path)
            
            # Construct the fixture file name.
            fixture_name = self._construct_fixture_file_name(fixtures_path)

            # Inform the user that the fixture is being generated.
            print 'Using fixture generator "%s" to generate fixture "%s".' % \
                (self.generator_module_name, fixture_name)
            
            #TODO: No way.  
            self._run_appropriate_modules(
                generator_file_name, current_globals)
            
            self._dump_results_of_generator_to_fixture_file(fixture_name)
            
        except Exception, generate_fixture_exception:
            print generate_fixture_exception
        
    
    def _dump_results_of_generator_to_fixture_file(self, fixture_name):
        from django.core import management
        
        # Create the fixture file to write to.
        fixture_file = open(fixture_name, FIXTURE_FILE_WRITE_OPTIONS)
        
        # Redirect stdout to the fixture file.
        # TODO: Check for exceptions here
        
        original_stdout = sys.stdout
        sys.stdout = fixture_file
        
        # Call the dumpdata command on management, which is the same as
        # running
        # python manage.py dumpdata
        # from the command line.  It just outputs to stdout, which is why we
        # needed to redirect stdout to our target file.
        management.call_command(DJANGO_DUMP_DATA_INTO_FIXTURE_COMMAND)
        
        # Restore stdout.
        sys.stdout = original_stdout
        
        # Close the fixture file.
        fixture_file.close()

def _setup_temporary_database():
    from django.conf import settings
    from django.db import connection
    
    original_database_name = settings.DATABASE_NAME
    
    connection.creation.create_test_db(0, autoclobber=False)
    
    return original_database_name

def _file_is_valid_and_in_directory_listing(file_name, directory_listing):
    base_file_name = os.path.basename(file_name)
    
    if base_file_name in directory_listing and \
       os.path.exists(file_name) and \
       os.path.isfile(file_name):
        return True
    
    return False

def _is_valid_file_and_not_init_module(listing):
    listing_basename = os.path.basename(listing)
    
    if listing_basename != INIT_MODULE_FILE_NAME and \
       os.path.exists(listing) and \
       os.path.isfile(listing):
        return True
    
    return False

def _file_is_valid_python_module(split_file_name):
    if len(split_file_name) >= 1 and split_file_name[-1] == 'py':
        return True
    
    return False

def _get_current_directory_listing():
    directory_path = os.getcwd()
    directory_listing = os.listdir(directory_path)
    
    return directory_listing

def _validate_application_path(
        application_path, application_name, print_invalid_name_warnings):
    if not os.path.isdir(application_path):
        if print_invalid_name_warnings:
            print ('The application path %s either does not exist or is ' + \
                    'not a valid path.  Cannot add application "%s".') % \
                (application_path, application_name)
        
        return False
    
    return True

def _validate_fixtures_path(
        fixtures_path, application_name, print_invalid_name_warnings):
    if not os.path.exists(fixtures_path) or not os.path.isdir(fixtures_path):
        if print_invalid_name_warnings:
            print ('The fixtures path %s either does not exist or is ' + \
                    'not a valid path.  Cannot add application "%s".') % \
                (fixtures_path, application_name)
        
        return False
    
    return True

def _validate_generators_path(
        generators_path, application_name, print_invalid_name_warnings):
    if not os.path.exists(generators_path) or \
       not os.path.isdir(generators_path):
        if print_invalid_name_warnings:
            print ('The generators path %s either does not exist or is ' + \
                    'not a valid path.  Cannot add application "%s".') % \
                (generators_path, application_name)
        
        return False
    
    return True

def _validate_application(application_name,
                          application_path,
                          fixtures_path,
                          generators_path,
                          print_invalid_name_warnings):
    # Verify that the application name is a directory, and print a message to
    # the user if not.
    if not _validate_application_path(
            application_path, application_name, print_invalid_name_warnings):
        return False
    
    # The application name is at least a directory, so now verify that it
    # contains a fixtures/ directory.
    if not _validate_fixtures_path(
            fixtures_path, application_name, print_invalid_name_warnings):
        return False
            
    # The fixtures directory exists within the application directory, so now
    # verify that the generators/ directory exists within it.
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

def _destroy_temporary_database_and_restore_original(original_database_name):
    from django.db import connection
    
    connection.creation.destroy_test_db(original_database_name, 0)
