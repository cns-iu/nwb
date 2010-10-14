#!/usr/bin/env python

from sys import argv
from gzip import open as gzopen
from math import sqrt
from stats2 import statistics, getdata

def main():

    data = getdata(argv)

    result = statistics(data)
    
    print "# time avg stderr median 95%low 95%high"

    for i in xrange(len(result[0])):
        print i, result[0][i], result[1][i], result[2][i], result[3][i][0], result[3][i][1]
    return data

if __name__ == "__main__":
    main()
