# an example to show how new menus can be added to the interface

from javax.swing import *
from java.awt import *
from java.lang import *
from com.hp.hpl.guess.ui import StatusBar

def test():
	StatusBar.setStatus("test",0)

testMenu = JMenu("Test")

testi = JMenuItem("Test")
testMenu.add(testi)
testi.actionPerformed=lambda event : test()

# uncomment the line below to add the test menu
#ui.getGMenuBar().add(testMenu)
