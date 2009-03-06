from epic.core.models import Item
from epic.datasets.forms import NewDataSetForm
from epic.datasets.models import DataSetFile, DataSet

from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response, get_object_or_404

from django.contrib.auth.decorators import login_required

from datetime import datetime


def index(request):
    datasets = DataSet.objects.all().order_by('-created_at')
    return render_to_response('datasets/index.html', {'datasets': datasets,'user':request.user})


def view_dataset(request, dataset_id=None):
    dataset = get_object_or_404(DataSet,pk=dataset_id)
    return render_to_response('datasets/view_dataset.html', {'dataset': dataset, 'user':request.user})


#@login_required
#def upload(request, dataset_id):
#    if request.method == 'POST':
#        form = NewDataSetForm(request.POST, request.FILES)
#        if form.is_valid():
#            dataset = DataSet.objects.get(pk=dataset_id)
#            data_file = form.cleaned_data['file']
#            f = DataSetFile(dataset=dataset, file=data_file)
#            f.save()
#            return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', args=(dataset.id,)))
#        else:
#            print request.POST
#            print form.errors
#    else:
#        form = NewDataSetForm()
#        return render_to_response('datasets/upload.html', {'form':form, })

    
@login_required
def new_dataset(request):
    u = request.user
    if u.is_authenticated():
        if request.method != 'POST':
            #show them the upload form
              form = NewDataSetForm()
        else:
            # handle the submission of their upload form
            form = NewDataSetForm(request.POST, request.FILES)
            if form.is_valid():
                item_name = form.cleaned_data['item_name']
                item_description = form.cleaned_data['item_description']
                uploaded_file = form.cleaned_data['file']
                
                dataset = DataSet(creator=u, name=item_name, description=item_description)
                dataset.save()
                f = DataSetFile(parent_dataset=dataset, file=uploaded_file)
                f.save()
                return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'dataset_id':dataset.id,}))
            else:
                print request.POST
                print form.errors
                
        return render_to_response('datasets/new.html', {'form':form, 'user':request.user,})
    else:
        print "user not logged in"
        return HttpResponseRedirect('/login/?next=%s' % request.path)