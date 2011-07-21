# encoding: utf-8
import datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models

class Migration(SchemaMigration):
    
    depends_on = (
        ('projects', '0002_auto__add_projectdownload'),
    )
    
    def forwards(self, orm):
        
        # Adding model 'DataSetDownload'
        db.create_table('datasets_datasetdownload', (
            ('downloader', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('file_name', self.gf('django.db.models.fields.CharField')(max_length=1000)),
            ('is_download_all', self.gf('django.db.models.fields.BooleanField')(default=False, blank=True)),
            ('is_readme', self.gf('django.db.models.fields.BooleanField')(default=False, blank=True)),
            ('parent_dataset', self.gf('django.db.models.fields.related.ForeignKey')(related_name='downloaded_files', to=orm['datasets.DataSet'])),
            ('downloaded_at', self.gf('django.db.models.fields.DateTimeField')(auto_now_add=True, db_index=True, blank=True)),
            ('parent_project', self.gf('django.db.models.fields.related.ForeignKey')(related_name='downloaded_projects', null=True, to=orm['projects.ProjectDownload'])),
            ('id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
        ))
        db.send_create_signal('datasets', ['DataSetDownload'])
    
    
    def backwards(self, orm):
        
        # Deleting model 'DataSetDownload'
        db.delete_table('datasets_datasetdownload')
    
    
    models = {
        'auth.group': {
            'Meta': {'object_name': 'Group'},
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '80'}),
            'permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Permission']", 'blank': 'True'})
        },
        'auth.permission': {
            'Meta': {'unique_together': "(('content_type', 'codename'),)", 'object_name': 'Permission'},
            'codename': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'content_type': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['contenttypes.ContentType']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '50'})
        },
        'auth.user': {
            'Meta': {'object_name': 'User'},
            'date_joined': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'email': ('django.db.models.fields.EmailField', [], {'max_length': '75', 'blank': 'True'}),
            'first_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'groups': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Group']", 'blank': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_active': ('django.db.models.fields.BooleanField', [], {'default': 'True', 'blank': 'True'}),
            'is_staff': ('django.db.models.fields.BooleanField', [], {'default': 'False', 'blank': 'True'}),
            'is_superuser': ('django.db.models.fields.BooleanField', [], {'default': 'False', 'blank': 'True'}),
            'last_login': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'last_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'password': ('django.db.models.fields.CharField', [], {'max_length': '128'}),
            'user_permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Permission']", 'blank': 'True'}),
            'username': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '30'})
        },
        'categories.category': {
            'Meta': {'object_name': 'Category'},
            'created_at': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'db_index': 'True', 'blank': 'True'}),
            'description': ('django.db.models.fields.TextField', [], {'null': 'True', 'blank': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '256'})
        },
        'contenttypes.contenttype': {
            'Meta': {'unique_together': "(('app_label', 'model'),)", 'object_name': 'ContentType', 'db_table': "'django_content_type'"},
            'app_label': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'model': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '100'})
        },
        'core.item': {
            'Meta': {'object_name': 'Item'},
            'category': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['categories.Category']"}),
            'created_at': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'db_index': 'True', 'blank': 'True'}),
            'creator': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['auth.User']"}),
            'description': ('django.db.models.fields.CharField', [], {'max_length': '16384'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_active': ('django.db.models.fields.BooleanField', [], {'default': 'False', 'blank': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '256'}),
            'rendered_description': ('django.db.models.fields.TextField', [], {'null': 'True', 'blank': 'True'}),
            'slug': ('django.db.models.fields.SlugField', [], {'max_length': '50', 'db_index': 'True'}),
            'tagless_description': ('django.db.models.fields.TextField', [], {'null': 'True', 'blank': 'True'})
        },
        'datasets.dataset': {
            'Meta': {'object_name': 'DataSet', '_ormbases': ['core.Item']},
            'geolocations': ('django.db.models.fields.related.ManyToManyField', [], {'related_name': "'datasets'", 'blank': 'True', 'to': "orm['geoloc.GeoLoc']"}),
            'item_ptr': ('django.db.models.fields.related.OneToOneField', [], {'to': "orm['core.Item']", 'unique': 'True', 'primary_key': 'True'}),
            'next_version': ('django.db.models.fields.related.ForeignKey', [], {'blank': 'True', 'related_name': "'next'", 'null': 'True', 'to': "orm['datasets.DataSet']"}),
            'previous_version': ('django.db.models.fields.related.ForeignKey', [], {'blank': 'True', 'related_name': "'previous'", 'null': 'True', 'to': "orm['datasets.DataSet']"}),
            'rating_score': ('django.db.models.fields.IntegerField', [], {'default': '0', 'blank': 'True'}),
            'rating_votes': ('django.db.models.fields.PositiveIntegerField', [], {'default': '0', 'blank': 'True'})
        },
        'datasets.datasetdownload': {
            'Meta': {'object_name': 'DataSetDownload'},
            'downloaded_at': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'db_index': 'True', 'blank': 'True'}),
            'downloader': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['auth.User']"}),
            'file_name': ('django.db.models.fields.CharField', [], {'max_length': '1000'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_download_all': ('django.db.models.fields.BooleanField', [], {'default': 'False', 'blank': 'True'}),
            'is_readme': ('django.db.models.fields.BooleanField', [], {'default': 'False', 'blank': 'True'}),
            'parent_dataset': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'downloaded_files'", 'to': "orm['datasets.DataSet']"}),
            'parent_project': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'downloaded_projects'", 'null': 'True', 'to': "orm['projects.ProjectDownload']"})
        },
        'datasets.datasetfile': {
            'Meta': {'object_name': 'DataSetFile'},
            'file_contents': ('epic.core.util.customfilefield.CustomFileField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_readme': ('django.db.models.fields.BooleanField', [], {'default': 'False', 'blank': 'True'}),
            'parent_dataset': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'files'", 'to': "orm['datasets.DataSet']"}),
            'uploaded_at': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'db_index': 'True', 'blank': 'True'})
        },
        'geoloc.geoloc': {
            'Meta': {'unique_together': "(('latitude', 'longitude', 'canonical_name'),)", 'object_name': 'GeoLoc'},
            'canonical_name': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'latitude': ('django.db.models.fields.DecimalField', [], {'max_digits': '9', 'decimal_places': '6'}),
            'longitude': ('django.db.models.fields.DecimalField', [], {'max_digits': '9', 'decimal_places': '6'})
        },
        'projects.project': {
            'Meta': {'object_name': 'Project', '_ormbases': ['core.Item']},
            'datasets': ('django.db.models.fields.related.ManyToManyField', [], {'related_name': "'projects'", 'to': "orm['datasets.DataSet']"}),
            'item_ptr': ('django.db.models.fields.related.OneToOneField', [], {'to': "orm['core.Item']", 'unique': 'True', 'primary_key': 'True'})
        },
        'projects.projectdownload': {
            'Meta': {'object_name': 'ProjectDownload'},
            'downloaded_at': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'db_index': 'True', 'blank': 'True'}),
            'downloader': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['auth.User']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'parent_project': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'downloaded_project'", 'to': "orm['projects.Project']"})
        }
    }
    
    complete_apps = ['datasets']
