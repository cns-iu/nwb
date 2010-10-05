resizeLinear(totalawardmoney, 5, 50)
colorize(directed, gray, black)
colorize(totalawardmoney, black, green)
nodesByTotalAwardMoney = g.nodes[:]

def bytotalawardmoney(n1,n2):
 	return cmp(n1.totalawardmoney, n2.totalawardmoney)

nodesByTotalAwardMoney.sort(bytotalawardmoney)
nodesByTotalAwardMoney.reverse()

for i in range(0, min(nodesByTotalAwardMoney.size(), 50)): 
 	nodesByTotalAwardMoney[i].labelvisible=true

for i in range(0, min(nodesByTotalAwardMoney, 10)):
	print str(nodesByTotalAwardMoney[i].label) + ": " + str(nodesByTotalAwardMoney[i].totalawardmoney)