import random

columns = 100
rows = 100
weightdim = 3

outFile = open('random.cod', 'w')
print >> outFile, weightdim, 'hexa', columns, rows, 'gaussian',
for i in range(columns * rows):
    print >> outFile

    # Generate a vector with at least one 1.
    while True:
        vector = [round(random.random(), 3) for j in xrange(weightdim)]
        if 1 in vector:
            break
        
    for coordinate in vector:
        print >> outFile, coordinate,
outFile.close()

print('Done.')
