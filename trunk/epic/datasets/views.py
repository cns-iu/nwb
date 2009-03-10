from epic.core.models import Item
from epic.datasets.forms import NewDataSetForm, RatingDataSetForm
from epic.datasets.models import DataSetFile, DataSet, RATING_SCALE

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

@login_required
def create_dataset(request):
    if request.method != 'POST':
        #user has not filled out the upload form yet
        
        #show them the upload form
        form = NewDataSetForm()
        return render_to_response('datasets/create_dataset.html', {'form':form, 'user':request.user,})
    else:
    	#user has filled out the upload form
        #handle the submission of their upload form  
        form = NewDataSetForm(request.POST, request.FILES)
        
        if form.is_valid():
        	name = form.cleaned_data['name']
        	description = form.cleaned_data['description']
        	uploaded_file = form.cleaned_data['file']
        	
        	#create a dataset with no files (yet), with metadata provided by the user
        	new_dataset = DataSet(creator=request.user, name=name, description=description)
        	new_dataset.save()
        	
        	#create file model objects for each file the user uploaded, and set their parent to be the new dataset
        	#TODO: add support for multiple files
        	new_datasetfile = DataSetFile(parent_dataset=new_dataset, file_contents=uploaded_file)
        	#(in addition to saving the uploaded file on the file system, with the save=True option this saves the model as well)
        	#TODO: put this in a directory specific to this dataset
        	new_datasetfile.file_contents.save(uploaded_file.name, uploaded_file, save=True)
        	#f.save() #this is redundant because of the save=True
        	
        	#show them the page for the dataset we just created
        	return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'dataset_id':new_dataset.id,}))
        else:
        	#form wasn't filled out correctly
            #show them the form again (modified to show which fields weren't filled out correctly)
            return render_to_response('datasets/create_dataset.html', {'form':form, 'user':request.user,})
              
    
   	
@login_required
def rate_dataset(request, dataset_id, input_rating=None):
	#TODO: Make commenting ajaxified?
	if input_rating:
		dataset = DataSet.objects.get(pk = dataset_id)
		user = request.user
		ip_address = request.META['REMOTE_ADDR']
		#Rely on the rating.add to make sure the that rating is valid
		rating = int(input_rating) 
		dataset.rating.add(rating, user, ip_address)
		dataset.save()
		return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'dataset_id':dataset.id,}))
	else:
		if request.method != 'POST':
			#show them the rate form
	  		form = RatingDataSetForm()
		else:
			# handle the submission of their rate form
			form = RatingDataSetForm(request.POST)
			if form.is_valid():
				rating = int(form.cleaned_data['rating']) #TODO: why doesn't it just return an int?
				ip_address = request.META['REMOTE_ADDR']
				user = request.user
				
				dataset = DataSet.objects.get(pk = dataset_id)
				dataset.rating.add(rating, user, ip_address)
				dataset.save()
				
				return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', kwargs={'dataset_id':dataset.id,}))
			else:
				print request.POST
				print form.errors
				
		return render_to_response('datasets/rate_dataset.html', {'form':form, 'user':request.user,})
