#!/usr/bin/env python

from sys import argv, stderr
import optparse
import os

def run(gleam_dir, run_num):
    GLEaM = gleam_dir + "/SPEM.x"

    if not os.path.exists(GLEaM):
        print >> stderr, "Unable to find SPEM.x"
        exit(4)

    args = [GLEaM,"simul.in", "-infections", "infections.txt","-name", str(run_num)]

    pid = os.fork()

    if pid == 0:
        # Child code
        os.execv(GLEaM, args)
    else:
        # Parent code

        pid_file = "run."+str(pid)+".pid"
        open(pid_file,"w").close()

        ppid = 0
        
        while ppid != pid:
            ppid, status = os.wait()

        print >> stderr, "Child", pid, "exited with status", status

        os.remove(pid_file)

def infections(file, sim_dir):
    infections_file = sim_dir + "infections.txt"

    fp = open(infections_file,"w")

    for line in open(file):
        if len(line.strip()) == 0 or line[0] == "#":
            continue
        
        fields = line.strip().split()

        if fields[0] == "infection":
            for i in xrange(1,len(fields),2):
                print >> fp, fields[i], fields[i+1]

    fp.close()
    
def main():
    p = optparse.OptionParser()
    p.add_option('--bla', '-b', default=2, help="bla")

    options, arguments = p.parse_args()

    sim_dir = argv[1]

    if not os.path.exists(sim_dir):
        print >> stderr, "Can't open simulation directory", sim_dir
        exit(1)

    if sim_dir[-1] != "/":
        sim_dir += "/"

    print >> stderr, "Running in", sim_dir

    input_file = sim_dir + "simul.in"
    model_file = sim_dir + "simul.mdl"
    cfg_file = sim_dir + "req.cfg"
    run_end_file = "run.end"
    gleam_dir = os.getcwd()

    if not os.path.exists(input_file) or not os.path.exists(model_file) or not os.path.exists(cfg_file):
        print >> stderr, "Invalid simulation directory."
        exit(2)

    nruns = 0
    comps = []

    for line in open(cfg_file):
        if len(line) == 0 or line[0] == "#":
            continue
        
        fields = line.split(":")

        if fields[0].strip() == "RUNS":
            nruns = int(fields[1].strip())
        elif fields[0].strip() == "OUTVAL":
            comps = fields[1].strip()

            if comps[-1] == ";":
                comps = comps[:-1]

            comps = comps.split(";")

    if nruns == 0 or len(comps) == 0:
        print >> stderr, "Invalid configuration file: Zero runs or no output compartments"
        exit(3)

    print >> stderr, "Running", nruns, "runs for", len(comps), "compartments"

    infections(input_file, sim_dir)

    if not os.path.exists(os.path.realpath(sim_dir)+"/data"):
        os.symlink(os.path.realpath("data"),os.path.realpath(sim_dir)+"/data")

    os.chdir(sim_dir)

    for rid in xrange(nruns):
        run(gleam_dir, rid)

    # Signal successful termination and exit
    if not os.path.exists(run_end_file):
        open(run_end_file, 'w').close() 
    else:
        print >> stderr, "Run war already over???"

if __name__ == "__main__":
    main()
