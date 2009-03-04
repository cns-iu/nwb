from epic_community_website.dataset.forms import UploadFileForm
from epic_community_website.dataset.models import File
from django.shortcuts import render_to_response, get_object_or_404
from django.http import HttpResponseRedirect
from django.core.urlresolvers import reverse
from django.contrib.contenttypes.models import ContentType

from epic_community_website.dataset.models import File, Dataset

from datetime import datetime

def upload(request):
    if request.method == 'POST':
        form = UploadFileForm(request.POST, request.FILES)
        if form.is_valid():
            ds = Dataset(owner=request.user, title="blah", description="blah", upload_date=datetime.now())
            ds.save()
            dataset_type = ContentType.objects.get_for_model(ds)
            data_file = request.FILES['file']
            owner = request.user
            title = form.cleaned_data['title']
            description = form.cleaned_data['description']
            upload_date = datetime.now()
            f = File(owner=owner, title=title, description=description,upload_date=upload_date,file=data_file,content_type=dataset_type, object_id=ds.id)
            f.save()
            print "title: %s" % (title)
            return HttpResponseRedirect('/thanks/')
        else:
            print request.POST
            print form.errors
    else:
        form = UploadFileForm()
    return render_to_response('dataset/upload.html', {'form':form, })