import os
import shutil
import stat
import sys

from django.core import management
from django.core.management import call_command
from django.core.management.base import BaseCommand

from haystack import backend

from epic.search.management.commands import RELATIVE_WHOOSH_PATH
from epic.search.management.commands import fix_permissions
from epic.search.management.commands import on_rmtree_error
from epic.search.management.commands import remove_if_exists


class Command(BaseCommand):
    help = "Wraps haystack's rebuild_index command, but then alters permissions on generated directories so stuff actually works later on"
    args = "No arguments"
    
    requires_model_validation = True
    
    def handle(self, **options):
        working_path = os.getcwd()
        absolute_whoosh_path = os.path.join(working_path, RELATIVE_WHOOSH_PATH)

        remove_if_exists(absolute_whoosh_path)
        self._clear_index()
        self._rebuild_index(**options)
        fix_permissions(absolute_whoosh_path)

    def _clear_index(self):
        search_backend = backend.SearchBackend()
        search_backend.clear()
    
    def _rebuild_index(self, **options):
        call_command('update_index', **options)
