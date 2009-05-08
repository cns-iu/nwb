from datarequests.models import DataRequest

######################################
# Create the Canceled DataRequests.  #
######################################

def _create_canceled_datarequests():
	first_canceled_data_request = DataRequest.objects.create(creator=bob, 
                                                             name='canceled_datarequest1', 
                                                             description='The first canceled datarequest', 
                                                             status='C',
                                                             is_active=True)
	second_canceled_data_request = DataRequest.objects.create(creator=admin, 
                                                              name='canceled_datarequest2', 
                                                              description='The second canceled datarequest', 
                                                              status='C',
                                                              is_active=True)
	third_canceled_data_request = DataRequest.objects.create(creator=bob, 
                                                             name='canceled_datarequest3', 
                                                             description='The third canceled datarequest', 
                                                             status='C',
                                                             is_active=True)
	fourth_canceled_data_request = DataRequest.objects.create(creator=admin, 
                                                              name='canceled_datarequest4', 
                                                              description='The fourth canceled datarequest', 
                                                              status='C',
                                                              is_active=True)
	fifth_canceled_data_request = DataRequest.objects.create(creator=bob, 
                                                             name='canceled_datarequest5', 
                                                             description='The fifth canceled datarequest', 
                                                             status='C',
                                                             is_active=True)
	return

######################################
# Create the Fulfilled DataRequests. #
######################################

def _create_fulfilled_datarequests():
	first_fulfilled_data_request = DataRequest.objects.create(creator=bob, 
                                                              name='fulfilled_datarequest1', 
                                                              description='The first fulfilled datarequest', 
                                                              status='F', 
                                                              is_active=True)
	second_fulfilled_data_request = DataRequest.objects.create(creator=admin, 
                                                               name='fulfilled_datarequest2', 
                                                               description='The second fulfilled datarequest', 
                                                               status='F', 
                                                               is_active=True)
	third_fulfilled_data_request = DataRequest.objects.create(creator=bob, 
                                                              name='fulfilled_datarequest3', 
                                                              description='The third fulfilled datarequest', 
                                                              status='F', 
                                                              is_active=True)
	fourth_fulfilled_data_request = DataRequest.objects.create(creator=admin, 
                                                               name='fulfilled_datarequest4', 
                                                               description='The fourth fulfilled datarequest', 
                                                               status='F', 
                                                               is_active=True)
	fifth_fulfilled_data_request = DataRequest.objects.create(creator=bob, 
                                                              name='fulfilled_datarequest5', 
                                                              description='The fifth fulfilled datarequest', 
                                                              status='F', 
                                                              is_active=True)
	sixth_fulfilled_data_request = DataRequest.objects.create(creator=admin, 
                                                              name='fulfilled_datarequest6', 
                                                              description='The sixth fulfilled datarequest', 
                                                              status='F', 
                                                              is_active=True)
	return 

########################################
# Create the unfulfilled DataRequests. #
########################################

def _create_unfulfilled_datarequests():
	first_unfulfilled_data_request = DataRequest.objects.create(creator=bob, 
                                                                name='unfulfilled_datarequest1', 
                                                                description='The first unfulfilled datarequest', 
                                                                status='U',
                                                                is_active=True)
	second_unfulfilled_data_request = DataRequest.objects.create(creator=admin, 
                                                                 name='unfulfilled_datarequest2', 
                                                                 description='The second unfulfilled datarequest', 
                                                                 status='U',
                                                                 is_active=True)
	third_unfulfilled_data_request = DataRequest.objects.create(creator=bob, 
                                                                name='unfulfilled_datarequest3', 
                                                                description='The third unfulfilled datarequest', 
                                                                status='U',
                                                                is_active=True)
	fourth_unfulfilled_data_request = DataRequest.objects.create(creator=admin, 
                                                                 name='unfulfilled_datarequest4', 
                                                                 description='The fourth unfulfilled datarequest', 
                                                                 status='U',
                                                                 is_active=True)
	return

######################################
# Generate the actual fixtures here. #
######################################

_create_canceled_datarequests()
_create_fulfilled_datarequests()
_create_unfulfilled_datarequests()