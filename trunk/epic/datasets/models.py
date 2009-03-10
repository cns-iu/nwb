"""
These are the DataSet and DataSetFile models.

"""
from epic.core.models import Item
from epic.djangoratings import RatingField

from django.db import models
from django.contrib.auth.models import User

RATING_SCALE = [(n, str(n)) for n in range(1, 6)]

class DataSet(Item):
	"""
	This is the DataSet model.  You work with it as follows:
 	
	>>> user = User(username="bob23452345", password="blah")
	>>> user.save()
	>>> user
	<User: bob23452345>
	
	>>> dataset = DataSet(creator=user, name="Item #1", description="This is the first item")
	>>> dataset.save()
	
	"""
	
	rating = RatingField(choices=RATING_SCALE)
	
	#supposedly better to do this some other newer way where it's not nested
	class Admin:
		pass
	
	def __unicode__(self):
		return "Dataset %s created at %s" % (self.name, self.created_at)
	
	@models.permalink
	def get_absolute_url(self):
		return ("epic.datasets.views.view_dataset", [], {'dataset_id':self.id})
	
    
class DataSetFile(models.Model):
    parent_dataset = models.ForeignKey(DataSet, related_name="files")
    file_contents = models.FileField(upload_to="dataset_files")
    
    class Admin:
		pass

    def __unicode__(self):
        return self.get_short_name()
       
    #returns the non-path component of the file name (the real name)
    def get_short_name(self):
    	before_last_slash, slash, after_last_slash = self.file_contents.name.rpartition('/')
    	short_name = after_last_slash
    	return short_name
