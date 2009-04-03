from django.core.management.base import BaseCommand
import os
import sys

#RELATIVE_FIXTURES_PATH = os.path.join("fixtures", "")
#RELATIVE_GENERATORS_PATH = os.path.join("generators", "")
RELATIVE_FIXTURES_PATH = "fixtures"
RELATIVE_GENERATORS_PATH = "generators"

INIT_MODULE_FILE_NAME = "__init__.py"

DJANGO_DUMP_DATA_INTO_FIXTURE_COMMAND = "dumpdata"
FIXTURE_FILE_NAME_EXTENSION = "json"

FIXTURE_FILE_WRITE_OPTIONS = "w"

class Command(BaseCommand):
	help = "Runs Fixture Generators and outputs appropriate fixtures."
	args = "[Appname ...]"
	
	requires_model_validation = True
	
	def handle(self, *application_labels, **options):
		num_application_labels = len(application_labels)
		current_directory_path = os.getcwd()
		did_user_supply_application_names = False
		
		if num_application_labels > 0:
			application_names = application_labels
			did_user_supply_application_names = True
		else:
			application_names = _application_names_from_directory_listing(current_directory_path)
			# Also append the current directory as an "application".
			application_names.append(".")
		
		# TODO: What about generating fixtures for things other than applications
		# (maybe a renaming is in order).
		applications = _generate_applications_from_application_names(application_names,
			did_user_supply_application_names)
		
		if len(applications) != 0:
			for application in applications:
				application.generate_and_output_fixtures()
		else:
			print "No applications with fixture generators have been specified.  No fixtures have been created."

class FixtureGenerator(object):
	def __init__(self, generator_module_name, init_file_name=None):
		self.generator_module_name = generator_module_name
		self.init_file_name = init_file_name
	
	def execute(self, fixtures_path, generators_path):
		#TODO: Maybe better way to do the return arguments?
		original_database_name = _setup_temporary_database()
		
		try:
			# Get the current "snapshot" of globals.
			current_globals = globals()
			
			# Reconstruct the generator file name.
			generator_file_name = self._reconstruct_generator_file_name(generators_path)
			
			# Construct the fixture file name.
			fixture_name = self._construct_fixture_file_name(fixtures_path)

			# Inform the user that the fixture is being generated.
			print "Using fixture generator '%s' to generate fixture '%s'." % (self.generator_module_name, fixture_name)
			
			self._run_appropriate_modules(generator_file_name,
										  current_globals)
			
			# Now, dump the contents of the database into an actual fixture.
			# TODO: (Check if the file already exists, and raise an exception if
			# it does.)
			self._generate_fixture(fixture_name)
			
		except Exception, exception:
			print exception
		
		_destroy_temporary_database_and_restore_original(original_database_name)
	
	def _reconstruct_generator_file_name(self, generators_path):
		generator_file_name = "%s.%s" % (self.generator_module_name, "py")
		
		return os.path.join(generators_path, generator_file_name)
	
	def _construct_fixture_file_name(self, fixtures_path):
		return "%s.%s" % (os.path.join(fixtures_path, self.generator_module_name), FIXTURE_FILE_NAME_EXTENSION)
	
	def _run_appropriate_modules(self,
								 generator_file_name,
								 current_globals):
		# If an init module was specified, execute it.
		if self.init_file_name:
			execfile(self.init_file_name, current_globals)
		
		# Finally, execute our fixture generator.
		execfile(generator_file_name, current_globals)
	
	def _generate_fixture(self, fixture_name):
		from django.core import management
		
		# Create the fixture file to write to.
		fixture_file = open(fixture_name, FIXTURE_FILE_WRITE_OPTIONS)
		
		# Redirect stdout to the fixture file.
		# TODO: Check for exceptions here
		
		original_stdout = sys.stdout
		sys.stdout = fixture_file
		
		# Call the dumpdata command on management, which is the same as running
		# python manage.py dumpdata
		# from the command line.  It just outputs to stdout, which is why we
		# needed to redirect stdout to our target file.
		management.call_command(DJANGO_DUMP_DATA_INTO_FIXTURE_COMMAND)
		
		# Restore stdout.
		sys.stdout = original_stdout
		
		# Close the fixture file.
		fixture_file.close()

class Application(object):
	def __init__(self, application_name, fixtures_path, generators_path):
		self.application_name = application_name
		self.fixtures_path = fixtures_path
		self.generators_path = generators_path
	
	def generate_and_output_fixtures(self):
		directory_listing = os.listdir(self.generators_path)
		
		# Check if the init file (__init__.py) exists as a valid init file in the
		# generators directory.
		if self._init_module_is_valid(directory_listing):
			init_file_name = INIT_MODULE_FILE_NAME
		else:
			init_file_name = None
		
		fixture_generators = self._gather_fixture_generators(init_file_name, directory_listing)
		
		print "Generating fixtures for application '%s'." % self.application_name
		
		self._execute_all_fixture_generators(fixture_generators)
		
		print ""
	
	def __str__(self):
		return "Name: '%s', Fixtures Path: '%s', Generators Path: '%s'" % (self.application_name, self.fixtures_path, self.generators_path)
	
	def _init_module_is_valid(self, directory_listing):
		absolute_init_module_file_name = os.path.join(self.generators_path, INIT_MODULE_FILE_NAME)
		
		return _file_is_valid_and_in_directory_listing(absolute_init_module_file_name,
			directory_listing)
	
	def _gather_fixture_generators(self, init_file_name, directory_listing):
		fixture_generators = []
		
		for listing in directory_listing:
			absolute_file_name = os.path.join(self.generators_path, listing)
			
			# If this listing is not the init module (__init__.py), split it by "."
			# (to see if it's a Python module).
			# Also, this listing should exist and should be a file.
			if _is_valid_file_and_not_init_module(absolute_file_name):
				split_file_name = listing.split(".")
				
				if _file_is_valid_python_module(split_file_name):
					reformed_module_name = ".".join(split_file_name[: -1])
					
					absolute_init_file_name = os.path.join(self.generators_path,
						init_file_name)
					
					fixture_generators.append(FixtureGenerator(reformed_module_name,
						absolute_init_file_name))
		
		return fixture_generators
	
	def _execute_all_fixture_generators(self, fixture_generators):
		for fixture_generator in fixture_generators:
			fixture_generator.execute(self.fixtures_path, self.generators_path)

def _file_is_valid_and_in_directory_listing(file_name, directory_listing):
	base_file_name = os.path.basename(file_name)
	
	if base_file_name in directory_listing and os.path.exists(file_name) and os.path.isfile(file_name):
		return True
	
	return False

def _is_valid_file_and_not_init_module(listing):
	listing_basename = os.path.basename(listing)
	
	if listing_basename != INIT_MODULE_FILE_NAME and os.path.exists(listing) and os.path.isfile(listing):
		return True
	
	return False

def _file_is_valid_python_module(split_file_name):
	if len(split_file_name) >= 1 and split_file_name[-1] == "py":
		return True
	
	return False
			
def _application_names_from_directory_listing(directory_path):
	directory_listing = os.listdir(directory_path)
	application_names = []
	
	for path in directory_listing:
		if os.path.isdir(path):
			application_names.append(path)
	
	return application_names

def _validate_application_path(application_path, application_name, should_print_upon_invalid_name):
	if not os.path.isdir(application_path):
		if should_print_upon_invalid_name:
			print "The application path %s either does not exist or is not a valid path.  Cannot add application '%s'." % (application_path, application_name)
		
		return False
	
	return True

def _validate_fixtures_path(fixtures_path, application_name, should_print_upon_invalid_name):
	if not os.path.exists(fixtures_path) or not os.path.isdir(fixtures_path):
		if should_print_upon_invalid_name:
			print "The fixtures path %s either does not exist or is not a valid path.  Cannot add application '%s'." % (fixtures_path, application_name)
		
		return False
	
	return True

def _validate_generators_path(generators_path, application_name, should_print_upon_invalid_name):
	if not os.path.exists(generators_path) or not os.path.isdir(generators_path):
		if should_print_upon_invalid_name:
			print "The generators path %s either does not exist or is not a valid path.  Cannot add application '%s'." % (generators_path, application_name)
		
		return False
	
	return True

def _validate_application(application_name,
						  application_path,
						  fixtures_path,
						  generators_path,
						  should_print_upon_invalid_name):
	# Verify that the application name is a directory, and print a message to
	# the user if not.
	if not _validate_application_path(application_path,
									  application_name,
									  should_print_upon_invalid_name):
		return False
	
	# The application name is at least a directory, so now verify that it
	# contains a fixtures/ directory.
	if not _validate_fixtures_path(fixtures_path,
								   application_name,
								   should_print_upon_invalid_name):
		return False
			
	# The fixtures directory exists within the application directory, so now
	# verify that the generators/ directory exists within it.
	if not _validate_generators_path(generators_path,
									 application_name,
									 should_print_upon_invalid_name):
		return False
	
	return True

def _generate_applications_from_application_names(original_application_names,
									 			  should_print_upon_invalid_name):
	filtered_applications = []
	absolute_path = os.getcwd()
	
	for application_name in original_application_names:
		application_path = os.path.join(absolute_path, application_name)
		fixtures_path = os.path.join(application_path, RELATIVE_FIXTURES_PATH)
		generators_path = os.path.join(fixtures_path, RELATIVE_GENERATORS_PATH)
		
		if _validate_application(application_name,
								 application_path,
								 fixtures_path,
								 generators_path,
								 should_print_upon_invalid_name):
			# This application passed all of our filters, so add it to the
			# list of application names that we're returning.
			filtered_applications.append(Application(application_name,
				fixtures_path,
				generators_path))

	return filtered_applications

def _setup_temporary_database():
	from django.conf import settings
	from django.db import connection
	
	original_database_name = settings.DATABASE_NAME
	
	connection.creation.create_test_db(0, autoclobber=False)
	
	return original_database_name

def _destroy_temporary_database_and_restore_original(original_database_name):
	from django.db import connection
	
	connection.creation.destroy_test_db(original_database_name, 0)
