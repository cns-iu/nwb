#!/usr/bin/env python 

from sys import argv
from gzip import open as gzopen
from airports import *
from transitions import *

def main():
    

    cities = Airports(argv[1])

    infected = [0 for i in xrange(900)]

    for line in gzopen(argv[2]):
        line = line.strip()
        
        if len(line) == 0:
            continue
        
        if line[0] == "#":
            continue
        
        trans = Transitions(line)
        
        fields = line.split()
        
        city = cities.codes[trans.city]
        time = int(trans.time)
        region = int(cities.data[city].region)
        hemi = int(cities.data[city].hemisphere)

        for t in trans.trans:
            if t.i == "L" and (t.j == "It" or t.j == "Int"):
                infected[time] += int(t.total)
                
    
    for time in xrange(900):
#        if infected[time] != 0:
        print time, infected[time] 


if __name__ == "__main__":
    main()
