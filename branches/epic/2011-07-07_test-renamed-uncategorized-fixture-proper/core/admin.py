from django.contrib import admin

from epic.core.models import Author


class AuthorAdmin(admin.ModelAdmin):
    list_display = ('author',)
    list_filter = ('items', 'author',)
    filter_horizontal = ('items',)
    
admin.site.register(Author, AuthorAdmin)