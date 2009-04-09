from datasets.models import DataSet
from epic.messages.models import ReceivedMessage, SentMessage

#######################
# Create the DataSets #
#######################

def _create_messages():
	first_received_message = ReceivedMessage.objects.create(recipient=bob, sender=admin, subject="m1r", message="this is the first received message", read=False, replied=False, deleted=False)
	first_sent_message = SentMessage.objects.create(recipient=bob, sender=admin, subject="m1s", message="this is the first sent message", deleted=False)
	second_received_message = ReceivedMessage.objects.create(recipient=admin, sender=admin, subject="m2r", message="this is the second received message", read=False, replied=False, deleted=False)
	first_sent_message = SentMessage.objects.create(recipient=admin, sender=admin, subject="m2s", message="this is the second sent message", deleted=False)
	
######################################
# Generate the actual fixtures here. #
######################################

_create_messages()