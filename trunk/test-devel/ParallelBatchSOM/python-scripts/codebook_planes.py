import re
import sys
import fileinput

RADIUS = 18 # points

def header(numPages):
    return '''%!PS-Adobe-2.0
%%Title: undefined
%%Creator: planes
%%BoundingBox: 0 0 11020 9350
%%Pages: ''' + str(numPages) + '''
%%EndComments
/myLN
{ setgray
newpath
radius 0 360 arc gsave fill grestore 0.9 setgray stroke
} def

/radius ''' + str(RADIUS) + ''' def
'''

def show(mesh):
    return 'gsave /Garamond findfont 50 scalefont setfont 0.2 setgray 30 50 moveto (' + mesh + ') show grestore'


def main():
    # Use hard-coded input filename unless one argument is given
    inCodebookFilename = 'final.cod'
    if len(sys.argv) > 1:
        inCodebookFilename = sys.argv[1]
    
    planeRange = range(1, 3 + 1)


    # Read the MeSH term corresponding to each plane/ID/coordinate.
    inDatasetFilename = 'training-top-2300-after-0.dat'    
    meshIDToName = dict()
    for line in fileinput.input(inDatasetFilename):
        if fileinput.isfirstline():
            continue
        if line.startswith('#'):
            marker, index, mesh = map(lambda s: s.strip(), line.split(None, 2))
            meshIDToName[int(index)] = mesh
            #print index, meshIDToName[index]
        else:
            break
    fileinput.close()
    

    # Figure out the positions and gray levels of the nodes for each plane.
    nodesPerPlane = dict()
    print 'Reading codebook file.. ',
    codebookFile = open(inCodebookFilename)
    dim, topology, columns, rows, neighborhood = codebookFile.next().split()
    lineIndex = 0
    for line in codebookFile:
        for plane in planeRange:
            coords = line.split()
            coord = coords[plane - 1]
            i, j = (int(lineIndex / int(columns)), lineIndex % int(columns))
            # Rotate to portrait orientation if it better suits the dimensions.
            if columns < rows:
                temp = j
                j = i
                i = temp                

            nodesPerPlane[plane] = nodesPerPlane.setdefault(plane, []) + [(i, j, float(coord))]
        
        lineIndex += 1
    codebookFile.close()
    print 'Done.' 

    if columns < rows:
        temp = columns
        columns = rows
        rows = temp
    xscale = 500.0 / (2 * RADIUS * int(rows))
    yscale = 700.0 / (2 * RADIUS * int(columns))
    print xscale, yscale
    scale = min(xscale, yscale)

    # Create PostScript.
    outPSFilename = ''.join(inCodebookFilename.split('.')[:-1]) + '.planes.ps'
    outPSFile = open(outPSFilename, 'w')

    print >> outPSFile, header(len(planeRange))

    # One page per plane.
    for plane in planeRange:
        mesh = meshIDToName[plane]
        print >> outPSFile, '%%Page: ' + str(plane) + ' ' + str(plane)
        print >> outPSFile, '% MeSH: ' + mesh
        print >> outPSFile, show(mesh)
        print >> outPSFile, '20 120 translate'
        print >> outPSFile, scale, scale, 'scale'

        for node in nodesPerPlane[plane]:
            i, j, gray = node
            print >> outPSFile, str(20 + 40*i + ((j%2)*20)), str(17 + 34*j), str(gray), 'myLN'

        print >> outPSFile, 'showpage'
        
    outPSFile.close()

    print 'Done.'


main()
