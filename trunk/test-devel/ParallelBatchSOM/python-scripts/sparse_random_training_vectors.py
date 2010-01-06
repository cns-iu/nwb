import random

numberOfVectors = 1000#2000000
dimension = 3#2300
#denseness = 10.0 / float(dimension)
mean = 2
stddev = 0.5

validIDs = xrange(1, 1 + dimension)

outFile = open('training.dat', 'w')
print >> outFile, numberOfVectors, dimension,
for i in range(numberOfVectors):
    print >> outFile
    
    # Dummy document ID
    print >> outFile, '0',

    nonzeroCount = max(1, int(random.gauss(mean, stddev)))
    for id in sorted(random.sample(validIDs, nonzeroCount)):
        print >> outFile, id,

    '''
    for j in range(dimension):
        if random.random() <= denseness:
            print >> outFile, (j + 1),
    '''
outFile.close()
