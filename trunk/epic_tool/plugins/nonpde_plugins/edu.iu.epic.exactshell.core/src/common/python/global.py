#!/usr/bin/python 

from sys import argv
from gzip import open as gzopen
from airports import *
from transitions import *

def main():
    
    for file in argv[1:]:

        states = {}

        population = []
        
        time = 0

        for line in gzopen(file):
            line = line.strip()
            
            if len(line) == 0:
                continue

            if line[0] == "#" and line[2:6] == "time":
                fields = line.split()

                print line;

                for id in xrange(2,len(fields)):
                    states[fields[id]] = id

                continue

            if line[0] == "#":
                continue

            fields = line.split()

            if fields[1] != time:
                temp = []
                temp.append(str(time))

                total = 0 

                for i in xrange(len(population)):
                   temp.append(str(population[i]))
                   total += population[i]

                temp.append(str(total))
                   
                population = [0 for i in xrange(len(states))]

                if total != 0:
                    print " ".join(temp)

            time = fields[1]

            for i in xrange(2,len(fields)):
                population[i-2] += int(fields[i])

           

if __name__ == "__main__":
    main()
