from operator import itemgetter
import itertools as its

def ilen(it):
    '''Return the length of an iterable.
    
    >>> ilen(range(7))
    7
    '''
    return sum(1 for _ in it)

def runlength_enc(xs):
    '''Return a run-length encoded version of the stream, xs.
    
    The resulting stream consists of (count, x) pairs.
    
    >>> ys = runlength_enc('AAABBCCC')
    >>> next(ys)
    (3, 'A')
    >>> list(ys)
    [(2, 'B'), (3, 'C')]
    '''
    return ((ilen(gp), x) for x, gp in its.groupby(xs))


topComponents = []

inFile = open('trained.cod', 'r')
inFile.next()
for line in inFile:
    components = line.split()

    maxComponent = 0
    maxIndex = -1
    index = 1 # One-indexed!
    for component in components:
        if float(component) > maxComponent:
            maxComponent = float(component)
            maxIndex = index            
        maxIndex += 1
    topComponents += [maxIndex]
inFile.close()

for count, topic in sorted(runlength_enc(sorted(topComponents)), key=itemgetter(1)):
    print str(topic) + ', ' + str(count)
