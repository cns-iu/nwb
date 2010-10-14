#!/usr/bin/env python

from sys import argv, stderr
import optparse
import os
from math import floor, ceil, sin
from math import pi as Pi
import datetime
import glob

from ShellFramework import run, stats, input

# Config
data_path = '/Users/kongch/Desktop/EpiC-SPEM/SPEM/data/'
gleam_path = '/Users/kongch/Desktop/EpiC-SPEM/SPEM/'

SPEM = os.path.join(gleam_path, 'SPEM')

def main():
    sim_dir = os.path.join(os.getcwd(),argv[1])
    
    if not os.path.exists(sim_dir):
        print >> stderr, "Can't open simulation directory", sim_dir
        exit(1)
    
    print >> stderr, "Running in", sim_dir
    
    input_file = os.path.join(sim_dir, "simul.in")
    model_file = os.path.join(sim_dir, "simul.mdl")
    cfg_file = os.path.join(sim_dir, "req.cfg")
    run_end_file = os.path.join(sim_dir, "run.end")
    frames_dir = os.path.join(sim_dir, "frames")
    output_dir = os.path.join(sim_dir,"output")
    
    if not os.path.exists(SPEM):
        print >> stderr, "SPEM not found!"
        exit(1)
    
    if not os.path.exists(input_file) or not os.path.exists(model_file) or not os.path.exists(cfg_file):
        print >> stderr, "Invalid simulation directory."
        exit(2)
    
    nruns = 0
    comps = []
    
    outVal = ""
    
    for line in open(cfg_file):
        if len(line) == 0 or line[0] == "#":
            continue
        
        fields = line.split(":")
        
        if fields[0].strip() == "RUNS":
            nruns = int(fields[1].strip())
        elif fields[0].strip() == "OUTVAL":
            comps = fields[1].strip()
            outVal = fields[1].strip()
            
            if comps[-1] == ";":
                comps = comps[:-1]
            
            comps = comps.split(";")
    
    if nruns == 0 or len(comps) == 0:
        print >> stderr, "Invalid configuration file: Zero runs or no output compartments"
        exit(3)
    
    print >> stderr, "Running", nruns, "runs for", len(comps), "compartments"
    
    if not os.path.exists(os.path.realpath(sim_dir)+"/data"):
        os.symlink(os.path.realpath("data"),os.path.realpath(sim_dir)+"/data")

    infection_file = input.infections(input_file, sim_dir, False)
    initial_file = input.initial(input_file, sim_dir)
    runTime, startDate = input.parameters(input_file)
    
    os.chdir(sim_dir)
    
    for line in open(input_file):
        fields = line.strip().split()

        if len(fields) == 2 and fields[0] == "alphamin":
            alphamin = float(fields[1])

            input.seasonality(1.1, alphamin)
            print >> stderr, "Found seasonal factor",alphamin
            break

    aggregate = []
    
    aggregate.append("basins")

    if nruns == 1:
        run.runSingle(gleam_path, input_file, model_file, initial_file, infection_file, 0, outVal, output_dir, GLEaM = SPEM)

        fp = open(os.path.join(output_dir, "outVal.dat"), "w")

        for time in xrange(runTime+1):
            filename = os.path.join(output_dir, "sum." + aggregate[0] + "." + str(time) + ".dat")
            for line in open(filename):
                fields = line.strip()
                
                print >> fp, time, fields[1:]
        stats.extract_compartments(os.path.join(output_dir,"SPEM.0.out.dat.gz"), output_dir)
        
    else:
        run.runSequential(gleam_path, nruns, outVal, output_dir, GLEaM=SPEM)
        
        print >> stderr, "Running aggregation"
            
        for a in xrange(len(aggregate)):
            agg = aggregate[a]
                
            file = os.path.join(sim_dir, "output/")
                
            for time in xrange(runTime+1):
                #                for comp in comps:
                filename = file + "sum." + agg + "." + str(time) + ".dat"
                stats.stats(startDate, time, nruns, filename, "sum", agg, a+1, output_dir)
        
        list = glob.glob(os.path.join(output_dir,"step-*.1.sum.dat"))
        
        filename = os.path.join(output_dir, "outVal.dat")
        
        fp = open(filename, "w")
        
        count = 0
        
        for file in list:
            for line in open(file):
                fields = line.strip()
                
                print >> fp, count, fields[1:]
                count += 1
        
        stats.compartments(os.path.join(output_dir,"list.out.dat"), output_dir)
    
    # Signal successful termination and exit
    if not os.path.exists(run_end_file):
        open(run_end_file, 'w').close()
    else:
        print >> stderr, "Run was already over???"


if __name__ == "__main__":
    main()
