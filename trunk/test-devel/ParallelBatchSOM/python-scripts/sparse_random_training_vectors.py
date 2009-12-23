import random

numberOfVectors = 200000
dimension = 2300
denseness = 10.0 / float(dimension)

outFile = open('color.dat', 'w')
print >> outFile, numberOfVectors, dimension,
for i in range(numberOfVectors):
    print >> outFile

    for j in range(dimension):
        if random.random() <= denseness:
            print >> outFile, j,
outFile.close()
