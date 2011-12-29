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
gleam_path = None
SPEM = 'ExactEM_mac_64.x'

def main():
    global SPEM, gleam_path
    
    if not gleam_path:
        gleam_path = os.getcwd()
    
    SPEM = os.path.join(gleam_path, SPEM)
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
    
    outVal = "I"
    
    input.infections(input_file, sim_dir, False)
    input.initial(input_file, sim_dir)
    
    os.chdir(sim_dir)
    
    aggregate = []
    
    aggregate.append("basins")

    args = [SPEM, "simul.in", "-outVal", input.quoteString(outVal), "-infections", "infections.txt", "-initial", "initial.txt", "-output", input.quoteString(output_dir)]

    run.runSingle(args, 0, outVal, output_dir)
    time.sleep(2)
    stats.extract_compartments(os.path.join(output_dir,"ExactEM.0.out"), output_dir)

    # Signal successful termination and exit
    if not os.path.exists(run_end_file):
        open(run_end_file, 'w').close()
    else:
        print >> stderr, "Run was already over???"


if __name__ == "__main__":
    main()
