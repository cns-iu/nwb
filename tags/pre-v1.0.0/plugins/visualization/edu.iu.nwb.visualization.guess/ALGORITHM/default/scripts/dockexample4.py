# this is a network toolbar.  It will listen to incoming connections
# (only one allowed at a time) and will execute the command received
# in the interpreter
#
# to run:
# load up any graph
# execfile("scripts/dockexample4.py")
# guessnetwork()
# 
# Once running you will be able to telnet to port 2222 on the machine
# and start typing commands (one per line).  Each command
# will get evaluated against the interpreter

import java
import javax.swing
import com

# this is our toolbar

class dockexample4(com.hp.hpl.guess.ui.DockableAdapter):

	myLabel = javax.swing.JLabel("Waiting for connection")

	def __init__(self):

		# add our toolbar
		self.add(self.myLabel)
		ui.dock(self)

	def getTitle(self):
		return("dockexample4")

	def update(self,val):
		# eval or execute the command as appropriate
		self.myLabel.setText(val);
		try:
			eval(val)
		except (SyntaxError,ValueError,NameError):
			try:
				exec(val)
			except (SyntaxError,ValueError,NameError):
				self.myLabel.setText("command error "+val)
		v.repaint()
				
# extend the java thread object

class guessnetwork(java.lang.Thread):
	
	# keep a reference to our toolbar
	screeninterface = None;

	def __init__(self):
		self.screeninterface = dockexample4()

		# start the thread
		self.start()

	def run(self):
		mySocket = java.net.ServerSocket(2222) # run on port 2222
		clientSocket = mySocket.accept() # accept a connection
		inst = java.io.BufferedReader(java.io.InputStreamReader(clientSocket.inputStream))
		val = inst.readLine() # read the line, and execute:
		while (val != None):
			self.screeninterface.update(val)
			val = inst.readLine()
	
