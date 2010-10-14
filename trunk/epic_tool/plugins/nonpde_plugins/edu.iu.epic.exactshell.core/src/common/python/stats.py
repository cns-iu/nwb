#!/usr/bin/env python

from sys import argv
from gzip import open as gzopen
from math import sqrt

def average(data):
    sum = 0

    for i in xrange(len(data)):
        sum+= data[i] 

    return sum/float(len(data))
    

def std(data):
    sum = average(data);
    
    d2 = [data[i]*data[i] for i in xrange(len(data))]

    sum2 = average(d2)

    return sqrt(sum2-sum*sum)

def median(data):
    data.sort()

    mid = int(len(data)/2.0)

    if len(data)%2 == 0:
        m = 0.5*data[mid]+0.5*data[mid+1]
    else:
        m = data[mid]

    return m

def getdata(argv):
    files = []
    data = []
    max = 0

    ops = argv[1].split("+")

    if len(ops) == 1:
        index = int(argv[1])
    else:
        index1 = int(ops[0])
        index2 = int(ops[1])

    for filename in argv[2:]:
        files.append(filename)
        file = len(files)-1
        data.append([])

        print "#",len(files),filename

        if filename[-3:] == ".gz":
            f = gzopen(filename)
        else:
            f = open(filename)
            
        for line in f:
            if len(line) == 0:
                continue

            if line[0] == "#" or line[0] == "@":
                continue

            fields = line.strip().split()
            
            if len(data[file]) <= int(fields[0]):
                data[file].extend([0 for  i in xrange(int(fields[0])-len(data[file])+1)])
                

            if len(ops) == 1:
                data[file][int(fields[0])] = float(fields[index])
            else:
                data[file][int(fields[0])] = float(fields[index1])+float(fields[index2])

            if len(data[file]) > max:
                max = len(data[file])
                
    return data

def statistics(data):
    def ninetyfive(values):
        values.sort()

        n95 = int(0.05*len(values)+1.0)

        d05 = values[n95]
        d95 = values[len(values) - n95]
        
        return (d05, d95)

    print "#", len(data)
    
    avg = [0 for i in xrange(len(data[0]))]
    stderr = [0 for i in xrange(len(data[0]))]
    med = [0 for i in xrange(len(data[0]))]
    n95 = [0 for i in xrange(len(data[0]))]

    try:
        for i in xrange(len(data[0])):
            avg[i] = average([data[j][i] for j in xrange(len(data))])
            stderr[i] = std([data[j][i] for j in xrange(len(data))])
            med[i] = median([data[j][i] for j in xrange(len(data))])
            n95[i] = ninetyfive([data[j][i] for j in xrange(len(data))])
    except IndexError, e:
        print e
        exit()

    

    return [avg, stderr, med, n95]
