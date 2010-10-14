#!/usr/bin/env python 

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
        files[index] = open("secondary" + name + "." + value + ".dat", "w")

    infected = [[0 for j in xrange(900)] for i in xrange(17)]

    for file in argv[4:]:

        states = {}

        time = 0

        for line in gzopen(file):
            line = line.strip()
            
            if len(line) == 0:
                continue

            if line[0] == "#" and line[2:6] == "time":
                fields = line.split()

                for id in xrange(2,len(fields)):
                    states[fields[id]] = id
                    for i in xrange(len(files)):
                        files[i].write("@    s"+str(id-2)+" legend  \""+fields[id]+"\"\n")

                continue

            if line[0] == "#":
                continue

            trans = Transitions(line)
                        
            fields = line.split()

            city = trans.city
            time = int(trans.time)

            fields = line.split()
            region = cities.data[cities.codes[city]].region

            for t in trans.trans:
                if t.i == "L" and (t.j == "It" or t.j == "Int"):
                    infected[region][time]+=int(t.total)

        
        for i in xrange(len(infected)):
            for time in xrange(len(infected[i])):
                if infected[i][time] != 0:
                    files[i].write(str(time) + " " + str(infected[i][time]) + "\n")
           

if __name__ == "__main__":
    main()
