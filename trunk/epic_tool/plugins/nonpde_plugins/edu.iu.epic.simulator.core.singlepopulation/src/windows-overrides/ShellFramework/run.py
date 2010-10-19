import os
import sys
import glob

MAX_RUNS = '100'# (must be a string)

def runSequential(args, nRuns, outVal, output_dir, frames = "none"):

    for run_num in xrange(nRuns):
        runSingle(args, run_num, outVal, output_dir, frames)

    listFile = os.path.join(output_dir, "list.out.dat")
    fp = open(listFile, "w")
    
    list = glob.glob(os.path.join(output_dir,"*.out"))
    
    for file in list:
        print >> fp, file

def runSingle(argv, run_num, outVal, output_dir, frames = "none"):
    args = argv
    args.append("-maxRuns")
    args.append(MAX_RUNS)
    args.append("-name")
    args.append(str(run_num))
    args.append("-output_frames")
    args.append(frames)

    executable = args[0]
    
    fp = open("run.args","w")
    print >> fp, " ".join(args);
    fp.close()

    try:
        return os.spawnv(os.P_WAIT, executable, args)
    except os.error:
        print >> sys.stderr, "Failed to spawnv child"
        pass
