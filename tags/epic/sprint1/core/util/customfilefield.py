#modified from http://scottbarnham.com/blog/2007/07/31/uploading-images-to-a-dynamic-path-with-django/#working_solution
#and http://www.djangosnippets.org/snippets/949/

from django.db.models import FileField, FileField, signals
from django.conf import settings
from distutils.dir_util import mkpath
import shutil, os, glob, re

class CustomFileField(FileField):
    """Allows model instance to specify upload_to dynamically.

    Model class should have a method like:

        def get_upload_to(self, attname):
            return 'path/to/%d' % self.id

    PLEASE NOTE:
        This should be pointless once fs-refactor lands.
        See the following page for updates:

         http://code.djangoproject.com/ticket/5361

    Based closely on: http://scottbarnham.com/blog/2007/07/31/uploading-Files-to-a-dynamic-path-with-django/
    Later updated for newforms-admin by jamstooks: http://pandemoniumillusion.wordpress.com/2008/08/06/django-Filefield-and-filefield-dynamic-upload-path-in-newforms-admin/
    """
    def __init__(self, *args, **kwargs):
        if not 'upload_to' in kwargs:
            kwargs['upload_to'] = 'tmp'
        self.use_key = kwargs.get('use_key', False)
        if 'use_key' in kwargs:
            del(kwargs['use_key'])
        super(CustomFileField, self).__init__(*args, **kwargs)

    def contribute_to_class(self, cls, name):
        """Hook up events so we can access the instance."""
        super(CustomFileField, self).contribute_to_class(cls, name)
        signals.post_save.connect(self._move_File, sender=cls)

    def _move_File(self, instance=None, **kwargs):
        """
            Function to move the temporarily uploaded File to a more suitable directory
            using the model's get_upload_to() method.
        """
        if hasattr(instance, 'get_upload_to'):
            src = str(getattr(instance, self.attname))
            if src:
                m = re.match(r"%s/(.*)" % self.upload_to, src)
                if m:
                    if self.use_key:
                        dst = "%s%d_%s" % (
                          instance.get_upload_to(self.attname), 
                          instance.id, 
                          m.groups()[0]
                        )
                    else:
                        dst = "%s%s" % (
                          instance.get_upload_to(self.attname), 
                          m.groups()[0]
                        )
                    basedir = os.path.join(
                      settings.MEDIA_ROOT, 
                      os.path.dirname(dst)
                    )
                    fromdir = os.path.join(
                      settings.MEDIA_ROOT, 
                      src
                    )
                    mkpath(basedir)
                    shutil.move(fromdir, 
                      os.path.join(basedir, 
                                   m.groups()[0])
                    )
                    setattr(instance, self.attname, dst)
                    instance.save()

    def db_type(self):
        """Required by Django for ORM."""
        return 'varchar(200)'
