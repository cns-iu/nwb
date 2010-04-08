from django.core.urlresolvers import reverse


# user should be an actual User object.
def user_is_item_creator(user, item):
    if user == item.creator or user.id == item.creator.id:
        return True
    
    return False

def get_item_url(item, item_view_function_name):
    item_url_reverse_data = {'item_id': item.id, 'slug': item.slug,}
    item_url = reverse(item_view_function_name, kwargs=item_url_reverse_data)
    
    return item_url

def get_specifics_from_item_ids(model, item_ids):
    specifics = model.objects.filter(is_active=True).\
                              filter(id__in=item_ids)
    
    return specifics
