import os
import sys

from django.core.management.base import BaseCommand

from epic.core.management.commands.generate_fixtures_real import generate_fixtures

class Command(BaseCommand):
    help = 'Runs Fixture Generators and outputs appropriate fixtures.'
    args = '[Appname ...]'
    
    requires_model_validation = True
    
    #MAIN ENTRY-POINT FROM COMMAND-LINE
    def handle(self, *app_labels, **options):
        print "Running generate_fixtures command"
        generate_fixtures(app_labels)
 