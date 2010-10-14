#!/usr/bin/env python 

from sys import argv
from gzip import open as gzopen
from airports import *
from transitions import *

def main():
    
    value = argv[1]

    cities = Airports(argv[2])

    files = [0 for i in xrange(18)]

    hemispheres = [0 for i in xrange(17)]
    globe = 17

    for line in open(argv[3]):
        fields = line.strip().split()

        index = int(fields[0])
        hemi = int(fields[1])
        name = fields[2]

        hemispheres[index] = hemi
        files[index] = open("output/secondary." + name + "." + value + ".dat", "w")

    files[globe] = open("output/secondary.globe." + value + ".dat", "w")

    infected = [[0 for j in xrange(900)] for i in xrange(18)]

    for line in gzopen(argv[4]):
        line = line.strip()
        
        if len(line) == 0:
            continue
        
        if line[0] == "#":
            continue
        
        
        fields = line.split()
        
        city = fields[0]
        time = int(fields[1])
        total = int(fields[2])
        
        region = cities.data[cities.codes[city]].region
        
        infected[region][time] += total
        infected[globe][time] += total
        
    for i in xrange(len(infected)):
        for time in xrange(len(infected[i])):
            if infected[i][time] != 0:
                files[i].write(str(time) + " " + str(infected[i][time]) + "\n")

if __name__ == "__main__":
    main()
