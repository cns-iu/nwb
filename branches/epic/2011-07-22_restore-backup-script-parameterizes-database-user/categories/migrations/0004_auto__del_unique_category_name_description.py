# encoding: utf-8
import datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models

class Migration(SchemaMigration):
    
    def forwards(self, orm):
        
        # Removing unique constraint on 'Category', fields ['name', 'description']
        db.delete_unique('categories_category', ['name', 'description'])
    
    
    def backwards(self, orm):
        
        # Adding unique constraint on 'Category', fields ['name', 'description']
        db.create_unique('categories_category', ['name', 'description'])
    
    
    models = {
        'categories.category': {
            'Meta': {'object_name': 'Category'},
            'created_at': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'db_index': 'True', 'blank': 'True'}),
            'description': ('django.db.models.fields.TextField', [], {'null': 'True', 'blank': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '256'})
        }
    }
    
    complete_apps = ['categories']
