from django.contrib import admin

from epic.datasets.models import DataSet, DataSetFile, ReadMeFile

class InlineDataSetFile(admin.StackedInline):
    model = DataSetFile

class InlineReadMeFile(admin.StackedInline):
    model = ReadMeFile
    
class DataSetAdmin(admin.ModelAdmin):
    list_display = ('creator', 'name', 'created_at', 'is_active')
    list_filter = ('creator', 'is_active')
    inlines = [InlineDataSetFile, InlineReadMeFile]
    
admin.site.register(DataSet, DataSetAdmin)
