#!/usr/bin/python 

from sys import argv
from gzip import open as gzopen
from airports import *
from transitions import *

def main():
    
    value = argv[1]

    cities = Airports(argv[2])

    files = [0 for i in xrange(17)]

    hemispheres = [0 for i in xrange(17)]

    for line in open(argv[3]):
        fields = line.strip().split()

        index = int(fields[0])
        hemi = int(fields[1])
        name = fields[2]

        hemispheres[index] = hemi
        
        
    for airport in xrange(len(cities.data)):
        index = int(cities.data[airport].region)

        if int(cities.data[airport].hemisphere) == hemispheres[index]:
            print airport, index


           

if __name__ == "__main__":
    main()
