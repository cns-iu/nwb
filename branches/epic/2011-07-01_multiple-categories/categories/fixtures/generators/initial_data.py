from epic.categories.models import Category, default_category as get_or_create_default_category

REAL_CATEGORY_NAMES = \
    ['Behavioral sciences', 'Computer science', 'Critical care medicine', 'Demography',
     'Epidemiology', 'Geography', 'Health policy & services', 'Healthcare sciences & services',
     'Immunology', 'Information science & library science', 'International health',
     'Mathematical & computational biology', 'Medicine', 'Parasitology', 'Pathology', 'Pediatrics',
     'Public health', 'Psychology', 'Substance abuse', 'Survey',
     'Transportation science & technology', 'Virology', 'Emotion', 'Beliefs', 'Memory',
     'Attitudes', 'Behavior']
    
def _create_default_category():
    default_category = get_or_create_default_category()
    #default_category.save()
    
def _create_real_categories():
    for category_name in REAL_CATEGORY_NAMES:
        category = Category.objects.get_or_create(name=category_name, description=category_name)[0]
        #category.save()
        
_create_default_category()
_create_real_categories()