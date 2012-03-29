# Recipe 425044: Splitting up a sequence 
def split_seq(seq,size):
    """ Split up seq in pieces of size """
    return [seq[i:i+size] for i in range(0, len(seq), size)]



inFile = open('nih_2007_doc_topic_prob.dat', 'r')
inFile.next()
for line in inFile:
    if line.startswith('#'):
        continue
    tokens = line.split()
    valuepairs = tokens[1:]
    total = 0.0
    for index, value in split_seq(valuepairs, 2):
        total += float(value)
    print total
    
inFile.close()
