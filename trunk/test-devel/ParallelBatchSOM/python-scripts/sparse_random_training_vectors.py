import random

numberOfVectors = 1000#2000000
dimension = 3#2300
denseness = 0.5#10.0 / float(dimension)

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
