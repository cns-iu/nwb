#!/usr/bin/env python

from sys import argv

def parse_model(filename):

    for line in open(filename):
        line = line.strip()

        if len(line) == 0 or line[0]=="#" or line[0] == "@" or line[0:1] == "#&":
            continue
        
        fields = line.split()

        initial = fields[0]
        operation = fields[1]

        if operation == "=" or initial == "time":
            continue

        final = fields[2]
        rate = fields[3]
        agent = final

        if rate[0] == "=":
            final = fields[4]
            rate = fields[5]

        yield [initial, operation, agent, final, rate]
        

def main():
    print """digraph G {
    graph[rankdir=LR];
    edge[minlen=6 align=l];"""
    
    for line in parse_model(argv[1]):
        initial = line[0]
        operation = line[1]
        agent = line[2]
        final = line[3]
        rate = line[4]

        options = ""
        
        if operation == "--":
            options = "[";

            if agent != final:
                options += "label=\"["+agent+"] "+rate+"\""

            options += "];"
            
            print initial, "->", final, options
        else:
            options = "[label=\""+rate+"\"];"
            print initial, operation, final, options

    print "}"


if __name__ == "__main__":
    main()
