from epic.datasets.models import DataSet
from epic.tags.models import Tagging

############
# dataset1 #
############

def _create_dataset1():
	dataset1 = DataSet.objects.create(creator=bob,
									  name="dataset1",
									  description="dataset number one",
									  slug="dataset1")
	
	return dataset1

############
# dataset2 #
############

def _create_dataset2():
	dataset2 = DataSet.objects.create(creator=bill,
									  name="dataset2",
									  description="dataset number two",
									  slug="dataset2")
	
	return dataset2

#################
# dataset1 tags #
#################

def _bob_tag_dataset1(dataset1):
	bob_tags = "tag1 tag2"
	
	dataset1.add_tags_and_return_added_tag_names(
        bob_tags, item=dataset1, user=bob)

def _bill_tag_dataset1(dataset1):
	bill_tags = "tag3"
	
	dataset1.add_tags_and_return_added_tag_names(
        bill_tags, item=dataset1, user=bill)

def _tag_dataset1(dataset1):
	_bob_tag_dataset1(dataset1)
	_bill_tag_dataset1(dataset1)

def _create_dataset1_with_tags():
	dataset1 = _create_dataset1()
	
	_tag_dataset1(dataset1)
	
	return dataset1

#################
# dataset2 tags #
#################

def _bill_tag_dataset2(dataset2):
	bill_tags = "tag3"
	
	dataset2.add_tags_and_return_added_tag_names(
        bill_tags, item=dataset2, user=bill)

def _bob_tag_dataset2(dataset2):
	bob_tags = "tag4"
	
	dataset2.add_tags_and_return_added_tag_names(
        bob_tags, item=dataset2, user=bob)

def _tag_dataset2(dataset2):
	_bill_tag_dataset2(dataset2)
	_bob_tag_dataset2(dataset2)

def _create_dataset2_with_tags():
	dataset2 = _create_dataset2()
	
	_tag_dataset2(dataset2)
	
	return dataset2

######################################
# Generate the actual fixtures here. #
######################################

dataset1 = _create_dataset1_with_tags()
dataset2 = _create_dataset2_with_tags()
