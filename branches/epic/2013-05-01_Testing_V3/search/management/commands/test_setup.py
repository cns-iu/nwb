#import os
#
#from django.core import management
#from django.core.management import call_command
#from django.core.management.base import BaseCommand
#
#from epic.search.management.commands import RELATIVE_WHOOSH_PATH
#from epic.search.management.commands import TEMP_WHOOSH_DIRECTORY
#from epic.search.management.commands import fix_permissions
#from epic.search.management.commands import on_rmtree_error
#from epic.search.management.commands import remove_if_exists
#
#
## TODO: I know this and test_teardown could probably be combined into one command that takes an
## appropriate option, but MEH.
#class Command(BaseCommand):
#    help = "Hack for testing"
#    args = "No argmuments"
#    
#    requires_model_validation = True
#    
#    def handle(self, **options):
#        working_path = os.getcwd()
#        absolute_whoosh_path = os.path.join(working_path, RELATIVE_WHOOSH_PATH)
#        temp_absolute_whoosh_path = os.path.join(working_path, TEMP_WHOOSH_DIRECTORY)
#
#        remove_if_exists(temp_absolute_whoosh_path)
#        self._rename_current(absolute_whoosh_path, temp_absolute_whoosh_path)
#        self._rebuild_index(**options)
#
#    def _rename_current(self, absolute_whoosh_path, temp_absolute_whoosh_path):
#        if os.path.exists(absolute_whoosh_path):
#            os.rename(absolute_whoosh_path, temp_absolute_whoosh_path)
#
#    def _rebuild_index(self, **options):
#        call_command('epic_rebuild_index', **options)
