#!/usr/bin/python 

from sys import argv
from gzip import open as gzopen
from airports import *
from transitions import *

def main():
    
     cities = Airports(argv[1])
     
     for file in argv[2:]:

        pop = [0 for i in xrange(len(cities.data))]

        time = 0

        for line in gzopen(file):
            line = line.strip()
            
            if len(line) == 0:
                continue

            if line[0] == "#":
                continue

            fields = line.split()

            if fields[1] != time and time != 0:
                avg = 0.0

                for i in xrange(0,len(pop)):
                    avg += abs(pop[i]-int(cities.data[i].population))/float(cities.data[i].population)
                    pop[i] = 0

                print time, avg/len(pop)
                   
                for i in xrange(len(pop)):
                    pop[i] = 0

            time = fields[1]

            for i in xrange(2,len(fields)):
                pop[cities.codes[fields[0]]] += int(fields[i])

           

if __name__ == "__main__":
    main()
