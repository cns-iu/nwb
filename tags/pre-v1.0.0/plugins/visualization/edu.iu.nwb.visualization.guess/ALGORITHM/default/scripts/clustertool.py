import java
import javax
import com

# Implements Wu/Huberman algorithm for an unweighted version of the network
from edu.uci.ics.jung.algorithms.cluster import VoltageClustererL
from com.hp.hpl.guess.jung import NEVMap

# user either asks for the community around a node
# or to break up the graph into clusters
# a slider is used to specify the number of clusters
# the algorithm will return somewhere between half and double the number of requested clusters
# Author: Lada Adamic
# Modified by Eytan Adar (6/8/2005)
class clustertool(com.hp.hpl.guess.ui.DockableAdapter):

	num_candidates = (int)(g.nodes.__len__())  # how many pairs of nodes to select for voltage assignment
						     # the higher this number, the more accurate and consistent
						     # the clustering. here it is set to 1/2 of the number of
						     # nodes

						 
	rank_iterations = 1000;	  # maximum number of iterations in solution of Kirchoff's equations
	                          
	rank_convergence = 0.001;  # change in voltage on any node must be below this amount in order for
	                           # the voltage calculation to terminate. the smaller the number, the
				   # greater the precision, but also the longer the algorithm takes

	# instantiate Java class that will do the clustering		   
	vc = VoltageClustererL(num_candidates,rank_iterations,rank_convergence)  

	clusters = ()
	
	# add buttons, a slider, and text fields to GUI
	field = javax.swing.JTextField(preferredSize=(210,20))
	vertexname = javax.swing.JTextField(preferredSize=(50,20))

	numclustersSlider = javax.swing.JSlider()

	# by default aim for 4 clusters (may get less than 4, or up to double)
	label = javax.swing.JLabel("4 clusters")
	iterations = javax.swing.JTextField()

	clusterButton = javax.swing.JButton("cluster graph")
	communityButton = javax.swing.JButton("find community")
	outputButton = javax.swing.JButton("output clusters")

	def __init__(self):

		# uncomment the following line (and change it if you like) to use edge weights
		# the default will use a weight of 1, and the following line (unchanged) will
		# use the "weight" field
		self.vc.setEdgeWeight(NEVMap(weight))

		g.setSynchronous(true);
		ui.enableButtons(true);

		maxnumclusters = (int)(g.nodes.__len__()/4);
		# set up the slider limits
		self.numclustersSlider.setMinimum((int)(2))
		self.numclustersSlider.setMaximum((int)(maxnumclusters))
		self.numclustersSlider.setValue((int)(4))
		self.numclustersSlider.setPaintTicks(true)
		self.numclustersSlider.setPaintLabels(true)
		self.numclustersSlider.setMajorTickSpacing(25)
		self.numclustersSlider.setMinorTickSpacing(5)
		#set up action buttons
		self.clusterButton.actionPerformed = self.buttonPressed

		self.communityButton.actionPerformed = self.buttonPressed
		self.outputButton.actionPerformed = self.buttonPressed

		# every time the mouse is released call the "sc" event	
		self.numclustersSlider.mouseReleased = self.sc

		# add the label and buttons to the UI
		self.add(self.label)
		self.add(self.numclustersSlider)
		self.add(self.clusterButton)
		self.add(self.vertexname)
		self.add(self.communityButton)
		self.add(self.field)
		self.add(self.outputButton)

		# dock the new panel into the UI
		ui.dock(self)

		# call the event function once so that the
		# display matches the slider value
		self.sc(None)

		# center the display
		v.center()

		# if user clicks on node, 
		com.hp.hpl.guess.ui.VisFactory.getFactory().shiftClickNode = self.clickCommunity

	def clickCommunity(self,_node):
			self.vertexname.text = _node.name
			self.findCommunity()

	def getTitle(self):
		return("clustertool")

	def buttonPressed(self,event):
	  	# check if user wants the whole graph clustered, or to find the community around a node
		self.field.text = event.source.text
		if (event.source.text == "cluster graph"):
			self.field.text = "finding clusters..."
			self.clustertheGraph()
		if (event.source.text == "find community"):
			self.findCommunity()
		if (event.source.text == "output clusters"):
			self.outputClusters()


	def clustertheGraph(self):
	  	#reset appeareance of nodes
		g.nodes.style=2
		g.edges.width=0.3
		g.nodes.color='darkgray'
#		g.nodes.size=20

		# colors to assign to clusters
		colorlist = ['blue','red','turquoise','yellow','green','plum','yelloworange','midnightblue','0,0,75','yellowgreen','wildstrawberry','0,75,0','violet','255,102,0','175,0,175','brown']

		# call the clustering method, asking for the number of clusters specified by the slider
		self.clusters = self.vc.cluster(g,self.numclustersSlider.getValue())

		# get the number of clusters found and report to user
		clustnum = self.clusters.__len__() - 1
	 	tmp = ""+java.lang.String.valueOf(clustnum)+" clusters found (lightgray is 'leftovers')\n"
		self.field.text = tmp;

		g.edges.color='darkgray'
		g.edges.width=0.3

		# color the clusters
		i = 0
		for z in self.clusters:
			if (i == clustnum): rcolor = 'lightgray'
			elif (i < 16): rcolor = colorlist[i]
			else: rcolor = randomColor()
#			print "cluster ",i," with ",z.__len__()," members and color ",rcolor,"\n";

			z.color=rcolor
			(z-z).color=rcolor
			(z-z).width=0.4
			i = i+1


	def outputClusters(self):
		chooser = JFileChooser()
		
		returnVal = chooser.showOpenDialog(None)
		if (returnVal == JFileChooser.APPROVE_OPTION): myfile = chooser.getSelectedFile().getName()
		s1 = repr(chooser.getCurrentDirectory())+'/'+myfile
		output = open(s1,'w')
		print s1,'\n'

		i = 0
		for z in self.clusters:
		  	i = i + 1
			s="cluster "+repr(i)+':'
			output.write(s);
			for clusternode in z:
	  			s = " "+clusternode.name
  				output.write(s)
			output.write('\n')	

		output.close()
			
	#find community around a node
	def findCommunity(self):
	  	#reset appearance of nodes
		nodeset = (name == self.vertexname.text)
		g.nodes.style=2
#		g.nodes.size=10
		g.nodes.color='lightgray'
		g.edges.width=0.3
		g.edges.color='lightgray'
		if (nodeset.__len__() == 1):
			self.field.text = "finding community for node "+self.vertexname.text+"\n"
			for mynode in nodeset:
				self.clusters = self.vc.getCommunity(g,mynode,12)

				colorlist = ['blue','red','turquoise','yellow','green','plum','yelloworange','midnightblue','0,0,75','yellowgreen','wildstrawberry','0,75,0','violet','255,102,0','175,0,175','brown']
				clustnum = self.clusters.__len__() - 1
				print clustnum , " clusters found (lightgray cluster is 'garbage')\n"

				i = 0
				for z in self.clusters:
					if (i == clustnum): rcolor = 'lightgray'

					if (i == 0): 
						csize = z.__len__()
						(z-z).width=0.5
						z.color='blue'
						(z-z).color='blue'
	 					tmp = ""+java.lang.String.valueOf(csize)+" nodes in community\n"
						self.field.text = tmp;
					i = i+1

				mynode.size = 10
				mynode.color='red'

		else:
			self.field.text =  "can't figure out which node you mean\n"

	# read the value of the cluster
	def sc(self,evt):
		val = self.numclustersSlider.getValue()
		# set the label text
		self.label.setText(str(val)+" clusters")


