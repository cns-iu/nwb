from django import template


register = template.Library()

@register.inclusion_tag('templatetags/list_comments.html', takes_context=True)
def list_comments(context, item):
    comments = item.comments.all()
    return {'comments': comments}

@register.inclusion_tag('templatetags/display_post_comment_form.html')
def display_post_comment_form(user, form, item):
    return {'user': user, 'form': form, 'item': item}

@register.inclusion_tag('templatetags/comments_section.html',
                        takes_context=True)
def comments_section(context, item, user, form):
    comments = item.comments.all()    
    return {'comments': comments,
            'user': user,
            'item': item,   
            'form': form,}
