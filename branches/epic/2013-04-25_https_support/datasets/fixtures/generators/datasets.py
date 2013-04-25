from datasets.models import DataSet

#######################
# Create the DataSets #
#######################

def _create_datasets():
    dataset1 = DataSet.objects.create(
        creator=bob, 
        name='dataset1', 
        description='this is the first dataset',
        is_active=True)
    
    dataset2 = DataSet.objects.create(
        creator=admin,
        name='dataset2',
        description='this is the second dataset',
        is_active=True)

######################################
# Generate the actual fixtures here. #
######################################

_create_datasets()