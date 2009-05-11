from datarequests.models import DataRequest


#######################################
# Utilities to generate DataRequests. #
#######################################

def _create_datarequest(creator, status, ordinal, number):
    if status == 'C':
        type_string = 'canceled'
    elif status == 'F':
        type_string = 'fulfilled'
    else:
        type_string = 'unfulfilled'
    
    datarequest_name = '%s_datarequest%s' % (type_string, number)
    datarequest_description = 'The %s datarequest' % ordinal
    
    datarequest = DataRequest.objects.create(
        creator=creator,
        name=datarequest_name,
        description=datarequest_description,
        status=status,
        is_active=True)
    
    return datarequest
                                             

######################################
# Create the canceled DataRequests.  #
######################################

def _create_canceled_datarequests():
    canceled_datarequest1 = _create_datarequest(bob, 'C', 'first', 1)
    canceled_datarequest2 = _create_datarequest(admin, 'C', 'second', 2)
    canceled_datarequest3 = _create_datarequest(bob, 'C', 'third', 3)
    canceled_datarequest4 = _create_datarequest(admin, 'C', 'fourth', 4)
    canceled_datarequest5 = _create_datarequest(bob, 'C', 'fifth', 5)

######################################
# Create the fulfilled DataRequests. #
######################################

def _create_fulfilled_datarequests():
    fulfilled_datarequest1 = _create_datarequest(bob, 'F', 'first', 1)
    fulfilled_datarequest2 = _create_datarequest(admin, 'F', 'second', 2)
    fulfilled_datarequest3 = _create_datarequest(bob, 'F', 'third', 3)
    fulfilled_datarequest4 = _create_datarequest(admin, 'F', 'fourth', 4)
    fulfilled_datarequest5 = _create_datarequest(bob, 'F', 'fifth', 5)
    fulfilled_datarequest6 = _create_datarequest(admin, 'F', 'sixth', 6)

########################################
# Create the unfulfilled DataRequests. #
########################################

def _create_unfulfilled_datarequests():
    unfulfilled_datarequest1 = _create_datarequest(bob, 'U', 'first', 1)
    unfulfilled_datarequest2 = _create_datarequest(admin, 'U', 'second', 2)
    unfulfilled_datarequest3 = _create_datarequest(bob, 'U', 'third', 3)
    unfulfilled_datarequest4 = _create_datarequest(admin, 'U', 'fourth', 4)

######################################
# Generate the actual fixtures here. #
######################################

_create_canceled_datarequests()
_create_fulfilled_datarequests()
_create_unfulfilled_datarequests()