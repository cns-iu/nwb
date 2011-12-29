import random

columns = 240#30#0
rows = 200#25#2
dim = 529

outFile = open('random.cod', 'w')
print >> outFile, dim, 'hexa', columns, rows, 'gaussian'
for _ in range(rows * columns):
	vector = [random.random() for _ in range(dim)]
	total = sum(vector)
	
	for value in vector:
		print >> outFile, round(value / total, 6),

	print >> outFile
outFile.close()

