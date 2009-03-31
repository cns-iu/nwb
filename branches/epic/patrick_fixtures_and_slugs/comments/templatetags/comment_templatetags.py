from django import template

register = template.Library()

@register.inclusion_tag("templatetags/list_comments.html")
def list_comments(item):
	comments = item.comments.all()
	
	return { "comments": comments, "owning_item": item }

@register.inclusion_tag("templatetags/display_post_comment_form.html")
def display_post_comment_form(user, post_to_url, item_id, post_comment_form):
	return { "user": user, "post_to_url": post_to_url, "item_id": item_id, "post_comment_form": post_comment_form }
