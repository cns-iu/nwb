import os
from operator import itemgetter
import random

NUMBER_OF_TERMS = 2300

# Check tags -- ignore them.
MESH_TO_SKIP = {'Adolescent', 'Adult', 'Aged', 'Aged, 80 and over', 'Animals',  \
                'Cats', 'Cattle', 'Chick Embryo', 'Child', 'Child, Preschool', \
                'Cricetinae', 'Dogs', 'Female', 'Guinea Pigs', 'History, 15th Century', \
                'History, 16th Century', 'History, 17th Century', 'History, 18th Century', \
                'History, 19th Century', 'History, 20th Century', 'History, 21st Century', \
                'History, Ancient', 'History, Medieval', 'Humans', 'Infant', 'Infant, Newborn', \
                'Male', 'Mice', 'Middle Aged', 'Pregnancy', 'Rabbits', 'Rats', 'Young Adult'}


meshToMeshID = dict()
documentToMeshID = dict()
skippedMesh = set()


# Pass through the file, creating vector indices for each MeSH term
# (arbitrarily just the order in which they are first encountered)
# and assocating with each document its list of MeSH terms.
previousMesh = None
runningMeshCount = 0
nextAvailableMeshID = 1
annotationCount = 0
previousAnnotationCount = 0

with open('sts-mesh-adj-top-first', 'r') as inFile:
    for line in inFile:
        if runningMeshCount >= NUMBER_OF_TERMS:
            break
        
        _rank, mesh, document, _garbage = tuple(map(lambda s: s.strip(), line.split('|')))

        if mesh in MESH_TO_SKIP:
            skippedMesh.add(mesh)
            continue

        annotationCount += 1
        
        if mesh != previousMesh:
            runningMeshCount += 1
            previousMesh = mesh
            previousAnnotationCount = annotationCount
            annotationCount = 0
        
        if mesh not in meshToMeshID:
            print(previousMesh + ',', str(previousAnnotationCount) + ',', mesh)
            meshToMeshID[mesh] = nextAvailableMeshID
            nextAvailableMeshID += 1

        documentToMeshID.setdefault(document, set()).add(meshToMeshID[mesh])

        


# Create the output file, each line a vector representing one document
# and each binary-valued coordinate indicating its having that MeSH term.
with open('training-top-' + str(NUMBER_OF_TERMS) + '.dat', 'w') as outFile:
    print(str(len(documentToMeshID)), str(len(meshToMeshID)), file=outFile)
    
    for meshToMeshIDItem in sorted(meshToMeshID.items(), key=itemgetter(1)):
        print('#', meshToMeshIDItem[1], meshToMeshIDItem[0], file=outFile)

    documentKeys = list(documentToMeshID.keys())
    random.shuffle(documentKeys)
    for document in documentKeys:
        print(document, end=' ', file=outFile)
        
        for meshID in sorted(documentToMeshID[document]):
            print(meshID, end=' ', file=outFile)

        print(file=outFile)

print('Skipped the following MeSH terms:')
print(sorted(skippedMesh))
