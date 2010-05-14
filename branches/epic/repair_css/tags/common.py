# Font size distribution algorithms
LOGARITHMIC = 1
LINEAR = 2


# Tag is meant to be a template-displayable tag.  It has no actual ties to the
# database/Tagging model.
# The reason why it exists is because it contains additional data on Taggings that
# is not actually stored in Tagging.
class Tag:
	def __init__(self, tag_name, count):
		self.tag_name = tag_name
		self.count = count
	
	def get_tag_url(self):
		from models import Tagging
		return Tagging.objects.get_url_for_tag(self.tag_name)
