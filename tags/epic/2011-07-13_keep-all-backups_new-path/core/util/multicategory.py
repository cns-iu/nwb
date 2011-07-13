from django.forms.fields import Field, EMPTY_VALUES
from django.forms.util import ErrorList, ValidationError, flatatt
from django.forms.widgets import Select
from django.utils.datastructures import MultiValueDict
from django.utils.encoding import force_unicode
from django.utils.translation import ugettext

from epic.categories.models import Category

#TODO: Add ability to remove file field boxes
class MultiCategoryInput(Select):
    """
    A widget to be used by the CategorySelectionField to allow the user to select
    multiple categories at one time.
    """
    
    def __init__(self, attrs=None):
        """
        Create a MultiCategoryInput.
        The 'count' attribute can be specified to default the number of 
        category boxes initially presented.
        """

        super(MultiCategoryInput, self).__init__(attrs)
        self.attrs = {'count': 1}

        if attrs:
            self.attrs.update(attrs)
        
    def render(self, name, value, attrs=None):
        """
        Renders the MultiCategoryInput.
        Should not be overridden.  Instead, subclasses should override the
        js, link, and/or fields methods which provide content to this method.
        """

        final_attrs = self.build_attrs(attrs, name=(name + '[]'))
        count = final_attrs['count']

        if count < 1:
            count = 1

        del final_attrs['count']

        js = self.js(name, value, count, final_attrs)
        link = self.link(name, value, count, final_attrs)
        fields = self.fields(name, value, count, final_attrs) 

        return js + fields + " " + link
    
    def fields(self, name, value, count, attrs=None):
        """
        Renders the necessary number of file input boxes.
        """
        
        categories = Category.objects.all().order_by('name')
        
#        for category in categories:
#            print category

        return u''.join(
            [u'<input%s /><br />\n' % flatatt(dict(attrs, id=attrs['id']+str(i))) \
                for i in range(count)])
        
    def link(self, name, value, count, attrs=None):
        """
        Renders a link to add more file input boxes.
        """

        # TODO: Figure out a way to clean this up.
        # TODO: Continue cleaning up after this point.
        return u"""<p><a onclick="javascript:new_%(name)s(); return false;">Attach another file.</a></p>""" % {'name': name,}
        
    #TODO: new upload fields should only be added when the user fills in all the existing file fields (like in Gmail)
    def js(self, name, value, count, attrs=None):
        """
        Renders a bit of Javascript to add more file input boxes.
        """
        return u"""
        <script>
        <!--
        %(id)s_counter=%(count)d;
        function new_%(name)s() {
        
            // Find the last element in our list of input fields (add new stuff to the end of this)
            
            last_input = document.getElementById('%(id)s' + (%(id)s_counter - 1));
            insertBeforeElement = last_input.parentNode.lastChild.previousSibling;
           
            // Insert the actual input field.
            
            input_to_add = document.createElement("input");
            input_to_add.type = "file";
            input_to_add.name = "files[]";
            input_to_add.id = '%(id)s'+(%(id)s_counter++);
            input_to_add.size = 40;
            last_input.parentNode.insertBefore(input_to_add, insertBeforeElement);
            
            // Insert the "remove" link.
            
            removeLink = document.createElement("a");
            removeLink.id = 'remove_' + input_to_add.id;
            removeLink.setAttribute('onclick', "javascript:remove_%(name)s('" + input_to_add.id + "'); return false;");
            removeLink.innerHTML = '&nbsp;&nbsp;Remove this file.'; /** TODO: style this instead of using nbsp **/
            last_input.parentNode.insertBefore(removeLink, insertBeforeElement);
            
            // Insert the final line break.
            
            br = document.createElement("br");
            br.id = 'break_' + input_to_add.id;
            last_input.parentNode.insertBefore(br, insertBeforeElement);
            
        }
        function remove_%(name)s(id) {
            removeNode = document.getElementById(id);
            removeNode.parentNode.removeChild(removeNode);
            removeText = document.getElementById('remove_' + removeNode.id);
            removeText.parentNode.removeChild(removeText);
            removeBreak = document.getElementById('break_' + removeNode.id);
            removeBreak.parentNode.removeChild(removeBreak);
            %(id)s_counter--;
        }
        -->
        </script>
        """ % {'id':attrs['id'], 'name':name, 'count':count}

    def value_from_datadict(self, data, files, name):
        """
        File widgets take data from FILES, not POST.
        """
        name = name+'[]'
        if isinstance(files, MultiValueDict):
            return files.getlist(name)
        else:
            return None

    def id_for_label(self, id_):
        """
        The first file input box always has a 0 appended to it's id.
        """
        if id_:
            id_ += '0'
        return id_
    id_for_label = classmethod(id_for_label)

class MultiCategoryField(Field):
    """
    A field allowing users to upload multiple files at once.
    """

    cats = Category.objects.all().order_by('name')
    
    r_cats = []
    
    for i in cats:
        r_cats.append((i.id, i.name))

    widget = Select(choices=r_cats)
#    widget = MultiCategoryInput()
    
    count = 1
    
    def __init__(self, count=1, strict=False, *args, **kwargs):
        """
        strict is whether the number of files uploaded must equal count
        """
        self.count = count
        self.strict = strict
        super(MultiCategoryField, self).__init__(*args, **kwargs)

    def widget_attrs(self, widget):
        """
        Adds the count to the MultiCategoryInput widget.
        """
        if isinstance(widget, MultiCategoryInput):
            return {'count':self.count}
        return {}

    def clean(self, data):
        """
        Cleans the data and makes sure that all the files had some content.
        """
        super(MultiCategoryField, self).clean(data)
        
        if not self.required and data in EMPTY_VALUES:
            return None
        try:
            f = data
        except TypeError:
            raise ValidationError(ugettext(u"No file was submitted. Check the encoding type on the form..."))
        except KeyError:
            raise ValidationError(ugettext(u"No file was submitted."))

        #for a_file in f:
        #    if not a_file.content:
        #         raise ValidationError(ugettext(u"The submitted file is empty."))
               
        if self.strict and len(f) != self.count:
            raise ValidationError(ugettext(u"An incorrect number of files were uploaded."))
            
        return f

class FixedMultiCategoryInput(MultiCategoryInput):
    """
    A MultiCategoryInput widget that doesn't print the javascript code to allow
    the user to add more file input boxes.
    """
    def link(self, name, value, count, attrs=None):
        return u''
    