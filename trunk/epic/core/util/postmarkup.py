"""
Post Markup v1.0.1
Author: Will McGugan (http://www.willmcgugan.com)
"""

import re
from urllib import quote, unquote, quote_plus
from urlparse import urlparse, urlunparse
from copy import copy


pygments_available = True
try:
    from pygments import highlight
    from pygments.lexers import get_lexer_by_name, ClassNotFound
    from pygments.formatters import HtmlFormatter
except ImportError:
    pygments_available = False


def Create(include=None, exclude=None, use_pygments=True):
    
    """
    Create a postmarkup object.
    include -- List or similar iterable containing the names of the tags to use
               If omitted, all tags will be used
    exclude -- List or similar iterable containing the names of the tags to exclude.
               If omitted, no tags will be excluded
    use_pygments -- If True, Pygments (http://pygments.org/) will be used for the code tag,
                    otherwise it will use <pre>code</pre>
    """
    
    markup = PostMarkup()
    
    def add_tag(name, tag_class, *args):
        if include is None or name in include:
            if exclude is not None and name in exclude:
                return
            markup.add_tag(name, tag_class, *args)
    
    add_tag('b', SimpleTag, 'b', 'strong')
    add_tag('i', SimpleTag, 'i', 'em')
    add_tag('u', SimpleTag, 'u', 'u')
    add_tag('s', SimpleTag, 's', 'strike')
    add_tag('link', LinkTag, 'link')
    add_tag('url', LinkTag, 'url')    
    add_tag('quote', QuoteTag)
    
    add_tag('wiki', SearchTag, 'wiki',
            "http://en.wikipedia.org/wiki/Special:Search?search=%s", 'Wikipedia')
    add_tag('google', SearchTag, 'google',
            "http://www.google.com/search?hl=en&q=%s&btnG=Google+Search", 'Google')
    add_tag('wiki', SearchTag, 'wiki',
            "http://www.reference.com/search?q=%s", 'Definition')
    
    if use_pygments:
        assert pygments_available, "Could Not import pygments (http://pygments.org/)"
        add_tag('code', PygmentsCodeTag, 'code')
    else:
        add_tag('code', SimpleTag, 'code', 'pre')
    
    return markup


re_html=re.compile('<.*?>|\&.*?\;')
def textilize(s):
    """Remove markup from html"""
    return re_html.sub("", s)    

re_excerpt = re.compile(r'\[".*?\]+?.*?\[/".*?\]+?', re.DOTALL)
re_remove_markup = re.compile(r'\[.*?\]', re.DOTALL)

def remove_markup(post):
    return re_remove_markup.sub("", post)

def get_excerpt(post):
    match = re_excerpt.search(post)
    if match is None:
        return ""
    excerpt = match.group(0)
    excerpt=excerpt.replace('\n', "<br/>")
    return remove_markup(excerpt)


class TagBase(object):    
    """
    Base class for a Post Markup tag.        
    """
    
    def __init__(self, name):
        self.name = name
        self.params = None
        self.auto_close = False
        self.enclosed = False
                
    def open(self, open_pos):
        """Called when the tag is opened. Should return a string or a stringifyable object."""
        self.open_pos = open_pos
        return ''
    
    def close(self, close_pos, content):
        """Called when the tag is closed. Should return a string or a stringifyable object."""
        self.close_pos = close_pos
        self.content = content
        return ''

    def get_tag_contents(self):
        """Gets the contents of the tag."""
        content_elements = self.content[self.open_pos+1:self.close_pos]
        contents = "".join([str(element) for element in content_elements if isinstance(element, StringToken)])
        contents = textilize(contents)
        return contents
    
    def get_raw_tag_contents(self):
        """Gets the raw contents (includes html tags) of the tag."""
        content_elements = self.content[self.open_pos+1:self.close_pos]
        contents = "".join(element.raw for element in content_elements)        
        return contents
    
# A proxy object that calls a callback when converted to a string
class TagStringify(object):
    def __init__(self, callback, raw):
        self.callback = callback
        self.raw = raw
    def __str__(self):
        return self.callback()
    def __repr__(self):
        return self.__str__()


class SimpleTag(TagBase):
    
    """Simple substitution tag."""
    
    def __init__(self, name, substitute):
        TagBase.__init__(self, name)        
        self.substitute = substitute
    
    def open(self, open_pos):
        """Called to render the opened tag."""
        return "<%s>"%(self.substitute)
        
    def close(self, close_pos, content):
        """Called to render the closed tag."""
        return "</%s>"%(self.substitute)    


class LinkTag(TagBase):
    
    """Tag that generates a link (</a>)."""
    
    def __init__(self, name):
        TagBase.__init__(self, name)
        
    def open(self, open_pos):
        self.open_pos = open_pos
        return TagStringify(self._open, self.raw)
    
    def close(self, close_pos, content):
        
        self.close_pos = close_pos
        self.content = content        
        return TagStringify(self._close, self.raw)
    
    def _open(self):
        if self.params:
            url=self.params
        else:
            url=self.get_tag_contents()
        
        self.domain = ""
        #Unquote the url
        self.url=unquote(url)        
        
        #Disallow javascript links
        if "javascript:" in self.url.lower():            
            return ""
                
        #Disallow non http: links
        url_parsed = urlparse(self.url)
        if url_parsed[0] and url_parsed[0].lower()!='http':
            return ""
        
        #Prepend http: if it is not present
        if not url_parsed[0]:
            self.url="http://"+self.url
            url_parsed = urlparse(self.url)
            
        #Get domain       
        self.domain = url_parsed[1].lower()
        
        #Remove www for brevity
        if self.domain.startswith('www.'):
            self.domain = self.domain[4:]
        
        #Quote the url        
        self.url="http:"+urlunparse( map(quote, ("",)+url_parsed[1:]) )
        
        #Sanity check        
        if not self.url:
            return ""   
        
        if self.domain:
            return '<a href="%s">'%self.url
        else:
            return ""
        
    def _close(self):
        
        if self.domain:
            return '</a>'+self.annotate_link(self.domain)
        else:
            return ''        
    
    def annotate_link(self, domain):
        """Annotates a link with the domain name.
        Override this to disable or change link annotation.
        """
        return " [%s]"%domain

class ImageTag(TagBase):
    """Tag that generates an image (</img>)."""
    
    def __init__(self, name):
        TagBase.__init__(self, name)
        
    def open(self, open_pos):
        self.open_pos = open_pos
        
        return TagStringify(self._open, self.raw)
    
    def close(self, close_pos, content):
        self.close_pos = close_pos
        self.content = content
        
        return TagStringify(self._close, self.raw)
    
    def _open(self):
        if self.params:
            url = self.params
        else:
            url = self.get_tag_contents()
        
        self.domain = ''
        self.url=unquote(url)        
        
        # Disallow javascript links.
        if 'javascript:' in self.url.lower():            
            return ''
                
        # Disallow non http: links.
        url_parsed = urlparse(self.url)
        
        if url_parsed[0] and url_parsed[0].lower() != 'http':
            return ''
        
        # Prepend http: if it is not present.
        if not url_parsed[0]:
            self.url = 'http://%s' % self.url
            url_parsed = urlparse(self.url)
            
        # Get domain.       
        self.domain = url_parsed[1].lower()
        
        # Remove www for brevity.
        if self.domain.startswith('www.'):
            self.domain = self.domain[4:]
        
        # Quote the url.        
        self.url = 'http:%s' % urlunparse(map(quote, ('',) + url_parsed[1:]))
        
        # Sanity check.        
        if not self.url:
            return ''  
        
        # TODO: This is kind of a hack.  Learn how this "BBCode" module
        # actually works, and make it less hacky.
        if self.domain:
            return '<img src="'
        else:
            return ''
    
    # TODO: See above TODO about hackyness. 
    def _close(self):
        return '" />'        
    
    def annotate_image(self, domain):
        """Annotates an image with the domain name.
        Override this to disable or change image annotation.
        """
        
        return ' [%s]' % domain

class QuoteTag(TagBase):
    """
    Generates a blockquote with a message regarding the author of the quote.
    """
    def __init__(self):
        TagBase.__init__(self, 'quote')
        
    def open(self, open_pos):
        return '<blockquote><em>%s</em><br/>'%(self.params)
        
    def close(self, close_pos, content):
        return "</blockquote>"


class SearchTag(TagBase):
    """
    Creates a link to a search term.
    """
    
    def __init__(self, name, url, label=""):
        TagBase.__init__(self, name)
        self.url=url
        self.search = ""
        self.label = label or name
    
    def __str__(self):
        
        link='<a href="%s">'%self.url
        
        if '%' in link:
            return link%quote_plus(self.get_tag_contents())
        else:
            return link
    
    def open(self, open_pos):
        self.open_pos = open_pos
        return TagStringify(self._open, self.raw)
    
    def close(self, close_pos, content):
        
        self.close_pos = close_pos
        self.content = content        
        return TagStringify(self._close, self.raw)
    
    def _open(self):
        if self.params:
            search=self.params
        else:
            search=self.get_tag_contents()
        link='<a href="%s">'%self.url
        if '%' in link:
            return link%quote_plus(search)
        else:
            return link
        
    def _close(self):
        
        if self.label:
            return '</a>'+self.annotate_link(self.label)
        else:
            return ''
        
    def annotate_link(self, domain):
        return " [%s]"%domain   



class PygmentsCodeTag(TagBase):
    
    def __init__(self, name):
        TagBase.__init__(self, name)
        self.enclosed = True
    
    def open(self, open_pos):
        self.open_pos = open_pos
        return TagStringify(self._open, self.raw)
    
    def close(self, close_pos, content):
        
        self.close_pos = close_pos
        self.content = content        
        return TagStringify(self._close, self.raw)

    def _open(self):
        
        try:
            lexer = get_lexer_by_name(self.params, stripall=True)
        except ClassNotFound:
            return '<div style="code"><pre>%s</pre>'%postmarkup.Escape(self.get_raw_tag_contents())
        formatter = HtmlFormatter(linenos=False, cssclass="code")        
        code = self.get_raw_tag_contents()
        result = highlight(code, lexer, formatter)
        return result+"\n<div style='display:none'>"
    
    def _close(self):
        return "</div>"
            

# http://effbot.org/zone/python-replace.htm
class MultiReplace:

    def __init__(self, repl_dict):
        # "compile" replacement dictionary

        # assume char to char mapping
        charmap = map(chr, range(256))
        for k, v in repl_dict.items():
            if len(k) != 1 or len(v) != 1:
                self.charmap = None
                break
            charmap[ord(k)] = v
        else:
            self.charmap = string.join(charmap, "")
            return

        # string to string mapping; use a regular expression
        keys = repl_dict.keys()
        keys.sort() # lexical order
        keys.reverse() # use longest match first        
        pattern = "|".join(re.escape(key) for key in keys)
        self.pattern = re.compile(pattern)
        self.dict = repl_dict

    def replace(self, str):
        # apply replacement dictionary to string
        if self.charmap:
            return string.translate(str, self.charmap)
        def repl(match, get=self.dict.get):
            item = match.group(0)
            return get(item, item)
        return self.pattern.sub(repl, str)


class StringToken(object):
    
    def __init__(self, raw):
        self.raw = raw
    
    def __str__(self):
        return PostMarkup.standard_replace.replace(self.raw)
    

def Escape(s):
    return PostMarkup.standard_replace.replace(s)

class PostMarkup(object):
    
    standard_replace = MultiReplace({'<':'&lt;', '>':'&gt;', '&':'&amp;', '\n':'<br/>'})
    
    TOKEN_TAG, TOKEN_PTAG, TOKEN_TEXT = range(3)
        
    
    @staticmethod
    def TagFactory(tag_class, *args):
        """
        Returns a callable that returns a new tag instance.
        """
        def make():
            return tag_class(*args)
        
        return make    
    
    
    # I tried to use RE's. Really I did.
    @staticmethod
    def tokenize(post):
        
        text = True
        pos = 0
        
        def find_first(post, pos, c):
            f1 = post.find(c[0], pos)
            f2 = post.find(c[1], pos)
            if f1 == -1:
                return f2
            if f2 == -1:
                return f1
            return min(f1, f2)
        
        while True:
            
            brace_pos = post.find('[', pos)
            if brace_pos == -1:
                yield PostMarkup.TOKEN_TEXT, post[pos:]
                return
            if brace_pos - pos > 0:
                yield PostMarkup.TOKEN_TEXT, post[pos:brace_pos]
                
            pos = brace_pos                        
            end_pos = pos+1
            
            end_pos = find_first(post, end_pos, ']=')            
            if end_pos == -1:
                yield post[pos:]
                return            
                
            if post[end_pos] == ']':
                yield PostMarkup.TOKEN_TAG, post[pos:end_pos+1]
                pos = end_pos+1
                continue
            
            if post[end_pos]=='=':            
                try:
                    end_pos += 1
                    while post[end_pos]==' ':
                        end_pos += 1
                    if post[end_pos]!='"':
                        end_pos = post.find(']', end_pos+1)
                        if end_pos == -1:
                            return
                        yield PostMarkup.TOKEN_TAG, post[pos:end_pos+1]
                    else:
                        end_pos = find_first(post, end_pos, '"]')
                        if end_pos==-1:
                            return                        
                        if post[end_pos] == '"':
                            end_pos = post.find('"', end_pos+1)
                            if end_pos == -1:
                                return
                            end_pos = post.find(']', end_pos+1)
                            if end_pos == -1:
                                return
                            yield PostMarkup.TOKEN_PTAG, post[pos:end_pos+1]
                        else:
                            yield PostMarkup.TOKEN_TAG, post[pos:end_pos+1] 
                    pos = end_pos+1
                except IndexError:
                    return
                
                
    def __init__(self):
        
        self.tags={}
    
    
    def default_tags(self):        
        """
        Sets up a minimal set of tags.
        """
        self.tags['b'] = PostMarkup.TagFactory(SimpleTag, 'b', 'strong')
        self.tags['i'] = PostMarkup.TagFactory(SimpleTag, 'i', 'em')
        self.tags['u'] = PostMarkup.TagFactory(SimpleTag, 'u', 'u')
        self.tags['s'] = PostMarkup.TagFactory(SimpleTag, 's', 'strike')
        self.tags['img'] = PostMarkup.TagFactory(ImageTag, 'img')
        # TODO: Take this out?
        self.tags['blink'] = \
            PostMarkup.TagFactory(SimpleTag, 'blink', 'blink')
        
        return self
    
    def add_tag(self, name, tag_class, *args):
        """
        Add a tag factory to the markup.
        """
        self.tags[name] = PostMarkup.TagFactory(tag_class, *args)
        

    def __call__(self, post_markup):
        return self.render_to_html(post_markup)


    def render_to_html(self, post_markup):        
        """
        Converts Post Markup to xhtml.
        """        
        
        post=[]
        tag_stack=[]        
        break_stack=[]
        enclosed = False
        
        def check_tag_stack(tag_name):
            """Check to see if a tag has been opened."""
            for tag in reversed(tag_stack):
                if tag_name == tag.name:
                    return True
            return False
        
        def redo_break_stack():
            """Re-opens tags that have been closed prematurely."""
            while break_stack:
                tag= copy(break_stack.pop())
                tag.raw=""
                tag_stack.append(tag)
                post.append(tag.open(len(post)))
        
        for tag_type, tag_token in PostMarkup.tokenize(post_markup):
            #print tag_type, tag_token
            raw_tag_token = tag_token
            if tag_type == PostMarkup.TOKEN_TEXT:
                redo_break_stack()                                    
                post.append(StringToken(tag_token))
                continue
            elif tag_type == PostMarkup.TOKEN_TAG:
                tag_token = tag_token[1:-1].lstrip()
                if ' ' in tag_token:
                    tag_name, tag_attribs = tag_token.split(' ', 1)
                    tag_attribs = tag_attribs.strip()
                else:
                    tag_name = tag_token
                    tag_attribs = ""
            else:
                tag_token = tag_token[1:-1].lstrip()
                tag_name, tag_attribs = tag_token.split('=', 1)
                tag_attribs=tag_attribs.strip()[1:-1]

            tag_name = tag_name.strip().lower()
            
            end_tag = False
            if tag_name.startswith('/'):
                end_tag = True
                tag_name = tag_name[1:]
            
            if not end_tag:
                if enclosed:
                    post.append(StringToken(raw_tag_token))
                    continue                    
                if tag_name not in self.tags:                    
                    continue                    
                tag = self.tags[tag_name]()
                enclosed = tag.enclosed
                tag.raw = raw_tag_token
                
                redo_break_stack()
                tag.params=tag_attribs
                tag_stack.append(tag)                    
                post.append(tag.open(len(post)))
                if tag.auto_close:
                    end_tag = True
                
            if end_tag:
                if not check_tag_stack(tag_name):
                    if enclosed:
                        post.append(StringToken(raw_tag_token))
                    continue                
                enclosed = False 
                while tag_stack[-1].name != tag_name:
                    tag = tag_stack.pop()
                    break_stack.append(tag)
                    if not enclosed:
                        post.append(tag.close(len(post), post))
                post.append(tag_stack.pop().close(len(post), post))
                
        if tag_stack:            
            redo_break_stack()
            while tag_stack:            
                post.append(tag_stack.pop().close(len(post), post))
                     
        return "".join(str(p) for p in post)
                
                

def test():
    
    #post_markup = PostMarkup()
    post_markup = Create()
        
    tests = []
    print """<link rel="stylesheet" href="/core_media/css/postmarkup.css" type="text/css" />\n"""
    tests.append("[b]Hello[/b]")
    tests.append("[s]Strike through[/s]")
    tests.append("[b]bold [i]bold and italic[/b] italic[/i]")
    tests.append("[google]Will McGugan[/google]")
    tests.append("[wiki Will McGugan]Look up my name in Wikipedia[/wiki]")
    tests.append("[link http://www.willmcgugan.com]My homepage[/link]")
    tests.append("[link]http://www.willmcgugan.com[/link]")
    tests.append("[quote Will said...]BBCode is very cool[/quote]")

    tests.append("""[code python]
# A proxy object that calls a callback when converted to a string
class TagStringify(object):
    def __init__(self, callback, raw):
        self.callback = callback
        self.raw = raw
    def __str__(self):
        return self.callback()
    def __repr__(self):
        return self.__str__()
[/code]""")

    long_test="""[b]Long test[/b]

New lines characters are converted to breaks. Tags my be [b]ove[i]rl[/b]apped[/i].

[i]Open tags will be closed.
"""

    tests.append(long_test)

    for test in tests:        
        print "<pre>%s</pre>"%test
        print "<p>%s</p>"%post_markup(test)
        print "<br/>"
        print
        
if __name__ == "__main__":
    test()