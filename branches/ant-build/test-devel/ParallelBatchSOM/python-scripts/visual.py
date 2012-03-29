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

def inputIsValid(line, dimLimit):
    try:
        if len(line) == 0:
            return False

        dimensions = map(lambda x: int(x), line.split())

        for dimension in dimensions:
            if not (1 <= dimension <= dimLimit):
                return False
    except Exception:
        return False
    
    return True

def findBMUIndex(oneIndices, codebook, codebookVectorIndices):
    winningCodebookVectorIndex = None
    leastDissim = 999999999999999999
    for codebookVectorIndex in codebookVectorIndices:
        codebookVector = codebook[codebookVectorIndex]
        dissim = dissimilarity(oneIndices, codebookVector)

        if dissim < leastDissim:
            winningCodebookVectorIndex = codebookVectorIndex
            leastDissim = dissim

    return winningCodebookVectorIndex, leastDissim
        

def main():
    # Use hard-coded input codebook filename unless 1 argument is given.
    inCodebookFilename = 'test.cod'
    if len(sys.argv) > 1:
        inCodebookFilename = sys.argv[1]

    codebook = []
    print 'Loading codebook.. ',
    inCodebookFile = open(inCodebookFilename, 'r')
    dim, topology, columns, rows, neighborhood = inCodebookFile.next().split()
    for codebookLine in inCodebookFile:
        codebook += [map(float, codebookLine.split())]        
    inCodebookFile.close()
    codebookVectorIndices = xrange(len(codebook))
    print 'Done.'

    stillRunning = True
    while stillRunning:
        userInput = raw_input('Enter dimensions: ')
        
        if userInput == 'exit':
            stillRunning = False
        else:
            if not inputIsValid(userInput, int(dim)):
                print 'Invalid input.'
                continue

            oneIndices = set(map(lambda x: int(x) - 1, userInput.split()))

            winningCodebookVectorIndex, dissim = findBMUIndex(oneIndices, codebook, codebookVectorIndices)

            winnerRow = winningCodebookVectorIndex / int(columns)
            winnerCol = winningCodebookVectorIndex % int(columns)

            print 'Best-matching unit is at (' + str(winnerRow) + ', ' + str(winnerCol) + ') with dissimilarity', round(dissim, 8)

    print "Exiting."


main()              
