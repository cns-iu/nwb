import fileinput

#inFile = open()
for line in fileinput.input('topics.csv',):
    if fileinput.isfirstline():
        continue
    cnt, tid, label = line.split(',')[:3]

    print '#', tid, label
    
inFile.close()
