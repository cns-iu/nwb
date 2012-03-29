from math import sqrt, fmod


g_rows = 200
g_columns = 240

neuronToCluster = dict()


def row(neuron):
    return neuron[0]

def column(neuron):
    return neuron[1]

def up(row):
    return row - 1

def down(row):
    return row + 1

def dueLeftColumn(column):
    return column - 1
def slantLeft(neuron):
    slantLeftColumn = column(neuron)
    if row(neuron) % 2 == 0:
        slantLeftColumn -= 1

    return slantLeftColumn

def dueRightColumn(column):
    return column + 1
def slantRight(neuron):
    slantRightColumn = column(neuron)
    if row(neuron) % 2 == 1:
        slantRightColumn += 1

    return slantRightColumn

def upLeft(neuron):
    return (up(row(neuron)), slantLeft(neuron))

def upRight(neuron):
    return (up(row(neuron)), slantRight(neuron))

def dueLeft(neuron):
    return (row(neuron), dueLeftColumn(column(neuron)))

def dueRight(neuron):
    return (row(neuron), dueRightColumn(column(neuron)))

def downLeft(neuron):
    return (down(row(neuron)), slantLeft(neuron))

def downRight(neuron):
    return (down(row(neuron)), slantRight(neuron))


def neuronInRange(neuron):
    return ((0 <= row(neuron) < g_rows) and (0 <= column(neuron) < g_columns));

def displayCoord(neuron):
    row, column = neuron
    return (19.7 + 39.4*column + ((round(row)%2)*19.7), 17 + 34*row)

def perpAtOrigin(coord1, coord2):
    #return (, ), (, ))
    return (((coord1[0] - coord2[0]) / 2.0, (coord2[1] - coord1[1]) / 2.0), ((coord2[0] - coord1[0]) / 2.0, (coord1[1] - coord2[1]) / 2.0))

def perp(coord1, coord2):
    o = mean(displayCoord(coord1), displayCoord(coord2))
    return map(lambda p: map(lambda t1, t2: t1+t2, o, map(lambda x: 10*x, p)), perpAtOrigin(coord1, coord2))

def mean(coord1, coord2):
    return ((coord1[0] + coord2[0]) / 2.0, (coord1[1] + coord2[1]) / 2.0)



def euclidean(neuron1, neuron2):
    return sqrt((neuron1[0] - neuron2[0])**2 + (neuron1[1] - neuron2[1])**2)

def drawBorderIfSeparated(outStream, center, neighbor):
    if neuronInRange(neighbor) and neuronToCluster[center] != neuronToCluster[neighbor]:
        #print euclidean(perp(center, neighbor)[0], perp(center, neighbor)[1])
        endpoint1, endpoint2 = perp(center, neighbor)
        #print euclidean(endpoint1, endpoint2)
        print >> outStream, 'newpath', endpoint1[0], endpoint1[1], 'moveto', endpoint2[0], endpoint2[1], 'lineto', 'closepath', 0, 'setgray', 'stroke'

def printNeuron(outFile, neuron, cluster):
    x, y = displayCoord(neuron)
    
    drawBorderIfSeparated(outFile, neuron, upRight(neuron))
    drawBorderIfSeparated(outFile, neuron, dueRight(neuron))
    drawBorderIfSeparated(outFile, neuron, downRight(neuron))
    drawBorderIfSeparated(outFile, neuron, downLeft(neuron))
    drawBorderIfSeparated(outFile, neuron, dueLeft(neuron))
    drawBorderIfSeparated(outFile, neuron, upLeft(neuron))
    
    print >> outFile, str(x), str(y), str(0.95), 'myLN'
    

def printNeurons(outFile):
    for neuron, cluster in neuronToCluster.iteritems():
        printNeuron(outFile, neuron, cluster)


def main():
    inClusterFile = open('nih_2007_doc_topic_prob.neuron-to-cluster.bysim.tosize2.txt', 'r')
    for line in inClusterFile:
        neuron, cluster = line.split()
        row, column = map(lambda s: int(s.strip()), neuron.split(','))

        neuronToCluster[(row, column)] = cluster
    inClusterFile.close()


    outFile = open('nih_2007_doc_topic_prob.neuron-to-cluster.bysim.tosize2.eps', 'w')
    inFile = open('clusterTemplate.eps')
    for line in inFile:
        if line.startswith('NEURONS GO HERE'):
            printNeurons(outFile)
        else:
            print >> outFile, line,
    inFile.close()
    outFile.close()


main()
