from django.contrib.auth.models import User
from django.db import models
from epic.core.models import Item
from epic.datasets.models import DataSet



class ProjectManager(models.Manager):
    use_for_related_fields = True
    
    def active(self):
        return self.filter(is_active=True)

class Project(Item):
    objects = ProjectManager()
    
    # TODO: This should probably be Item, since Algorithms will certainly be
    # Project'able and DataRequests might be.  (But what about Projects?)
    datasets = models.ManyToManyField(DataSet, related_name='projects')
    
    def __unicode__(self):
        return 'Project %s (containing datasets %s)' % (self.name, self.datasets.active())
    
    @models.permalink
    def get_absolute_url(self):
        kwargs = {'item_id': self.id, 'slug': self.slug,}
        
        return ('epic.projects.views.view_project', [], kwargs)

class ProjectDownload(models.Model):
    parent_project = models.ForeignKey(Project, related_name="downloaded_project")
    downloaded_at = models.DateTimeField(auto_now_add=True, db_index=True)
    downloader = models.ForeignKey(User)
    
    class Admin:
        pass