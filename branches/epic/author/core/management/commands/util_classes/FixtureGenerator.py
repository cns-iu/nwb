import os
import sys


FIXTURE_FILE_NAME_EXTENSION = 'json'
DJANGO_DUMP_DATA_INTO_FIXTURE_COMMAND = 'dumpdata'

class FixtureGenerator(object):
    def __init__(self,
                 fixtures_path,
                 generators_path,
                 generator_module_name,
                 init_file_name=None):
        self.fixtures_path = fixtures_path
        self.generators_path = generators_path
        self.generator_module_name = generator_module_name
        self.init_file_name = init_file_name
    
    def execute(self):
        # We need a temporary database to store the data created within the
        # fixture generators.  If we don't setup the temporary database, our
        # actual production database will most likely be dirtied.
        original_database_name = _setup_temporary_database()
        
        self._try_to_generate_fixture()
        
        _destroy_temporary_database_and_restore_original(
            original_database_name)
    
    def _try_to_generate_fixture(self):
        try:
            # The generator file name is relative to its path.  Since we're
            # not executing from that path, we need the full file path to it.
            generator_file_name = self._get_full_generator_file_name()
            
            fixture_name = \
                self._construct_fixture_file_name(self.fixtures_path)
            
            print 'Using fixture generator "%s" to generate fixture "%s".' % \
                (self.generator_module_name, fixture_name)
              
            self._run_file_as_python_module(generator_file_name)
            
            self._dump_results_of_generator_to_fixture_file(fixture_name)
            
        except Exception, generate_fixture_exception:
            print generate_fixture_exception
    
    def _get_full_generator_file_name(self):
        generator_file_name = '%s.py' % self.generator_module_name
        
        return os.path.join(self.generators_path, generator_file_name)
    
    def _construct_fixture_file_name(self, fixtures_path):
        absolute_base_filename = \
            os.path.join(fixtures_path, self.generator_module_name)
        
        return '%s.%s' % (absolute_base_filename, FIXTURE_FILE_NAME_EXTENSION)
    
    def _run_file_as_python_module(self, generator_file_name):
        # Get the current 'snapshot' of globals.
        current_globals = globals()
        
        # If an init module was specified, execute it.
        if self.init_file_name:
            execfile(self.init_file_name, current_globals)
        
        # Finally, execute our fixture generator.
        execfile(generator_file_name, current_globals)
    
    def _dump_results_of_generator_to_fixture_file(self, fixture_name):
        from django.core import management
        
        # Create the fixture file to write to.
        fixture_file = open(fixture_name, 'w')
        
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

def _destroy_temporary_database_and_restore_original(original_database_name):
    from django.db import connection
    
    connection.creation.destroy_test_db(original_database_name, 0)
