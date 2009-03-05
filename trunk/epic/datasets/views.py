from epic.core.models import Item
from epic.datasets.forms import UploadDataSetFileForm, NewDataSetForm
from epic.datasets.models import DataSetFile, DataSet

from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404

from datetime import datetime


def index(request):
    datasets = DataSet.objects.all().order_by('-timestamp')
    return render_to_response('datasets/index.html', {'datasets': datasets,})

def view_dataset(request, dataset_id):
    dataset = DataSet.objects.get(pk=dataset_id)
    datasetfiles = DataSetFile.objects.filter(dataset=dataset)
    return render_to_response('datasets/view_dataset.html', {'dataset': dataset, 'datasetfiles':datasetfiles,})

def upload(request, dataset_id):
    if request.user.is_authenticated():
        if request.method == 'POST':
            form = UploadDataSetFileForm(request.POST, request.FILES)
            if form.is_valid():
                dataset = DataSet.objects.get(pk=dataset_id)
                data_file = request.FILES['file']
                f = DataSetFile(dataset=dataset, file=data_file, timestamp=datetime.now())
                f.save()
                return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', args=(dataset.id,)))
            else:
                print request.POST
                print form.errors
        else:
             form = UploadDataSetFileForm()
             return render_to_response('datasets/upload.html', {'form':form, })
    else:
        print "user not logged in"
        return HttpResponseRedirect('/login/?next=%s' % request.path)
    
def new_dataset(request):
    u = request.user
    if u.is_authenticated():
        if request.method == 'POST':
            form = NewDataSetForm(request.POST)
            if form.is_valid():
                item_name = form.cleaned_data['item_name']
                item_description = form.cleaned_data['item_description']
                item = Item(user=u, name=item_name, description=item_description, timestamp=datetime.now())
                item.save()
                dataset = DataSet(item=item, timestamp=datetime.now())
                dataset.save()
                return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', args=(dataset.id,)))
            else:
                print request.POST
                print form.errors
        else:
            form = NewDataSetForm()
        return render_to_response('datasets/new.html', {'form':form, })
    else:
        print "user not logged in"
        return HttpResponseRedirect('/login/?next=%s' % request.path)