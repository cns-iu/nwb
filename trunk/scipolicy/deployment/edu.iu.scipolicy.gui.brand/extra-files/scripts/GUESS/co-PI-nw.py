n1.indegree
n2.indegree
resizeLinear(totalawardmoney, 5, 50)
colorize(directed,gray,black)
colorize(totalawardmoney,black,green)
nodesbytotalawardmoney=g.nodes[:]
def bytotalawardmoney(n1,n2):
 	return cmp(n1.totalawardmoney, n2.totalawardmoney)
nodesbytotalawardmoney.sort(bytotalawardmoney)
nodesbytotalawardmoney.reverse()
for i in range(0, 50): 
 	nodesbytotalawardmoney[i].labelvisible=true
for i in range(0, 10):
	print str(nodesbytotalawardmoney[i].label) + ": " + str(nodesbytotalawardmoney[i].totalawardmoney)