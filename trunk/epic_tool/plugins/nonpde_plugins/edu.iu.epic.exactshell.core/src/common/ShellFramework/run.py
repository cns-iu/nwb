import os
import sys
import glob

MAX_RUNS = '100'# (must be a string)

def runSequential(gleam_dir, nRuns, outVal, output_dir, frames = "none", GLEaM = "GLEaM.x"):

    for run_num in xrange(nRuns):
        runSingle(gleam_dir, run_num, outVal, output_dir, frames, GLEaM)

    listFile = os.path.join(output_dir, "list.out.dat")
    fp = open(listFile, "w")
    
    list = glob.glob(os.path.join(output_dir,"*.out.dat.gz"))
    
    for file in list:
        print >> fp, file

def runSingle(gleam_dir, run_num, outVal, output_dir, frames = "none", GLEaM = "GLEaM.x"):
    
    args = [GLEaM, "simul.in", "-outVal", outVal, "-infections", "infections.txt",
            "-nonTravel", "NT.txt","-name", str(run_num), "-maxRuns", MAX_RUNS,
            "-initial", "initial.txt", "-output", output_dir, "-rho", "rho.txt", "-output_frames"]
    
    args.append(frames)
    
    #print " ".join(args)
    
    fp = open("run.args","w")
    print >> fp, " ".join(args);
    fp.close()

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
        
        print >> sys.stderr, "Child", pid, "exited with status", status
        os.remove(pid_file)
