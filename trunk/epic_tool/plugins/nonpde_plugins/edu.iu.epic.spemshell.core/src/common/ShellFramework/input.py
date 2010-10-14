import os
import sys

def nonTravel(file, sim_dir):
    NT_file = os.path.join(sim_dir,"NT.txt")
    
    fp = open(NT_file,"w")
    
    for line in open(file):
        if len(line.strip()) == 0:
            continue
        
        fields = line.strip().split()
        
        if len(fields) >= 3 and fields[0] == "#" and fields[1] == "nonTravel:":
            if fields[2].lower() != "none":
                for c in fields[2:]:
                    print >> fp, c
    
    fp.close()

def getCompartments(file):
    comps = set([])
    
    
    for line in open(file):
        fields = line.strip().split()
        
        if len(fields) >= 4:
            if fields[1] == "--" or fields[1] == "->":
                comps.add(fields[0])
                comps.add(fields[2])
                
                if fields[3] == "=":
                    comps.add(fields[4])
    
    return comps

def nonCommute(model_file, sim_dir):
    rho_file = os.path.join(sim_dir,"rho.txt")
    
    comps = getCompartments(model_file)
    
    rho = {}
    
    for c in comps:
        rho[c] = 1.0
    
    fp = open(rho_file,"w")
    
    for line in open(model_file):
        if len(line.strip()) == 0:
            continue
        
        fields = line.strip().split()
        
        if len(fields) >= 3 and fields[0] == "#" and fields[1] == "nonCommute:":
            if fields[2].lower() != "none":
                for c in fields[2:]:
                    rho[c] = -1.0
    
    for c in comps:
        print >> fp, c, rho[c]
    
    fp.close()


def infections(file, sim_dir, multiple=True):
    infections_file = os.path.join(sim_dir,"infections.txt")
    
    fp = open(infections_file,"w")
    
    for line in open(file):
        if len(line.strip()) == 0 or line[0] == "#":
            continue
        
        fields = line.strip().split()
        
        if fields[0] == "infection":
            for i in xrange(2,len(fields),2):
                if multiple:
                    print >> fp, fields[1], fields[i+1], fields[i]
                else:
                    print >> fp, fields[i-1], fields[i]
    
    fp.close()

def initial(file, sim_dir):
    initial_file = os.path.join(sim_dir, "initial.txt")
    
    fp = open(initial_file,"w")
    
    norm = 0
    
    for line in open(file):
        if len(line.strip()) == 0 or line[0] == "#":
            continue
        
        fields = line.strip().split()
        
        if fields[0] == "initial":
            for i in xrange(1,len(fields),2):
                print >> fp, fields[i], fields[i+1]
                norm += float(fields[i+1])
    
    if norm!=100 and norm!=1.0:
        print >> sys.stderr, "Initial population does not sum up to 100%!"
        sys.exit(10)
    
    fp.close()


def parameters(file):
    count = 0
    
    for line in open(file):
        fields = line.strip().split()
        
        if len(fields) == 0 or fields[0][0] == "#":
            continue
        
        if fields[0] == "days":
            count += 1
            runTime = int(fields[1])
        elif fields[0] == "date":
            count += 1
            startDate = fields[1]
    
    if count != 2:
        print >> sys.stderr, "Error parameters not found!"
        sys.exit(3)
    
    return runTime, startDate

def seasonality(alpha_max, alpha_min):
    fp = open("hemisphereseason.txt","w")

    TmaxN = 15
    TmaxS = TmaxN + 183

    print >> fp, 0, 0, 0, 0

    for t in xrange(1,3*365+1):
        SN = ((alpha_max-alpha_min)*sin(2 * Pi * (t-TmaxN)/365 + Pi / 2)+(alpha_max+alpha_min))/2.0
        SS = ((alpha_max-alpha_min)*sin(2 * Pi * (t-TmaxS)/365 + Pi / 2)+(alpha_max+alpha_min))/2.0

        print >> fp, t, SS, 1.0, SN
