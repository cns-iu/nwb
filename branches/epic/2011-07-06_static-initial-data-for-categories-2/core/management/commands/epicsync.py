from django.core.management.base import BaseCommand


class Command(BaseCommand):
    help = "syncdb replacement"
    args = "No argmuments"
    
    requires_model_validation = True
    
    def handle(self, *args, **kwargs):
        from django.core import management
        management.call_command('syncdb', interactive=False)
        management.call_command('reset', 'auth', 'sites', 'contenttypes', interactive=False)
        management.call_command('loaddata', 'initial_data')