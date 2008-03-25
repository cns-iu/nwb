# adds a slider that controls the visibility of edges based on 
# the "freq" attribute
# 
# to execute do:
# load up the sample.gdf
# execfile("scripts/dockexample2.py")
# dockexample2()

import java
import javax.swing
import com

class dockexample2(com.hp.hpl.guess.ui.DockableAdapter):

	testSlider = JSlider()
	label = JLabel("Frequency threshold (0)   ")

	def __init__(self):
		# set up the slider limits
		self.testSlider.setMinimum(freq.min)
		self.testSlider.setMaximum(freq.max + 1)
	
		# set up the slider visual properties
		self.testSlider.setMajorTickSpacing(50)
		self.testSlider.setMinorTickSpacing(10)
		self.testSlider.setPaintTicks(true)
		self.testSlider.setPaintLabels(true)	
		self.testSlider.setValue(freq.min)  # default value

		# every time the mouse is released call the "sc" event	
		self.testSlider.mouseReleased = self.sc

		# add the label and slider to the UI
		self.add(self.label)
		self.add(self.testSlider)

		# dock the new panel into the UI
		ui.dock(self)

		# call the event function once so that the
		# display matches the slider value
		self.sc(None)

	def getTitle(self):
		return("dockexample2")

	def sc(self,evt):
		# get the value
		val = self.testSlider.getValue()

		# show all the nodes
		g.nodes.visible = 1
	
		# hide all edges under value and show all over
		(freq < val).visible = 0
		(freq >= val).visible = 1

		# hide nodes not connected to visible edges
		self.hideDisconnectedNodes()

		# set the label text
		self.label.setText("Frequency threshold ("+str(val)+")")

	def hideDisconnectedNodes(self):
		# keep a list of nodes to hide
		toHide = []

	
		for nod in g.nodes:  # for all nodes
			vis = 0      # default to invisble

			# for all edges connected to this node
			# if there is any visible edge
			# keep this node visible
			for ed in nod.getOutEdges():
				if (ed.visible == 1):
					vis = 1
					break

			if (vis == 0):  # should we hide the node?
				toHide += [nod]

		# hide all the nodes we put in our list
		toHide.visible = 0  


dockexample2()
