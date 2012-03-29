codebookFilename = 'trained.cod'

with open(codebookFilename, 'r') as codebookFile:
    it = iter(codebookFile)
    next(it)

    firstVector = next(it)
    maxCoords = list(map(float, firstVector.strip().split()))

    print(maxCoords)
    
    for line in it:
        if len(line.strip()) == 0:
            break
        
        coords = list(map(float, line.strip().split()))

        for i in range(len(maxCoords)):
            maxCoords[i] = max(coords[i], maxCoords[i])

print('Maxes:', maxCoords)

with open(codebookFilename, 'r') as codebookFile:
    with open('.'.join(codebookFilename.split('.')[:-1]) + '.normalized.cod', 'w') as outFile:
        it = iter(codebookFile)
        print(next(it), end='', file=outFile)
        
        for line in it:
            coords = list(map(float, line.strip().split()))

            for i in range(len(coords)):
                print(coords[i] / maxCoords[i], end=' ', file=outFile)

            print(file=outFile)


