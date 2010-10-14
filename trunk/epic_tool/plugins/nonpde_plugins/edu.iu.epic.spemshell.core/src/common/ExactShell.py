#!/usr/bin/env python

from sys import argv, stderr
import optparse
import os
from math import floor, ceil, sin
from math import pi as Pi
import datetime
import glob
import time

from ShellFramework import run, stats, input

# Config
data_path = '/Users/bgoncalves/progs/EpiC/data'
gleam_path = '/Users/bgoncalves/progs/EpiC/'

SPEM = os.path.join(gleam_path, 'ExactEM.x')

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
        print >> stderr, "ExactEM not found!"
        exit(1)
    
    if not os.path.exists(input_file) or not os.path.exists(model_file):
        print >> stderr, "Invalid simulation directory."
        exit(2)
    
    nruns = 0
    comps = []
    
    outVal = ""
    
    input.infections(input_file, sim_dir, False)
    input.initial(input_file, sim_dir)
    #runTime, startDate = input.parameters(input_file)
    
    os.chdir(sim_dir)
    
    aggregate = []
    
    aggregate.append("basins")

    run.runSingle(gleam_path, 0, outVal, output_dir, GLEaM = SPEM)
    time.sleep(2)
    stats.extract_compartments(os.path.join(output_dir,"ExactEM.0.out.dat.gz"), output_dir)

    # Signal successful termination and exit
    if not os.path.exists(run_end_file):
        open(run_end_file, 'w').close()
    else:
        print >> stderr, "Run was already over???"


if __name__ == "__main__":
    main()
