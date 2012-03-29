import random

numberOfVectors = 1000#2000000
dimension = 3#2300
#denseness = 10.0 / float(dimension)
mean = 1.5
stddev = 1.1

validIndices = xrange(1, 1 + dimension)

outFile = open('training.dat', 'w')
print >> outFile, numberOfVectors, dimension,
for i in range(numberOfVectors):
    print >> outFile
    
    # Dummy document ID
    print >> outFile, random.randrange(100000, 999999),

    nonzeroCount = min(3, max(1, int(random.gauss(mean, stddev))))
    for index in sorted(random.sample(validIndices, nonzeroCount)):
        print >> outFile, index, 10*round(random.random(), 4),

    '''
    for j in range(dimension):
        if random.random() <= denseness:
            print >> outFile, (j + 1),
    '''
outFile.close()
