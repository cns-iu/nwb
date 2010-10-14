#!/usr/bin/env python

from sys import argv, stderr
from gzip import open as gzOpen
import numpy

def main():
    NRuns = len(argv)
    data = []
    nsize = int(0.05*0.5*NRuns)
    
    print >> stderr, NRuns, nsize
    
    field = int(argv[1])

    for file in argv[2:]:
        print >> stderr, file
        
        for line in gzOpen(file):
            if len(line.strip()) == 0 or line[0] == "#":
                continue
        
            fields = line.strip().split()

            #print line.strip()

            time = int(fields[0])

            if time >= len(data):
                temp = len(data)
                data.extend([[] for i in xrange(temp, time+1)])

            data[time].append(int(fields[field]))


    for t in xrange(len(data)):
        avg = numpy.average(data[t])
        count = len(data[t])
        scale = float(count)/(NRuns-1)

        add = NRuns - count

        if add != 0:
            data[t].extend([0 for i in xrange(add)])

        data[t].sort()

        min = data[t][nsize-1]
        max = data[t][-(nsize-1)]
        med = numpy.median(data[t])

        print t, med, min, max, avg, count

if __name__ == "__main__":
    main()
