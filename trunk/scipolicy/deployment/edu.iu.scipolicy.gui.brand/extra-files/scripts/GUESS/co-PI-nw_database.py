resizeLinear(total_award_amount, 5, 50)
colorize(directed,gray,black)
colorize(total_award_amount,black,green)
nodesbytotalawardmoney=g.nodes[:]
def bytotalawardmoney(n1,n2):
	return cmp(n1.total_award_amount, n2.total_award_amount)
nodesbytotalawardmoney.sort(bytotalawardmoney)
nodesbytotalawardmoney.reverse()
for i in range(0, 50): 
 	nodesbytotalawardmoney[i].labelvisible=true
for i in range(0, 10):
	print str(nodesbytotalawardmoney[i].label) + ": " + str(nodesbytotalawardmoney[i].total_award_amount)