from django import template
from django.core.urlresolvers import reverse
register = template.Library()

@register.inclusion_tag("templatetags/list_comments.html")
def list_comments(item):
	comments = item.comments.all()
	return { "comments": comments, }

@register.inclusion_tag("templatetags/display_post_comment_form.html")
def display_post_comment_form(user, post_comment_form, post_comment_url):
	return { "user": user, "post_comment_form": post_comment_form, 'post_comment_url':post_comment_url, }

@register.inclusion_tag("templatetags/display_post_comment_form_and_list_comments.html")
def display_post_comment_form_and_list_comments(item, user, post_comment_form, post_comment_reverse_view):
	comments = item.comments.all()
	post_comment_url = reverse(post_comment_reverse_view, kwargs={'item_id':item.id})
	return {
			'comments':comments,
			'user': user,
			'item': item,
			'post_comment_form': post_comment_form,
			'post_comment_url': post_comment_url,
		   }