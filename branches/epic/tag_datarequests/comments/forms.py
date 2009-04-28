from django import forms
from django.forms import Form

from epic.comments.models import Comment


class PostCommentForm(forms.Form):
    comment = forms.CharField(
        label='', widget=forms.Textarea(attrs={'rows': 3}))
