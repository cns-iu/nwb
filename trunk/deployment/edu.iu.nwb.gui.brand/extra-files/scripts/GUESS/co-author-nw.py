resizeLinear(number_of_authored_works, 1, 50)
colorize(number_of_authored_works, gray, black)
for n in g.nodes:
	# border color same as its inside color
	n.strokecolor = n.color
resizeLinear(number_of_coauthored_works, .25, 8)
colorize(number_of_coauthored_works, "127,193,65,255", black)
# make a copy of the list of all nodes
nodesbynumworks = g.nodes[:]
# define a function for comparing nodes	
def bynumworks(n1, n2):
	return cmp(n1.number_of_authored_works, n2.number_of_authored_works)
nodesbynumworks.sort(bynumworks)
# reverse sorting, list starts with highest number 	 
nodesbynumworks.reverse()
# make labels of most productive authors visible	
for i in range(0, 50):
	nodesbynumworks[i].labelvisible = true
