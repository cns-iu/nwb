# -*- coding: iso-8859-1 -*-
# $Id: network.py,v 1.1 2005/10/05 20:19:28 eytanadar Exp $


"""Nework storage

Defines a Network that can be imported/exported in many formats.
Cluster files and other attributes are also supported.

@author: Marcio Rosa da Silva
@organization: Systems Biology Group / GBF
@contact: mrs@gbf.de
@license: GPL

"""

import sys
import re
from java.lang import Class

class Node:
    "Defines a Node and its properties"
    def __init__(self, name = None, id = None, x = None, y = None, z = None):
        """
        @param name: canonical name of node
        @param x: pos x
        @param y: pos y
        @param z: pos z
        """
        self.attributes = {}
        self.attributes['graphics'] = {}
        self.attributes['cluster'] = {}
        self.attributes['expression'] = {}
        self.inEdges = []
        self.outEdges = []
        if name != None:
            self.attributes['label'] = name
        if id != None:
            self.attributes['id'] = id
        if x != None:
            self.attributes['graphics']['x'] = x
        if y != None:
            self.attributes['graphics']['y'] = y
        if z != None:
            self.attributes['graphics']['z'] = z

    def getName(self):
        "@return: name (canonical) of the node"
        return self.attributes['label']
    
    def setName(self, name):
        "@param name: name (canonical) of the node"
        self.attributes['label'] = name
    
    def setX(self, x):
        "@param x: position X of the node"
        self.attributes['graphics']['x'] = x

    def setY(self, y):
        "@param y: position Y of the node"
        self.attributes['graphics']['y'] = y

    def setZ(self, z):
        "@param z: position Z of the node"
        self.attributes['graphics']['z'] = z

    def getX(self):
        "@return: position X of the node"
        if self.attributes['graphics'].has_key('x'):
            return self.attributes['graphics']['x']
        else:
            return None

    def getY(self):
        "@return: position Y of the node"
        if self.attributes['graphics'].has_key('y'):
            return self.attributes['graphics']['y']
        else:
            return None

    def getZ(self):
        "@return: position Z of the node"
        if self.attributes['graphics'].has_key('z'):
            return self.attributes['graphics']['z']
        else:
            return None

    def getXYZ(self):
        "@return: list with the node position"
        r = []
        for i in ["x", "y", "z"]:
            if self.attributes['graphics'].has_key(i):
                r.append(self.attributes['graphics'][i])
        return r

    def setCluster(self, cluster):
        """Defines cluster to which the node belongs
        @param cluster: number of cluster
        """
        self.attributes['cluster']['cluster'] = cluster

    def getCluster(self):
        "@return: cluster to which the node belongs"
        return self.attributes['cluster']['cluster']

    def setAttribute(self, attr, value, attribs = None):
        """
        Defines generic attribute for the node
        @param attr: attribute to be set (can be a tuple)
        @param value: value of attribute
        @param attribs: dict of attributes
        """
        if attribs == None:
            attribs = self.attributes
        if isinstance(attr,Class.forName("org.python.core.PyTuple")):
            if len(attr) > 1:
                if not attribs.has_key(attr[0]):
                    attribs[attr[0]] = {}
                attribs = attribs[attr[0]]
                self.setAttribute(attr[1:], value, attribs)
            else:
                attribs[attr[0]] = value
        else:
            attribs[attr] = value

    def getAttribute(self, attr, attribs = None):
        """
        @param attr: attribute to be read
        @param attribs: dict of attributes
        @return: generic attribute for the node
        """
        if attribs == None:
            attribs = self.attributes
        if isinstance(attr,Class.forName("org.python.core.PyTuple")):
            if len(attr) > 1:
                attribs = attribs[attr[0]]
                return self.getAttribute(attr[1:], attribs)
            else:
                return attribs[attr[0]]
        else:
            return attribs[attr]

    def setGraphAttribute(self, attr, value):
        """Defines graphic attribute for the node
        @param attr: attribute to be set
        @param value: value of attribute"""
        self.attributes['graphics'][attr] = value

    def getGraphAttribute(self, attr):
        "@return: graphic attribute of the node"
        return self.attributes['graphics'][attr]

    def __getitem__(self, attr):
        "@return: attribute value"
        return self.attributes[attr]

    def __setitem__(self, attr, value):
        """Set attribute's value

        @param attr: attribute
        @param value: new value
        """
        self.attributes[item] = value

    def getExpression(self, attr):
        """Get expression value

        @param attr: attribute to be taken (signal, p-value, etc...)
        """
        return self.attributes['expression'][attr]

    def setExpression(self, attr, value):
        """Set expression value

        @param attr: attribute to be set (signal, p-value, etc...)
        """
        self.attributes['expression'][attr] = value

    def getExpressionAttributes(self):
        "@return: all the expression attributes stored"
        return self.attributes['expression'].keys()

class Edge:
    "Defines a Edge and its properties"
    def __init__(self, node1 = None, node2 = None, id = None, weight = 1):
        """
        @param node1: source node for edge
        @param node2: target node for edge
        @param weight: weight of edge
        """
        self.attributes = {}
        self.attributes['graphics'] = {}
        if node1 != None:
            self.attributes['source'] = node1
        if node2 != None:
            self.attributes['target'] = node2
        self.attributes['weight'] = weight
        if id != None:
            self.attributes['id'] = id

    def getNode1(self):
        "@return: 1st node of the edge"
        return self.attributes['source']
    
    def getNode2(self):
        "@return: 2nd node of the edge"
        return self.attributes['target']

    def setNode1(self, source):
        "@param source: 1st node of the edge"
        self.attributes['source'] = source
    
    def setNode2(self, target):
        "@param target: 2nd node of the edge"
        self.attributes['target'] = target
    
    def getWeight(self):
        "@return: the weight of the edge"
        return self.attributes['weight']
    
    def setWeight(self, w):
        """Defines the weight of the edge

        @param w: weight"""
        self.attributes['weight'] = w
        
    def getName(self):
        "@return: name (canonical) of the edge"
        return self.attributes['label']
    
    def setName(self, name):
        "@param name: name (canonical) of the edge"
        self.attributes['label'] = name
    
    def setAttribute(self, attr, value, attribs = None):
        """
        Defines generic attribute for the edge
        @param attr: attribute to be set (can be a tuple)
        @param value: value of attribute
        @param attribs: dict of attributes
        """
        if attribs == None:
            attribs = self.attributes
        if isinstance(attr, Class.forName("org.python.core.PyTuple")):
            if len(attr) > 1:
                if not attribs.has_key(attr[0]):
                    attribs[attr[0]] = {}
                attribs = attribs[attr[0]]
                self.setAttribute(attr[1:], value, attribs)
            else:
                attribs[attr[0]] = value
        else:
            attribs[attr] = value

    def getAttribute(self, attr, attribs = None):
        """
        @param attr: attribute to be read
        @param attribs: dict of attributes
        @return: generic attribute for the edge
        """
        if attribs == None:
            attribs = self.attributes
        if isinstance(attr, Class.forName("org.python.core.PyTuple")):
            if len(attr) > 1:
                attribs = attribs[attr[0]]
                return self.getAttribute(attr[1:], attribs)
            else:
                return attribs[attr[0]]
        else:
            return attribs[attr]


class Network:
    """
    Stores information of network

    Methods for importing/exporting network are provided

    @ivar coordsModel: set the model used to store network coords
      - pajek: coords range from 0 to 1
      - pos: coords are the position in graph
    """
    
    #clusterChar = ""
    coordsModel = "pos"
    
    def __init__(self, nnodes = 0, nedges = 0):
        """
        @param nnodes: number of nodes
        @param nedges: number of edges
        """
        self.reset()
        self.nnodes = nnodes
        self.nedges = nedges
        self.attributes = {}
        self.attributes['graphics'] = {}
        
    def reset(self):
        "Cleans the network"
        self.nodes = []
        self.edges = []
        self.nnodes = 0
        self.nedges = 0
        self.nclusters = 0

    def findNode(self, node):
        """Finds node by name

        @param node: name (canonical) of node
        @return: index in the network"""
        for i in range(0, self.nnodes):
            if self.nodes[i].getName() == node: ##@@
                return i
        return None

    def findId(self, id):
        """Finds node by Id

        @param id: Id of node
        @return: index in the network"""
        for i in range(0, self.nnodes):
            if self.nodes[i].getAttribute('id') == id: ##@@
                return i
        return None

    def attributes2Cluster(self, attr = ('cluster', 'cluster')):
        """Converts attributes to cluster

        @param attr: attribute to convert from (defaults to 'Cluster')
        """
        self.clusterType = 'attr'
        cluster = []
        for i in range(0, self.nnodes):
            n = self.nodes[i]
            try:
                cluster.index(n.getAttribute(attr))
            except:
                cluster.append(n.getAttribute(attr))
            n.setCluster(cluster.index(n.getAttribute(attr)))
        self.nclusters = len(cluster)

    def cluster2Attributes(self, attr = ('cluster', 'cluster'), s = ""):
        """Converts cluster info to attributes

        @param attr: attribute that will receive the information (defaults
        to 'Cluster')
        @param s: string to add at the begining of the value of attribute
        to avoids problems with "numerical" nodes with the
        VisualMapper from Cytoscape"""
        self.clusterType = 'clu'
        for i in range(0, self.nnodes):
            n = self.nodes[i]
            n.setAttribute(attr, "%s%s" % (s, n.getCluster()))

    def pajek2pos(self, mult):
        """
        Converts pajek node positions (range from 0 to 1) to graph position

        @param mult: valut to multiply the pajek value
        """
        for n in self.nodes:
            n.setX(float(n.getX())*mult)
            n.setY(float(n.getY())*mult)
            n.setZ(float(n.getZ())*mult)
        
    def pos2pajek(self):
        "Converts node positions to pajek format (range from 0 to 1)"
        max = 0
        for i in self.nodes:
            for p in i.getXYZ():
                n = float(p)
                if n > max:
                    max = n
        for n in self.nodes:
            if n.getX():
                n.setX(`(float(n.getX())/max)`)
            if n.getY():
                n.setY(`(float(n.getY())/max)`)
            if n.getZ():
                n.setZ(`(float(n.getZ())/max)`)
        self.coordsMode = 'pajek'

    def setAttribute(self, attr, value, attribs = None):
        """
        Defines generic attribute for the network
        @param attr: attribute to be set (can be a tuple)
        @param value: value of attribute
        @param attribs: dict of attributes
        """
        if attribs == None:
            attribs = self.attributes
        if isinstance(attr, Class.forName("org.python.core.PyTuple")):
            if len(attr) > 1:
                if not attribs.has_key(attr[0]):
                    attribs[attr[0]] = {}
                attribs = attribs[attr[0]]
                self.setAttribute(attr[1:], value, attribs)
            else:
                attribs[attr[0]] = value
        else:
            attribs[attr] = value

    def getAttribute(self, attr, attribs = None):
        """
        @param attr: attribute to be read
        @param attribs: dict of attributes
        @return: generic attribute for the network
        """
        if attribs == None:
            attribs = self.attributes
        if isinstance(attr, Class.forName("org.python.core.PyTuple")):
            if len(attr) > 1:
                attribs = attribs[attr[0]]
                return self.getAttribute(attr[1:], attribs)
            else:
                return attribs[attr[0]]
        else:
            return attribs[attr]

    def toList(self):
        """
        Converts the network to a list of interactions.

        @return: list of interactions
        """
        list = []
        for e in self.edges:
            l = [e.getNode1().getName(), e.getNode2().getName()]
            list.append(l)
        return list

    def fromList(self,list):
        """
        reads the network from a list of interactions.

        @param list: list of interactions
        """
        nodes = []
        for e in list:
            try:
                nodes.index(e[0])
            except:
                nodes.append(e[0])
                self.nodes.append(Node(e[0]))
            try:
                nodes.index(e[1])
            except:
                nodes.append(e[1])
                self.nodes.append(Node(e[1]))
            node1 = self.nodes[nodes.index(e[0])]
            node2 = self.nodes[nodes.index(e[1])]
            self.edges.append(Edge(node1,node2))
            node1.outEdges.append(node2)
            node2.inEdges.append(node1)
        # adds Id to nodes
        for i in range(len(self.nodes)):
            self.nodes[i].setAttribute('id', i)
        self.nnodes = len(self.nodes)
        self.nedges = len(self.edges)

    

def _test():
    import cytoscape
    import NX

    n = Network()
    cytoscape.importSIF(n, "/home/mrsva/projects/data/mrs/network.sif")
    print n.toList()
    G=NX.DiGraph()
    G.add_edges_from(n.toList())
    print G.nodes()
    print G.edges()
    n2 = Network()
    n2.fromList(G.edges())
    cytoscape.exportSIF(n2)

if __name__ == '__main__':
    _test()

