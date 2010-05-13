"""
Tests for the multiple file upload widget and field.

Created by Edward Dale (www.scompt.com)
Released into the Public Domain
"""

from multifile import *
import unittest

from django import newforms as forms
from django.test.client import Client
from django.utils.datastructures import MultiValueDict

from epic.core.test import CustomTestCase


INPUT_NAME = 'files'
MULTI_FILE_INPUT_VALUE = 'testinput'
INPUT_ID = 'id_files'

COUNT_KEY = 'count'
ID_KEY = 'id'

class MultiFileInputTest(unittest.TestCase):
    """
    Tests for the widget itself.
    """
    
    def setUp(self):
        self.multi_file_input_attributes = {ID_KEY: INPUT_ID}
    
    def testBasics(self):
        """
        Make sure the basics are correct (needs_multipart_form & is_hidden).
        """
        
        multi_file_input = MultiFileInput()
        self.assertTrue(multi_file_input.needs_multipart_form)
        self.assertFalse(multi_file_input.is_hidden)
    
    def testNoRender(self):
        """
        Makes sure we show a minimum of 1 input box.
        """

        multi_file_input_html = _make_multi_file_input_html(0)
        _assertHTMLContainsFileInput(multi_file_input_html, 0)
        
    def testSingleRender(self):
        """
        Test the output of a single field being rendered.
        """
        
        multi_file_input_html = _make_multi_file_input_html()
        _assertHTMLContainsFileInput(multi_file_input_html, 0)
    
    def testMultiRender(self):
        """
        Tests that two input boxes are rendered when given a count of 2.
        """
        
        multi_file_input_html = _make_multi_file_input_html(2)
        _assertHTMLContainsFileInput(multi_file_input_html, 0)
        _assertHTMLContainsFileInput(multi_file_input_html, 1)
    
    def _make_multi_file_input_html(count=None):
        
        if count is None:
            multi_file_input = MultiFileInput()
        else:
            multi_file_input = MultiFileInput({COUNT_KEY: 2})
            
        multi_file_input_html = multi_file_input.render(
            name=INPUT_NAME, value=MULTI_FILE_INPUT_VALUE, attrs=self.multi_file_input_attributes)

        return multi_file_input_html

class MultiFileFieldTest(unittest.TestCase):
    """
    Tests the MultiFileField field.
    """
    
    def testOneRender(self):
        """
        Test the rendering of a MultiFileField with 1 input box.
        """
        
        form = self._RequiredForm()
        form_html = form.as_p()
        input0_html = self._make_input_html(0)
        self.assert_(input0_html in form_html)
    
    def testTwoRender(self):
        """
        Test the rendering of a MultiFileField with 2 input boxes.
        """
        
        form = self._MultiForm()
        form_html = form.as_p()
        input0_html = self._make_input_html(0)
        input1_html = self._make_input_html(1)
        self.assert_(input0_html in form_html)
        self.assert_(input1_html in form_html)
        
    def testNoFiles(self):
        """
        Tests binding a Form with required and optional MultiFileFields.
        """
        
        required_form = self._RequiredForm({}, {})
        self.assertTrue(required_form.is_bound)
        self.assertFalse(required_form.is_valid())
        
        optional_form = self._OptionalForm({}, {})
        self.assertTrue(optional_form.is_bound)
        self.assertTrue(optional_form.is_valid())
    
    def testOneFile(self):
        """
        Tests the binding of a Form with a single file attached.
        """
        
        file_data = MultiValueDict({'files[]': [{'filename': 'face.jpg', 'content': 'www'}]})
        f = self._RequiredForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertTrue(f.is_valid())
        
        self.assertEquals(len(f.cleaned_data['files']), 1)
        self.assertEquals(f.cleaned_data['files'][0].filename, file_data['files[]']['filename'])
        self.assertEquals(f.cleaned_data['files'][0].content, file_data['files[]']['content'])
        
        f = self._OptionalForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertTrue(f.is_valid())
    
    def testTwoFile(self):
        """
        Tests the binding of a Form with two files attached.
        """
        file_data = MultiValueDict(
            {'files[]': [
                {'filename':'face.jpg', 'content': 'www'},
                {'filename':'lah.jpg', 'content': 'woop'}
            ]})
        f = self._RequiredForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertTrue(f.is_valid())
        self.assertEquals(len(f.cleaned_data['files']), 2)

        for input_file in file_data.getlist('files[]'):
            found = False

            for output_file in f.cleaned_data['files']:
                filenames_match = output_file.filename == input_file['filename']
                contents_match = output_file.content == input_file['content']
                found = found or (filenames_match and contents_match)

            self.assertTrue(found)
    
    def testEmptyFile(self):
        """
        Tests the binding of a Form with 1 empty file.
        """
        file_data = MultiValueDict({'files[]': [{'filename':'face.jpg', 'content': ''}]})
        f = self._RequiredForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertFalse(f.is_valid())
        
        f = self._OptionalForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertFalse(f.is_valid())
    
    def testOneEmptyFile(self):
        """
        If any file is empty, then the whole form is invalid.
        """
        file_data = MultiValueDict(
            {'files[]': [
                {'filename':'face.jpg', 'content': 'www'},
                {'filename':'lah.jpg', 'content': ''}
            ]})
        f = self._RequiredForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertFalse(f.is_valid())
        
        f = self._OptionalForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertFalse(f.is_valid())
    
    def testStrict(self):
        """
        Tests whether the strict attribute works to force a user to upload n files.
        """
        
        # 1 file is no good, we want 2
        file_data = MultiValueDict({'files[]': [{'filename':'face.jpg', 'content': 'www'}]})
        f = self._StrictForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertFalse(f.is_valid())        

        # 2 files is great, we want 2
        file_data = MultiValueDict(
            {'files[]': [
                {'filename':'face.jpg', 'content': 'www'},
                {'filename':'lah.jpg', 'content': 'asdf'}
            ]})
        f = self._StrictForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertTrue(f.is_valid())        

        # 3 files is no good, we want 2
        file_data = MultiValueDict(
            {'files[]': [
                {'filename':'face.jpg', 'content': 'www'},
                {'filename':'lah.jpg', 'content': 'asdf'},
                {'filename':'blah.jpg', 'content': 'asdf'}
            ]})
        f = self._StrictForm({}, file_data)
        self.assertTrue(f.is_bound)
        self.assertFalse(f.is_valid())        
    
    def testBind(self):
        """
        Tests the binding of the form.  Probably not necessary.
        """
        file_data = {'files': {'filename':'face.jpg', 'content': ''}}
        f = self._RequiredForm()
        self.assertFalse(f.is_bound)
        
        f = self._RequiredForm({}, file_data)
        self.assertTrue(f.is_bound)
    
    def _make_input_html(self, id_index):
        input_attributes_dictionary = {'name': INPUT_NAME, 'id': INPUT_ID, 'id_index': id_index}
        
        input_html = '<input type="file" name="%(name)s[]" id="%(id)s%(id_index)s" />' % \
            input_attributes_dictionary
        
        return input_html
    
    class _OptionalForm(forms.Form):
        """
        A simple Form that has an optional MultiFileField.
        """
        
        files = MultiFileField(required=False)
    
    class _RequiredForm(forms.Form):
        """
        A simple Form that has an required MultiFileField.
        """
        
        files = MultiFileField(required=True)
    
    class _MultiForm(forms.Form):
        """
        A simple Form with a MultiFileField with 2 input boxes.
        """
        
        files = MultiFileField(count=2)
    
    class _StrictForm(forms.Form):
        files = MultiFileField(count=2, strict=True)

class FixedMultiFileTest(unittest.TestCase):
    def testSingleRender(self):
        """
        Test the output of a single field being rendered.
        """
        m = FixedMultiFileInput()
        r = m.render(name='blah', value='bla', attrs={'id':'test'})
        self.assertEquals('<input type="file" name="blah[]" id="test0" />\n', r)

#TODO: Rename or add comments to clarify
def _assertHTMLContainsFileInput(html, id_index):
    file_input_html_dictionary = {'name': INPUT_NAME, 'id': INPUT_ID, 'id_index': id_index}
    
    file_input_html = '<input type="file" name="%(name)s[]" id="%(id)s%(id_index)s" />' % \
        file_input_html_dictionary
    self.assert_(file_input_html in html)
