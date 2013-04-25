from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext

from epic.comments.forms import PostCommentForm
from epic.comments.models import Comment
from epic.core.models import Item

@login_required
def post_comment(request, item_id, slug):
    user = request.user
    item = get_object_or_404(Item, pk=item_id)
    
    if request.method == 'POST':
        form = PostCommentForm(request.POST)
        
        if form.is_valid():
            comment_contents = form.cleaned_data['comment']
            Comment.objects.create(posting_user=user,
                              parent_item=item,
                              contents=comment_contents)
            
    return HttpResponseRedirect(item.specific.get_absolute_url())
