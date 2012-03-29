import re
import sys
import operator
import math

def similarity(oneIndices, vector):
    dotProduct = 0.0
    
    for oneIndex in oneIndices:
        dotProduct += vector[oneIndex]

    vectorSquaredNorm = 0.0
    for coord in vector:
        vectorSquaredNorm += coord * coord

    return dotProduct / (math.sqrt(len(oneIndices)) * math.sqrt(vectorSquaredNorm))
        

def dissimilarity(oneIndices, vector):
    return 1 - similarity(oneIndices, vector)

def main():
    # Use hard-coded input codebook filename unless 1 argument is given.
    inCodebookFilename = 'random.cod'
    if len(sys.argv) > 1:
        inCodebookFilename = sys.argv[1]
    
    # Use hard-coded input dataset filename unless 2 arguments are given.
    inDatasetFilename = 'training.dat'
    if len(sys.argv) > 2:
        inDatasetFilename = sys.argv[2]

    # Use hard-coded output EPS filename unless 3 arguments are given.
    outFilename = ''.join(inCodebookFilename.split('.')[:-1]) + '.activation' + '.eps'
    if len(sys.argv) > 3:
        outFilename = sys.argv[3]

    codebook = []
    inCodebookFile = open(inCodebookFilename, 'r')
    dim, topology, columns, rows, neighborhood = inCodebookFile.next().split()
    for codebookLine in inCodebookFile:
        codebook += [map(float, codebookLine.split())]
        
    inCodebookFile.close()


    codebookHitcounts = dict()

    inDatasetFile = open(inDatasetFilename, 'r')
    dataVectorCount, dim = inDatasetFile.next().split()
    for dataLine in inDatasetFile:
        # 1: to skip the document ID
        dataOneIndices = set(map(lambda x: x - 1, map(int, dataLine.split()[1:])))

        winningCodebookVectorIndex = None
        leastDissim = 999999999999999999
        for codebookVectorIndex in xrange(len(codebook)):
            codebookVector = codebook[codebookVectorIndex]
            dissim = dissimilarity(dataOneIndices, codebookVector)

            if dissim < leastDissim:
                winningCodebookVectorIndex = codebookVectorIndex
                leastDissim = dissim

        oldHitcount = codebookHitcounts.setdefault(winningCodebookVectorIndex, 0)
        codebookHitcounts[winningCodebookVectorIndex] = oldHitcount + 1
            
    inDatasetFile.close()


    print 'Finding max hitcount..'
    maxHitcount = max(codebookHitcounts.values())
    print 'Max =', maxHitcount


    outFile = open(outFilename, 'w')
    inTemplateFile = open('planeTemplate.eps')
    for templateLine in inTemplateFile:
        if templateLine.startswith('NEURONS GO HERE'):
            for codebookVectorIndex in xrange(len(codebook)):
                i, j = (int(codebookVectorIndex / int(columns)), codebookVectorIndex % int(columns))
                codebookVector = codebook[codebookVectorIndex]
                hits = codebookHitcounts.get(codebookVectorIndex, 0)
                gray = 1.0 - (float(hits) / float(maxHitcount))

                print >> outFile, str(19.7 + 39.4*i + ((j%2)*19.7)), str(17 + 34*j), str(gray), 'myLN'
        else:
            print >> outFile, templateLine,
    inTemplateFile.close()
    outFile.close()

    print "Done."


main()              
