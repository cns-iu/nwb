inCalibratedFile = open('final.calibrated', 'r')


docToNeuron = dict()

inCalibratedFile.next()
lineNumber = 0
for line in inCalibratedFile:
    if line.startswith('#'):
        continue

    if lineNumber % 100000 == 0:
        print '.',

    document, row, column = line.split()[:3]
    docToNeuron[document] = (row + ',' + column)
    

    lineNumber += 1
inCalibratedFile.close()


outKevinFile = open('pmid-to-neuron.txt', 'w')
for document, neuron in sorted(docToNeuron.iteritems()):
    print >> outKevinFile, document, neuron
outKevinFile.close()


print 'Done.'
