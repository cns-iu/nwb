import os
import gzip
from operator import itemgetter

numberOfDocuments = 2153769.0

previousMesh = 'Humans'
meshHits = 0

inFile = open('sts-mesh-adj-top-first', 'r')
outFile = open('mesh-hit-counts.tsv', 'w')
print >> outFile, 'MeSH' + '\t' + 'Number of documents' + '\t' + 'Proportion of documents'
for line in inFile:    
    _rank, mesh, document, _garbage = line.split('|')

    meshHits += 1
    
    if mesh != previousMesh:
        print >> outFile, previousMesh + '\t' + str(meshHits) + '\t' + str(round(meshHits / numberOfDocuments, 3))
        previousMesh = mesh
        meshHits = 0

outFile.close()
inFile.close()

print "Done."
