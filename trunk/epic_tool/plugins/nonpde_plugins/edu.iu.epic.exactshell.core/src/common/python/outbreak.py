#!/usr/bin/python 

from sys import argv
from gzip import open as gzopen
from airports import *
from transitions import *

def main():
    
    cities = Airports(argv[1])

    for file in argv[2:]:
        aCities = set([])
        aCountries = set([])
    
        infected = {}

        for line in gzopen(file):
            line = line.strip()
            
            if len(line) == 0:
                continue

            if line[0] == "#":
                continue

            trans = Transitions(line)
                        
            fields = line.split()

            city = trans.city
            time = trans.time

            aCities.add(city)
            aCountries.add(cities.country(city))

            for t in trans.trans:
                if t.i == "L" and (t.j == "It" or t.j == "Int"):
                    if city not in infected:
                        infected[city] = {"time" : time }
                        
                    if "count" in infected[city]:
                        total = infected[city]["count"] + int(t.total)
                    else:
                        total = int(t.total)

                    infected[city]["count"] = total


        print file, len(aCities), len(aCountries)


if __name__ == "__main__":
    main()
