import re


def main():
    positionToColor = dict()

    nodes = []

    codebookFile = open('trained.cod')
    dim, topology, columns, rows, neighborhood = codebookFile.next().split()
    lineIndex = 0
    for line in codebookFile:
        r, g, b = line.split()
        i, j = (int(lineIndex / int(columns)), lineIndex % int(columns))

        nodes += [(i, j, float(r), float(g), float(b))]
        
        lineIndex += 1
    codebookFile.close()

    outFile = open('trained.eps', 'w')
    inFile = open('template.eps')
    for line in inFile:
        if line.startswith('NEURONS GO HERE'):
            for node in nodes:
                i, j, r, g, b = node
                print >> outFile, str(20 + 40*i + ((j%2)*20)), str(17 + 34*j), str(r), str(g), str(b), 'myLN'
        else:
            print >> outFile, line,
    inFile.close()
    outFile.close()

    print "Done."
    
            

main()              
