from datasets.models import DataSet

#######################
# Create the DataSets #
#######################

def _create_datasets():
	ds1 = DataSet.objects.create(creator=bob, name='ds1', description='this is the first dataset', slug='ds1')
	ds2 = DataSet.objects.create(creator=admin, name='ds2', description='this is the second dataset', slug='ds2')
	
######################################
# Generate the actual fixtures here. #
######################################

_create_datasets()