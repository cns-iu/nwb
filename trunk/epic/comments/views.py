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



def logged_view(view):
    from datetime import datetime # TODO: move
     
    def decorated_view(request, **kwargs):
        # Ask Russell how to do this with Django.  Reverse URL from request?
        view_qualified_name = '%s.%s' % (view.__module__, view.__name__)
        
        print '=====', view_qualified_name        
        print 'Start: ', datetime.now()
        
        for kwarg, kwval in kwargs.items():
            print '\t', kwarg, '=', kwval
        
        response = view(request, **kwargs)
        
        print 'Finish:', datetime.now()        
        print '====='

        return response        
    
    return decorated_view


@login_required
@logged_view
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


