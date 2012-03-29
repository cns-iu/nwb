#!/usr/bin/env python

from math import ceil

class CI:
    def __init__(self, ci = 0.95):
        self.avg = 0.0
        self.data = []
        self.sorted = False
        self.ci = ci
        pass

    def _sort_(self):
        self.sorted = True
        self.data.sort()

    def append(self, value):
        self.sorted = False
        self.data.append(value)
        self.avg += value

    def ensure(self, runs):
        if runs > len(self.data):
            for i in xrange(runs-len(self.data)):
                self.append(0)

    def max(self):
        nsize = int(ceil(0.5*(1-self.ci)*len(self.data)))
    
        if not self.sorted:
            self._sort_()
    
        return self.data[-nsize]

    def min(self):
        nsize = int(ceil(0.5*(1-self.ci)*len(self.data)))
    
        if not self.sorted:
            self._sort_()
    
        return self.data[nsize]

    def mean(self):
        if len(self.data) != 0:
            return self.avg/len(self.data)
        
        return 0.0

    def size(self):
        return len(self.data)
        
    def median(self):
        if not self.sorted:
            self._sort_()
            
        runs = len(self.data)
        
        if runs%2 == 1:
            return self.data[runs/2]
        else:
            return 0.5*self.data[runs/2-1]+0.5*self.data[runs/2]

def main():
    full = [i+1 for i in xrange(999)]

    msize = 30
    nsize = 10

    bla = CI(0.95)

    for i in xrange(999):
        bla.append(full[i])
        
    count = bla.size()    
    
    #bla.ensure(1500)
    
    print bla.median()
    print bla.mean()        
    print bla.max()
    print bla.min()


if __name__ == "__main__":
    main()
