import random

numberOfVectors = 2000000
dimension = 2300
denseness = 10.0 / float(dimension)

outFile = open('training.dat', 'w')
print >> outFile, numberOfVectors, dimension,
for i in range(numberOfVectors):
    print >> outFile
    
    # Dummy document ID
    print >> outFile, '0',

    for j in range(dimension):
        if random.random() <= denseness:
            print >> outFile, (j + 1),
outFile.close()
