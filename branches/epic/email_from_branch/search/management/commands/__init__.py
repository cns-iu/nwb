import os
import shutil
import stat


RELATIVE_WHOOSH_PATH = 'whoosh'
TEMP_WHOOSH_DIRECTORY = '_%s' % RELATIVE_WHOOSH_PATH

def remove_if_exists(absolute_path):
        if os.path.exists(absolute_path):
            shutil.rmtree(absolute_path, onerror=on_rmtree_error)

def on_rmtree_error(func, path, exc_info):
    if not os.access(path, os.W_OK):
        fix_permissions(path)
        func(path)
    else:
        raise

def fix_permissions(path):
    os.chmod(path, stat.S_IWUSR)