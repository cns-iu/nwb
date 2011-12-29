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


counts = []




inFile = open('final.25clustered', 'r')
inFile.next()
inFile.next()
for line in inFile:
    cluster, documents, coordinates, vector = line.split('|')
    counts += [len(documents.split())]
inFile.close()

for freq, count in runlength_enc(sorted(counts)):
        print count, ',', freq
