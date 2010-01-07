import os
import gzip
from operator import itemgetter

numberOfTopMeshToSkip = 0
requestedNumberOfMeshes = 2300

name = 'topmeshof' + str(requestedNumberOfMeshes) + 'from' + str(numberOfTopMeshToSkip)

meshToMeshID = dict()
documentToMeshID = dict()


# Pass through the file, creating vector indices for each MeSH term
# (arbitrarily just the order in which they are first encountered)
# and assocating with each document its list of MeSH terms.
previousMesh = None
runningMeshCount = 0
nextAvailableMeshID = 1
meshHits = 0
previousMeshHits = 0

inFile = open('sts-mesh-adj-top-first', 'r')
for line in inFile:    
    _rank, mesh, document, _garbage = line.split('|')

    meshHits += 1
    
    if mesh != previousMesh:
        runningMeshCount += 1
        previousMesh = mesh
        previousMeshHits = meshHits
        meshHits = 0
        
    if runningMeshCount > (numberOfTopMeshToSkip + requestedNumberOfMeshes):
        break
    
    if runningMeshCount > numberOfTopMeshToSkip:        
        if mesh not in meshToMeshID:
            print "Reading MeSH term ", mesh, ".  The previous MeSH term hit ", previousMeshHits, " documents."
            meshToMeshID[mesh] = nextAvailableMeshID
            nextAvailableMeshID += 1
            
        documentToMeshID.setdefault(document, []).append(meshToMeshID[mesh])
    
inFile.close()


# Create the output file, each line a vector representing one document
# and each binary-valued coordinate indicating its having that MeSH term.
outFile = open('training-top-' + str(requestedNumberOfMeshes) + '-after-' + str(numberOfTopMeshToSkip) + '.dat', 'w')

print >> outFile, str(len(documentToMeshID)), str(len(meshToMeshID))
for meshToMeshIDItem in sorted(meshToMeshID.items(), key=itemgetter(1)):
    print >> outFile, "#", meshToMeshIDItem[1], meshToMeshIDItem[0]
    
resultingDocumentCount = 0

for document in documentToMeshID:
    print >> outFile, document,
    
    for meshID in documentToMeshID[document]:
        print >> outFile, meshID,

    print >> outFile

    resultingDocumentCount += 1

outFile.close()

print "Done."
