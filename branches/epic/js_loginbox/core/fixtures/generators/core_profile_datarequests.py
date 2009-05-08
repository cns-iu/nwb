from datarequests.models import DataRequest


################
# datarequest1 #
################

def _create_datarequest1():
    datarequest1 = DataRequest(
        creator=bill,
        name='Data Request Item 3',
        description='This is a datarequest which is canceled, and it ' + \
            'should be on the profile page <- by bill ',
        status='C',
        is_active=True)
    
    datarequest1.save()
    
    return datarequest1


################
# datarequest2 #
################

def _create_datarequest2():
    datarequest2 = DataRequest(
        creator=bill,
        name='Data Request Item 4',
        description='This is a datarequest which is unfulfilled, and it ' + \
            'should be on the profile page <- by bill ',
        status='U',
        is_active=True)
    
    datarequest2.save()
    
    return datarequest2


######################################
# Generate the actual fixtures here. #
######################################

datarequest1 = _create_datarequest1()
datarequest2 = _create_datarequest2()