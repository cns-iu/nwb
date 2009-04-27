from datasets.models import DataSet


###################################
# Utilities to generate Datasets. #
###################################

def _create_dataset(creator, ordinal, number):
    dataset_name = 'dataset%s' % number
    dataset_description = 'This is the %s dataset' % ordinal
    dataset_slug = dataset_name
    
    dataset = DataSet.objects.create(
        creator=creator,
        name=dataset_name,
        description=dataset_description,
        slug=dataset_slug,
        is_active=True)
    
    return dataset

#######################
# Create the DataSets #
#######################

def _create_datasets():
    dataset1 = _create_dataset(bob, 'first', 1)
    dataset2 = _create_dataset(admin, 'second', 2)
	
######################################
# Generate the actual fixtures here. #
######################################

_create_datasets()