#!/usr/bin/python

class transition:
    def __init__(self):
        self.i = ""
        self.j = ""
        self.agent = ""
        self.total = ""
        self.time = 0
        self.done = False

class Transitions:
      def __init__(self, line):
          fields = line.strip().split()

          self.time = fields[-1]
          self.city = fields[-2]
          
          self.trans = []

          skip = False

          for id in xrange(len(fields) - 3):
               last = len(self.trans) - 1

               if skip == True:
                   skip = False
                   continue

               if len(self.trans) == 0 or self.trans[last].done == True:
                   self.trans.append(transition())
                   last += 1
                   
               if fields[id] == "--" or fields[id] == "->":
                   continue

               if self.trans[last].i == "":
                   self.trans[last].i = fields[id]
                   continue
               elif self.trans[last].j == "":
                   self.trans[last].j = fields[id]
                   continue
               elif self.trans[last].total == "":
                   self.trans[last].total = fields[id]
                   
                   if fields[id+1][0] == "(":
                       self.trans[last].agent = fields[id+1][1:-1]
                       skip = True
                   else:
                       skip = False
                       
                   self.trans[last].done = True
                   continue
