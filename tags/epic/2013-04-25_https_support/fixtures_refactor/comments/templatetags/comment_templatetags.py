from django import template
from django.core.urlresolvers import reverse


COMMENTS_KEY = 'comments'
USER_KEY = 'user'
FORM_KEY = 'form'
POST_COMMENT_URL_KEY = 'post_comment_url'
ITEM_ID_KEY = 'item_id'
ITEM_KEY = 'item'
SLUG_KEY = 'slug'

register = template.Library()

@register.inclusion_tag('templatetags/list_comments.html', takes_context=True)
def list_comments(context, item):
    comments = item.comments.all()
    
    return {COMMENTS_KEY: comments}

@register.inclusion_tag('templatetags/display_post_comment_form.html')
def display_post_comment_form(user, form, post_comment_url):
    return {
        USER_KEY: user,
        FORM_KEY: form,
        POST_COMMENT_URL_KEY: post_comment_url
    }

@register.inclusion_tag(
    'templatetags/comments_section.html', takes_context=True)
def comments_section(context, item, user, form, the_view_to_post_to):
    kwargs_for_comment_url_reverse = {
        ITEM_ID_KEY: item.id,
        SLUG_KEY: item.slug
    }
    
    url_to_post_comment_to = reverse(the_view_to_post_to,
                                     kwargs=kwargs_for_comment_url_reverse)
    
    comments = item.comments.all()
    
    return {
        COMMENTS_KEY: comments,
        USER_KEY: user,
        ITEM_KEY: item,
        FORM_KEY: form,
        POST_COMMENT_URL_KEY: url_to_post_comment_to,
    }
