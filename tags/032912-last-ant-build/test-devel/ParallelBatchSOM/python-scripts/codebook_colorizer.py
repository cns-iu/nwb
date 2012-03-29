import re


def main():
    positionToColor = dict()

    nodes = []

    codebookFilename = 'normalized.cod'
    codebookFile = open(codebookFilename, 'r')
    dim, topology, columns, rows, neighborhood = codebookFile.next().split()
    lineIndex = 0
    for line in codebookFile:
        r, g, b = line.split()
        row, column = (int(lineIndex / int(columns)), lineIndex % int(columns))

        nodes += [(row, column, float(r), float(g), float(b))]
        
        lineIndex += 1
    codebookFile.close()

    outFilename = '.'.join(codebookFilename.split('.')[:-1]) + '.colored.eps'
    outFile = open(outFilename, 'w')
    inFile = open('colorTemplate.eps')
    for line in inFile:
        if line.startswith('NEURONS GO HERE'):
            for node in nodes:
                row, column, r, g, b = node
                yrow = int(rows) - row - 1
                print >> outFile, str(19.7 + 39.4*column + ((row%2)*19.7)), str(17 + 34*yrow), str(r), str(g), str(b), 'myLN'
        else:
            print >> outFile, line,
    inFile.close()
    outFile.close()

    print "Done."
    
            

main()              
