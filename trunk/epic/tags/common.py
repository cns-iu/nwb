# Font size distribution algorithms
LOGARITHMIC = 1
LINEAR = 2

class Tag:
	def __init__(self, tag_name, created_at, count):
		self.tag_name = tag_name
		self.created_at = created_at
		self.count = count
	
	def get_tag_url(self):
		from tags.models import Tagging
		
		return Tagging.objects.get_url_for_tag(self.tag_name)
