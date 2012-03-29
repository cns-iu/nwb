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


def main():
    columns = 252
    rows = 300

    hits = [[0 for c in range(columns)] for r in range(rows)]

    inFile = open('training-top-2300-after-0.cal', 'r')
    vectorCount, dim = inFile.next().split()
    lineCount = 0
    for line in inFile:
        if line.startswith('#'):
            continue

        if lineCount % 100000 == 0:
            print lineCount, "lines read."

        doc, row, col, diss = line.split()[:4]
        hits[int(row)][int(col)] += 1

        lineCount += 1
    inFile.close()

    print "Done reading calibrated data."

    outFile = open('cal-dist.csv', 'w')
    print >> outFile, 'count', ',', 'frequency'
    for freq, count in runlength_enc(sorted(sum(hits, []))):
        print >> outFile, count, ',', freq
    outFile.close()

main()
