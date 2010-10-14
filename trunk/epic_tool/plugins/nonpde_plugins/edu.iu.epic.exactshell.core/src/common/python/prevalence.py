#!/usr/bin/python 

from sys import argv
from gzip import open as gzopen
from airports import *
from transitions import *

def main():

    population = []

    for line in gzopen(argv[1]):
        line = line.strip()
        
        if len(line) == 0:
            continue
        
        if line[0] == "#":
            continue
        
        fields = line.split()
        
        if len(population)<= fields[1]:
            population.extend([0 for i in xrange(len(fields)-2)] for i in xrange(len(population), int(fields[1])+1))
            
        for i in xrange(2, len(fields)):
            population[int(fields[1])][i-2]+=int(fields[i])


        
    for i in xrange(len(population)):
        line = [str(population[i][j]) for j in xrange(len(population[i]))]

        print i, " ".join(line)

        

if __name__ == "__main__":
    main()
