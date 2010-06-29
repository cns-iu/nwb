# We must remove all attributes except x, y, qerror
# Recall that the input data is row, column and not x, y

outBMUFile = open('calibrated.bmuqerror.txt', 'w')
outIDFile = open('calibrated.idmesh.tsv', 'w')
labelSeparator = '|'

inFile = open('item-distributions-over-200-tags.calibrated.dat', 'r')
count, dim = next(inFile).split()

# Notice these are set manually..
print(dim, 'hexa', '100', '100', 'gaussian', file=outBMUFile)
print('id', 'itemid', 'tag', sep='\t', file=outIDFile)
iden = 1

meshIDToName = dict()

for line in inFile:
    if line.startswith('#'):
        _, meshID, *meshNamePieces = line.split()
        meshIDToName[meshID] = ' '.join(meshNamePieces).strip('"')
        if labelSeparator in meshIDToName[meshID]:
            print('Warning: Label', meshIDToName[meshID], 'contains the separator.')
        continue

    itemid, row, col, qerror, *meshIDAndValues = line.split()

    meshIDs = []
    meshIDValIt = iter(meshIDAndValues)
    for meshIDOrValue in meshIDValIt:
        meshIDs += [meshIDOrValue]
        next(meshIDValIt)

    x = col
    y = row
    
    print(x, y, qerror, file=outBMUFile)
    print(iden, itemid, sep='\t', end='\t', file=outIDFile)
    first = True
    for meshID in meshIDs:
        if first:
            first = False    
        else:
            print(labelSeparator, end='', file=outIDFile)

        print(meshIDToName[meshID], end='', file=outIDFile)
    print(file=outIDFile)

    iden += 1
inFile.close()

outIDFile.close()
outBMUFile.close()
