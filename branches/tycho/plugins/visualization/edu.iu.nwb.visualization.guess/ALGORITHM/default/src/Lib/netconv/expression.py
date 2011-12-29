# -*- coding: iso-8859-1 -*-
# $Id: expression.py,v 1.1 2005/10/05 20:19:28 eytanadar Exp $


"""
Functions to manipulate expression data (microarray)

@author: Marcio Rosa da Silva
@organization: Systems Biology Group / GBF
@contact: mrs@gbf.de
@license: GPL

"""

import csv
import re
import sys

GENE_NAME = re.compile('([a-zA-Z0-9]*?)_')

def importExpression(obj, filename = '-'):
    """Imports expression from MAS5.0 generated CSV file

    @param obj: network
    @param filename: .csv file
    """
    importTabFile(obj, filename, 'expression')

def importTabFile(obj, filename, attribute):
    """Imports expression from MAS5.0 generated CSV file

    @param obj: network
    @param filename: .csv file
    @param attribute: attribute to receive the file contents
    """
    if filename == '-':
        arq = sys.stdin
    else:
        arq = open(filename, 'r')
    reader = csv.reader(arq, dialect='excel-tab')
    # first line contains the labels for the columns
    # first column is gene name
    # last column are descriptions
    header = reader.next()
    for i in range(len(header)):
        header[i] = header[i].replace(' ', '_')
    obj.expression_header = header[1:-1]
    # number of experiments counting the control
    nexperiments = (len(header)-2)/6
    for row in reader:
        node = obj.findNode(GENE_NAME.findall(row[0])[0])
        if node != None:
            for col in range(1,len(row)-2):
                if row[col] == '':
                    continue
                obj.nodes[node].attributes[attribute][header[col]] = row[col]
    if filename != '-':
        arq.close()
            
def exportCyExpression(obj, attributes, filename = '-'):
    """Exports expression to Cytoscape expression data format

    @param obj: network
    @param filename: .pvals file
    """
    if filename == '-':
        arq = sys.stdout
    else:
        arq = open(filename, 'w')
        arq.write('GENE')
    for name in attributes:
        arq.write('\t'+name)
    arq.write('\n')
    for node in obj.nodes:
        arq.write(node.getName())
        for attr in attributes:
            arq.write('\t'+node.getExpression(attr))
        arq.write('\n')
    if filename != '-':
        arq.close()

def _test():
    import network
    import cytoscape
    import gml
    n = network.Network()
    gml.importGML(n, '/home/mrsva/projects/data/paeregex2.gml')
    importExpression(n, '/home/mrsva/projects/data/expression1.txt')
    #cytoscape.exportCyAttributes(n,attrname=('expression',
    #                                         'GBF0446 Pae_G1a_Signal'))
    exportCyExpression(n, ('GBF0446 Pae_G1a_Signal', 'GBF0446 Pae_G1a_Detection'))
    #gml.exportGML(n)

if __name__ == '__main__':
    _test()

