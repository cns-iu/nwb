import random

numberOfVectors = 5000
dimension = 3

outFile = open('color.dat', 'w')
print >> outFile, numberOfVectors, dimension,
for i in range(numberOfVectors):
    print >> outFile
    
    for j in range(dimension):
        print >> outFile, int(255 * random.random()),
outFile.close()
