# generates a "simulation" of a network.  A random graph is generated 
# and layed out.  Every second a new load is placed on each edge and
# the colors of the nodes and edges will reflect this.  A toolbar at the
# bottom shows a "heartbeat" using jfreechart
#
# to run:
# load up an empty graph
# execfile("scripts/dockexample3.py")
# example3driver()

# import stuff from jfreechart (see www.jfree.org)
import java
import javax.swing
import com
import time
import org
import org.jfree

from org.jfree.chart import *
from org.jfree.chart.axis import *
from org.jfree.chart.plot import *
from org.jfree.data.time import *
from org.jfree.data.xy import *
from org.jfree.ui import *

# this is our toolbar

class dockexample3(com.hp.hpl.guess.ui.DockableAdapter):

	# need to keep some top level variables (like the
	# min and max for the chart)
	series = TimeSeries("Random Data", 
		Class.forName("org.jfree.data.time.Millisecond"))
	lastValue = 100.0
	raxis = None
	_min = 30.0
	_max = 31.0

	def __init__(self):

		# set up the jfreechart object
		dataset = TimeSeriesCollection(self.series)
	        jfc = ChartFactory.createTimeSeriesChart("Network Heartbeat",
			"Time","Value",dataset,true,true,false)
		plot = jfc.getXYPlot()
		#jfc.setLegend(None)
		axis = plot.getDomainAxis()
		axis.setAutoRange(true)
		axis.setFixedAutoRange(60000.0)
		axis = plot.getRangeAxis()
		axis.setRange(self._min,self._max) 
		self.raxis = axis
		chartPanel = ChartPanel(jfc)

		# we want to tell the GUESS ui how big to make this
		# object, and then we dock it
		chartPanel.setPreferredSize(java.awt.Dimension(600, 150))
		self.setPreferredSize(java.awt.Dimension(600, 300))
		self.add(chartPanel)
		ui.dock(self)

	def getTitle(self):
		return("dockexample3")

	def update(self,val):
		# change the min and max values on the chart
		if (val > self._max):
			self._max = val
		if (val < self._min):
			self._min = val
		self.raxis.setRange(self._min,self._max)

		# add a new value to the heart monitor
               	self.series.addOrUpdate(Millisecond(), val)
		
				
# extend the java thread object
class example3driver(java.lang.Thread):
	
	# keep a reference to our hearbeat monitor
	heartbeat = None;

	def __init__(self):
		makeSimpleRandom(40,50)    # make a random graph
		centerAfterLayout(false)   # turn off centering after layout
		setSynchronous(true)       # make layouts run in same thread
		gemLayout()                # initial layout
		binPackLayout()            # pack all the subgraphs together
		rescaleLayout(1.2)         # make it a bit larger

		# create new node and edge fields for the load
		addEdgeField("load",Types.DOUBLE,20.0)
		addNodeField("load",Types.DOUBLE,20.0)

		# create a new heartbeat toolbar
		self.heartbeat = dockexample3()

		# force GUESS to calculate the outdegree
		nd1.outdegree

		# make the background dark gray
		setDisplayBackground(darkgray)

		# for every node make its label the same as 
		# its name and change it to a slightly different style
		for _n in g.nodes:
			_n.label = _n.name
			_n.style = 6

		# start the thread
		self.start()

	def run(self):
		center()  # center once

		# run the simulation for 10000 seconds
		for _i in range(1,10000):
			_load = self.getLoad()        # get the load
			Thread.sleep(1000)            # sleep
			self.heartbeat.update(_load)  # update the monitor
			colorize(Edge.load,green,red) # color the edges
			self.colorNodes()             # color the nodes
			v.repaint()                   # force a repaint now!
	
	def colorNodes(self):

		# for every node, figure out the average load on
		# all its edges, set the load property to that average
		for _n in g.nodes:
			_avgedgeload = 0
			for _e in _n.getOutEdges():
				_avgedgeload += _e.load
			if _n.outdegree > 0:
				_avgedgeload = _avgedgeload / _n.outdegree
			_n.load = _avgedgeload

		# color the nodes from green to red
		colorize(Node.load,green,red)

	def getLoad(self):
		# generate a random new load on the network
		for _e in g.edges:
			_e.load = Math.random() * 50
		return load.avg

example3driver()