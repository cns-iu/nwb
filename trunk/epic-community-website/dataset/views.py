from epic_community_website.dataset.forms import UploadFileForm
from epic_community_website.dataset.models import File
from django.shortcuts import render_to_response, get_object_or_404
from django.http import HttpResponseRedirect
from django.core.urlresolvers import reverse

from epic_community_website.dataset.models import File, Dataset

from datetime import datetime

def index(request):
    dataset_list = Dataset.objects.all().order_by('-upload_date')
    return render_to_response('dataset/index.html', {'dataset_list': dataset_list,})

def view_dataset(request, dataset_id):
    dataset = Dataset.objects.get(pk=dataset_id)
    files = File.objects.filter(dataset=dataset)
    return render_to_response('dataset/view_dataset.html', {'dataset': dataset, 'files':files,})

def upload(request, dataset_id):
    if request.method == 'POST':
        form = UploadFileForm(request.POST, request.FILES)
        if form.is_valid():
            ds = Dataset.objects.get(pk=dataset_id)
            data_file = request.FILES['file']
            owner = request.user
            title = form.cleaned_data['title']
            description = form.cleaned_data['description']
            upload_date = datetime.now()
            f = File(owner=owner, title=title, description=description,upload_date=upload_date,file=data_file,dataset=ds)
            f.save()
            print "title: %s" % (title)
            return HttpResponseRedirect(reverse('epic_community_website.dataset.views.view_dataset', args=(ds.id,)))
        else:
            print request.POST
            print form.errors
    else:
        form = UploadFileForm()
    return render_to_response('dataset/upload.html', {'form':form, })

def new_dataset(request):
    pass