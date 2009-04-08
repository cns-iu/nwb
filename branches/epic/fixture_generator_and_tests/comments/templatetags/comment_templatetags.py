from django import template
from django.core.urlresolvers import reverse
register = template.Library()

@register.inclusion_tag("templatetags/list_comments.html", takes_context=True)
def list_comments(context, item):
	comments = item.comments.all()
	
	return { "comments": comments, }

@register.inclusion_tag("templatetags/display_post_comment_form.html")
def display_post_comment_form(user, form, post_comment_url):
	return { "user": user,
			 "form": form,
			 "post_comment_url": post_comment_url, }

@register.inclusion_tag("templatetags/display_post_comment_form_and_list_comments.html",
						takes_context=True)
def display_post_comment_form_and_list_comments(
		context, item, user, form, post_comment_reverse_view):
	comments = item.comments.all()
	
	post_comment_url = reverse(post_comment_reverse_view,
							   kwargs={ 'item_id': item.id, 'slug': item.slug })
	
	return { "comments": comments,
			 "user": user,
			 "item": item,
			 "form": form,
			 "post_comment_url": post_comment_url,
			 }