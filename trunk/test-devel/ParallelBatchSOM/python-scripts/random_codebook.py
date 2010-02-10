import random

columns = 150
rows = 116
weightdim = 529#2300

outFile = open('random.cod', 'w')
print >> outFile, weightdim, 'hexa', columns, rows, 'gaussian',
for i in range(columns * rows):
    print >> outFile

    # Generate a vector with at least one 1.
    while True:
        vector = [random.randint(0, 1) for j in xrange(weightdim)]
        if 1 in vector:
            break
        
    for coordinate in vector:
        print >> outFile, coordinate,
outFile.close()
