from epic.core.test import CustomTestCase
from epic.core.util.postmarkup import *


TEST_TAG_NAME = 'test_tag'
TEST_TAG_SUBSTITUTE = 'tag_test'
TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT = 'test_contents'
TEST_TAG_CONTENTS_WITH_JAVASCRIPT = 'javascript:%s' % TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT

# Weak.
class TagBaseTestCase(CustomTestCase):
    def setUp(self):
        self.tag_base = TagBase(TEST_TAG_NAME)
    
    def testInit(self):
        self.assertTrue(self.tag_base.display_contents)
    
    def testOpen(self):
        open_result = self.tag_base.open(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        self.assertEqual(open_result, '')

    def testClose(self):
        close_result = self.tag_base.open(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        self.assertEqual(close_result, '')

class SimpleTagTestCase(CustomTestCase):
    def setUp(self):
        self.simple_tag = SimpleTag(TEST_TAG_NAME, TEST_TAG_SUBSTITUTE)
    
    def testInit(self):
        self.assertTrue(self.simple_tag.display_contents)
    
    def testOpen(self):
        open_result = self.simple_tag.open(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        expected_open_result = '<%s>' % TEST_TAG_SUBSTITUTE
        self.assertEqual(open_result, expected_open_result)
    
    def testClose(self):
        close_result = self.simple_tag.close(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        expected_close_result = '</%s>' % TEST_TAG_SUBSTITUTE
        self.assertEqual(close_result, expected_close_result)

class LinkTagTestCase(CustomTestCase):
    def setUp(self):
        self.link_tag = LinkTag(TEST_TAG_NAME)
    
    def testInit(self):
        self.assertTrue(self.link_tag.display_contents)
    
    def testOpenWithoutJavaScript(self):
        open_result = self.link_tag.open(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        expected_open_result = '<a href="%s">' % TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT
        self.assertEqual(open_result, expected_open_result)
    
    def testOpenWithJavaScript(self):
        open_result = self.link_tag.open(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITH_JAVASCRIPT)
        expected_open_result = '<a href="%s">' % TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT
        self.assertEqual(open_result, expected_open_result)
    
    def testClose(self):
        close_result = self.link_tag.close(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        expected_close_result = '</a>'
        self.assertEqual(close_result, expected_close_result)

class ImageTagTestCase(CustomTestCase):
    def setUp(self):
        self.image_tag = ImageTag(TEST_TAG_NAME)
    
    def testInit(self):
        self.assertFalse(self.image_tag.display_contents)
    
    def testOpenWithoutJavaScript(self):
        open_result = self.image_tag.open(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        expected_open_result = '<img src="%s" />' % TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT
        self.assertEqual(open_result, expected_open_result)
    
    def testOpenWithJavaScript(self):
        open_result = self.image_tag.open(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITH_JAVASCRIPT)
        expected_open_result = '<img src="%s" />' % TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT
        self.assertEqual(open_result, expected_open_result)
    
    def testClose(self):
        close_result = self.image_tag.close(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        expected_close_result = ''
        self.assertEqual(close_result, expected_close_result)

class QuoteTagTestCase(CustomTestCase):
    def setUp(self):
        self.quote_tag = QuoteTag()
    
    def testInit(self):
        self.assertTrue(self.quote_tag.display_contents)
    
    def testOpen(self):
        open_result = self.quote_tag.open(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        expected_open_result = \
            '<blockquote><em>%s</em><br />' % TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT
        self.assertEqual(open_result, expected_open_result)
    
    def testClose(self):
        close_result = self.quote_tag.close(TEST_TAG_NAME, TEST_TAG_CONTENTS_WITHOUT_JAVASCRIPT)
        expected_close_result = '</blockquote>'
        self.assertEqual(close_result, expected_close_result)

class PostMarkupTestCase(CustomTestCase):
    def setUp(self):
        self.post_markup = PostMarkup()
        self.default_tags = ('b', 'i', 'u', 's', 'img', 'blink',)
        self.DIRTY_INPUT = '<img src="test.jpg" />'
        self.CLEANED_INPUT = '&lt;img src="test.jpg" /&rt;'
        self.MARKUP = (
            'pre[tag]contents',
            'pre[tag]contents[/tag]post',
            'blah',
            'pre[invalid_tag]invalid tag contents[/invalid_tag][b]bold![/b]',
            'pre[b]contents[/b]post',
            'pre[b][u]contents[/u][/b]post',
            'pre[b]contents1[/b]post1[u]contents2[/u]post2',
            'pre[b][u]contents1[/u][/b]post1[b]contents2[/b]post2',
            'pre[b][u]contents[/b]post',
        )
        self.EXPECTED_RENDERED_MARKUP = (
            self.MARKUP[0],
            self.MARKUP[1],
            self.MARKUP[2],
            'pre[invalid_tag]invalid tag contents[/invalid_tag]<b>bold!</b>',
            'pre<b>contents</b>post',
            'pre<b><u>contents</u></b>post',
            'pre<b>contents1</b>post1<u>contents2</u>post2',
            'pre<b><u>contents1</u></b>post1<b>contents2</b>post2',
            'pre<b>[u]contents</b>post',
        )
        expected_stripped_markup_data = {'replacement': STRIP_TAGS_REPLACEMENT}
        self.EXPECTED_STRIPPED_MARKUP = (
            self.MARKUP[0],
            self.MARKUP[1],
            self.MARKUP[2],
            ('pre[invalid_tag]invalid tag contents[/invalid_tag]' + \
             '%(replacement)sbold!%(replacement)s') % \
                expected_stripped_markup_data,
            'pre%(replacement)scontents%(replacement)spost' % \
                expected_stripped_markup_data,
            ('pre%(replacement)s%(replacement)scontents' + \
             '%(replacement)s%(replacement)spost') % \
                expected_stripped_markup_data,
            ('pre%(replacement)scontents1%(replacement)spost1' + \
             '%(replacement)scontents2%(replacement)spost2') % \
                expected_stripped_markup_data,
            ('pre%(replacement)s%(replacement)scontents1%(replacement)s' + \
             '%(replacement)spost1%(replacement)s' + \
             'contents2%(replacement)spost2') % expected_stripped_markup_data,
            ('pre%(replacement)s%(replacement)scontents' + \
             '%(replacement)spost') % expected_stripped_markup_data,
        )
    
    def testInit(self):
        self.assertEqual(self.post_markup.tags, {})
        self.assertTrue(self.post_markup.tag_pattern is not None)
    
    def testTagFactory(self):
        tag_factory = self.post_markup.TagFactory(SimpleTag, 'b', 'b')
        tag_factory_result = tag_factory()
        self.assertEqual(type(tag_factory_result), SimpleTag)
    
    def testAddTag(self):
        self.post_markup.add_tag('b', SimpleTag, 'b', 'b')
        self.assertTrue('b' in self.post_markup.tags)
    
    def testDefaultTags(self):
        self.post_markup.default_tags()
        self.failIfNot(_verifyPostMarkupHasTags(self.post_markup, include_tags=self.default_tags))
    
    def testRenderDirtyInputToHTML(self):
        rendered_html = self.post_markup.render_to_html(self.DIRTY_INPUT)
        self.assertEqual(rendered_html, self.CLEANED_INPUT)
    
    def testRenderMarkupToHTML(self):
        self.post_markup.default_tags()
        
        for i in range(len(self.MARKUP)):
            markup = self.MARKUP[i]
            expected_rendered_markup = self.EXPECTED_RENDERED_MARKUP[i]
            
            rendered_markup = self.post_markup.render_to_html(markup)
            self.assertEqual(
                rendered_markup,
                expected_rendered_markup,
                'Failed at %s; "%s" != "%s"' % (i, rendered_markup, expected_rendered_markup))
    
    def testStripMarkup(self):
        self.post_markup.default_tags()
        
        for i in range(len(self.MARKUP)):
            markup = self.MARKUP[i]
            expected_stripped_markup = self.EXPECTED_STRIPPED_MARKUP[i]
            rendered_markup = self.post_markup.strip_tags(markup)
            self.assertEqual(
                rendered_markup,
                expected_stripped_markup,
                'Failed at %s; "%s" != "%s"' % (i, rendered_markup, expected_stripped_markup))
    
    def testRenderTooDeepOfMarkupToHTML(self):
        self.post_markup.default_tags()
        
        markup = """[b][u][i][s][blink][b][img]test
                    [/img][/b][/blink][/s][/i][/u][/b]"""
        
        try:
            self.post_markup.render_to_html(markup)
        except InvalidBBCodeException, invalid_bbcode_exception:
            self.fail()

def _verifyPostMarkupHasTags(post_markup, include_tags=None, exclude_tags=None):
    if include_tags is not None:
        for tag in include_tags:
            if tag not in post_markup.tags:
                return False
    
    if exclude_tags is not None:
        for tag in exclude_tags:
            if tag in post_markup.tags:
                return False
    
    return True
