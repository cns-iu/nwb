resizeLinear(globalcitationcount,2,40)
colorize(globalcitationcount,(200,200,200),(0,0,0))
resizeLinear(weight,.25,8)
colorize(weight, "127,193,65,255", black)
for n in g.nodes:
	n.strokecolor=n.color
toptc = g.nodes[:]
def bytc(n1, n2):
	return cmp(n1.globalcitationcount, n2.globalcitationcount)
toptc.sort(bytc)
toptc.reverse()
toptc
for i in range(0, 20):
	toptc[i].labelvisible = true
