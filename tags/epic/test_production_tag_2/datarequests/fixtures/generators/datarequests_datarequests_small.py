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

######################################
# Generate the actual fixtures here. #
######################################

fulfilling_dataset = _create_fulfilling_dataset()

_create_c_datarequests()
_create_f_datarequests(fulfilling_dataset)
_create_u_datarequests()