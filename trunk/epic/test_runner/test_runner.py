import django.test.simple
import os
import shutil

import xmlrunner.extra.djangotestrunner

def run_tests(test_labels, verbosity=1, interactive=True, extra_tests=[]):

    #filterwarnings("ignore")
    
    result = xmlrunner.extra.djangotestrunner.run_tests(test_labels, verbosity, extra_tests)
    print "deleting temporary_media_root..."
    shutil.rmtree('temporary_media_root/', ignore_errors=True)
    return result
    
