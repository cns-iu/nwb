from django.forms.fields import EMPTY_VALUES
from django.forms.fields import Field
from django.forms.fields import UploadedFile
from django.forms.util import ErrorList
from django.forms.util import flatatt
from django.forms.util import ValidationError
from django.forms.widgets import TextInput
from django.utils.datastructures import MultiValueDict
from django.utils.encoding import force_unicode
from django.utils.translation import ugettext


class MultiStringInput(TextInput):
    def __init__(self, attrs=None):
        
        super(MultiStringInput, self).__init__(attrs)
        self.attrs = {'count': 1}
        
        if attrs:
            self.attrs.update(attrs)
        
    def render(self, name, value, attrs=None):
        final_attrs = \
            self.build_attrs(attrs, type=self.input_type, name=name + '[]')
        count = final_attrs['count']
        
        if count < 1:
            count = 1
        
        del final_attrs['count']

        javascript = self.form_javascript(name, value, count, final_attrs)
        link = self.link(name, value, count, final_attrs)
        fields = self.fields(name, value, count, final_attrs) 

        return javascript + fields + " " + link
    
    def fields(self, name, value, count, attrs=None):
        return u''.join([u'<input%s /><br />\n' % flatatt(dict(attrs, id=attrs['id']+str(i))) for i in range(count)])
        
    def link(self, name, value, count, attrs=None):
        return u"""<p><a onclick="javascript:new_%(name)s(); return false;">Attach another file.</a></p>""" % {'name': name,}
    
    def form_javascript(self, name, value, count, attrs=None):
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
        """ % {'id': attrs['id'], 'name': name, 'count': count}

    def value_from_datadict(self, data, files, name):
        name = name + '[]'
        
        if isinstance(files, MultiValueDict):
            return files.getlist(name)
        
        return None

    def id_for_label(self, id_):
        if id_:
            id_ += '0'
        
        return id_
    
    id_for_label = classmethod(id_for_label)

class MultiStringField(Field):
    widget = MultiStringInput
    count = 1
    
    def __init__(self, count=1, strict=False, *args, **kwargs):
        self.count = count
        self.strict = strict
        super(MultiStringField, self).__init__(*args, **kwargs)

    def widget_attrs(self, widget):
        if isinstance(widget, MultiStringInput):
            return {'count': self.count}
        
        return {}

    def clean(self, data):
        super(MultiStringField, self).clean(data)
        
        if not self.required and data in EMPTY_VALUES:
            return None
        
        try:
            file = data
        except TypeError:
            raise ValidationError(ugettext(u"No file was submitted. Check the encoding type on the form..."))
        except KeyError:
            raise ValidationError(ugettext(u"No file was submitted."))
               
        if self.strict and len(file) != self.count:
            raise ValidationError(ugettext(u"An incorrect number of files were uploaded."))
            
        return file

class FixedMultiStringInput(MultiStringInput):
    def link(self, name, value, count, attrs=None):
        return u''
    