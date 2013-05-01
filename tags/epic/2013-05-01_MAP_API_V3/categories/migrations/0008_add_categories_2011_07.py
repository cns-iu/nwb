# encoding: utf-8
import datetime
from south.db import db
from south.v2 import DataMigration
from django.db import models
from django.template.defaultfilters import slugify
from epic.categories.models import default_category

# Excludes "Uncategorized"
NEW_CATEGORY_NAMES = \
    ['Behavioral sciences', 'Computer science', 'Critical care medicine', 'Demography',
     'Epidemiology', 'Geography', 'Health policy & services', 'Healthcare sciences & services',
     'Immunology', 'Information science & library science', 'International health',
     'Mathematical & computational biology', 'Medicine', 'Parasitology', 'Pathology', 'Pediatrics',
     'Public health', 'Psychology', 'Substance abuse', 'Survey',
     'Transportation science & technology', 'Virology', 'Emotion', 'Beliefs', 'Memory',
     'Attitudes', 'Behavior']
    
class Migration(DataMigration):

    def forwards(self, orm):
        # Create default ("Uncategorized") category   
        default_category()

        # Create the other ("real") categories
        for name in NEW_CATEGORY_NAMES:
            category = orm.Category.objects.get_or_create(name=name, description=name)[0]
            category.slug = slugify(category.name)
            category.save()

    def backwards(self, orm):
        "Write your backwards methods here."
        # TODO


    models = {
        'categories.category': {
            'Meta': {'object_name': 'Category'},
            'created_at': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'db_index': 'True', 'blank': 'True'}),
            'description': ('django.db.models.fields.TextField', [], {'null': 'True', 'blank': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '256'}),
            'slug': ('django.db.models.fields.SlugField', [], {'max_length': '50', 'db_index': 'True'})
        }
    }

    complete_apps = ['categories']
