"""
These are the DataSet and DataSetFile models.

"""
from epic.core.models import Item
from epic.djangoratings import RatingField
from epic.core.util.customfilefield import CustomFileField

from epic.geoloc.models import GeoLoc
from epic.core.util.multifile import MultiFileField
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
	geolocations = models.ManyToManyField(GeoLoc, related_name='datasets')
	#supposedly better to do this some other newer way where it's not nested
	class Admin:
		pass
	
	def __unicode__(self):
		return "Dataset %s" % (self.name)
	
	@models.permalink
	def get_absolute_url(self):
		if self.slug:
			kwargs = {'item_id':self.id, 'slug':self.slug,}
		else:
			kwargs = {'item_id':self.id,}
		return ("epic.datasets.views.view_dataset", [], kwargs)
	
    
class DataSetFile(models.Model):
	
	parent_dataset = models.ForeignKey(DataSet, related_name="files")
	file_contents = CustomFileField()
	uploaded_at = models.DateTimeField(auto_now_add=True, db_index=True)

	class Admin:
		pass
	
	def __unicode__(self):
		return self.get_short_name()
       

	def get_short_name(self):
		#returns the non-path component of the file name (the real name)
		before_last_slash, slash, after_last_slash = self.file_contents.name.rpartition('/')
		short_name = after_last_slash
		return short_name
	
	def get_upload_to(self, attrname=None):
		return str("dataset_files/%d/" % self.parent_dataset.id)