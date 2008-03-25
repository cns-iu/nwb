# work in progress

import java
import javax.swing
import com

# this is our toolbar

class dockexample5int(com.hp.hpl.guess.ui.DockableAdapter):

	myLabel = javax.swing.JLabel("Setting up...")

	def __init__(self):
		self.add(self.myLabel)
		ui.dock(self)

	def getTitle(self):
		return("dockexample5")

	def update(self,val):
		self.myLabel.setText(val);
		v.repaint()
		
class dockexample5(java.lang.Thread):
	
	screeninterface = None;

	def __init__(self,imageN="images/map.jpg"):
		self.screeninterface = dockexample5int()

		setBackgroundImage(imageN)

		# start the thread
		self.start()

	def run(self):
		g.nodes.visible = false

		# grab the first and last nodes, we'll use those to
		# orient ourselves
		_n1 = g.nodes[0]
		_n2 = g.nodes[len(g.nodes)-1]

		# we want to ignore the last place the user clicked on 
		# and then ask them to click again
		nullClick = v.getLastClickedPosition()


		self.screeninterface.update("Please click on the position for " + _n1.toString())
		firstClick = v.getLastClickedPosition()

		while (firstClick is nullClick):
			Thread.sleep(100)            # sleep
			firstClick = v.getLastClickedPosition()


		__tempN = "t"+str(System.currentTimeMillis())
		__tempN = addNode(__tempN,label=str(_n1),labelvisible=true,x=firstClick.getX(),y=firstClick.getY(),style=2,color=red)

		self.screeninterface.update("Please click on the position for " + _n2.toString())
		secondClick = v.getLastClickedPosition()

		# wait until there's a new value for second click
		# This may change if we go to the Java event mechanism
		# but this seems easier for now
		while (secondClick is firstClick):
			Thread.sleep(100)            # sleep
			secondClick = v.getLastClickedPosition()
		removeNode(__tempN);
		self.screeninterface.update("updating...")
		Thread.sleep(500)

		centerAfterLayout(false)   # turn off centering after layout
		setSynchronous(true)       # make layouts run in same thread

		xscale = 1
		if _n1.x != _n2.x:
			xscale = (firstClick.getX() - secondClick.getX()) / (_n1.x - _n2.x)

		yscale = 1
		if _n1.y != _n2.y:
			yscale = (firstClick.getY() - secondClick.getY()) / (_n1.y - _n2.y)

		rescaleLayout(xscale,yscale)

		xtrans = firstClick.getX() - _n1.x
		ytrans = firstClick.getY() - _n1.y

		for _n in g.nodes:
			_n.x += xtrans
			_n.y += ytrans

		g.nodes.visible = true

		ui.close(self.screeninterface)
		centerAfterLayout(true)
		setSynchronous(false)  
	
#dockexample5()
