# -*- coding: iso-8859-1 -*-
# $Id: gml.py,v 1.1 2005/10/05 20:19:28 eytanadar Exp $


"""
Functions to work with GML files

@author: Marcio Rosa da Silva
@organization: Systems Biology Group / GBF
@contact: mrs@gbf.de
@license: GPL

@var gml_tab: used to indent GML file
@var gml_tab_i: internal field separation in GML file
"""

import sys
from network import *

gml_tab = '\t'
gml_tab_i = '\t'

def _bExportGML(obj, name, attribs, indent):
    """Exports attributes in GML format

    @param obj: network
    @param name: name of the group being exported
    @param attribs: attribute to export (use recursively to work on
    dictionary attributes)
    @param indent: level of indentation (tabs)
    """
    retstr = ""
    # pre-tabs: indentation for the group
    ptabs = indent*gml_tab
    indent = indent + 1
    # tabs: indentation for contents of group
    tabs = indent*gml_tab
    indent = indent + 1
    retstr += ptabs + name + " [\n"
    for attr in attribs.keys():
        value = attribs[attr]
        if isinstance(value, dict):
            if len(value) == 0:
                continue
            retstr += _bExportGML(obj,attr, value, indent-1)
        else:
            if name == 'edge':
                if attr == 'source' or attr == 'target':
                    node = value.getAttribute('id')
                    retstr += tabs + '%s%s%s\n' % (attr, gml_tab_i, node)
                    continue
            if attr == 'id':
                retstr += tabs + '%s%s%d\n' % (attr, gml_tab_i,
                                               int(value))
                continue
            #if attr == 'label':
            #    retstr += tabs + '%s%s"%s"\n' % (attr, gml_tab_i,
            #                                     value)
            #    continue
            if isinstance(value,str):
                try:
                    # just in case it's a number inside a string
                    float(value)
                except:
                    if value[0] == '"' and value[-1] == '"':
                        value = value[1:-1]
                    #@@ TODO: fix this!!!
                    value = value.replace('&', '&amp;')
                    retstr += tabs + '%s%s"%s"\n' % (attr, gml_tab_i,
                                                     value)
                    continue
            retstr += tabs + "%s%s%s\n" % (attr, gml_tab_i,
                                           value)
    if name != 'graph':
        retstr += ptabs + ']\n'
    return retstr

def _bImportGML(obj, arq, attribs):
    """Imports the attributes in GML format
    
    @param obj: network
    @param arq: .gml file
    @param attribs: attributes dict (used recursively)
    """
    oldline = ''
    for line in arq:
        l = line.split()
        if l[0] == '[':
            if oldline == '':
                continue
            attribs[oldline] = {}
            _bImportGML(obj, arq, attribs[oldline]) # @@
            continue
        if l[0] == ']':
            return
        if len(l) == 1:
            oldline = l[0]
            continue
        if l[1] == '[':
            attribs[l[0]] = {}
            _bImportGML(obj, arq, attribs[l[0]])
            continue
        if len(l) > 1:
            attribs[l[0]] = ' '.join(l[1:])
        if l[0] == ']':
            return
        oldline = l[0]

def _neExportGML(obj, name = None, attribs = None, indent = 0):
    """Exports the attributes of the node in GML format
    
    @param obj: node/edge
    @param name: name of the group being exported
    @param attribs: attribute to export (use recursively to work on
    dictionary attributes)
    @param indent: level of indentation (tabs)
    """
    if isinstance(obj, Node):
        tt = 'node'
    if isinstance(obj, Edge):
        tt = 'edge'
    return _bExportGML(obj, tt, obj.attributes, indent)

def _neImportGML(obj, arq, attribs = None):
    """Imports the attributes of the node in GML format

    @param obj: node/edge
    @param arq: .gml file
    @param attribs: attributes list (used recursively)
    """
    if attribs == None:
        attribs = obj.attributes
    _bImportGML(obj, arq, attribs) #@@

def exportGML(obj, filename = '-'):
    """Exports network to GML file

    @param obj: network
    @param filename: .gml file
    """
    if filename == '-':
        arq = sys.stdout
    else:
        arq = open(filename, 'w')
    arq.write(_bExportGML(obj, 'graph', obj.attributes, 0))
    for i in obj.nodes:
        arq.write(_neExportGML(i, indent=1))
    for i in obj.edges:
        arq.write(_neExportGML(i, indent=1))
    arq.write(']\n')
    if filename != '-':
        arq.close()
            
def importGML(obj, filename = '-'):
    """Exports network to GML file

    @param obj: network
    @param filename: .gml file
    """
    #obj.reset()
    if filename == '-':
        arq = sys.stdin
    else:
        arq = open(filename, 'r')
    # ignore lines before 'graph [' generated by Cytoscape
    for line in arq:
        l = line.split()
        if len(l) == 0:
            continue
        if l[0] != 'graph':
            continue
        break
    # if len(l) == 1 then 'graph' is in one line and '[' in another
    # like in Cytoscape export format
    if len(l) == 1:
        arq.next() # then skip the '['
    # we are now inside the graph [ ] group
    oldline = ""
    for line in arq:
        l = line.split()
        if l[0] == ']':
            break # finished graph [ ] group
        # In Cytoscape-generated GML file, the [ is alone in the line
        if l[0] == '[':
            if oldline == 'node':
                addGMLNode(obj, arq) # @@
                continue
            if oldline == 'edge':
                addGMLEdge(obj, arq)
                continue
            obj.attributes[l[0]] = {}
            _neImportGML(obj,arq, obj.attributes[l[0]])
            continue
        if l[0] == 'node':
            addGMLNode(obj, arq)
            continue
        if l[0] == 'edge':
            addGMLEdge(obj, arq)
            continue
        if l[1] == '[':
            obj.attributes[l[0]] = {}
            _neImportGML(obj,arq, obj.attributes[l[0]])
            continue
        if len(l) > 1:
            obj.attributes[l[0]] = ''.join(l[1:])
        oldline = l[0]
    if filename != '-':
        arq.close()
    obj.coordsModel = 'pos'
    obj.clusterType = 'attr'
        
def addGMLNode(obj, arq):
    """Adds GML node to the the network

    @param obj: node
    @param arq: .gml file
    """
    n = Node()
    _neImportGML(n, arq)
    obj.nodes.append(n)
    n.setAttribute('id', obj.nnodes)
    obj.nnodes += 1
    try:
        if n.getName()[0] == '"' and n.getName()[-1] == '"':
            n.setName(n.getName()[1:-1])
    except:
        pass
        
def addGMLEdge(obj, arq):
    """Adds GML edge to the the network

    @param obj: edge
    @param arq: .gml file
    """
    e = Edge()
    _neImportGML(e, arq)
    try:
        del(e.attributes['graphics']['Line'])
    except:
        pass
    obj.edges.append(e)
    obj.nedges += 1
    # This block is a workaround, because GML files sometimes
    # use 0 as the first Id and other times 1.
    # pyNetConv uses allways 0 as the first Id.
    if obj.nodes[0].getAttribute('id') != '0':
        correct = -1
    else:
        correct = 0
    n1 = obj.nodes[int(e.getNode1())+correct]
    n2 = obj.nodes[int(e.getNode2())+correct]
    e.setNode1(n1)
    e.setNode2(n2)
    n1.outEdges.append(n2)
    n2.inEdges.append(n1)
    if e.attributes.has_key('label'):
        if e.getName()[0] == '"' and e.getName()[-1] == '"':
            e.setName(e.getName()[1:-1])
      
def _test():
    #arq_in = "/home/mrsva/projects/data/galFiltered.gml"
    arq_in = "/home/mrsva/projects/data/paeregex2.gml"
    #arq_in = "/home/mrsva/projects/data/network.gml"
    n = Network()
    importGML(n,arq_in)
    exportGML(n)

if __name__ == '__main__':
    _test()

