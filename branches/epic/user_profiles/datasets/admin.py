from django.contrib import admin

from epic.datasets.models import DataSet, DataSetFile

class InlineDataSetFile(admin.StackedInline):
    model = DataSetFile
    
class DataSetAdmin(admin.ModelAdmin):
    list_display = ('creator', 'name', 'created_at', 'is_active')
    list_filter = ('creator', 'is_active')
    inlines = [InlineDataSetFile]
    
admin.site.register(DataSet, DataSetAdmin)
