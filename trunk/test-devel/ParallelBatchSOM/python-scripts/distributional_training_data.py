import random

numberOfVectors = 1000#2000000
dimension = 3#2300

outFile = open('training.dat', 'w')
print >> outFile, numberOfVectors, dimension,
for i in range(numberOfVectors):
    print >> outFile
    
    # Dummy training datum label
    print >> outFile, random.randrange(100000, 999999),

    vector = [random.random() for _ in range(dimension)]
    vector = [value / sum(vector) for value in vector]
    
    for index in range(1, 1 + dimension):
        print >> outFile, index, round(vector[index - 1], 4),

    '''
    for j in range(dimension):
        if random.random() <= denseness:
            print >> outFile, (j + 1),
    '''
outFile.close()
