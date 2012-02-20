#!/usr/bin/env python

import os
import os.path
import lxml
from lxml import etree
import pprint

pom_ns = "{http://maven.apache.org/POM/4.0.0}"

def find_java_projects(dir_to_walk):
    for root, dirs, files in os.walk(dir_to_walk):
        if 'META-INF' in dirs:
            yield root
            del dirs[:]

def find_pom_projects(dir_to_walk):
    for root, dirs, files in os.walk(dir_to_walk):
        if 'pom.xml' in (lower(f) for f in files):
            yield root
            

def walk_pom_tree(dir_to_walk):
    pom_file_name = os.path.join(dir_to_walk, 'pom.xml')
    
    try:
        tree = etree.parse(pom_file_name)
    except IOError:
        print "Couldn't read POM for", dir_to_walk
        return
    yield dir_to_walk
    
    root = tree.getroot()
    module_elems = root.getiterator(pom_ns + "module")
    for elem in module_elems:
        for sub in walk_pom_tree(os.path.join(dir_to_walk, elem.text)):
            yield sub    

if __name__ == '__main__':
    dirs_included = set(walk_pom_tree("d:\\work\\sci2-tycho\\trunk\\"))

    java_projects = set(find_java_projects("d:\\work\\sci2-tycho\\trunk\\"))

    print "Directories containing a META-INF directory, but not included in a parent pom:"
    pprint.pprint(java_projects.difference(dirs_included))
                  
