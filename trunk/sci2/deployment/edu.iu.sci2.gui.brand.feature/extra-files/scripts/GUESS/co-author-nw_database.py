resizeLinear(number_of_authored_works,1,50)
colorize(number_of_authored_works,gray,black)
for n in g.nodes:
	n.strokecolor = n.color 	# border color same as its inside color
resizeLinear(number_of_coauthored_works, .25, 8) 
colorize(number_of_coauthored_works, "127,193,65,255", black) 
nodesbynumworks = g.nodes[:] 		# make a copy of the list of all nodes
def bynumworks(n1, n2): 		# define a function for comparing nodes
	return cmp(n1.number_of_authored_works, n2.number_of_authored_works) 
nodesbynumworks.sort(bynumworks) 	# sort list 
nodesbynumworks.reverse() 		# reverse sorting, list starts with highest #
for i in range(0, 50): 		# make labels of most productive authors visible 
	nodesbynumworks[i].labelvisible = true