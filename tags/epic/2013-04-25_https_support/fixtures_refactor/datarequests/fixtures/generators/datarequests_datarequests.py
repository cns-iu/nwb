from datarequests.models import DataRequest

######################################
# Create the Canceled DataRequests.  #
######################################

def _create_c_datarequests():
	canceled_datarequest1 = DataRequest.objects.create(creator=bob, name='canceled_datarequest1', description='The first canceled datarequest', status='C', slug='canceled_datarequest1')
	canceled_datarequest2 = DataRequest.objects.create(creator=admin, name='canceled_datarequest2', description='The second canceled datarequest', status='C', slug='canceled_datarequest2')
	canceled_datarequest3 = DataRequest.objects.create(creator=bob, name='canceled_datarequest3', description='The third canceled datarequest', status='C', slug='canceled_datarequest3')
	canceled_datarequest4 = DataRequest.objects.create(creator=admin, name='canceled_datarequest4', description='The fourth canceled datarequest', status='C', slug='canceled_datarequest4')
	canceled_datarequest5 = DataRequest.objects.create(creator=bob, name='canceled_datarequest5', description='The fifth canceled datarequest', status='C', slug='canceled_datarequest5')
	return

######################################
# Create the Fulfilled DataRequests. #
######################################

def _create_f_datarequests():
	fulfilled_datarequest1 = DataRequest.objects.create(creator=bob, name='fulfilled_datarequest1', description='The first fulfilled datarequest', status='F', slug='fulfilled_datarequest1')
	fulfilled_datarequest2 = DataRequest.objects.create(creator=admin, name='fulfilled_datarequest2', description='The second fulfilled datarequest', status='F', slug='fulfilled_datarequest2')
	fulfilled_datarequest3 = DataRequest.objects.create(creator=bob, name='fulfilled_datarequest3', description='The third fulfilled datarequest', status='F', slug='fulfilled_datarequest3')
	fulfilled_datarequest4 = DataRequest.objects.create(creator=admin, name='fulfilled_datarequest4', description='The fourth fulfilled datarequest', status='F', slug='fulfilled_datarequest4')
	fulfilled_datarequest5 = DataRequest.objects.create(creator=bob, name='fulfilled_datarequest5', description='The fifth fulfilled datarequest', status='F', slug='fulfilled_datarequest5')
	fulfilled_datarequest6 = DataRequest.objects.create(creator=admin, name='fulfilled_datarequest6', description='The sixth fulfilled datarequest', status='F', slug='fulfilled_datarequest6')
	return 

########################################
# Create the unfulfilled DataRequests. #
########################################

def _create_u_datarequests():
	unfulfilled_datarequest1 = DataRequest.objects.create(creator=bob, name='unfulfilled_datarequest1', description='The first unfulfilled datarequest', status='U', slug='unfulfilled_datarequest1')
	unfulfilled_datarequest2 = DataRequest.objects.create(creator=admin, name='unfulfilled_datarequest2', description='The second unfulfilled datarequest', status='U', slug='unfulfilled_datarequest2')
	unfulfilled_datarequest3 = DataRequest.objects.create(creator=bob, name='unfulfilled_datarequest3', description='The third unfulfilled datarequest', status='U', slug='unfulfilled_datarequest3')
	unfulfilled_datarequest4 = DataRequest.objects.create(creator=admin, name='unfulfilled_datarequest4', description='The fourth unfulfilled datarequest', status='U', slug='unfulfilled_datarequest4')
	return

######################################
# Generate the actual fixtures here. #
######################################

_create_c_datarequests()
_create_f_datarequests()
_create_u_datarequests()