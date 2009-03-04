#############################################################
from epic.datasets.forms import UploadFileForm, NewDatasetForm
from epic.datasets.models import File, Dataset

from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404

from datetime import datetime

#############################################################

def index(request):
    dataset_list = Dataset.objects.all().order_by('-upload_date')
    return render_to_response('datasets/index.html', {'dataset_list': dataset_list,})

def view_dataset(request, dataset_id):
    dataset = Dataset.objects.get(pk=dataset_id)
    files = File.objects.filter(dataset=dataset)
    return render_to_response('datasets/view_dataset.html', {'dataset': dataset, 'files':files,})

def upload(request, dataset_id):
    if request.user.is_authenticated():
        if request.method == 'POST':
            form = UploadFileForm(request.POST, request.FILES)
            if form.is_valid():
                ds = Dataset.objects.get(pk=dataset_id)
                data_file = request.FILES['file']
                owner = request.user
                title = form.cleaned_data['title']
                description = form.cleaned_data['description']
                f = File(owner=owner, title=title, description=description, upload_date=datetime.now(), file=data_file, dataset=ds)
                f.save()
                return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', args=(ds.id,)))
            else:
                print request.POST
                print form.errors
        else:
             form = UploadFileForm()
             return render_to_response('datasets/upload.html', {'form':form, })
    else:
        print "user not logged in"
        return HttpResponseRedirect('/login/?next=%s' % request.path)
    
def new_dataset(request):
    u = request.user
    if u.is_authenticated():
        print "user is: %s" % (u)
        if request.method == 'POST':
            form = NewDatasetForm(request.POST)
            if form.is_valid():
                title = form.cleaned_data['title']
                description = form.cleaned_data['description']
                owner = u
                upload_date = datetime.now()
                dataset = Dataset(owner=owner, title=title, description=description, upload_date=upload_date)
                dataset.save()
                return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', args=(dataset.id,)))
            else:
                print request.POST
                print form.errors
        else:
            form = NewDatasetForm()
        return render_to_response('datasets/new.html', {'form':form, })
    else:
        print "user not logged in"
        return HttpResponseRedirect('/login/?next=%s' % request.path)