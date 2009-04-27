import os
import sys

from FixtureGenerator import FixtureGenerator


INIT_MODULE_FILE_NAME = '__init__.py'

class Application(object):
    """
    An Application is a collection of fixture generators that holds information
    regarding where those fixture generators should output their fixtures to.
    See FixtureGenerator for a more detailed explanation of what a fixture
    generator is.
    """
    
    def __init__(self, application_name, fixtures_path, generators_path):
        self.application_name = application_name
        self.fixtures_path = fixtures_path
        self.generators_path = generators_path
        self._fixture_generators = self._gather_fixture_generators()
    
    def __str__(self):
        data_for_user_readable_string = {
            'application_name': self.application_name,
            'fixtures_path': self.fixtures_path,
            'generators_path': self.generators_path
        }
        
        user_readable_string = \
                'Name: "%(application_name)s", ' + \
                'Fixtures Path: "%(fixtures_path)s", ' + \
                'Generators Path: "%(generators_path)s"' % \
            data_for_user_readable_string
        
        return user_readable_string
    
    def generate_and_output_fixtures(self):
        print 'Generating fixtures for application "%s".' % \
            self.application_name
        
        self._execute_all_fixture_generators()
        
        print ''
    
    # TODO: Needs a short, high-level description of our strategy for gathering fixture generators
    # TODO: A little bit shorter than this kthxbai.
    def _gather_fixture_generators(self):
        """
        Look in the application/fixtures/generators directory for all valid
        Python modules and create a set of FixtureGenerators from them.
        
        Look through all the applications in the root directory of this project.
        FixtureGenerator modules should be found in the fixtures/generators directory of each application.
        Create FixtureGenerator objects from the FixtureGenerator modules.
        Collect all these FixtureGenerators, and return them in a list.
        
        1.
        Generate applications.
        Generate fixtures from those applications.
        
        
        Generate fixtures for each application, using fixture generator scripts found in each application.
        
        2.
        
        
        
        For each application in the root directory:
            Get all the modules in that application's fixtures/generators directory.
              For each of these fixture generator modules:
                Create a fixture generator object based on that module.
                Append that fixture generator object to a list
        Return the list of fixture generators.
        
       
                
        
        
        
        
        
        
        
           For each of these fixture generator modules:
                Create a fixture generator object based on the module.
                Append that fixture generator object to our list of all fixture generators
                
        For each application in the root directory:
            Get all the modules in that application's fixtures/generators directory.
            Create Fixture Generators based on those modules.
        Return the Fixture Generators we created.       
        
        
        See FixtureGenerator for a more detailed explanation of what a fixture
        generator is.
        """
        directory_listing = os.listdir(self.generators_path)
        init_file_name = self._determine_init_file_name(directory_listing)
        
        fixture_generators = \
            self._create_fixture_generators_from_directory_listing(
                directory_listing, init_file_name)
        
        return fixture_generators
    
    def _execute_all_fixture_generators(self):
        for fixture_generator in self._fixture_generators:
            fixture_generator.execute()
    
    def _determine_init_file_name(self, directory_listing):
        # Check if the init file (__init__.py) exists as a valid init file in
        # the generators directory.
        if self._init_module_is_valid(directory_listing):
            return INIT_MODULE_FILE_NAME
        
        return None
    
    def _create_fixture_generators_from_directory_listing(
            self, directory_listing, init_file_name):
        fixture_generators = []
        
        for directory_element in directory_listing:
            absolute_file_name = \
                os.path.join(self.generators_path, directory_element)
            
            # If this listing is not the init module (__init__.py), split it
            # by '.' (to see if it's a Python module).
            # Also, this listing should exist and should be a file.
            if _is_valid_file_and_not_init_module(absolute_file_name):
                split_file_name = directory_element.split('.')
                
                if _file_is_valid_python_module(split_file_name):
                    reformed_module_name = '.'.join(split_file_name[: -1])
                    
                    absolute_init_file_name = \
                        os.path.join(self.generators_path, init_file_name)
                    
                    new_fixture_generator = FixtureGenerator(
                        self.fixtures_path,
                        self.generators_path,
                        reformed_module_name,
                        absolute_init_file_name)
                    
                    fixture_generators.append(new_fixture_generator)
        
        return fixture_generators
    
    def _init_module_is_valid(self, directory_listing):
        absolute_init_module_file_name = \
            os.path.join(self.generators_path, INIT_MODULE_FILE_NAME)
        
        return _file_is_valid_and_in_directory_listing(
            absolute_init_module_file_name, directory_listing)

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
