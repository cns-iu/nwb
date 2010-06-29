def neuronCmp(n1, n2):
    if n1[0] == n2[0]:
        return cmp(n1[1], n2[1])
    else:
        return cmp(n1[0], n2[0])


neuronToCluster = dict()


inClusterFile = open('nih_2007_doc_topic_prob.clustered.dat', 'r')

inClusterFile.next()
inClusterFile.next()
lineNumber = 0
for line in inClusterFile:
    cluster, documents, coordinatesstring, vector = line.split('|')

    coordinates = filter(lambda s: s != '', map(lambda x: x.strip(), coordinatesstring.split(';')))

    for coordinate in coordinates:
        row, column = map(lambda x: int(x.strip()), coordinate.split(','))
        if (row, column) in neuronToCluster:
            print 'Error: more than one cluster in row ', row, ', column', column
            exit(1)
        neuronToCluster[(row, column)] = cluster

    if lineNumber % 5000 == 0:
        print '.',    

    lineNumber += 1

inClusterFile.close()

outKevinFile = open('nih_2007_doc_topic_prob.neuron-to-cluster.bysim.tosize2.txt', 'w')
for neuron, cluster in sorted(neuronToCluster.iteritems()):#, cmp=neuronCmp):
    row, column = neuron
    print >> outKevinFile, (str(row) + ',' + str(column)), cluster
outKevinFile.close()


print 'Done.'
