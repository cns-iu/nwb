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

def semicolonHandling(args):
    newArgs = []
    for i in range(0,len(args)):
        newArgs.append(args[i].replace(';', '\;'))

    print args
    print newArgs
    return newArgs

def runSingle(argv, run_num, outVal, output_dir, frames = "none"):
    args = argv
    args.append("-maxRuns")
    args.append(MAX_RUNS)
    args.append("-name")
    args.append(str(run_num))
    args.append("-output_frames")
    args.append(frames)
    #args = semicolonHandling(args)

    executable = args[0] 
     
    fp = open("run.args","w")
    print >> fp, " ".join(args);
    fp.close()

    pid = os.fork()
    
    if pid == 0:
        # Child code
         print os.execv(executable, args)
    else:
        # Parent code
        pid_file = "run."+str(pid)+".pid"
        open(pid_file,"w").close()
        
        ppid = 0
        
        while ppid != pid:
            ppid, status = os.wait()
        
        print >> sys.stderr, "Child", pid, "exited with status", status
        os.remove(pid_file)
