from django import template


register = template.Library()

@register.inclusion_tag('templatetags/comment_list.html', takes_context=True)
def comment_list(context, item):
    comments = item.comments.all()
    
    return {'comments': comments}

@register.inclusion_tag('templatetags/comment_posting_form.html')
def comment_posting_form(user, form, item):
    return {'user': user, 'form': form, 'item': item}

@register.inclusion_tag('templatetags/comments_section.html', takes_context=True)
def comments_section(context, item, user, form):
    comments = item.comments.all()
    
    return {'comments': comments, 'user': user, 'item': item, 'form': form,}
