import re
import sys

# Note that 'plane' indexes from 1.

def main():
    # Use hard-coded input filename unless one argument is given
    inFilename = 'trained.cod'
    if len(sys.argv) > 1:
        inFilename = sys.argv[1]
    
    # Use hard-coded plane unless two arguments are given.
    plane = 1    
    if len(sys.argv) > 2:
        plane = int(sys.argv[2])
        
    positionToColor = dict()

    nodes = []

    codebookFile = open(inFilename)
    dim, topology, columns, rows, neighborhood = codebookFile.next().split()
    lineIndex = 0
    for line in codebookFile:
        coords = line.split()
        coord = coords[plane - 1]
        i, j = (int(lineIndex / int(columns)), lineIndex % int(columns))

        nodes += [(i, j, float(coord))]
        
        lineIndex += 1
    codebookFile.close()

    outFilename = ''.join(inFilename.split('.')[:-1]) + '.plane' + str(plane) + '.eps'
    outFile = open(outFilename, 'w')
    inFile = open('planeTemplate.eps')
    for line in inFile:
        if line.startswith('NEURONS GO HERE'):
            for node in nodes:
                i, j, gray = node
                print >> outFile, str(20 + 40*i + ((j%2)*20)), str(17 + 34*j), str(gray), 'myLN'
        else:
            print >> outFile, line,
    inFile.close()
    outFile.close()

    print "Done."


main()              
