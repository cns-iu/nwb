#!/usr/bin/env python

import datetime
import os
from math import ceil, floor
from sys import stderr
from gzip import open as gzOpen
from CI import CI

def stats(startDate, time, runs, filename, comp, aggregate, index, output_data):
    dataFile = []
    data = {}
    
    for line in open(filename):
        if len(line.strip()) == 0 or line[0] == "#":
            continue
        
        fields = line.strip().split()
        
        id = fields[0]
        
        if id not in data:
            data[id] = {}
            
            data[id]["data"] = []
            data[id]["cumul"] = []
        
        value = float(fields[1])
        cumul = float(fields[2])
        
        data[id]["data"].append(value)
        data[id]["cumul"].append(cumul)
    
    n95 = int(ceil(0.5*(1-0.95)*runs))
    
    fields = startDate.split("/")
    
    day = datetime.date(int(fields[2]),int(fields[0]),int(fields[1]))
    one_day = datetime.timedelta(days=1)
    
    day += one_day*time
    
    filename = day.strftime("step-%Y-%m-%d")
    filename = os.path.join(output_data, "%s.%u.%s.dat" % (filename, index, comp))
    
    fp = open(filename,"w")
    
    for id in data:
        if len(data[id]["data"]) < runs:
            for i in xrange(len(data[id]["data"]),runs):
                data[id]["data"].append(0);
                data[id]["cumul"].append(0);
        
        d = data[id]["data"]
        c = data[id]["cumul"]
        
        d.sort()
        c.sort()
        
        pos = int(floor(runs/2.0))
        
        if runs%2 == 0:
            medData = d[pos]
            medCumul = c[pos]
        else:
            medData = d[pos]*0.5 + d[pos+1]*0.5
            medCumul = c[pos]*0.5 + c[pos+1]*0.5
        
        line = []
        
        line.append(str(medData))
        line.append(str(d[n95]))
        line.append(str(d[-n95]))
        line.append(str(medCumul))
        line.append(str(c[n95]))
        line.append(str(c[-n95]))
        
        print >> fp, id, " ".join(line)

def extract_compartments(filename, output_data):
    Found = False
    files = {}
    
    for line in gzOpen(filename):
        fields = line.strip().split()
        
        if len(fields) == 0:
            continue

        # Found compartment list
        if fields[0] == "#" and fields[1] == "time":
            if not Found:
                Found = True
                print >> stderr, "Found %u compartments:\n %s" % (len(fields)-2, "\n".join(fields[2:]))

                for i in xrange(2,len(fields)):
                    files[i-1] = open(os.path.join(output_data, "comp."+fields[i]+".dat"),"w")

            continue
            
        
        if Found:
            for i in xrange(1,len(fields)):
                print >> files[i], fields[0], fields[i]
        
def compartments(filename, output_data):
    Found = False
    data = {}
    files = {}
    
    for file in open(filename):
        print >> stderr, "Reading %s..." % file.strip()

        for line in gzOpen(os.path.join(output_data, file.strip())):
            fields = line.strip().split()

            if len(fields) == 0:
                continue

            # Found compartment list
            if fields[0] == "#" and fields[1] == "time":
                if not Found:
                    Found = True
                    print >> stderr, "Found %u compartments:\n %s" % (len(fields)-2, "\n".join(fields[2:]))

                    for i in xrange(2,len(fields)):
                        files[i-1] = open(os.path.join(output_data, "comp."+fields[i]+".dat"),"w")
                        data[i-1] = {}#[CI(0.95) for j in xrange(time + 1)]

                continue
                
            if fields[0][0] == "#" or len(line.strip()) == 0:
                continue

            if len(fields) == len(files) + 1:
                for i in xrange(1, len(fields)):
                    data[i].setdefault(int(fields[0]), CI(0.95))
                    data[i][int(fields[0])].append(int(fields[i]))
                    
    for key in data.keys():
        for time in data[key].keys():
            print >> files[key], time, data[key][time].mean(), data[key][time].min(), data[key][time].max()

if __name__ == "__main__":
    compartments("output/list.dat", "output")
