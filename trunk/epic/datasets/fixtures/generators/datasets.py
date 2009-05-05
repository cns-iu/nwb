from datasets.models import DataSet

#######################
# Create the DataSets #
#######################

def _create_datasets():
    dataset1 = DataSet.objects.create(
        creator=bob, 
        name='dataset1', 
        description='this is the first dataset', 
        slug='dataset1',
        is_active=True)
    dataset1.render_description()
    dataset1.save()
    
    dataset2 = DataSet.objects.create(
        creator=admin,
        name='dataset2',
        description='this is the second dataset',
        slug='dataset2',
        is_active=True)
    dataset2.render_description()
    dataset2.save()

######################################
# Generate the actual fixtures here. #
######################################

_create_datasets()