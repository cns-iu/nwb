g.nodes.labelvisible=true
for node in g.nodes:
	node.x=node.x*10
	node.y=node.y*10
(label=="psychiatrist").color=red
(label=="med dir, inpt. unit").color=red
(label=="nurse").color=blue
clusts = groupBy(area)
for c in clusts: createConvexHull(c,randomColor(50))