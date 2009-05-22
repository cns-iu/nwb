from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet


################################
# Create a fulfilling DataSet. #
################################

# TODO: Actually test for this on the New DataRequest page.
def _create_fulfilling_dataset():
    fulfilling_dataset = DataSet.objects.create(
        creator=bob,
        name='Fulfilling DataSet',
        description='I fulfill data requests!',
        is_active=True)
    
    return fulfilling_dataset

#####################################
# Create the Canceled DataRequests. #
#####################################

def _create_c_datarequests():
    canceled_datarequest1 = DataRequest.objects.create(
        creator=bob,
        name='canceled_datarequest1',
        description='The first canceled datarequest',
        status='C',
        is_active=True)
    
    canceled_datarequest2 = DataRequest.objects.create(
        creator=admin,
        name='canceled_datarequest2',
        description='The second canceled datarequest',
        status='C',
        is_active=True)
    
    canceled_datarequest3 = DataRequest.objects.create(
        creator=bob,
        name='canceled_datarequest3',
        description='The third canceled datarequest',
        status='C',
        is_active=True)
    
    canceled_datarequest4 = DataRequest.objects.create(
        creator=admin,
        name='canceled_datarequest4',
        description='The fourth canceled datarequest',
        status='C',
        is_active=True)
    
    canceled_datarequest5 = DataRequest.objects.create(
        creator=bob,
        name='canceled_datarequest5',
        description='The fifth canceled datarequest',
        status='C',
        is_active=True)

######################################
# Create the Fulfilled DataRequests. #
######################################

def _create_f_datarequests(fulfilling_dataset):
    fulfilled_datarequest1 = DataRequest.objects.create(
        creator=bob,
        name='fulfilled_datarequest1',
        description='The first fulfilled datarequest',
        status='F',
        fulfilling_item=fulfilling_dataset,
        is_active=True)
    
    fulfilled_datarequest2 = DataRequest.objects.create(
        creator=admin,
        name='fulfilled_datarequest2',
        description='The second fulfilled datarequest',
        status='F',
        fulfilling_item=fulfilling_dataset,
        is_active=True)
    
    fulfilled_datarequest3 = DataRequest.objects.create(
        creator=bob,
        name='fulfilled_datarequest3',
        description='The third fulfilled datarequest',
        status='F',
        fulfilling_item=fulfilling_dataset,
        is_active=True)
    
    fulfilled_datarequest4 = DataRequest.objects.create(
        creator=admin,
        name='fulfilled_datarequest4',
        description='The fourth fulfilled datarequest',
        status='F',
        fulfilling_item=fulfilling_dataset,
        is_active=True)
    
    fulfilled_datarequest5 = DataRequest.objects.create(
        creator=bob,
        name='fulfilled_datarequest5',
        description='The fifth fulfilled datarequest',
        status='F',
        fulfilling_item=fulfilling_dataset,
        is_active=True)
    
    fulfilled_datarequest6 = DataRequest.objects.create(
        creator=admin,
        name='fulfilled_datarequest6',
        description='The sixth fulfilled datarequest',
        status='F',
        fulfilling_item=fulfilling_dataset,
        is_active=True)

########################################
# Create the unfulfilled DataRequests. #
########################################

def _create_u_datarequests():
    unfulfilled_datarequest1 = DataRequest.objects.create(
        creator=bob,
        name='unfulfilled_datarequest1',
        description='The first unfulfilled datarequest',
        status='U',
        is_active=True)
    
    unfulfilled_datarequest2 = DataRequest.objects.create(
        creator=admin,
        name='unfulfilled_datarequest2',
        description='The second unfulfilled datarequest',
        status='U',
        is_active=True)
    
    unfulfilled_datarequest3 = DataRequest.objects.create(
        creator=bob,
        name='unfulfilled_datarequest3',
        description='The third unfulfilled datarequest',
        status='U',
        is_active=True)
    
    unfulfilled_datarequest4 = DataRequest.objects.create(
        creator=admin,
        name='unfulfilled_datarequest4',
        description='The fourth unfulfilled datarequest',
        status='U',
        is_active=True)

######################################
# Generate the actual fixtures here. #
######################################

fulfilling_dataset = _create_fulfilling_dataset()

_create_c_datarequests()
_create_f_datarequests(fulfilling_dataset)
_create_u_datarequests()