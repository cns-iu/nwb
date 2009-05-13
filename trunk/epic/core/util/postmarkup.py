from copy import copy
import re
from urllib import quote
from urllib import quote_plus
from urllib import unquote
from urlparse import urlparse
from urlparse import urlunparse


STRIP_TAGS_REPLACEMENT = ' '

class TagBase(object):
    def __init__(self, tag_name):
        self.display_contents = True
    
    def open(self, tag_name, contents):
        return ''
    
    def close(self, tag_name, contents):
        return ''

class SimpleTag(TagBase):
    def __init__(self, tag_name, substitute):
        TagBase.__init__(self, tag_name)        
        self.substitute = substitute
    
    def open(self, tag_name, contents):
        return '<%s>' % (self.substitute)
    
    def close(self, tag_name, contents):
        return '</%s>' % (self.substitute)    

class LinkTag(TagBase):
    def __init__(self, name):
        TagBase.__init__(self, name)
        
    def open(self, tag_name, contents):
        safe_contents = contents.replace('javascript:', '')
        
        return '<a href="%s">' % safe_contents
    
    def close(self, tag_name, contents):
        return '</a>'

class ImageTag(TagBase):
    def __init__(self, name):
        TagBase.__init__(self, name)
        self.display_contents = False
        
    def open(self, tag_name, contents):
        safe_contents = contents.replace('javascript:', '')
        
        return '<img src="%s" />' % safe_contents
    
    def close(self, tag_name, contents):
        return ''

class QuoteTag(TagBase):
    def __init__(self):
        TagBase.__init__(self, 'quote')
        
    def open(self, tag_name, contents):
        return '<blockquote><em>%s</em><br />' % contents
        
    def close(self, tag_name, contents):
        return '</blockquote>'

class PostMarkup(object):
    def __init__(self):
        self.tags = {}
        
        pre_tag_expression = r'(?P<pre_tag>.*?)'
        opening_tag_expression = r'\[(?P<tag>.+?)\]'
        contents_expression = r'(?P<contents>.*?)'
        closing_tag_expression = r'\[/(?P=tag)\]'
        post_tag_expression = r'(?P<post_tag>.*?)$'
        
        expression = r'%s%s%s%s%s' % (pre_tag_expression,
                                      opening_tag_expression,
                                      contents_expression,
                                      closing_tag_expression,
                                      post_tag_expression)
        
        self.tag_pattern = re.compile(expression, re.I | re.S)

    def __call__(self, post_markup):
        return self.render_to_html(post_markup)
    
    @staticmethod
    def TagFactory(tag_class, *args):
        def make_tag():
            return tag_class(*args)
        
        return make_tag
    
    def add_tag(self, name, tag_class, *args):
        self.tags[name] = PostMarkup.TagFactory(tag_class, *args)
    
    def default_tags(self):        
        self.tags['b'] = PostMarkup.TagFactory(SimpleTag, 'b', 'b')
        self.tags['i'] = PostMarkup.TagFactory(SimpleTag, 'i', 'em')
        self.tags['u'] = PostMarkup.TagFactory(SimpleTag, 'u', 'u')
        self.tags['s'] = PostMarkup.TagFactory(SimpleTag, 's', 'strike')
        self.tags['img'] = PostMarkup.TagFactory(ImageTag, 'img')
        # TODO: Take this out?
        self.tags['blink'] = \
            PostMarkup.TagFactory(SimpleTag, 'blink', 'blink')
        
        return self
    
    def render_to_html(self, post_markup):
        safe_post_markup = post_markup.replace('<', '&lt;')
        safe_post_markup = safe_post_markup.replace('>', '&rt;')
        max_recursion_level = len(self.tags)
        
        try:
            rendered_html = \
                self._convert_markup(safe_post_markup, 0, max_recursion_level)
        except InvalidBBCodeException, invalid_bbcode_exception:
            return invalid_bbcode_exception.value
        
        return rendered_html
    
    def strip_tags(self, post_markup):
        safe_post_markup = post_markup.replace('<', '&lt;')
        safe_post_markup = safe_post_markup.replace('>', '&rt;')
        
        stripped_post_markup = safe_post_markup
        
        for tag_name in self.tags.keys():
            opening_tag_string = '[%s]' % tag_name
            closing_tag_string = '[/%s]' % tag_name
            
            stripped_post_markup = stripped_post_markup.replace(
                opening_tag_string, STRIP_TAGS_REPLACEMENT)
            stripped_post_markup = stripped_post_markup.replace(
                closing_tag_string, STRIP_TAGS_REPLACEMENT)
        
        unmarked_up_result = stripped_post_markup
        
        return unmarked_up_result
    
    def _convert_markup(self, markup, recursion_level, max_recursion_level):
        converted_markup_so_far = ''
        markup_to_convert = markup
        there_is_markup_to_convert = True
        
        while there_is_markup_to_convert:
            match = self.tag_pattern.match(markup_to_convert)
            
            if match is None:
                converted_markup_so_far += markup_to_convert
                
                break
            
            pre_tag = match.group('pre_tag')
            tag_name = match.group('tag')
            contents = match.group('contents')
            post_tag = match.group('post_tag')
            
            pre_tag_string = pre_tag
            markup_to_convert = post_tag
            
            exception_string = 'ERROR: There are too many nested BBCode ' + \
                'tags.  The maximum number of nested BBCode tags is %s.' % \
                    max_recursion_level
            
            if tag_name in self.tags:
                tag = self.tags[tag_name]()
                
                opening_string = tag.open(tag_name, contents)
                closing_string = tag.close(tag_name, contents)
                
                if tag.display_contents:
                    try:
                        if recursion_level == max_recursion_level:
                            raise InvalidBBCodeException(exception_string)
                        
                        contents_string = self._convert_markup(
                            contents, recursion_level + 1, max_recursion_level)
                    except InvalidBBCodeException, invalid_bbcode_exception:
                        raise invalid_bbcode_exception
                else:
                    contents_string = ''
            else:
                opening_string = '[%s]' % tag_name
                closing_string = '[/%s]' % tag_name
                
                try:
                    if recursion_level == max_recursion_level:
                        raise InvalidBBCodeException(exception_string)
                    
                    contents_string = self._convert_markup(
                        contents, recursion_level + 1, max_recursion_level)
                except InvalidBBCodeException, invalid_bbcode_exception:
                    raise invalid_bbcode_exception
            
            converted_markup_so_far += pre_tag_string
            converted_markup_so_far += opening_string
            converted_markup_so_far += contents_string
            converted_markup_so_far += closing_string
        
        final_converted_markup = converted_markup_so_far
        
        return final_converted_markup

class InvalidBBCodeException(Exception):
    def __init__(self, value):
        self.value = value

    def __str__(self):
        return repr(self.value)
