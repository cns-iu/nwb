from django.db import models

class GeoLoc(models.Model):
	GEOLOC_ADDRESS_MAX = 64
	longitude = models.DecimalField(max_digits=9, decimal_places=6)
	latitude = models.DecimalField(max_digits=9, decimal_places=6)
	canonical_name = models.CharField(max_length=GEOLOC_ADDRESS_MAX)
	
	class Meta:
		unique_together = (('latitude', 'longitude', 'canonical_name'),)
		
	def __unicode__(self):
		urlNameList = []
		for dataset in self.datasets.all():
			urlNameList.append([dataset.name, dataset.get_absolute_url()])
		return '[[%s, %s], %s, %s]' % (self.latitude, self.longitude, self.canonical_name, urlNameList)