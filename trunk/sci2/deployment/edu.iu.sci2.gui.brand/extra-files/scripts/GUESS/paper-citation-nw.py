resizeLinear(globalcitationcount,1,50)
colorize(globalcitationcount,gray,black)
for e in g.edges:
	e.color="127,193,65,255" 
toptc = g.nodes[:]
def bytc(n1, n2):
	return cmp(n1.globalcitationcount, n2.globalcitationcount)
toptc.sort(bytc)
toptc.reverse()
toptc
for i in range(0, 20):
	toptc[i].labelvisible = true
