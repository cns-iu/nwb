from django.contrib.auth.models import User
from django.contrib.auth import logout
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404

from django.contrib.auth.decorators import login_required

from epic.comments.models import Comment
from epic.comments.forms import PostCommentForm

# make_comment_view generates a Django view function.
# The generated view is for posting comments to the specified model.
# reverse_function_name is used to redirect users back to the model-view view
# (i.e. the "view dataset" view).
# render_to_response_template is the template file to use in render_to_response when
# directly redisplaying the page.  This happens instead of a redirect when the
# passed-in form data needs to be used in the page (re)display (since forms can't be
# passed around between different URLs).
# template_item_name refers to the name of the item OBJECT used in the template
# (ie "dataset" for the DataSet templates or "datarequest" for the DataRequest
# templates).
def make_comment_view(model, reverse_function_name, render_to_response_template, template_item_name):
	
	@login_required
	def post_comment_view(request, item_id):
		user = request.user
		item = get_object_or_404(model, pk=item_id)
		
		if request.method != "POST":
			return HttpResponseRedirect(reverse(reverse_function_name, kwargs={'item_id':item_id,}))
		else:
			post_comment_form = PostCommentForm(request.POST)
			
			if post_comment_form.is_valid():
				comment_contents = post_comment_form.cleaned_data["comment"]
				comment = Comment(posting_user=user, parent_item=item, contents=comment_contents)
				comment.save()
				
				return HttpResponseRedirect(reverse(reverse_function_name, kwargs={'item_id':item_id,}))
			else:
				return render_to_response(render_to_response_template, {
					template_item_name: item,
					"user": user,
					"post_comment_form": post_comment_form
				})
		
		return HttpResponseRedirect("/login/?next=%s" % request.path)
	
	return post_comment_view
