# adds a new button to the interface which centers the display
# to execute do:
# execfile("scripts/dockexample1.py")
# dockexample1()

import java
import javax
import com

class dockexample1(com.hp.hpl.guess.ui.DockableAdapter):

	def __init__(self):
		testButton = javax.swing.JButton("center")
		action = lambda event: v.center()
		testButton.actionPerformed = action
		self.add(testButton)
		ui.dock(self)

	def getTitle(self):
		return("dockexample1")

dockexample1()