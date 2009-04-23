from django.contrib import admin

from epic.datasets.models import DataSet

class DataSetAdmin(admin.ModelAdmin):
    list_display = ('creator', 'name', 'created_at', 'is_active')
    list_filter = ('creator', 'is_active')

admin.site.register(DataSet, DataSetAdmin)