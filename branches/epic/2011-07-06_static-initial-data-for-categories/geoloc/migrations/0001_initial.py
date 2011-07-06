# encoding: utf-8
import datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models

class Migration(SchemaMigration):
    
    def forwards(self, orm):
        
        # Adding model 'GeoLoc'
        db.create_table('geoloc_geoloc', (
            ('latitude', self.gf('django.db.models.fields.DecimalField')(max_digits=9, decimal_places=6)),
            ('canonical_name', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('longitude', self.gf('django.db.models.fields.DecimalField')(max_digits=9, decimal_places=6)),
        ))
        db.send_create_signal('geoloc', ['GeoLoc'])

        # Adding unique constraint on 'GeoLoc', fields ['latitude', 'longitude', 'canonical_name']
        db.create_unique('geoloc_geoloc', ['latitude', 'longitude', 'canonical_name'])
    
    
    def backwards(self, orm):
        
        # Deleting model 'GeoLoc'
        db.delete_table('geoloc_geoloc')

        # Removing unique constraint on 'GeoLoc', fields ['latitude', 'longitude', 'canonical_name']
        db.delete_unique('geoloc_geoloc', ['latitude', 'longitude', 'canonical_name'])
    
    
    models = {
        'geoloc.geoloc': {
            'Meta': {'unique_together': "(('latitude', 'longitude', 'canonical_name'),)", 'object_name': 'GeoLoc'},
            'canonical_name': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'latitude': ('django.db.models.fields.DecimalField', [], {'max_digits': '9', 'decimal_places': '6'}),
            'longitude': ('django.db.models.fields.DecimalField', [], {'max_digits': '9', 'decimal_places': '6'})
        }
    }
    
    complete_apps = ['geoloc']
