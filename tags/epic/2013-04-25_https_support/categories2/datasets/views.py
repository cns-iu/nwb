from datetime import datetime
from decimal import Decimal
import re
import tarfile
import tempfile
import zipfile

from django.conf import settings
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.core.files.base import ContentFile
from django.core.servers.basehttp import FileWrapper
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect
from django.http import HttpResponse
from django.forms.formsets import formset_factory
from django.forms.util import ErrorList
from django.shortcuts import get_list_or_404
from django.shortcuts import get_object_or_404
from django.shortcuts import render_to_response
from django.template.defaultfilters import slugify
from django.template import RequestContext
from django.utils import simplejson
from django.utils.datastructures import MultiValueDictKeyError

from epic.comments.forms import PostCommentForm
from epic.core.models import AcademicReference
from epic.core.models import Author
from epic.core.models import Item
from epic.core.util.view_utils import *
from epic.datasets.forms import AcademicReferenceFormSet
from epic.datasets.forms import AuthorFormSet
from epic.datasets.forms import EditDataSetForm
from epic.datasets.forms import GeoLocationFormSet
from epic.datasets.forms import NewDataSetForm
from epic.datasets.forms import RatingDataSetForm
from epic.datasets.forms import RemoveGeoLocationFormSet
from epic.datasets.forms import TagDataSetForm
from epic.datasets.forms import UploadReadMeForm
from epic.datasets.models import DataSet
from epic.datasets.models import DataSetFile
from epic.datasets.models import RATING_SCALE
from epic.geoloc.models import GeoLoc
from epic.geoloc.utils import CouldNotFindLocation
from epic.geoloc.utils import get_best_location
from epic.geoloc.utils import parse_geolocation
from epic.tags.models import Tagging


def view_datasets(request):
    datasets = DataSet.objects.active().order_by('-created_at')
    return render_to_response('datasets/view_datasets.html',
                              {'datasets': datasets,},
                              context_instance=RequestContext(request))

def view_dataset(request, item_id=None, slug=None):
    dataset = get_object_or_404(DataSet, pk=item_id)
    form = PostCommentForm()
    user = request.user
    
    return render_to_response('datasets/view_dataset.html', 
                              {'dataset': dataset, 'form': form},
                              context_instance=RequestContext(request))

def view_user_dataset_list(request, user_id=None):

    requested_user = get_object_or_404(User, pk=user_id)
    datasets = DataSet.objects.active().filter(creator=user_id).order_by('-created_at')
    return render_to_response('datasets/view_user_dataset_list.html', 
                              {'datasets': datasets, 'requested_user':requested_user,},
                              context_instance=RequestContext(request))

@login_required
def create_dataset(request):
    if request.method != 'POST':
        form = NewDataSetForm(request.user)
        geoloc_add_formset = GeoLocationFormSet(prefix='add')
        geoloc_remove_formset = RemoveGeoLocationFormSet(prefix='remove')
        ref_formset = AcademicReferenceFormSet(prefix='reference')
        author_formset = AuthorFormSet(prefix='author')
    else:
        form = NewDataSetForm(request.user, request.POST, request.FILES)
        geoloc_add_formset = GeoLocationFormSet(request.POST, prefix='add')
        geoloc_remove_formset = RemoveGeoLocationFormSet(request.POST, 
                                                         prefix='remove')
        ref_formset = AcademicReferenceFormSet(request.POST, 
                                               prefix='reference')
        author_formset = AuthorFormSet(request.POST, prefix='author')
        
        if form.is_valid() and ref_formset.is_valid() \
                and author_formset.is_valid():
            
            new_dataset = _save_new_dataset(form, request.user)
            _save_previous_version(form, new_dataset)
            _save_tags(form, new_dataset, request.user)
            _save_geolocs(geoloc_add_formset, 
                          geoloc_remove_formset, 
                          new_dataset)
            _save_references(ref_formset, new_dataset)
            _save_authors(author_formset, new_dataset)
            
            #   If there is a readme, activate the dataset and let
            # the user view the new dataset, otherwise send
            # them to a page where they can specify the readme.
            try:
                uploaded_files = form.cleaned_data['files']
                # Raises a 'NoReadMeException' if no readme is found.
                _add_uploaded_files(new_dataset, uploaded_files)
                new_dataset.is_active = True
                new_dataset.save()
                
                return HttpResponseRedirect(
                            reverse('epic.datasets.views.view_dataset', 
                                    kwargs={'item_id':new_dataset.id,
                                            'slug':new_dataset.slug}))
            except NoReadMeException:
                return HttpResponseRedirect(
                            reverse('epic.datasets.views.upload_readme', 
                                    kwargs={'item_id':new_dataset.id,
                                            'slug':new_dataset.slug}))
        
    return render_to_response('datasets/create_dataset.html', 
                              {'form': form, 
                               'geoloc_add_formset': geoloc_add_formset, 
                               'geoloc_remove_formset': geoloc_remove_formset,
                               'ref_formset': ref_formset,
                               'author_formset': author_formset}, 
                              context_instance=RequestContext(request))

class NoReadMeException(Exception):
    pass

def _save_new_dataset(form, user):
    name = form.cleaned_data['name']
    category = form.cleaned_data['category']
    description = form.cleaned_data['description']
    previous_version = form.cleaned_data['previous_version']
    
    new_dataset = DataSet.objects.create(
        creator=user, 
        name=name,
        description=description,
        category=category,
        previous_version=previous_version, 
        is_active=False)

    return new_dataset

def _save_previous_version(form, dataset):
    # TODO: Maybe only the latest version of a dataset should show up
    #  override the objects.all() or objects.active() maybe?
    previous_version = form.cleaned_data['previous_version']
    if previous_version:
        #(Note that this will overwrite previous "new versions")
        previous_version.next_version = dataset
        previous_version.save()
        
def _save_tags(form, dataset, user):
    tags = form.cleaned_data['tags']
    Tagging.objects.update_tags(tags, 
                                item=dataset, 
                                user=user)
def _save_geolocs(add_formset, remove_formeset, dataset):
    for geoloc in _get_geolocs_from_formset(add_formset, 'add_location'):
        dataset.geolocations.add(geoloc)
    
    for geoloc in \
            _get_geolocs_from_formset(remove_formeset, 'remove_location'):
        dataset.geolocations.remove(geoloc)

def _save_references(formset, dataset):
    for form in formset.forms:
        if form.is_valid():
            if form.cleaned_data:
                reference = form.cleaned_data['reference'] 
                AcademicReference.objects.create(
                    item=dataset, reference=reference)
                
def _save_authors(formset, dataset):
    for form in formset.forms:
        if form.is_valid():
            if form.cleaned_data:
                author_name = form.cleaned_data['author']
                author, created = Author.objects.\
                    get_or_create(author=author_name)
                author.items.add(dataset)
                
def _add_uploaded_files(dataset, uploaded_files):
    """ Add all the uploaded files to the dataset.  The first 'readme' file
    found will be added with a flag that indicates it is a 'readme'.
    
    Arguments:
    dataset -- the dataset the files are to be added to
    uploaded_files -- the files to be added
    
    Raises:
    NoReadMeException -- if there was not a readme file in the uploaded_files
    
    """
    
    readme_file = _get_readme(uploaded_files)
    
    for uploaded_file in uploaded_files:
        if uploaded_file == readme_file:
            # The readme file will be added below
            pass
        else:
            new_datasetfile = DataSetFile(parent_dataset=dataset, 
                                          file_contents=uploaded_file)
            new_datasetfile.file_contents.save(uploaded_file.name, 
                                               uploaded_file, 
                                               save=True)
            new_datasetfile.save()
    
    if readme_file:
        dataset_readmefile = DataSetFile(parent_dataset=dataset, 
                                         file_contents=readme_file, 
                                         is_readme=True)
        dataset_readmefile.file_contents.save('readme.txt', 
                                              readme_file, 
                                              save=True)
        dataset_readmefile.save()
    else:
        raise NoReadMeException

def is_valid_readme_filename(filename):
    
    pattern = re.compile(r'^(.*/)?(?P<filename>.*?)(\.txt)?$')
    match = re.match(pattern, filename.lower())

    if match.group('filename') == 'readme':
        return True
    else:
        return False

def _get_readme(uploaded_files):
    """ Return the readme file in the uploaded_files 
    or None if there is not one.
    
    """
    
    # If one of the files is readme.txt, this has priority over 
    #     something that is compressed
    for uploaded_file in uploaded_files:
        if is_valid_readme_filename(uploaded_file.name):
            return uploaded_file
    
    # If there was no readme found in the list of uploaded files, 
    #     check in the compressed files
    for uploaded_file in uploaded_files:
        readme_file = _get_compressed_readme(uploaded_file)
        if readme_file:
            return readme_file
        
    # We could not find a readme file anywhere.
    return None
        
def _get_zipped_readme(uploaded_file):
    """ Return the readme file out of a zip compressed file 
    or None if there is not one.
    
    Arguments:
    uploaded_file -- the zipfile
    
    """
    
    uploaded_zip = zipfile.ZipFile(uploaded_file.temporary_file_path(), 'r')
     
    # Find all the readmes in the zip file.
    
    readme_filenames = [] 
    for member in uploaded_zip.infolist():
        if is_valid_readme_filename(member.filename):
            readme_filenames.append(member.filename)
    
    # Pick the first of the shallowest of the readmes we found.
    
    readme_file = None
    shallowest_readme_filename = _get_shallowest_filename(readme_filenames)       
    if shallowest_readme_filename:
        readme_file = ContentFile(
                        uploaded_zip.read(shallowest_readme_filename))
           
    uploaded_zip.close()
    return readme_file

def _get_tarred_readme(uploaded_file):
    """ Return the readme file out of a tar compressed file 
    or None if there is not one.
    
    Arguments:
    uploaded_file -- the tarfile
    
    """
    
    uploaded_tar = tarfile.open(uploaded_file.temporary_file_path(), 'r:*')
       
    # Find all the readmes in the tar file.
    
    readme_filenames = []
    for member in uploaded_tar.getmembers():
        if is_valid_readme_filename(member.name):
            readme_filenames.append(member.name)
    
    #Pick the shallowest of the readmes we found.
    
    readme_file = None
    shallowest_readme_filename = _get_shallowest_filename(readme_filenames)    
    if shallowest_readme_filename:
        data = uploaded_tar.extractfile(shallowest_readme_filename).read()
        readme_file = ContentFile(data)
    
    uploaded_tar.close()
    return readme_file
    
def _get_shallowest_filename(readme_filenames):
    """ Return the 'shallowest' filename from a list 
    or None if there was no 'readme'.
    
    Arguments:
    readme_filenames -- a list of the full paths for the files to be checked
    
    """
    
    shallowest_readme_filename = None

    for readme_filename in readme_filenames:
        if shallowest_readme_filename == None:
            shallowest_readme_filename = readme_filename
        elif shallowest_readme_filename.count('/') > \
             readme_filename.count('/'):
            shallowest_readme_filename = readme_filename
    
    return shallowest_readme_filename

def _get_compressed_readme(uploaded_file):
    """ Return the 'readme' file out of a compressed file 
    or None if there was no 'readme'.
    
    Arguments:
    uploaded_file -- the compressed file
    
    """
    
    readme_file = None
    
    if zipfile.is_zipfile(uploaded_file.temporary_file_path()):
        readme_file = _get_zipped_readme(uploaded_file)
    elif tarfile.is_tarfile(uploaded_file.temporary_file_path()):
        readme_file = _get_tarred_readme(uploaded_file)
    return readme_file

@login_required
def upload_readme(request, item_id, slug):
    """ Allow the user to add a readme to a dataset.
    
    Arguments:
    request -- the request
    item_id -- the id for the dataset the 'readme' is to be attached to
    slug -- the slug for the dataset the 'readme' is to be attached to
    
    """
    
    dataset = get_object_or_404(DataSet, pk=item_id)
    user = request.user
    
    if user != dataset.creator or dataset.is_active:
        return HttpResponseRedirect(
                    reverse('epic.datasets.views.view_dataset', 
                            kwargs={'item_id':dataset.id,
                                    'slug':dataset.slug}))
    if request.method != 'POST':
        form = UploadReadMeForm()
    else:
        form = UploadReadMeForm(request.POST, request.FILES)
        
        if form.is_valid():
            readme_file = form.cleaned_data['readme']
            if is_valid_readme_filename(readme_file.name):
                datasetreadmefile = DataSetFile(parent_dataset=dataset, 
                                                file_contents=readme_file, 
                                                is_readme=True)
                datasetreadmefile.file_contents.save('readme.txt', 
                                                     readme_file, 
                                                     save=True)
                datasetreadmefile.save()
                
                dataset.is_active = True
                dataset.save()
                
                return HttpResponseRedirect(
                            reverse('epic.datasets.views.view_dataset', 
                                    kwargs={'item_id':dataset.id,
                                            'slug':dataset.slug}))
            else: 
                msg = u"""The readme '%(readme)s' is 
                          not a valid readme.txt file.""" \
                          % {'readme': readme_file.name,}
                form._errors['readme'] = ErrorList([msg])
        
    return render_to_response('datasets/upload_readme.html',
                              {'form': form, 'dataset': dataset},
                              context_instance=RequestContext(request))
    
@login_required
def edit_dataset(request, item_id, slug=None):
    dataset = get_object_or_404(DataSet, pk=item_id)
    user = request.user
    
    # Make sure the current user is the creator of the dataset.
    if user != dataset.creator:
        return HttpResponseRedirect(
                    reverse('epic.datasets.views.view_dataset',
                            kwargs={'item_id': dataset.id, 
                                    'slug':slug,}))
    
    if request.method != "POST":
        current_tags = \
            Tagging.objects.get_edit_string(item=dataset, user=user)
        
        initial_dataset_data = {
            'name': dataset.name,
            'description': dataset.description,
            'tags': current_tags,
        }
        
        if dataset.category is not None:
            initial_dataset_data['category'] = dataset.category.id
        
        form = EditDataSetForm(initial=initial_dataset_data)
        initial_location_data = []
        geolocs = GeoLoc.objects.filter(datasets=dataset.id)
        
        for geoloc in geolocs:
            initial_location_data.append({'add_location':geoloc,})
        
        geoloc_add_formset = GeoLocationFormSet(prefix='add', 
                                         initial=initial_location_data)
        geoloc_remove_formset = RemoveGeoLocationFormSet(prefix='remove')
        
        initial_author_data = []
        
        for author in dataset.authors.all():
            initial_author_data.append({'author': author.author})
        
        author_formset = AuthorFormSet(
            initial=initial_author_data, prefix='author')
            
        initial_ref_data = []
        
        for ref in dataset.references.all():
            initial_ref_data.append({'reference': ref.reference})
        
        ref_formset = AcademicReferenceFormSet(
            initial=initial_ref_data, prefix='reference')
    else:
        form = EditDataSetForm(request.POST)
        geoloc_add_formset = GeoLocationFormSet(request.POST, prefix='add')
        geoloc_remove_formset = RemoveGeoLocationFormSet(request.POST, 
                                                  prefix='remove')
        author_formset = AuthorFormSet(request.POST, prefix='author')
        ref_formset = \
            AcademicReferenceFormSet(request.POST, prefix='reference')
            
        if form.is_valid() and \
                author_formset.is_valid() and \
                ref_formset.is_valid():     
            dataset.name = form.cleaned_data['name']
            dataset.description = form.cleaned_data['description']
            dataset.category = form.cleaned_data['category']
            dataset.save()
            
            tag_names = form.cleaned_data["tags"]
            Tagging.objects.update_tags(tag_names=tag_names, 
                                        item=dataset, 
                                        user=user)
            
            for geoloc in _get_geolocs_from_formset(geoloc_add_formset,
                                                    'add_location'):
                dataset.geolocations.add(geoloc)
            
            for geoloc in _get_geolocs_from_formset(geoloc_remove_formset,
                                                    'remove_location'):
                dataset.geolocations.remove(geoloc)
            
            dataset.references.all().delete()
            
            for ref_form in ref_formset.forms:
                if ref_form.is_valid():
                    if ref_form.cleaned_data:
                        reference = ref_form.cleaned_data['reference'] 
                        AcademicReference.objects.create(
                            item=dataset, reference=reference)
            
            for author in dataset.authors.all():
                author.items.remove(dataset)
            
            for author_form in author_formset.forms:
                if author_form.is_valid():
                    if author_form.cleaned_data:
                        author_name = author_form.cleaned_data['author']
                        author, created = \
                            Author.objects.get_or_create(author=author_name)
                        author.items.add(dataset)
            
            # If the user has set the flag to delete their datasets,
            # delete them.
            # TODO: these files need to be removed from the file system too!!

            delete_files = False
            
            try:
                # This implies that the checkbox was checked.
                request.POST['delete_dataset_files']
                delete_files = True
            except MultiValueDictKeyError:
                pass
            
            if delete_files:
                delete_dataset_files_url = get_item_url(
                    dataset, 'epic.datasets.views.delete_dataset_files')
                
                return HttpResponseRedirect(delete_dataset_files_url)
            else:
                view_dataset_url = get_item_url(
                    dataset, 'epic.datasets.views.view_dataset')
                
                return HttpResponseRedirect(view_dataset_url)
            
    return render_to_response('datasets/edit_dataset.html', 
                              {'dataset': dataset, 
                               'form': form, 
                               'geoloc_add_formset': geoloc_add_formset, 
                               'geoloc_remove_formset': geoloc_remove_formset,
                               'files': dataset.files.all(),
                               'author_formset': author_formset,
                               'ref_formset': ref_formset},
                               context_instance=RequestContext(request))

@login_required
def rate_dataset(request, item_id, input_rating=None, slug=None):
    if input_rating:
        # TODO: surely this isn't supposed to be pass?!  What used to be here? Wont' it complain if no response is returned?!
        pass
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
                
                dataset = DataSet.objects.get(pk = item_id)
                dataset.rating.add(rating, user, ip_address)
                dataset.save()
                
                return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset', 
                                                    kwargs={'item_id':dataset.id, 'slug':slug,}))
            else:
                pass
                
        return render_to_response('datasets/rate_dataset.html', 
                                  {'form':form, 'item':dataset,}, 
                                  context_instance=RequestContext(request))
    
@login_required
def rate_dataset_using_input_rating(request, item_id, input_rating=None, slug=None):
    """
    Used specifically when the input_ratings field is provided. Mainly used 
    when voting from the UI for datasets. 
    """
    if input_rating:
        dataset = DataSet.objects.get(pk = item_id)
        user = request.user
        ip_address = request.META['REMOTE_ADDR']
        #Rely on the rating.add to make sure the that rating is valid
        rating = int(input_rating) 
        dataset.rating.add(rating, user, ip_address)
        dataset.save()
        return HttpResponse(simplejson.dumps("TRUE"), mimetype="application/javascript")
    else:
        # TODO: Surely this is not supposed to be pass.  Won't it compain if there is no response returned?
        pass
    
@login_required
def tag_dataset(request, item_id, slug=None):
    dataset = DataSet.objects.get(pk = item_id)
    
    if request.method != 'POST':
        current_tags = Tagging.objects.get_edit_string(item=dataset,user=request.user)
        form = TagDataSetForm(initial={'tags': current_tags})
        
        return render_to_response('datasets/tag_dataset.html',
                                  {'item_id': dataset.id, 'form': form,}, 
                                  context_instance=RequestContext(request))
    else:
        form = TagDataSetForm(request.POST)

        
        if form.is_valid():
            tags = form.cleaned_data['tags']
            Tagging.objects.update_tags(tags, item=dataset, user=request.user)
            
            return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
                                                kwargs={'item_id': dataset.id, 'slug': slug,}))
        else:
            return render_to_response('datasets/tag_dataset.html', 
                                      {'form':form,'item':dataset}, 
                                      context_instance=RequestContext(request))
@login_required
def delete_dataset_files(request, item_id, slug):
    # TODO: Here would be the place to delete the files from the file system if that is required.
    dataset = DataSet.objects.get(pk=item_id)
    user = request.user
    if request.method != 'POST':
        return render_to_response('datasets/confirm_delete_dataset_files.html', 
                                  {'dataset':dataset}, 
                                  context_instance=RequestContext(request))
    else:
        try:
             # Make sure the user confirmed the deletion.
            request.POST['confirmed']
        except MultiValueDictKeyError:
            return render_to_response('datasets/confirm_delete_dataset_files.html', 
                                      {'dataset':dataset}, 
                                      context_instance=RequestContext(request))
        if user == dataset.creator:
            for file in dataset.files.all():
                 file.delete()
        
        return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
                                            kwargs={'item_id': dataset.id, 'slug': dataset.slug,}))
    
def _get_geolocs_from_formset(formset, key='add_location'):
    """Return the proper 'Geoloc's from a formset.
    
    Arguments:
    formset -- the formset that contains the geolocations in text form
    
    Keyword Arguments:
    key -- the key into geolocation for the formset (default 'add_location')
    
    """
    
    geolocs = []
    if not formset.is_valid():
        print 'The formset for the geolocations for the edit dataset page was not valid'
    else:
        for form in formset.forms:
            location = form.cleaned_data[key]
            location_dict = parse_geolocation(location)
            lat = location_dict['lat']
            lng = location_dict['lng']
            canonical_name = location_dict['canonical_name']
            geoloc, created = GeoLoc.objects.get_or_create(longitude=lng, latitude=lat, canonical_name=canonical_name)
            geolocs.append(geoloc)
    return geolocs

@login_required
def download_all_files(request, item_id, slug):
    dataset = DataSet.objects.get(pk=item_id)
    user = request.user
    
    if dataset.is_active:
    
        temp = tempfile.TemporaryFile()
        archive = zipfile.ZipFile(temp, 'w')
        
        for file in dataset.files.all():
            file.file_contents.open('r')
            archive.writestr(file.get_short_name(), file.file_contents.read())
            file.file_contents.close()
            
        archive.close()
        
        wrapper = FileWrapper(temp)
        
        response = HttpResponse(wrapper, content_type='application/zip')
        response['Content-Disposition'] = 'attachment; filename=%s.zip' % dataset.name
        response['Content-Length'] = temp.tell()
        
        temp.seek(0)
        return response
    else:
       return HttpResponseRedirect(reverse('epic.datasets.views.view_dataset',
                                            kwargs={'item_id': dataset.id, 'slug': dataset.slug,})) 