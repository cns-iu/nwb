from datasets.models import DataSet


############
# dataset1 #
############

def _create_dataset1():
    dataset1 = DataSet(creator=bill,
                       name='Item 1',
                       description='This is the first item (by bill)',
                       is_active=True)
    
    dataset1.save()
    
    return dataset1


######################################
# Generate the actual fixtures here. #
######################################

dataset1 = _create_dataset1()