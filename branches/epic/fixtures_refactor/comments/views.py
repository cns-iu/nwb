from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404
from django.template import RequestContext

from epic.comments.forms import PostCommentForm
from epic.comments.models import Comment


COMMENT_KEY = 'comment'
FORM_KEY = 'form'
ITEM_ID_KEY = 'item_id'
SLUG_KEY = 'slug'

# make_comment_view generates a Django view function.
# The generated view is for posting comments to the specified model.
# post_comment_function_name is used to redirect users back to the model-view
# view (i.e. the 'view dataset' view).
# comment_posted_template_name is the template file to use in
# render_to_response when directly redisplaying the page.  This happens
# instead of a redirect when the passed-in form data needs to be used in the
# page (re)display (since forms can't be passed around between different
# URLs).
# template_item_name refers to the name of the item OBJECT used in the
# template (ie 'dataset' for the DataSet templates or 'datarequest' for the
# DataRequest templates).
def make_comment_view(model,
                      post_comment_function_name,
                      comment_posted_template_name,
                      template_item_name):
    @login_required
    def post_comment_view(request, item_id, slug):
        user = request.user
        item = get_object_or_404(model, pk=item_id)
        
        kwargs_for_reverse_lookup = {
            ITEM_ID_KEY: item_id,
            SLUG_KEY: item.slug
        }
        
        post_comment_url = reverse(
            post_comment_function_name, kwargs=kwargs_for_reverse_lookup)
        
        if request.method != 'POST':
            return HttpResponseRedirect(post_comment_url)
        else:
            form = PostCommentForm(request.POST)
            
            if form.is_valid():
                comment_contents = form.cleaned_data[COMMENT_KEY]
                
                comment = Comment(posting_user=user,
                                  parent_item=item,
                                  contents=comment_contents)
                
                comment.save()
                
                return HttpResponseRedirect(post_comment_url)
            else:
                template_variables = {
                    template_item_name: item,
                    FORM_KEY: form
                }
                
                return render_to_response(
                    comment_posted_template_name,
                    template_variables,
                    context_instance=RequestContext(request))
    
    return post_comment_view
