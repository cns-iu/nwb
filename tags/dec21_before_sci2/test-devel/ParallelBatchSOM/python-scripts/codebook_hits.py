import re
import sys
import operator
import math

# Rewrite to use data from C++ calibration

def main():
    # Use hard-coded input codebook filename unless 1 argument is given.
    inCodebookFilename = 'final.cod'
    if len(sys.argv) > 1:
        inCodebookFilename = sys.argv[1]
    
    # Use hard-coded input dataset filename unless 2 arguments are given.
    inDatasetFilename = 'training-top-2300-after-0.dat'
    if len(sys.argv) > 2:
        inDatasetFilename = sys.argv[2]

    # Use hard-coded output EPS filename unless 3 arguments are given.
    outFilename = ''.join(inCodebookFilename.split('.')[:-1]) + '.activation' + '.eps'
    if len(sys.argv) > 3:
        outFilename = sys.argv[3]

    print 'Reading codebook..',
    codebook = []
    inCodebookFile = open(inCodebookFilename, 'r')
    dim, topology, columns, rows, neighborhood = inCodebookFile.next().split()
    for codebookLine in inCodebookFile:
        if codebookLine.startswith('#'):
            continue
        codebook += [map(float, codebookLine.split())]
        
    inCodebookFile.close()
    print 'Done.'


    codebookHitcounts = dict()

    print 'Reading input vectors..',
    inDatasetFile = open(inDatasetFilename, 'r')
    dataVectorCount, dim = inDatasetFile.next().split()
    for dataLine in inDatasetFile:
        if dataLine.startswith('#'):
            continue
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
    print 'Done.'


    maxHitcount = max(codebookHitcounts.values())
    print 'Max hitcount =', maxHitcount


    print 'Generating PostScript..',
    outPostScriptFile = open(outFilename, 'w')
    inTemplateFile = open('planeTemplate.eps')
    for templateLine in inTemplateFile:
        if templateLine.startswith('NEURONS GO HERE'):
            for codebookVectorIndex in xrange(len(codebook)):
                i, j = (int(codebookVectorIndex / int(columns)), codebookVectorIndex % int(columns))
                codebookVector = codebook[codebookVectorIndex]
                hits = codebookHitcounts.get(codebookVectorIndex, 0)
                gray = 1.0 - (float(hits) / float(maxHitcount))

                print >> outPostScriptFile, str(19.7 + 39.4*i + ((j%2)*19.7)), str(17 + 34*j), str(gray), 'myLN'
        else:
            print >> outPostScriptFile, templateLine,
    inTemplateFile.close()
    outPostScriptFile.close()

    print 'Done.'

    print 'Writing hitcount file..',
    outCountFile = open('counts.csv', 'w')
    for index, count in sorted(codebookHitcounts.items(), key=itemgetter(1), reverse=True):
        print >> outCountFile, count
    print 'Done.'


main()              
