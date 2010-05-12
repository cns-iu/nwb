from django.contrib.auth.models import User

from epic.categories.models import Category
from epic.core.models import Profile
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.geoloc.models import GeoLoc
from epic.projects.models import Project
from epic.tags.models import Tagging



User.objects.create_superuser('super_user', 'super_user@gmail.com', 'super_user')
super_user = User.objects.get(username='super_user')
super_user.first_name = 'Super'
super_user.last_name = 'User'
super_user.save()

super_profile = Profile.objects.for_user(user=super_user)


chintan = User.objects.create_user(
    username='chintan', email='chintan@gmail.com', password='chintan')
chintan.first_name = 'Chintan'
chintan.last_name = 'Tank'
chintan.save()

chintan_profile = Profile.objects.for_user(user=chintan)


david = User.objects.create_user(username='david', email='david@gmail.com', password='david')
david.first_name = 'David'
david.last_name = 'Coe'
david.save()

david_profile = Profile.objects.for_user(user=david)


elisha = User.objects.create_user(username='elisha', email='elisha@gmail.com', password='elisha')
elisha.first_name = 'David'
elisha.last_name = 'Coe'
elisha.save()

elisha_profile = Profile.objects.for_user(user=elisha)


micah = User.objects.create_user(username='micah', email='micah@gmail.com', password='micah')
micah.first_name = 'Micah'
micah.last_name = 'Linnemeier'
micah.save()

micah_profile = Profile.objects.for_user(user=micah)


patrick = User.objects.create_user(
    username='patrick', email='patrick@gmail.com', password='patrick')
patrick.first_name = 'Patrick'
patrick.last_name = 'Phillips'
patrick.save()

patrick_profile = Profile.objects.for_user(user=patrick)


russell = User.objects.create_user(
    username='russell', email='russell@gmail.com', password='russell')
russell.first_name = 'Russell'
russell.last_name = 'Duhon'
russell.save()

russell_profile = Profile.objects.for_user(user=russell)



alex = User.objects.create_user(username='alex', email='alex@gmail.com', password='alex')
alex.first_name = 'Alessandro'
alex.last_name = 'Vespignani'
alex.save()

alex_profile = Profile.objects.for_user(user=alex)


bruno = User.objects.create_user(username='bruno', email='bruno@gmail.com', password='bruno')
bruno.first_name = 'Bruno'
bruno.last_name = 'Goncalves'
bruno.save()

bruno_profile = Profile.objects.for_user(user=bruno)


jim = User.objects.create_user(username='jim', email='jim@gmail.com', password='jim')
jim.first_name = 'Jim'
jim.last_name = 'Sherman'
jim.save()

jim_profile = Profile.objects.for_user(user=jim)


katy = User.objects.create_user(username='katy', email='katy@gmail.com', password='katy')
katy.first_name = 'Katy'
katy.last_name = 'Borner'
katy.save()

katy_profile = Profile.objects.for_user(user=katy)

##########################
# Create the Categories. #
##########################

def _create_categories():
    social_contagion = Category.objects.create(
        name='Social Contagion',
        description='Social Contagion Category Description')
    
    infectious_diseases = Category.objects.create(
        name='Infectious Diseases',
        description='Infectious Diseases Category Description')
    
    demographics = Category.objects.create(
        name='Demographics',
        description='Demographics Category Description')
    
    return (social_contagion, infectious_diseases, demographics)

######################################
# Create the Canceled DataRequests. #
######################################

def _create_c_datarequests(social_contagion,
                           infectious_diseases,
                           demographics):
    canceled_datarequest1 = DataRequest.objects.create(
        creator=david, 
        name='Leishmaniasis', 
        description='Need dataset for the 2004 Leishmaniasis epidemic ' + \
                    'that happened in Afghanistan.',
        category=infectious_diseases,
        status='C', 
        is_active=True)

    canceled_datarequest2 = DataRequest.objects.create(
        creator=chintan, 
        name='Avian influenza', 
        description='Need dataset for the 1968 - 1969 Avian influenza or ' + \
                    'Hong Kong flu that happened in Hong Kong.',
        category=infectious_diseases,
        status='C', 
        is_active=True)
    
    canceled_datarequest3 = DataRequest.objects.create(
        creator=patrick, 
        name='Dengue fever', 
        description='Need dataset for the 2004 Dengue fever epidemic ' + \
                    'that happened in Indonesia.',
        category=infectious_diseases,  
        status='C', 
        is_active=True)    

    canceled_datarequest4 = DataRequest.objects.create(
        creator=russell, 
        name='Infantile paralysis', 
        description='Need dataset for the 2007 Infantile paralysis ' + \
                    'epidemic that happened in Nigeria.',
        category=infectious_diseases,  
        status='C', 
        is_active=True)
    
    canceled_datarequest5 = DataRequest.objects.create(
        creator=micah, 
        name='Menigitis', 
        description='Need dataset for the 1996 Menigitis epidemic ' + \
                    'that happened in West Africa.',
        category=infectious_diseases,
        status='C', 
        is_active=True)

######################################
# Create the Fulfilled DataRequests. #
######################################

def _create_fulfilled_datarequest(
        creator, name, description, category, fulfilling_dataset_name):
    fulfilling_dataset = DataSet.objects.get(name=fulfilling_dataset_name)
    fulfilled_datarequest = DataRequest.objects.create(
        creator=creator,
        name=name,
        description=description,
        category=category,
        fulfilling_item=fulfilling_dataset,
        status='F',
        is_active=True)
    
    return fulfilled_datarequest

def _create_f_datarequests(social_contagion,
                           infectious_diseases,
                           demographics):
    fulfilled_datarequest1 = _create_fulfilled_datarequest(
        creator=jim, 
        name='HIV/AIDS', 
        description='Need data on HIV.',
        category=infectious_diseases,
        fulfilling_dataset_name='HIV/AIDS')
    
    fulfilled_datarequest2 = _create_fulfilled_datarequest(
        creator=katy, 
        name='Flu', 
        description='Need data on all strains of the flu virus.',
        category=infectious_diseases,
        fulfilling_dataset_name='Spanish Flu')
    
    fulfilled_datarequest3 = _create_fulfilled_datarequest(
        creator=elisha, 
        name='Infant diseases', 
        description='Need data on all types of infant diseases.',
        category=infectious_diseases, 
        fulfilling_dataset_name='Poliomyelitis')
    
    fulfilled_datarequest4 = _create_fulfilled_datarequest(
        creator=micah, 
        name='Historical Chinese Pandemics', 
        description='Need data on pandemics in Chinese history',
        category=infectious_diseases, 
        fulfilling_dataset_name='Third Pandemic')

########################################
# Create the unfulfilled DataRequests. #
########################################

def _create_u_datarequests(social_contagion,
                           infectious_diseases,
                           demographics):
    unfulfilled_datarequest1 = DataRequest.objects.create(
        creator=david, 
        name='Malaria - Groningen', 
        description='Need dataset for the 1829 Malaria - Groningen ' + \
                    'epidemic that happened in Netherlands.',
        category=infectious_diseases, 
        status='U',
        is_active=True)
    
    unfulfilled_datarequest2 = DataRequest.objects.create(
        creator=chintan, 
        name='Plague Riot', 
        description='Need dataset for the 1771 Plague Riot epidemic that ' + \
                    'happened in Moscow possibly caused by bubonic plague.',
        category=infectious_diseases, 
        status='U',
        is_active=True)
    
    unfulfilled_datarequest3 = DataRequest.objects.create(
        creator=katy, 
        name='Smallpox', 
        description='Need dataset for the 1770s Smallpox ' + \
                    'epidemic that happened in Northwest Coast Indians.',
        category=infectious_diseases, 
        status='U',
        is_active=True)
        
    unfulfilled_datarequest4 = DataRequest.objects.create(
        creator=bruno, 
        name='Bilious disorder', 
        description='Need dataset for the 1783 Bilious disorder ' + \
                    'epidemic that happened in Dover, Delaware.',
        category=infectious_diseases, 
        status='U',
        is_active=True)
    
    unfulfilled_datarequest5 = DataRequest.objects.create(
        creator=patrick, 
        name='Chikungunya outbreaks in India', 
        description='Need dataset for the 2006 Chikungunya outbreaks ' + \
                    'that happened in India.',
         category=infectious_diseases,
         status='U',
         is_active=True)

########################
# Create the datasets. #
########################

def _create_datasets(social_contagion,
                     infectious_diseases,
                     demographics):
    dataset1 = DataSet.objects.create(
        creator=david,
        name='HIV/AIDS',
        description='Acquired immune deficiency syndrome or acquired ' + \
                    'immunodeficiency syndrome (AIDS) is a disease of ' + \
                    'the human immune system caused by the human ' + \
                    'immunodeficiency virus (HIV). This condition ' + \
                    'progressively reduces the effectiveness of the ' + \
                    'immune system and leaves individuals susceptible to ' + \
                    'opportunistic infections and tumors. HIV is ' + \
                    'transmitted through direct contact of a mucous ' + \
                    'membrane or the bloodstream with a bodily fluid ' + \
                    'containing HIV, such as blood, semen, vaginal ' + \
                    'fluid, preseminal fluid, and breast milk.',
        category=infectious_diseases,
        is_active=True)
    _tag_dataset1(dataset1)
    _geolocate_dataset1(dataset1)
    
    dataset2 = DataSet.objects.create(
        creator=alex,
        name='Spanish Flu', 
        description='The 1918 flu pandemic (commonly referred to as the ' + \
                    'Spanish flu) was an influenza pandemic that spread ' + \
                    'to nearly every part of the world. It was caused by ' + \
                    'an unusually virulent and deadly Influenza A virus ' + \
                    'strain of subtype H1N1. Historical and ' + \
                    'epidemiological data are inadequate to identify the ' + \
                    'geographic origin of the virus. Most of its victims ' + \
                    'were healthy young adults, in contrast to most ' + \
                    'influenza outbreaks which predominantly affect ' + \
                    'juvenile, elderly, or otherwise weakened patients. ' + \
                    'The flu pandemic has also been implicated in the ' + \
                    'sudden outbreak of Encephalitis lethargica ' + \
                    'in the 1920s.',
        category=infectious_diseases,
        is_active=True)
    _tag_dataset2(dataset2)
    _geolocate_dataset2(dataset2)
    

    dataset3 = DataSet.objects.create(
        creator=micah,
        name='Poliomyelitis', 
        description='Poliomyelitis, often called infantile paralysis, is ' + \
                    'an acute viral infectious disease spread from ' + \
                    'person to person, primarily via the fecal-oral ' + \
                    'route. The term derives from the Greek polios, ' + \
                    'meaning "grey", myelos, referring to the ' + \
                    '"spinal cord", and the suffix-itis, which denotes ' + \
                    'inflammation. Although around 90% of polio ' + \
                    'infections cause no symptoms at all, affected ' + \
                    'individuals can exhibit a range of symptoms if the ' + \
                    'virus enters the blood stream. In about 1% of cases ' + \
                    'the virus enters the central nervous system, ' + \
                    'preferentially infecting and destroying motor ' + \
                    'neurons, leading to muscle weakness and acute ' + \
                    'flaccid paralysis. Different types of paralysis may ' + \
                    'occur, depending on the nerves involved. Spinal ' + \
                    'polio is the most common form, characterized by ' + \
                    'asymmetric paralysis that most often involves the ' + \
                    'legs. Bulbar polio leads to weakness of muscles ' + \
                    'innervated by cranial nerves. Bulbospinal polio is ' + \
                    'a combination of bulbar and spinal paralysis.',
        category=infectious_diseases,
        is_active=True)
    _tag_dataset3(dataset3)
    _geolocate_dataset3(dataset3)
    
    dataset4 = DataSet.objects.create(
        creator=elisha,
        name='Third Pandemic', 
        description='Third Pandemic is the designation of a major plague ' + \
                    'pandemic that began in the Yunnan province in China ' + \
                    'in 1855. This episode of bubonic plague spread to ' + \
                    'all inhabited continents, and ultimately killed ' + \
                    'more than 12 million people in India and China ' + \
                    'alone. According to the World Health Organization, ' + \
                    'the pandemic was considered active until 1959, when ' + \
                    'worldwide casualties dropped to 200 per year.',
        category=infectious_diseases,
        is_active=True)
    _tag_dataset4(dataset4)
    _geolocate_dataset4(dataset4)
    
    dataset5 = DataSet.objects.create(
        creator=katy,
        name='Viral hemorrhagic fever', 
        description='The viral hemorrhagic fevers (VHFs) are a diverse ' + \
                    'group of animal and human illnesses that are caused ' + \
                    'by five distinct families of RNA viruses: the ' + \
                    'Arenaviridae, Filoviridae, Bunyaviridae, ' + \
                    'Togaviridae, and Flaviviridae. All types of VHF are ' + \
                    'characterized by fever and bleeding disorders and ' + \
                    'all can progress to high fever, shock and death in ' + \
                    'extreme cases. Some of the VHF agents cause ' + \
                    'relatively mild illnesses, such as the Scandinavian ' + \
                    'nephropathia epidemica, whilst others, such as the ' + \
                    'African Ebola virus, can cause severe, ' + \
                    'life-threatening disease.',
        category=infectious_diseases,
        is_active=True)
    _tag_dataset5(dataset5)
    _geolocate_dataset5(dataset5)
    
    dataset6 = DataSet.objects.create(
        creator=david,
        name='College Binge Drinking',
        description='Since 1993, the number of college students who ' + \
                    'drink and binge drink has remained about the same, ' + \
                    'but the intensity of excessive drinking and rates ' + \
                    'of drug abuse have jumped sharply, according to The ' + \
                    'National Center on Addiction and Substance Abuse at ' + \
                    'Columbia University.',
        category=demographics,
        is_active=True)
    dataset6.save()
    
#####################################
# Create geolocations for dataset1. #
#####################################

def _geolocate_dataset1(dataset1):
    geoLocationInformation = {
        'latitude':'2.235151',
        'longitude':'18.028564',
        'canonical_name':'Central Africa',
    }
    
    geoLocationObject = GeoLoc.objects.create(
        longitude=geoLocationInformation['longitude'],
        latitude=geoLocationInformation['latitude'],
        canonical_name=geoLocationInformation['canonical_name'],)    
    dataset1.geolocations.add(geoLocationObject)
    
#####################################
# Create geolocations for dataset2. #
#####################################

def _geolocate_dataset2(dataset2):

    geoLocationInformation = {
        'latitude':'29.425037',
        'longitude':'-98.493722',
        'canonical_name':'Continental USA',
    }
    
    geoLocationObject = GeoLoc.objects.create(
        longitude=geoLocationInformation['longitude'],
        latitude=geoLocationInformation['latitude'],
        canonical_name=geoLocationInformation['canonical_name'],)    
    dataset2.geolocations.add(geoLocationObject)    
    
#####################################
# Create geolocations for dataset3. #
#####################################

def _geolocate_dataset3(dataset3):

    geoLocationInformation = {
        'latitude':'39.074208',
        'longitude':'21.824312',
        'canonical_name':'Greece',
    }
    
    geoLocationObject = GeoLoc.objects.create(
        longitude=geoLocationInformation['longitude'],
        latitude=geoLocationInformation['latitude'],
        canonical_name=geoLocationInformation['canonical_name'],)    
    dataset3.geolocations.add(geoLocationObject)
    
#####################################
# Create geolocations for dataset4. #
#####################################

def _geolocate_dataset4(dataset4):

    geoLocationInformation = {
        'latitude':'25.043844',
        'longitude':'102.704567',
        'canonical_name':'Yunnan province, China',
    }
    
    geoLocationObject = GeoLoc.objects.create(
        longitude=geoLocationInformation['longitude'],
        latitude=geoLocationInformation['latitude'],
        canonical_name=geoLocationInformation['canonical_name'],)    
    dataset4.geolocations.add(geoLocationObject)

#####################################
# Create geolocations for dataset5. #
#####################################

def _geolocate_dataset5(dataset5):

    geoLocationInformation = {
        'latitude':'1.0101',
        'longitude':'13.9675',
        'canonical_name':'Mekambo, Gabon',
    }
    
    geoLocationObject = GeoLoc.objects.create(
        longitude=geoLocationInformation['longitude'],
        latitude=geoLocationInformation['latitude'],
        canonical_name=geoLocationInformation['canonical_name'],)    
    dataset5.geolocations.add(geoLocationObject)
    
#############################
# Create tags for dataset1. #
#############################

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

#############################
# Create tags for dataset2. #
#############################

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

#############################
# Create tags for dataset3. #
#############################

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
    
#############################
# Create tags for dataset4. #
#############################

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
    
#############################
# Create tags for dataset5. #
#############################

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

########################
# Create the projects. #
########################

PROJECT_DESCRIPTION = 'Lorem ipsum dolor sit amet, consectetur ' + \
                      'adipiscing elit. Mauris quam mauris, rutrum ' + \
                      'ullamcorper mattis et, venenatis et elit. Cras ' + \
                      'faucibus diam a nibh tempor ut imperdiet sapien ' + \
                      'euismod. In cursus purus nec ligula varius at ' + \
                      'aliquet est hendrerit. Nam felis arcu, malesuada ' + \
                      'sit amet feugiat ac, tristique eget augue. ' + \
                      'Vivamus sed velit magna. Proin eu consequat orci. ' + \
                      'Pellentesque vehicula auctor ullamcorper. ' + \
                      'Pellentesque imperdiet lacus id sapien ' + \
                      'condimentum ut fringilla metus tempor. Morbi ' + \
                      'iaculis ultricies augue eu fermentum. ' + \
                      'Pellentesque cursus nulla et elit pellentesque ' + \
                      'condimentum. Curabitur at mauris at eros rutrum ' + \
                      'dictum nec ut risus. Mauris tempus nulla vitae ' + \
                      'tellus sollicitudin adipiscing'

def _add_dataset_to_project(project, dataset_name):
    dataset = DataSet.objects.get(name=dataset_name)
    project.datasets.add(dataset)

def _create_empty_projects(social_contagion,
                                       infectious_diseases,
                                       demographics):
    project1 = Project.objects.create(
        creator=alex,
        name='Depression in the US',
        description='Depression in the US %s' % PROJECT_DESCRIPTION,
        category=social_contagion,
        is_active=True)
    project1.save()
    
    project2 = Project.objects.create(
        creator=chintan,
        name='Historical H1N1',
        description='Historical H1N1 %s' % PROJECT_DESCRIPTION,
        category=infectious_diseases,
        is_active=True)
    project2.save()

def _create_non_empty_projects(social_contagion,
                               infectious_diseases,
                               demographics):
    project1 = Project.objects.create(
        creator=russell,
        name='HIV and You',
        description='HIV and You %s' % PROJECT_DESCRIPTION,
        category=infectious_diseases,
        is_active=True)
    _add_dataset_to_project(project1, 'HIV/AIDS')
    project1.save()
    
    project2 = Project.objects.create(
        creator=patrick,
        name='Drinking Habits',
        description='The %s of drinking Infections' % PROJECT_DESCRIPTION,
        category=social_contagion,
        is_active=True)
    _add_dataset_to_project(project2, 'College Binge Drinking')
    project2.save()
    
    project3 = Project.objects.create(
        creator=elisha,
        name='Death by Plague',
        description='Death by %s Plague' % PROJECT_DESCRIPTION,
        category=infectious_diseases,
        is_active=True)
    _add_dataset_to_project(project3, 'Third Pandemic')
    project3.save()
    
    project4 = Project.objects.create(
        creator=alex,
        name='My favorite diseases',
        description='These diseases are just [i]so[/i] interesting[b]![/b]',
        category=infectious_diseases,
        is_active=True)
    _add_dataset_to_project(project4, 'HIV/AIDS')
    _add_dataset_to_project(project4, 'Spanish Flu')
    _add_dataset_to_project(project4, 'Viral hemorrhagic fever')

def _create_projects(*categories):
    _create_empty_projects(*categories)
    _create_non_empty_projects(*categories)
    
######################################
# Generate the actual fixtures here. #
######################################

categories = _create_categories()

_create_datasets(*categories)

_create_c_datarequests(*categories)
_create_f_datarequests(*categories)
_create_u_datarequests(*categories)

_create_projects(*categories)
