import random

columns = 14#252
rows = 8#300
weightdim = 3#2300

outFile = open('random.cod', 'w')
print >> outFile, weightdim, 'hexa', columns, rows, 'gaussian',
for i in range(columns * rows):
    print >> outFile
    for j in range(weightdim):
        print >> outFile, random.randint(0, 1),#random.randrange(0, 255),
outFile.close()
