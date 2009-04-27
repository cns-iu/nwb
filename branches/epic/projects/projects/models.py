from django.db import models

from epic.core.models import Item
from epic.datasets.models import DataSet

class Project(Item):
    datasets = models.ManyToManyField(DataSet, related_name='projects')
    
    class Admin:
        pass
    
    def __unicode__(self):
        return 'Project %s (containing datasets %s)' % \
            (self.name, self.datasets.all())
    
    @models.permalink
    def get_absolute_url(self):
        kwargs = {'item_id': self.id, 'slug': self.slug,}
        
        return ('epic.projects.views.view_project', [], kwargs)
    
    # TODO: Implement this for real.
    def get_download_all_files_url(self):
        return "http://www.PLACEHOLDER_FOR_DOWNLOAD_ALL_FILES_URL.com"