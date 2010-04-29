from django.contrib import admin

from epic.categories.models import Category
from epic.datarequests.models import DataRequest
from epic.datasets.models import DataSet
from epic.projects.models import Project


class InlineCategoryDataRequest(admin.StackedInline):
    model = DataRequest
    extra = 1

class InlineCategoryDataSet(admin.StackedInline):
    model = DataSet
    extra = 1

class InlineCategoryProject(admin.StackedInline):
    model = Project
    extra = 1

class CategoryAdmin(admin.ModelAdmin):
    date_hierarchy = 'created_at'
    fields = ('name', 'description')
    list_display = ('name', 'description')
    
    inlines = [InlineCategoryDataRequest, InlineCategoryDataSet, InlineCategoryProject]

admin.site.register(Category, CategoryAdmin)
