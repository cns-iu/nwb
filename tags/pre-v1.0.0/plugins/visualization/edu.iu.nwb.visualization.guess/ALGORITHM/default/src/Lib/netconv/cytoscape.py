# -*- coding: iso-8859-1 -*-
# $Id: cytoscape.py,v 1.1 2005/10/05 20:19:28 eytanadar Exp $


"""
Functions to work with Cytoscape files

@author: Marcio Rosa da Silva
@organization: Systems Biology Group / GBF
@contact: mrs@gbf.de
@license: GPL

"""

import sys
from network import *

def importSIF(obj, filename = '-'):
    """Imports network from Cytoscape's .sif file

    @param obj: network
    @param filename: .sif file
    """
    #obj.reset()
    if filename == '-':
        arq = sys.stdin
    else:
        arq = open(filename, 'r')
    nodes = []
    for i in arq:
        l = i.split()
        if len(l) == 3:
            # take " out of the name
            if l[0][0] == '"' and l[0][-1] == '"':
                l[0] = l[0][1:-1]
            if l[2][0] == '"' and l[2][-1] == '"':
                l[2] = l[2][1:-1]
        if len(l) < 3: continue
        try:
            nodes.index(l[0])
        except:
            nodes.append(l[0])
            obj.nodes.append(Node(l[0]))
        try:
            nodes.index(l[2])
        except:
            nodes.append(l[2])
            obj.nodes.append(Node(l[2]))
        node1 = obj.nodes[nodes.index(l[0])]
        node2 = obj.nodes[nodes.index(l[2])]
        obj.edges.append(Edge(node1,node2))
        #@@obj.edges.append(Edge(nodes.index(l[0]),
        #@@                       nodes.index(l[2])))
        # updates nodes from/to list
        node1.outEdges.append(node2)
        node2.inEdges.append(node1)
    # adds Id to nodes
    for i in range(len(obj.nodes)):
        obj.nodes[i].setAttribute('id', i)
    if filename != '-':
        arq.close()
    obj.nnodes = len(obj.nodes)
    obj.nedges = len(obj.edges)
                
def exportSIF(obj, filename = '-', connector = 'pp'):
    """Exports network to Cytoscape's .sif file

    @param obj: network
    @param filename: .sif file
    @param connector: type of relation between nodes (see Cytoscape
    manual section of SIF file format)
    """
    if filename == '-':
        arq = sys.stdout
    else:
        arq = open(filename, 'w')
    for i in obj.edges:
        arq.write("%s\t%s\t%s\n"% (i.getNode1().getName(),
                                   connector,
                                   i.getNode2().getName()))
    if filename != '-':
        arq.close()

def importCyAttributes(obj, filename = '-', attr = None):
    """Imports attributes from a Cytoscape's attributes file

    @param obj: network
    @param filename: attributes file
    """
    if filename == '-':
        arq = sys.stdin
    else:
        arq = open(filename, 'r')
    if attr == None:
        attr = arq.readline().split()[0]
        if attr == 'cluster' or attr == 'Cluster':
            attr = ('cluster','cluster')
    else:
        arq.next() # read header even if it's not used
    for i in arq:
        l = i.split()
        # TODO: error if n == None
        n = obj.findNode(l[0])
        if n != None:
            obj.nodes[n].setAttribute(attr, ' '.join(l[2:]))
    if filename != '-':
        arq.close()

def exportCyAttributes(obj, filename = '-', attrname = ('cluster','cluster'), s = ""):
    """Exports attributes to a Cytoscape's attributes file

    @param obj: network
    @param filename: file to export
    @param attrname: attribute to write as header of Cytoscape's
    attributes file"""
    if filename == '-':
        arq = sys.stdout
    else:
        arq = open(filename, 'w')
    if isinstance(attrname, tuple):
        arq.write("%s\n" % attrname[-1])
    else:
        arq.write("%s\n" % attrname)
    for i in range(0, obj.nnodes):
        arq.write("%s\t=\t%s%s\n" % (obj.nodes[i].getName(), s,
                          obj.nodes[i].getAttribute(attrname)))
    if filename != '-':
        arq.close()
            
def _test():
    arq_in = "/home/mrsva/projects/data/network.sif"
    arq_in2 = "/home/mrsva/projects/data/network.subnet"
    n = Network()
    importSIF(n,arq_in)
    #importCyAttributes(n, arq_in2, ('cluster', 'cluster'))
    exportSIF(n)
    #exportCyAttributes(n, attrname=('cluster', 'cluster'))

if __name__ == '__main__':
    _test()

