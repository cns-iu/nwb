import random

columns = 252
rows = 300
weightdim = 2300

outFile = open('random.cod', 'w')
print >> outFile, weightdim, 'hexa', columns, rows, 'gaussian',
for i in range(columns * rows):
    print >> outFile
    for j in range(weightdim):
        print >> outFile, random.randint(0, 1),#random.randrange(0, 255),
outFile.close()
