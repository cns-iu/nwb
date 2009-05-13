from django.contrib.auth.models import User
from datarequests.models import DataRequest
from datasets.models import DataSet
from core.models import Profile
from tags.models import Tagging


david = User.objects.get(username='david')
bruno = User.objects.get(username='bruno')
alex = User.objects.get(username='alex')
chintan = User.objects.get(username='chintan')
micah = User.objects.get(username='micah')
russell = User.objects.get(username='russell')
patrick = User.objects.get(username='patrick')
elisha = User.objects.get(username='elisha')
jim = User.objects.get(username='jim')
katy = User.objects.get(username='katy')

######################################
# Create the Canceled DataRequests.  #
######################################

def _create_c_datarequests():
	canceled_datarequest1 = DataRequest.objects.create(creator=david, 
                                                       name='Leishmaniasis', 
                                                       description='''Need dataset for the 2004 Leishmaniasis epidemic
that happened in Afghanistan.''',  
                                                       status='C', 
                                                       is_active=True)

	canceled_datarequest2 = DataRequest.objects.create(creator=chintan, 
                                                       name='Avian influenza', 
                                                       description='''Need dataset for the 1968 - 1969 Avian influenza or 
Hong Kong flu that happened in Hong Kong.''',  
                                                       status='C', 
                                                       is_active=True)
	
	canceled_datarequest3 = DataRequest.objects.create(creator=patrick, 
                                                       name='Dengue fever', 
                                                       description='''Need dataset for the 2004 Dengue fever epidemic
that happened in Indonesia.''',  
                                                       status='C', 
                                                       is_active=True)	

	canceled_datarequest4 = DataRequest.objects.create(creator=russell, 
                                                       name='Infantile paralysis', 
                                                       description='''Need dataset for the 2007 Infantile paralysis epidemic
that happened in Nigeria.''',  
                                                       status='C', 
                                                       is_active=True)
	
	canceled_datarequest5 = DataRequest.objects.create(creator=micah, 
                                                       name='Menigitis', 
                                                       description='''Need dataset for the 1996 Menigitis epidemic
that happened in West Africa.''',  
                                                       status='C', 
                                                       is_active=True)	
	return

######################################
# Create the Fulfilled DataRequests. #
######################################

def _create_f_datarequests():
	fulfilled_datarequest1 = DataRequest.objects.create(creator=jim, 
                                                        name='Yellow fever', 
                                                        description='''Need dataset for the 1878 Yellow Fever epidemic
that happened in Memphis, New Orleans.''', 
                                                        status='F', 
                                                        is_active=True)
	
	fulfilled_datarequest2 = DataRequest.objects.create(creator=katy, 
                                                        name='Cholera', 
                                                        description='''Need dataset for the 1849 Cholera epidemic
that happened in New York.''', 
                                                        status='F',
                                                        is_active=True)
	
	fulfilled_datarequest3 = DataRequest.objects.create(creator=elisha, 
                                                        name='Yellow fever', 
                                                        description='''Need dataset for the 1878 Yellow Fever epidemic
that happened in Memphis, New Orleans.''', 
                                                        status='F', 
                                                        is_active=True)
	
	fulfilled_datarequest4 = DataRequest.objects.create(creator=micah, 
                                                        name='Great Plague of Milan', 
                                                        description='''Need dataset for the 1630 Great Plague of Milan
that happened in Milan, Italy.''', 
                                                        status='F', 
                                                        is_active=True)
		
	fulfilled_datarequest5 = DataRequest.objects.create(creator=patrick, 
                                                        name='Typhus', 
                                                        description='''Need dataset for the 1816 - 1819 Typhus epidemic
that happened in Ireland.''', 
                                                        status='F', 
                                                        is_active=True)
	return 

########################################
# Create the unfulfilled DataRequests. #
########################################

def _create_u_datarequests():
	unfulfilled_datarequest1 = DataRequest.objects.create(creator=david, 
                                                          name='Malaria - Groningen', 
                                                          description='''Need dataset for the 1829 Malaria - Groningen 
epidemic that happened in Netherlands.''', 
                                                          status='U',
                                                          is_active=True)
	
	unfulfilled_datarequest2 = DataRequest.objects.create(creator=chintan, 
                                                          name='Plague Riot', 
                                                          description='''Need dataset for the 1771 Plague Riot 
epidemic that happened in Moscow possibly caused by bubonic plague.''', 
                                                          status='U',
                                                          is_active=True)
	
	unfulfilled_datarequest3 = DataRequest.objects.create(creator=katy, 
                                                          name='Smallpox', 
                                                          description='''Need dataset for the 1770s Smallpox
epidemic that happened in Northwest Coast Indians.''', 
                                                          status='U',
                                                          is_active=True)
		
	unfulfilled_datarequest4 = DataRequest.objects.create(creator=bruno, 
                                                          name='Bilious disorder', 
                                                          description='''Need dataset for the 1783 Bilious disorder 
epidemic that happened in Dover, Delaware.''', 
                                                          status='U',
                                                          is_active=True)
	
	unfulfilled_datarequest5 = DataRequest.objects.create(creator=patrick, 
                                                          name='Chikungunya outbreaks in India', 
                                                          description='''Need dataset for the 2006 Chikungunya outbreaks 
that happened in India.''', 
                                                          status='U',
                                                          is_active=True)		
			
	return

#######################
# Create the DataSets #
#######################

def _create_datasets():
	dataset1 = DataSet.objects.create(creator=david, name='HIV/AIDS', 
                                      description=
                                      '''Acquired immune deficiency syndrome or acquired immunodeficiency syndrome 
(AIDS) is a disease of the human immune system caused by the human immunodeficiency virus (HIV). This condition 
progressively reduces the effectiveness of the immune system and leaves individuals susceptible to opportunistic 
infections and tumors. HIV is transmitted through direct contact of a mucous membrane or the bloodstream with a 
bodily fluid containing HIV, such as blood, semen, vaginal fluid, preseminal fluid, and breast milk.''',
                                      is_active=True)
	_tag_dataset1(dataset1)
	
	dataset2 = DataSet.objects.create(creator=alex, name='Spanish Flu', 
                                      description=
									  '''The 1918 flu pandemic (commonly referred to as the Spanish flu) was an 
influenza pandemic that spread to nearly every part of the world. It was caused by an unusually virulent and 
deadly Influenza A virus strain of subtype H1N1. Historical and epidemiological data are inadequate to identify 
the geographic origin of the virus. Most of its victims were healthy young adults, in contrast to most influenza 
outbreaks which predominantly affect juvenile, elderly, or otherwise weakened patients. The flu pandemic has also 
been implicated in the sudden outbreak of Encephalitis lethargica in the 1920s.''',
                                      is_active=True)
	_tag_dataset2(dataset2)
	

	dataset3 = DataSet.objects.create(creator=micah, name='Poliomyelitis', 
                                      description=
                                      '''Poliomyelitis, often called infantile paralysis, is an acute viral infectious
disease spread from person to person, primarily via the fecal-oral route. The term derives from the Greek polios, meaning 
"grey", myelos, referring to the "spinal cord", and the suffix-itis, which denotes inflammation. Although around 90% of 
polio infections cause no symptoms at all, affected individuals can exhibit a range of symptoms if the virus enters the 
blood stream. In about 1% of cases the virus enters the central nervous system, preferentially infecting and destroying 
motor neurons, leading to muscle weakness and acute flaccid paralysis. Different types of paralysis may occur, depending 
on the nerves involved. Spinal polio is the most common form, characterized by asymmetric paralysis that most often involves 
the legs. Bulbar polio leads to weakness of muscles innervated by cranial nerves. Bulbospinal polio is a combination of bulbar 
and spinal paralysis.''',
                                      is_active=True)
	_tag_dataset3(dataset3)
	
	dataset4 = DataSet.objects.create(creator=elisha, name='Third Pandemic', 
                                  description=
                                      '''Third Pandemic is the designation of a major plague pandemic that began in the 
Yunnan province in China in 1855. This episode of bubonic plague spread to all inhabited continents, and ultimately killed 
more than 12 million people in India and China alone. According to the World Health Organization, the pandemic was considered 
active until 1959, when worldwide casualties dropped to 200 per year.''',
                                      is_active=True)
	_tag_dataset4(dataset4)
	
	dataset5 = DataSet.objects.create(creator=katy, name='Viral hemorrhagic fever', 
                                  description=
                                      '''The viral hemorrhagic fevers (VHFs) are a diverse group of animal and human illnesses 
that are caused by five distinct families of RNA viruses: the Arenaviridae, Filoviridae, Bunyaviridae, Togaviridae, and Flaviviridae. 
All types of VHF are characterized by fever and bleeding disorders and all can progress to high fever, shock and death in extreme 
cases. Some of the VHF agents cause relatively mild illnesses, such as the Scandinavian nephropathia epidemica, whilst others, such 
as the African Ebola virus, can cause severe, life-threatening disease.''',
                                      is_active=True)
	_tag_dataset5(dataset5)
	
	
#################
# Create tags for dataset1 #
#################

def _katy_tag_dataset1(dataset1):
	katy_tags = "hiv aids"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        katy_tags, item=dataset1, user=katy)

def _david_tag_dataset1(dataset1):
	david_tags = "bodily fluids"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        david_tags, item=dataset1, user=david)

def _tag_dataset1(dataset1):
	_katy_tag_dataset1(dataset1)
	_david_tag_dataset1(dataset1)

#################
# Create tags for dataset2 #
#################

def _micah_tag_dataset2(dataset2):
	micah_tags = "spanish 1918"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        micah_tags, item=dataset2, user=micah)

def _alex_tag_dataset2(dataset2):
	alex_tags = "flu"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        alex_tags, item=dataset2, user=alex)

def _tag_dataset2(dataset2):
	_micah_tag_dataset2(dataset2)
	_alex_tag_dataset2(dataset2)

#################
# Create tags for dataset3 #
#################

def _patrick_tag_dataset3(dataset3):
	patrick_tags = "Poliomyelitis greek"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        patrick_tags, item=dataset3, user=patrick)

def _chintan_tag_dataset3(dataset3):
	chintan_tags = "infantile"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        chintan_tags, item=dataset3, user=chintan)

def _tag_dataset3(dataset3):
	_patrick_tag_dataset3(dataset3)
	_chintan_tag_dataset3(dataset3)
	
#################
# Create tags for dataset4 #
#################

def _jim_tag_dataset4(dataset4):
	jim_tags = "Third Pandemic"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        jim_tags, item=dataset4, user=jim)

def _elisha_tag_dataset4(dataset4):
	elisha_tags = "1855"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        elisha_tags, item=dataset4, user=elisha)

def _tag_dataset4(dataset4):
	_jim_tag_dataset4(dataset4)
	_elisha_tag_dataset4(dataset4)
	
#################
# Create tags for dataset5 #
#################

def _russell_tag_dataset5(dataset5):
	russell_tags = "spanish 1918"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        russell_tags, item=dataset5, user=russell)

def _bruno_tag_dataset5(dataset5):
	bruno_tags = "flu"
	
	Tagging.objects.add_tags_and_return_added_tag_names(
        bruno_tags, item=dataset5, user=bruno)

def _tag_dataset5(dataset5):
	_russell_tag_dataset5(dataset5)
	_bruno_tag_dataset5(dataset5)
	
######################################
# Generate the actual fixtures here. #
######################################

_create_c_datarequests()
_create_f_datarequests()
_create_u_datarequests()

_create_datasets()