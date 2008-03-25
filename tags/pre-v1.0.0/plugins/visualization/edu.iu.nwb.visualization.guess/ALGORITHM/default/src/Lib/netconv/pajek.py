# -*- coding: iso-8859-1 -*-
# $Id: pajek.py,v 1.1 2005/10/05 20:19:28 eytanadar Exp $


"""
Functions to work with Pajek files

@author: Marcio Rosa da Silva
@organization: Systems Biology Group / GBF
@contact: mrs@gbf.de
@license: GPL

"""

import sys
from network import *

def processEdge(obj,l,nedges):
	node1 = obj.nodes[int(l[0])-1]
        node2 = obj.nodes[int(l[1])-1]
        e = Edge(node1, node2, nedges)
        obj.edges.append(e)
        obj.nedges += 1
        # update nodes from/to list
        node1.outEdges.append(node2)
        node2.inEdges.append(node1)
        if len(l) == 3:
            e.setWeight(int(l[2]))
        else:
            e.setWeight(1)

def importPajek(obj, filename = '-'):
    """Imports network from Pajek's .net file

    @param obj: network
    @param filename: .net file
    """
    #obj.reset()

    if filename == '-':
        arq = sys.stdin
    else:
        arq = open(filename, 'r')
    l = arq.readline()
    obj.nnodes = int(l.split()[1]) #@@
    for i in range(0, obj.nnodes):
        l = arq.readline().split()
        # take " out of the name
        if l[1][0] == '"' and l[1][-1] == '"':
            l[1] = l[1][1:-1]
        n = Node(l[1], i)
        obj.nodes.append(n)
        n.setAttribute('id', len(obj.nodes)-1)
        if len(l) > 4:
            n.setX(float(l[2]))
            n.setY(float(l[3]))
        if len(l) == 5:
            n.setZ(float(l[4]))
    nedges = 0
    # Read "*Arcs"
    l = arq.readline()
    # Read first arc
    l = arq.readline().split()
    if l[0] != '*Edges':
        # do *Arcs
        while (len(l) > 1):
            processEdge(obj,l,nedges)
            l = arq.readline().split()
            if l[0] == '*Edges':
                break
        # do *Edges now
        l = arq.readline().split()
        while (len(l) > 1):
            processEdge(obj,l,nedges)
            l = arq.readline().split()
    if filename != '-':
        arq.close()


def exportPajek(obj, filename = '-'):
    """Exports network to Pajek's .net file

    @param obj: network
    @param filename: .net file
    """
    if filename == '-':
        arq = sys.stdout
    else:
        arq = open(filename, 'w')
    arq.write("*Vertices    %s\n" % obj.nnodes)
    for i in range(0, obj.nnodes):
        arq.write('%8d "%s"' % (i+1, obj.nodes[i].getName()))
        pos = obj.nodes[i].getXYZ()
        for i in range(0,len(pos)):
            arq.write(" %16.8f" % float(pos[i]))
        arq.write("\n")
    arq.write("*Arcs\n")
    for e in obj.edges:
        arq.write("%4d %4d %4d\n" % (int(e.getNode1().getAttribute('id'))+1,
                                     int(e.getNode2().getAttribute('id'))+1,
                                     int(e.getWeight())))
    arq.write("*Edges\n")
    if filename != '-':
        arq.close()

def importPajekClu(obj, filename = '-'):
    """Import cluster information from Pajek's .clu file

    @param obj: network
    @param filename: .clu file
    """
    if filename == '-':
        arq = sys.stdin
    else:
        arq = open(filename, 'r')
    clusters = {}
    # ignore header
    arq.readline()
    node = 0
    for i in arq:
        if len(i.split()) < 1: continue
        obj.nodes[node].setCluster(int(i)-1)
        node += 1
        clusters[i] = 1
    if filename != '-':
        arq.close
    obj.clusterType = 'clu'
    obj.coordsModel = 'pajek'
    obj.nclusters = len(clusters)

def exportPajekClu(obj, filename = '-'):
    """Exports cluster information to Pajek's .clu file

    @param obj: network
    @param filename: .clu file
    """
    if filename == '-':
        arq = sys.stdout
    else:
        arq = open(filename, 'w')
    arq.write("*Vertices %d\n" % obj.nnodes)
    for i in range(0, obj.nnodes):
        if obj.clusterType == 'clu':
            arq.write("%s\n" % (int(obj.nodes[i].getCluster())+1))
        if obj.clusterType == 'attr':
            arq.write("%s\n" % (obj.nodes[i].getCluster()+1))
    if filename != '-':
        arq.close()
            
def _test():
    arq_in = "/home/mrsva/projects/data/network.net"
    arq_in2 = "/home/mrsva/projects/data/network.clu"
    n = Network()
    importPajek(n, arq_in)
    #importPajekClu(n, arq_in2)
    exportPajek(n)
    #exportPajekClu(n)

if __name__ == '__main__':
    _test()

