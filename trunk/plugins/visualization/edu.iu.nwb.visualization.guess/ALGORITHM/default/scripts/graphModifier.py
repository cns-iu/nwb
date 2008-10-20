import java.awt.event
import javax.swing
import javax.swing.text
import java.util.Vector
import java
from java.io import File
from java.io import BufferedWriter
from java.io import FileWriter
from java.lang import String
import com
import java.util.regex
import sys
import math

# Graph Modifier
# By: Jeffrey Wong and Bernie Hogan
# Version: 1.0 Release
# Date: March 15, 2006
# An extension to GUESS that enables the user to modify the graph via a GUI instead
# of the command prompt
# Auto Completion JComboBox was created with help from this tutorial: http://www.orbital-computer.de/JComboBox/
# Used under the following terms of this Creative Commons License: http://creativecommons.org/licenses/by-nc/2.5/
# Created in association with Netlab, University of Toronto. (http://www.chass.utoronto.ca/~wellman)
# For questions, comments, or inquiries, please contact Jeffrey (jeffo.wong@gmail.com) or Bernie (bernie.hogan@utoronto.ca)

######################### BEGIN GLOBAL VARIABLES ###############################
# general choices that allows you to select all nodes and/or edges
general = ["everything","all nodes", "all edges", "nodes based on ->", "edges based on ->"]

# constant variables of nodes and edges
defaultProperties = ["__edgeid", "color", "directed", "fixed", "height", "image", "label", "labelcolor", "strokecolor", "labelvisible", "name", "style", "visible", "weight", "width", "x", "y"]

doNotIncludeProperties = ["__edgeid", "image", "node1", "node2"]

enumeratedStringFieldMaxSize = 20

# keep track of the changes while using the panel
changeHistory = []

# tuple used to send data into the methods
t = [1,2]

# graph manipulation methods
method = {}
method["color"] = lambda x: changeColor(x) # x is a tuple (dataTuple)
method["show"] = lambda x: showIt(x)
method["hide"] = lambda x: hideIt(x)
method["size"] = lambda x: setSize(x)
method["show label"] = lambda x: showLabel(x)
method["hide label"] = lambda x: hideLabel(x)
method["change label"] = lambda x: setLabel(x)

# node information
nodeProperties = {} # stores the node property name and type
nodePropertiesList = [] # stores the node property names
nodePropertyValues = {} # stores the all the values for a node property
nodeIndex = [] # stores the node and it's index in g.nodes (via tuples)

# edge information
edgeProperties = {} # stores the edge property name and type
edgePropertiesList = [] # stores the node property names
edgePropertyValues = {} # stores all the values for an edge property
edgeIndex = [] # stores the edge and it's index in g.edges (via tuples)

allow_write_in_field = None # shows that a String property should be written in, instead of providing a combo box

# the colors available
colorInfo = [(apricot, 251, 213, 184), (aquamarine, 115, 253, 217), \
(bittersweet, 223, 45, 2), (bluegreen, 106, 253, 212), (blueviolet, 76, 66, 249), \
(brickred, 218, 1, 2), (brown, 168, 0, 1), (burntorange, 253, 184, 7), \
(cadetblue, 161, 174, 226), (canary, 251, 252, 187), (carnationpink, 250, 163, 253), \
(cerulean, 61, 240, 253), (cornflowerblue, 156, 238, 253), (cyan, 0, 255, 255), \
(dandelion, 252, 216, 112), (darkgray, 64, 64, 64), (darkorchid, 199, 124, 230), \
(emerald, 0, 253, 187), (forestgreen, 39, 239, 34), (fuchsia, 174, 45, 244), \
(goldenrod, 253, 241, 112), (gray, 128, 128, 128), (green, 0, 255, 0), \
(greenyellow, 236, 252, 151), (junglegreen, 23, 253, 184), (lavender, 251, 190, 254), \
(lfadedgreen, 241, 229, 254), (lightcyan, 227, 253, 254), (lightgray, 192,192,192), \
(lightmagenta, 251, 229, 254), (lightorange, 252, 229, 216), (lightpurple, 227, 229, 253),\
(lightyellow, 252, 252, 202), (limegreen, 188, 252, 9), (lskyblue, 234, 247, 254), \
(magenta, 255, 0, 255), (mahogany, 209, 0, 2), (maroon, 213, 0, 31), \
(melon, 252, 192, 187), (midnightblue, 0, 176, 198), (mulberry, 204, 82, 251), \
(navyblue, 57, 180, 254), (olivegreen, 13, 201, 7), (orange, 255, 200,0), \
(orangered, 251, 11, 187), (orchid, 210, 161, 253), (peach, 252, 186, 148), \
(periwinkle, 170, 171, 254), (pinegreen, 8, 222, 111), (pink, 255, 175, 175), \
(plum , 182, 18, 253), (processblue, 46, 253, 253), (purple, 191, 106, 253), \
(rawsienna, 194, 1, 1), (red, 255, 0, 0), (redorange, 252, 130, 101), \
(redviolet, 197, 13, 211), (rhodamine, 250, 118, 253), (royalblue, 0, 187, 253), \
(royalpurple, 130, 91, 253), (rubinered, 250, 14, 239), (salmon, 242, 181, 205), \
(seagreen, 150, 253, 187), (sepia, 147, 0, 1), (skyblue, 162, 253, 240), \
(springgreen, 221, 253, 134), (tan, 236, 198, 176), (tealblue, 5, 250, 208), \
(thistle, 237, 170, 253), (turquoise, 104, 253, 230), (violet, 119, 99, 253), \
(violetred, 250, 121, 253), (white, 255, 255, 255), (wildstrawberry, 251, 59, 203), \
(yellow, 255, 255, 0), (yellowgreen, 196, 252, 139), (yelloworange, 253, 198, 8)]

# list of just the color names
colorList = []

# dummy component to fulfill some parameters
dummyComponent = JComboBox()

# global dir
globalDir = dir()

######################## END GLOBAL VARIABLES ##################################

# initialize all the global variables
def initializeGlobalVariables():
	# put all the color names into an array
	for i in colorInfo:
		colorList.append(i[0])
	
	gStuff = globals() # all variables that have been declared
	# the properties of nodes and edges specified in the gdf file
	originalGraphProperties = []
	wantedDefaultProperties = []
	# get all methods and variables
	for i in globalDir:
		if gStuff.has_key(i) and not i == "Edge" and not i == "Node" and not i in colorList:
			# check to see if it is a variable
			if str(gStuff[i]) == i:
				if i in doNotIncludeProperties:
					#ignore it
					foo = 2
				elif i in defaultProperties: 				
					wantedDefaultProperties.append(i)				
				else:
					originalGraphProperties.append(i)
		
	#makes it so original graph properties appear first
	wantedProperties = originalGraphProperties + wantedDefaultProperties
	# check to see which properties belong to what
	for i in wantedProperties:
		# check to see if it's a node property
		try:
			property = "g.nodes[0]." + i
			eval(property) # will throw an error if it is not a property of a node
			
			# find out what type of variable it is
			nodeProperties[i] = type(eval(property))
			nodePropertiesList.append(i) # add to the list of node properties
			nodePropertyValues[i] = [] # store all the values of that property
			
			# find all the values for that node property if it is a string
			if nodeProperties[i] == type("string"):
				allvalues = eval("g.nodes."+ i)
				alluniquevalues = unique(allvalues)
				nodePropertyValues[i] = alluniquevalues
			
			# else find the range if the property is a flot or an integer
			elif nodeProperties[i] == type(1) or nodeProperties[i] == type(1.5):
				all_values = eval("g.nodes." + i)
				if len(all_values) > 0:
					smallest = min(all_values)
					largest  = max(all_values)
				else:
					smallest = 0
					largest  = 0
				nodePropertyValues[i].append(smallest)
				nodePropertyValues[i].append(largest)
			
		# there is an attribute error => i is not a property of a node
		except AttributeError:
			# check to see if it is an edge property
			try:
				property = "g.edges[0]." + i
				eval(property) # will throw an error if it is not a property of an edge
				edgeProperties[i] = type(eval(property))
				edgePropertiesList.append(i)
				edgePropertyValues[i] = []
				
				# find all the values for that edge property if property is a string
				if edgeProperties[i] == type("string"):
					allvalues = eval("g.edges."+ i)
					alluniquevalues = unique(allvalues)
					edgePropertyValues[i] = alluniquevalues
									
				# find the range of values for an edge if it is an integer or float
				elif edgeProperties[i] == type(1) or edgeProperties[i] == type(1.5):
					all_values = eval("g.edges." + i)
					if len(all_values) > 0:
						smallest = min(all_values)
						largest  = max(all_values)
					else:
						smallest = 0
						largest  = 0
					edgePropertyValues[i].append(smallest)
					edgePropertyValues[i].append(largest)
										
			# there is an attribute error => i is not a property of an edge
			except AttributeError:
				print sys.exc_info()
				print i + " is not a property of a node or an edge"

	# sort the node list
	counter = 0
	for i in g.nodes:
		nodeIndex.append((i.getName(), counter))
		counter = counter + 1
	nodeIndex.sort()
	
	# sort the edge list
	counter = 0
	for i in g.edges:
		edgeIndex.append((i.node1.getName() + " - " + i.node2.getName(), counter))
		counter = counter + 1
	edgeIndex.sort()

# the class to for the auto complete JComboBox
# automatically completes what is inserted into the JComboBox
class EDoc(javax.swing.text.PlainDocument):
	# EDocSelf: the self variable
	# j: JComboBox
	# m: ComboBoxModel
	# d: the dock it's in
	def __init__(EDocSelf, j, m, d):
		EDocSelf.jcb = j
		EDocSelf.cbm = m
		EDocSelf.dock = d # the dock the DropDownBox is in
		# flag to indicate that we're selecitng something so it doesn't restarnt on next letter
 		EDocSelf.selecting = false 
 		EDocSelf.currentOffs = 0 # the current value of offs
		
		# get the JTextComponent
 		EDocSelf.jtc = EDocSelf.jcb.getEditor().getEditorComponent()
	
 	# remove text from the JComboBox	
	def remove(EDocSelf, offs, len):
		if not EDocSelf.selecting:
			javax.swing.text.PlainDocument.remove(EDocSelf, offs, len)
			
	
	# insert a string into the JComboBox
	# offs: the offset position
	# string: the string to insert
	# a: the attribute set
	def insertString(EDocSelf, offs, string, a):
		if not EDocSelf.selecting:
			javax.swing.text.PlainDocument.insertString(EDocSelf, offs, string, a) # calls the insertString of the parent class
			EDocSelf.currentOffs = offs
			currentString = javax.swing.text.PlainDocument.getText(EDocSelf, 0, javax.swing.text.PlainDocument.getLength(EDocSelf))
			item = EDocSelf.findMatch(currentString)
						
			# if there is an item that matches
			if item:
				EDocSelf.setSelectedItem(item)
			
			# if there is no item that matches, get back the item that was last matched
			else:
				# get current selected item
				item = EDocSelf.jcb.getSelectedItem()
				# make the higlighting stop
				offs = offs - len(string)
				# play warning sound
				UIManager.getLookAndFeel().provideErrorFeedback(EDocSelf.jcb)
			# auto completion
			# highlights the letters of the possible word to match after the given letters
			# first remove whatever is in the textbox
			javax.swing.text.PlainDocument.remove(EDocSelf, 0, javax.swing.text.PlainDocument.getLength(EDocSelf))
			
			# insert back the string that user enterd
			userString = str(item) # to represent objects as strings
			if item in general: # the general are already strings, so don't need to represent
				userString = item
			javax.swing.text.PlainDocument.insertString(EDocSelf,0, userString, a)
			EDocSelf.highlightText(offs + len(string))
			
	# setSelectedItem
	# makes the object found the selected item
	def setSelectedItem(EDocSelf, item):
		EDocSelf.selecting = true
		EDocSelf.cbm.setSelectedItem(item)
		EDocSelf.jcb.setPopupVisible(true)
		EDocSelf.selecting = false
	
	# highlightText
	# highlights the completed part of the text
	# s start position
	def highlightText(EDocSelf, s):
		EDocSelf.jtc.setSelectionStart(s)
		EDocSelf.jtc.setSelectionEnd(javax.swing.text.PlainDocument.getLength(EDocSelf))
		
	# findMatch
	# search combo box to see if there is a match
	def findMatch(EDocSelf, s):
		for i in range(EDocSelf.cbm.getSize()):
			currentString = str(EDocSelf.cbm.getElementAt(i))
			if(currentString.lower().startswith(s.lower())):
				return EDocSelf.cbm.getElementAt(i)
				
# gets from a JComboBox text that is not an item in the list
class TDoc(javax.swing.text.PlainDocument):
	def __init_(TDocSelf, d):
		TDocSelf.dock = d
		
	def theText(TDocSelf):
		return javax.swing.text.PlainDocument.getText(TDocSelf, 0, javax.swing.text.PlainDocument.getLength(TDocSelf))
		
	def insertString(TDocSelf, offs, string, a):
		currentString = TDocSelf.theText()
		
		# see if if the current string with the new text will keep the number format
		testPattern = "^-?\d*\.?\d*$"
		testString = currentString + string
		
		valid = java.util.regex.Pattern.matches(testPattern, String(testString))
		
		# if valid, insert into the the textbox
		if valid:
			javax.swing.text.PlainDocument.insertString(TDocSelf, offs, string, a)
		# play sound if invalid input
		else:
			UIManager.getLookAndFeel().provideErrorFeedback(dummyComponent)
		
# enable or disable boxes due to what is put into the object box
# other boxes are enabled only if edges or nodes are selected in object box
class objectBoxFilter(java.awt.event.ActionListener):
	def __init__(obfSelf, d):
		obfSelf.dock = d
	
	def actionPerformed(obfSelf, e):
		# get the current element in object box
		currentIndex = obfSelf.dock.objectBox.getSelectedIndex()
		print "current index: " + str(currentIndex)
		print "length " + str(currentIndex - len(general))
		
		# disable other boxes if nodes or edges is not selected
		if (currentIndex >= 0 and currentIndex <=2) or currentIndex > len(general) - 1:
			obfSelf.dock.propertyBox.setEnabled(false)
			obfSelf.dock.operatorBox.setEnabled(false)
			obfSelf.dock.valueBox.setEnabled(false)
			
			# enable the size changers
			if currentIndex == 2:
				obfSelf.dock.heightSlider.setEnabled(false)
				obfSelf.dock.sameWidthHeightCheck.setEnabled(false)
			elif currentIndex - len(general) < len(g.nodes):
				obfSelf.dock.heightSlider.setEnabled(true)
				obfSelf.dock.sameWidthHeightCheck.setEnabled(true)
			else:
				obfSelf.dock.heightSlider.setEnabled(false)
				obfSelf.dock.sameWidthHeightCheck.setEnabled(false)
			
		else:# check to see if it's nodes or edges selected

			#if we are filtering on nodes...
			if currentIndex == 3:
				# change property box to display node properties
				if obfSelf.dock.propertiesShown == "edges":
					obfSelf.dock.propertiesShown = "nodes"

				#clear out the property box before we populate it with the correct items
				obfSelf.dock.propertyBox.removeAllItems()
				
				# insert node properties
				for i in obfSelf.dock.nodeProperties:
					obfSelf.dock.propertyBox.addItem(i)
				
				# enable height slider and check box in size panel
				obfSelf.dock.heightSlider.setEnabled(true)
				obfSelf.dock.sameWidthHeightCheck.setEnabled(true)
				
			# if we are filtering on edges...
			elif currentIndex == 4:
				# change property box to display edge properties
				if obfSelf.dock.propertiesShown == "nodes":
					obfSelf.dock.propertiesShown = "edges"
				
				#clear out the property box before we populate it with the correct items
				obfSelf.dock.propertyBox.removeAllItems()

				# insert edge properties
				for i in obfSelf.dock.edgeProperties:
					obfSelf.dock.propertyBox.addItem(i)
				
				# disable height slider and check box in size panel
				obfSelf.dock.heightSlider.setEnabled(false)
				obfSelf.dock.sameWidthHeightCheck.setEnabled(false)
			
			# enable the other boxes
			obfSelf.dock.propertyBox.setEnabled(true)
			obfSelf.dock.operatorBox.setEnabled(true)
			obfSelf.dock.valueBox.setEnabled(true)


# property box listener
# defines what goes into the operator box and value box depending on what is selected
class propertyBoxListener(java.awt.event.ActionListener):
	def __init__(pblSelf, d):
		pblSelf.dock = d
		
	def actionPerformed(pblSelf, e):
		# get some details about the property
		propertyFor = pblSelf.dock.propertiesShown
		propertyIndex = pblSelf.dock.propertyBox.getSelectedIndex()
		propertyName = ""
		propertyDictionary = ""
		propertyValues = None
		
		# these are node properties
		if propertyFor == "nodes":
			propertyDictionary = nodeProperties
			propertyName = pblSelf.dock.nodeProperties[propertyIndex]
			propertyValues = nodePropertyValues
		
		# these are edge properties
		else:
			propertyDictionary = edgeProperties
			propertyName = pblSelf.dock.edgeProperties[propertyIndex]
			propertyValues = edgePropertyValues
		
		# get the property type
		propertyType = propertyDictionary[propertyName]
		
		# remove all items in the JComboBox
		pblSelf.dock.valueBox.removeAllItems()
		
		# remove all items in the operators box
		pblSelf.dock.operatorBox.removeAllItems()
		
		# set the current property type
		pblSelf.dock.currentPropertyType = propertyType
		
		# what we add back in depends on what the property type is
		# propertyType is a string
		if propertyType == type("string"):
			# add an EDoc to the value box to restrict what user can input
			editor = pblSelf.dock.valueBox.getEditor().getEditorComponent()
			editor.setDocument(EDoc(pblSelf.dock.valueBox, pblSelf.dock.valueBox.getModel(), pblSelf.dock))
			pblSelf.dock.valueBox.setEditable(true)
			
			# add in the property values
			for i in propertyValues[propertyName]:
				pblSelf.dock.valueBox.addItem(str(i))
			
			# add in the appropriate operators
			for i in pblSelf.dock.stringOperators:
				pblSelf.dock.operatorBox.addItem(i)
		
		# propertyType is a number
		elif propertyType == type(1) or propertyType == type(1.5):
			# remove the EDoc from the value box
			# add in another type of document to get the value of the input text 
			# since it will not be a value from the list of items
			# add in the range of possible values for the user
			pblSelf.dock.valueBox.setEditable(true)
			editor = pblSelf.dock.valueBox.getEditor().getEditorComponent()
			editor.setDocument(pblSelf.dock.valueBoxValue) # none for now
			
			# add in the property values
			smallestValue = propertyValues[propertyName][0]
			largestValue = propertyValues[propertyName][1]
			valueRange = "Number from : " + str(smallestValue) + " - " + str(largestValue)
			pblSelf.dock.valueBox.addItem(valueRange)
			
			# add in the apropriate operators
			for i in pblSelf.dock.numberOperators:
				pblSelf.dock.operatorBox.addItem(i)

# width slider change listener
class widthSliderListener(javax.swing.event.ChangeListener):
	def __init__ (wslSelf, d):
		wslSelf.dock = d
		
	def stateChanged(wslSelf, e):
		checked = wslSelf.dock.sameWidthHeightCheck.isSelected()
		if checked:
			wslSelf.dock.heightSlider.setValue(wslSelf.dock.widthSlider.getValue())
		wslSelf.dock.sizeWidthLabel.setText("Width: " + str(wslSelf.dock.widthSlider.getValue()))
		wslSelf.dock.sizeHeightLabel.setText("Height: " + str(wslSelf.dock.heightSlider.getValue()))
		

# height slider change listener
class heightSliderListener(javax.swing.event.ChangeListener):
	def __init__ (hslSelf, d):
		hslSelf.dock = d
		
	def stateChanged(hslSelf, e):
		checked = hslSelf.dock.sameWidthHeightCheck.isSelected()
		if checked:
			hslSelf.dock.widthSlider.setValue(hslSelf.dock.heightSlider.getValue())
		hslSelf.dock.sizeWidthLabel.setText("Width: " + str(hslSelf.dock.widthSlider.getValue()))
		hslSelf.dock.sizeHeightLabel.setText("Height: " + str(hslSelf.dock.heightSlider.getValue()))

# initiate the dock properties
class GraphModifier(com.hp.hpl.guess.ui.DockableAdapter):
	self = ""
	def __init__(dockSelf):
		GraphModifier.self = dockSelf
		GraphModifier.tempColor = None # dummy variable
		dockSelf.currentColor = red
		
		# get only the node names
		nodes = []
		for i in nodeIndex:
			nodes.append(i[0])
		
		# get only the edge names
		edges = []
		for i in edgeIndex:
			edges.append(i[0])
		
		dockSelf.objects = general + nodes + edges
		
		# layout panel
		dockSelf.layoutPanel = JPanel() # top: main controls bottom: hidden controls
		dockSelf.layoutPanel.setLayout(BoxLayout(dockSelf.layoutPanel, BoxLayout.Y_AXIS))
		dockSelf.topPanel = JPanel() # the top panel
		macDimH = 40
		winDimH = 30
		macDimW = 900
		winDimW = 800
		# Here we assume people are using either mac or windows. 
		# If I had a way to test for linux and tweak the sizes I'd do it here.
		if System.getProperty("os.name") == "Mac OS X":
			dimWidth = macDimW
			dimHeight = macDimH
			print "Its a mac!"
		else:
			dimWidth = winDimW
			dimHeight = winDimH
		dockSelf.topPanel.setPreferredSize(Dimension(dimWidth, dimHeight))
		dockSelf.layoutPanel.add(dockSelf.topPanel)
		dockSelf.buttonPanel = JPanel() # the panel for the buttons
		dockSelf.buttonPanel.setPreferredSize(Dimension(dimWidth, dimHeight))
		dockSelf.layoutPanel.add(dockSelf.buttonPanel)
		dockSelf.bottomPanel = JPanel() # the bottom panel
		dockSelf.bottomPanel.setPreferredSize(Dimension(900, 280))
		dockSelf.layoutPanel.add(dockSelf.bottomPanel)
		
		# object box
		dockSelf.objectBox = JComboBox(dockSelf.objects)
		dockSelf.objectBox.setEditable(true)
		dockSelf.objectEditor = dockSelf.objectBox.getEditor().getEditorComponent()
		dockSelf.objectEditor.setDocument(EDoc(dockSelf.objectBox, dockSelf.objectBox.getModel(), dockSelf))
		obf = objectBoxFilter(dockSelf)
		dockSelf.objectBox.addActionListener(obf)
		
		# color panel
		dockSelf.colorPanel = JPanel(GridLayout(2, 40))
		dockSelf.colorPanel.setVisible(false)
		
		# colors
		dockSelf.colorInfo = colorInfo
		GraphModifier.currentIndex = 0
		dockSelf.colors = {}
		
		# create the color buttons
		for theColor in dockSelf.colorInfo:
	 		dockSelf.colors[theColor[0]] = JButton("#")
			dockSelf.colors[theColor[0]].setBackground(Color(theColor[1], theColor[2], theColor[3]))
			dockSelf.colors[theColor[0]].setForeground(Color(theColor[1], theColor[2], theColor[3]))
			dockSelf.colors[theColor[0]].setPreferredSize(Dimension(15,15))
 			dockSelf.colors[theColor[0]].actionPerformed = lambda event: setColor(event, GraphModifier.self)
			dockSelf.colorPanel.add(dockSelf.colors[theColor[0]])
			
		# size panel
		dockSelf.sizePanel = JPanel(GridLayout(2,4))
		
		# assuming this is for a node, do edges later
		dockSelf.widthSlider = JSlider(1, 100, int(g.nodes[0].width))
		dockSelf.widthSlider.setPreferredSize(Dimension(100, 2))
		dockSelf.wsl = widthSliderListener(dockSelf)
		dockSelf.widthSlider.addChangeListener(dockSelf.wsl)
		dockSelf.widthSlider.setMajorTickSpacing(99)
		dockSelf.widthSlider.setMinorTickSpacing(10)
		dockSelf.widthSlider.setPaintLabels(true)
		
		dockSelf.heightSlider = JSlider(1, 100, int(g.nodes[0].height))
		dockSelf.heightSlider.setPreferredSize(Dimension(100, 2))
		dockSelf.hsl = heightSliderListener(dockSelf)
		dockSelf.heightSlider.addChangeListener(dockSelf.hsl)
		dockSelf.heightSlider.setMajorTickSpacing(99)
		dockSelf.heightSlider.setMinorTickSpacing(10)
		dockSelf.heightSlider.setPaintLabels(true)
		
		dockSelf.sameWidthHeightCheck = JCheckBox()
		dockSelf.doneSizeButton = JButton("Change Size")
		dockSelf.doneSizeButton.actionPerformed = lambda event: changeAttribute(GraphModifier.self)
		
		# add the components into the sizePanel
		dockSelf.sizeWidthLabel = JLabel("Width: " + str(dockSelf.widthSlider.getValue()))
		dockSelf.sizeHeightLabel = JLabel("Height: " + str(dockSelf.heightSlider.getValue()))
		dockSelf.sizePanel.add(dockSelf.sizeWidthLabel)
		dockSelf.sizePanel.add(dockSelf.sizeHeightLabel)
		dockSelf.sizePanel.add(JLabel("Width = Height?"))
		dockSelf.sizePanel.add(JLabel(""))
		dockSelf.sizePanel.add(dockSelf.widthSlider)
		dockSelf.sizePanel.add(dockSelf.heightSlider)
		dockSelf.sizePanel.add(dockSelf.sameWidthHeightCheck)
		dockSelf.sizePanel.add(dockSelf.doneSizeButton)
		
		# change label panel
		dockSelf.changeLabelPanel = JPanel()
		dockSelf.changeLabelText = JTextField(50)
		dockSelf.doneChangeLabelButton = JButton("Change")
		dockSelf.doneChangeLabelButton.actionPerformed = lambda event: changeLabel(GraphModifier.self)
		
		# add the components into the change label panel
		dockSelf.changeLabelPanel.add(JLabel("Label Text: "))
		dockSelf.changeLabelPanel.add(dockSelf.changeLabelText)
		dockSelf.changeLabelPanel.add(dockSelf.doneChangeLabelButton)
		
		# change history panel
		dockSelf.changeHistoryPanel = JPanel()
		dockSelf.changeHistoryLeftPanel = JPanel()
		dockSelf.changeHistoryRightPanel = JPanel()
		dockSelf.changeHistoryListModel = DefaultListModel()
		dockSelf.changeHistoryList = JList(dockSelf.changeHistoryListModel)
		dockSelf.changeHistoryListScroller = JScrollPane(dockSelf.changeHistoryList)
		#dockSelf.changeHistoryListScroller.setPreferredSize(Dimension(300, 200))
		dockSelf.exportChangeHistoryButton = JButton("Export Change History")
		dockSelf.exportChangeHistoryButton.actionPerformed = lambda event: exportChangeHistory()
		dockSelf.exportGDFButton = JButton("Export GDF")
		dockSelf.exportGDFButton.actionPerformed = lambda event: exportGDFFile()
		dockSelf.changeHistoryLeftPanel.add(dockSelf.changeHistoryListScroller)
		dockSelf.changeHistoryRightPanel.add(dockSelf.exportChangeHistoryButton)
		dockSelf.changeHistoryRightPanel.add(dockSelf.exportGDFButton)
		dockSelf.changeHistoryPanel.add(dockSelf.changeHistoryLeftPanel)
		dockSelf.changeHistoryPanel.add(dockSelf.changeHistoryRightPanel)
		
		# colour button
		dockSelf.colourButton = JButton("Colour")
		dockSelf.colourButton.actionPerformed = lambda event:  updateBottomPanel(GraphModifier.self, GraphModifier.self.colorPanel)# this line has problems
		
		# show button
		dockSelf.showButton = JButton("Show")
		dockSelf.showButton.actionPerformed = lambda event: setVisible(true, GraphModifier.self) # show objects
		
		# hide button
		dockSelf.hideButton = JButton("Hide")
		dockSelf.hideButton.actionPerformed = lambda event: setVisible(false, GraphModifier.self) # hide objects
		
		# size button
		dockSelf.sizeButton = JButton("Size")
		dockSelf.sizeButton.actionPerformed = lambda event: setSizePanel(GraphModifier.self)
		
		# show label button
		dockSelf.showLabelButton = JButton("Show Label")
		dockSelf.showLabelButton.actionPerformed = lambda event: setLabelVisible(true, GraphModifier.self)
		
		# hide label button
		dockSelf.hideLabelButton = JButton("Hide Label")
		dockSelf.hideLabelButton.actionPerformed = lambda event: setLabelVisible(false, GraphModifier.self)
		
		# change label button
		dockSelf.changeLabelButton = JButton("Change Label")
		dockSelf.changeLabelButton.actionPerformed = lambda event: updateBottomPanel(GraphModifier.self, GraphModifier.self.changeLabelPanel)
		
		# center button
		dockSelf.centerButton = JButton("Center")
		dockSelf.centerButton.actionPerformed = lambda event: centerGraph(GraphModifier.self)
		
		# export change history
		dockSelf.changeHistoryButton = JButton("Change History")
		dockSelf.changeHistoryButton.actionPerformed = lambda event: updateBottomPanel(GraphModifier.self, GraphModifier.self.changeHistoryPanel)
		dockSelf.nodeProperties = nodePropertiesList
		dockSelf.edgeProperties = edgePropertiesList
		
		dockSelf.nodePropertiesInfo = nodeProperties
		dockSelf.edgePropertiesInfo = edgeProperties
		
		
		# property box
		dockSelf.propertyBox = JComboBox(dockSelf.nodeProperties)
		dockSelf.propertyBox.setEditable(false)
		dockSelf.propertiesShown = "nodes" # what properties are currently shown in the property box
		dockSelf.currentPropertyType = type("string")
		pbl = propertyBoxListener(dockSelf)
		dockSelf.propertyBox.addActionListener(pbl)
		
		# operator box
		dockSelf.numberOperators = ["==", "!=", "<=", "<", ">=", ">"]
		dockSelf.stringOperators = ["==", "!="]
		dockSelf.operatorBox = JComboBox(dockSelf.numberOperators)
		dockSelf.operatorBox.setEditable(false)
		
		# value box
		dockSelf.valueBox = JComboBox()
		dockSelf.valueBoxValue = TDoc()
		# other trackers
		dockSelf.currentAction = "" # keeps track of what button is pressed
		
		# put list into scroll pane
		dockSelf.add(dockSelf.layoutPanel)
		dockSelf.topPanel.add(JLabel("Object: "))
		dockSelf.topPanel.add(dockSelf.objectBox)
		dockSelf.topPanel.add(JLabel("Property: "))
		dockSelf.topPanel.add(dockSelf.propertyBox)
		dockSelf.topPanel.add(JLabel("Operator: "))
		dockSelf.topPanel.add(dockSelf.operatorBox)
		dockSelf.topPanel.add(JLabel("Value: "))
		dockSelf.topPanel.add(dockSelf.valueBox)
		dockSelf.buttonPanel.add(dockSelf.colourButton)
		dockSelf.buttonPanel.add(dockSelf.showButton)
		dockSelf.buttonPanel.add(dockSelf.hideButton)
		dockSelf.buttonPanel.add(dockSelf.sizeButton)
		dockSelf.buttonPanel.add(dockSelf.showLabelButton)
		dockSelf.buttonPanel.add(dockSelf.hideLabelButton)
		dockSelf.buttonPanel.add(dockSelf.changeLabelButton)
		dockSelf.buttonPanel.add(dockSelf.centerButton)
		dockSelf.buttonPanel.add(dockSelf.changeHistoryButton)
		
		# set intial value of object box to all
		dockSelf.objectBox.setSelectedIndex(0)
				
		# make it big enough to fit everything
		dockSelf.setPreferredSize(java.awt.Dimension(900, 360))
		ui.dock(dockSelf)

	# title of the panel
	def getTitle(dockSelf):
		return("Graph Modifier")

# update bottom panel (changes the bottom panel)
def updateBottomPanel(o, p):
	# remove what's in the bottom panel
	o.bottomPanel.removeAll()
	o.bottomPanel.add(p)
	p.setVisible(true)
	p.requestFocusInWindow()
	o.bottomPanel.setVisible(true)
	o.bottomPanel.repaint()
	p.repaint()

# set the color to change
# b, which button is pressed
def setColor(b, o):
	o.currentAction = "color"
	# figure out which button was pressed
	for color in o.colorInfo:
		if b.getSource() == o.colors[color[0]]:
			o.currentColor = color[0]
			break
	t[1] = o.currentColor
	changeAttribute(o)
	o.colorPanel.setVisible(false)

# shows the specified objects
# b the boolean variable to see whether it's to show or hide
def setVisible(b, o):
	if b:
		o.currentAction = "show"
	else:
		o.currentAction = "hide"
	changeAttribute(o)

# set up the size panel according to whether it's an edge or a node
def setSizePanel(o):
	# check to see what object is selected
	o.currentAction = "size"
	t[1] = o
	updateBottomPanel(o, o.sizePanel)
	
# set the object's label to visible or not
def setLabelVisible(b, o):
	if b:
		o.currentAction = "show label"
	else:
		o.currentAction = "hide label"
	changeAttribute(o)
	
# get the text to change the label
def changeLabel(o):
	o.currentAction = "change label"
	t[1] = o.changeLabelText.getText()
	changeAttribute(o)
	o.changeLabelPanel.setVisible(false)
	
# change the specified attribute based on the value
def changeAttribute(o):
	# history trackers
	action = o.currentAction # the current action we're going to do
	objects = ""
	property = ""
	operator = ""
	value = ""
	selectedIndex = o.objectBox.getSelectedIndex()
	
	# so the script will work if nothing is selected
	if selectedIndex < 0:
		selectedIndex = 0
		
	# get values of objects in string format
	if selectedIndex <= len(general)-1:
		
		# all is selected
		if selectedIndex == 0:
			objects = "all objects"
			# apply attribute change to all colours
			if o.currentAction == "color":
				# change all node colors
				g.nodes.color = o.currentColor
				
				# change all edge colors
				g.edges.color = o.currentColor
			elif o.currentAction == "show":
				# show all nodes
				g.nodes.visible = true
				
				# show all edges
				g.edges.visible = true
			elif o.currentAction == "hide":
				# hide all nodes
				g.nodes.visible = false
				
				# hide all edges
				g.nodes.visible = false
			elif o.currentAction == "size":
				# set the width and height of all nodes
				g.nodes.width = o.widthSlider.getValue()
				g.nodes.height = o.heightSlider.getValue()
				g.edges.width = o.widthSlider.getValue()
			elif o.currentAction == "show label":
				# show all labels of nodes
				g.nodes.labelVisible = true
				
				# show all labels of edges
				g.edges.labelVisible = true
				
				# repaint the graph
				v.repaint()
			elif o.currentAction == "hide label":
				# hide all labels of nodes
				g.nodes.labelVisible = false
				
				# hide all labels of edges
				g.edges.labelVisible = false
				
				# repaint the graph
				v.repaint()
			elif o.currentAction == "change label":
				# change the label of all nodes and edges
				all = g.nodes + g.edges
				for i in all:
					t[0] = i
					setLabel(t)
		# set all nodes to something
		elif selectedIndex == 1:
			objects = "all nodes"
			if o.currentAction == "color":
				g.nodes.color = o.currentColor
			elif o.currentAction == "show":
				g.nodes.visible = true
			elif o.currentAction == "hide":
				g.nodes.visible = false
			elif o.currentAction == "size":
				g.nodes.width = o.widthSlider.getValue()
				g.nodes.height = o.heightSlider.getValue()
			elif o.currentAction == "show label":
				g.nodes.labelVisible = true
			elif o.currentAction == "hide label":
				g.nodes.labelVisible = false
			elif o.currentAction == "change label":
				for i in g.nodes:
					t[0] = i
					setLabel(t)
		# set all edges to something
		elif selectedIndex == 2:
			objects = "all edges"
			if o.currentAction == "color":
				g.edges.color = o.currentColor
			elif o.currentAction == "show":
				g.edges.visible = true
			elif o.currentAction == "hide":
				g.edges.visible = false
			elif o.currentAction == "size":
				g.edges.width = o.widthSlider.getValue()
			elif o.currentAction == "show label":
				g.edges.labelVisible = true
			elif o.currentAction == "hide label":
				g.edges.labelVisible = false
			elif o.currentAction == "change label":
				for i in g.edges:
					t[0] = i
					setLabel(t)
		else:
			indexList = ""
			propertyIndex = o.propertyBox.getSelectedIndex()
			propertyList = ""
			propertyDictionary = ""
			if selectedIndex == 3:
				objects = "all nodes whose property: "
				propertyList = o.nodeProperties
				propertyDictionary = nodeProperties
				indexList = nodeIndex
			else:
				objects = "all edges whose property: "
				propertyList = o.edgeProperties
				propertyDictionary = edgeProperties
				indexList = edgeIndex
			
			property = propertyList[propertyIndex]
			propertyType = propertyDictionary[property]
			currentOperatorIndex = o.operatorBox.getSelectedIndex()
			currentOperator = o.numberOperators[currentOperatorIndex]
			currentValue = ""
			if propertyType == type("string"):
				if nodePropertyValues.has_key(property):				
					currentValue = nodePropertyValues[property][o.valueBox.getSelectedIndex()]
				elif edgePropertyValues.has_key(property):
					currentValue = edgePropertyValues[property][o.valueBox.getSelectedIndex()]
				else:
					raise AttributeError

			elif propertyType == type(1) or propertyType == type(1.5):
				currentValue = float(o.valueBoxValue.theText())
			value = currentValue
			operator = currentOperator
			# check to see which nodes or edges fulfill that property
			for i in indexList:
				currentThing = ""
				theValue = ""
				if selectedIndex == 3:
					currentThing = g.nodes[i[1]]
					theValue = eval("g.nodes[" + str(i[1]) + "]." + property)
				else:
					currentThing = g.edges[i[1]]
					theValue = eval("g.edges[" + str(i[1]) + "]." + property)
				t[0] = currentThing
				if currentOperator == o.numberOperators[0] and theValue == currentValue:
					method[o.currentAction](t)
				elif currentOperator == o.numberOperators[1] and theValue != currentValue:
					method[o.currentAction](t)
				elif currentOperator == o.numberOperators[2] and theValue <= currentValue:
					method[o.currentAction](t)
				elif currentOperator == o.numberOperators[3] and theValue < currentValue:
					method[o.currentAction](t)
				elif currentOperator == o.numberOperators[4] and theValue >= currentValue:
					method[o.currentAction](t)
				elif currentOperator == o.numberOperators[5] and theValue > currentValue:
					method[o.currentAction](t)
	else:
		currentObject = ""
		# check to see if a node or edge is selected
		# have to include the 3 things at the beginning
		selectedIndex = o.objectBox.getSelectedIndex()
		if selectedIndex >= len(g.nodes) + len(general):
			selectedIndex = selectedIndex - len(g.nodes) - len(general)
			currentObject = g.edges[edgeIndex[selectedIndex][1]]
			objects = "g.edges[" + str(edgeIndex[selectedIndex][1]) + "]"
		else:
			selectedIndex = selectedIndex - len(general)
			currentObject = g.nodes[nodeIndex[selectedIndex][1]]
			objects = "g.nodes[" + str(nodeIndex[selectedIndex][1]) + "]"
			
		# check what action were doing
		t[0] = currentObject
		method[o.currentAction](t)
		object = o.getName()
		action = o.currentAction
	
	# save history
	saveHistory(o, action, objects, property, operator, value)	
	o.bottomPanel.setVisible(false)
			
# change colour method
def changeColor(tup):
	#theObject.color = theColor
	tup[0].color = tup[1]

# show the specified object
def showIt(tup):
	tup[0].visible = true
	
# hide the specified object
def hideIt(tup):
	tup[0].visible = false
	
# set the size of the object
def setSize(tup):
	o = tup[1]
	# check to see if the object is a node or an edge
	selectedIndex = o.objectBox.getSelectedIndex()
	node = tup[0] in g.nodes
	getHeightValue = selectedIndex == 1 or selectedIndex - len(general) < len(g.nodes)
	
	widthValue = o.widthSlider.getValue()
	heightValue = widthValue
	
	if getHeightValue:
		heightValue = o.heightSlider.getValue()
	
	# change the object's size	
	tup[0].width = widthValue
	if node:
		tup[0].height = heightValue
		
# show the label
def showLabel(tup):
	tup[0].labelVisible = true
	v.repaint() # repaint the graph

# hide the label
def hideLabel(tup):
	tup[0].labelVisible = false
	v.repaint() # repaint the graph
	
# change the label text
def setLabel(tup):
	tup[0].label = tup[1]
	v.repaint()
	
# save the history of the changes made to the graph
def saveHistory(o, action, objects, property, operator, compareValue):
	historyDescription = ""
	historyCode = ""
	# generate history description
	if objects == "all objects" or objects == "all nodes" or objects == "all edges":
		historyDescription = action + " " + objects + " "
	elif objects == "all nodes whose property: " or objects == "all edges whose property: ":
		historyDescription = action + " " + objects + " " + property + " " + str(operator) + " " + str(compareValue)
	if action == "color":
		historyDescription = historyDescription + " " + o.currentColor
	elif action == "size":
		historyDescription = historyDescription + " to width: " + str(o.widthSlider.getValue()) + " height: " + str(o.heightSlider.getValue())
	elif action == "change label":
		historyDescription = historyDescription + " to " + o.changeLabelText.getText()
	
	# generate history code
	if objects == "all objects" or objects == "all nodes" or objects == "all nodes whose property: " or objects == "all edges" or objects == "all edges whose property: ":
		if objects == "all objects":
			historyCode = "objects = g.nodes + g.edges\n"
		elif objects == "all nodes" or objects == "all nodes whose property: ":
			historyCode = "objects = g.nodes\n"
		else:
			historyCode = "objects = g.edges\n"
		historyCode = historyCode + "for i in objects:\n"
		if objects == "all nodes whose property: " or objects == "all edges whose property: ":
			historyCode = historyCode + "\tif i." + property + " " + str(operator) + " " + str(compareValue) + ":\n\t"
		if action == "color":
			historyCode = historyCode + "\ti.color = " + str(o.currentColor) + "\n"
		elif action == "hide":
			historyCode = historyCode + "\ti.visible = false\n"
		elif action == "show":
			historyCode = historyCode + "\ti.visible = true\n"
		elif action == "show label":
			historyCode = historyCode + "\ti.labelVisible = true\n"
			historyCode = historyCode + "v.repaint()"
		elif action == "hide label":
			historyCode = historyCode + "\ti.labelVisible = false\n"
			historyCode = historyCode + "v.repaint()"
		elif action == "size":
			historyCode = historyCode + "\ti.width=" + str(o.widthSlider.getValue())+"\n"
			if not objects == "all objects":
				historyCode = historyCode + "\tif i in g.nodes:\n"
				historyCode = historyCode + "\t\ti.height=" + str(o.heightSlider.getValue())+"\n"
			else:
				historyCode = historyCode + "\tif i in g.nodes:\n"
				historyCode = historyCode + "\t\ti.height = " +  str(o.heightSlider.getValue()) + "\n"
		elif action == "change label":
			historyCode = historyCode + "\ti.label = \"" + o.changeLabelText.getText() + "\"\n"
			historyCode = historyCode + "v.repaint()"
		changeHistory.append((historyDescription, historyCode))
		o.changeHistoryListModel.addElement(historyDescription)
	
def centerGraph(o):
	center()
	changeHistory.append(("center graph","center()"))
	o.changeHistoryListModel.addElement("center graph")
		
def exportChangeHistory():
	jfc = JFileChooser()
	todo = jfc.showSaveDialog(GraphModifier.self.buttonPanel)
	if todo == JFileChooser.APPROVE_OPTION:
		file = jfc.getSelectedFile()
		filePath = file.getAbsolutePath()
		
		# write to the file
		try:
			bw = BufferedWriter(FileWriter(filePath))
			for i in changeHistory:
				bw.write("#" + i[0] + "\n")
				bw.write(i[1])
			bw.close()
			JOptionPane.showMessageDialog(GraphModifier.self.buttonPanel, "File: " + filePath + " saved!")
		except Exception:
			JOptionPane.showMwssageDialog(GraphModifier.self.buttonPanel, "Failed to save file:" + filePath)
	GraphModifier.self.bottomPanel.setVisible(false)
	
def exportGDFFile():
	defaultNodeProperties = ["name","x","y","visible","color","fixed","style","width","height","label","labelVisible","image"]
	defaultEdgeProperties = ["node1","node2","visible","color","weight","width","directed","label","labelVisible"]
	nodeHeader = "nodedef>"
	edgeHeader = "edgedef>"
	# create the node header line
	for i in defaultNodeProperties:
		nodeHeader = nodeHeader + i + ","
	# get the nodes other properties
	for i in nodeProperties.keys():
		if nodeProperties[i] == type("string"):
			nodeHeader = nodeHeader + i + " VARCHAR(100),"
		elif nodeProperties[i] == type(1):
			nodeHeader = nodeHeader + i + " INT,"
		elif nodeProperties[i] == type(1.5):
			nodeHeader = nodeHeader + i + " FLOAT,"
	nodeHeader = nodeHeader[0:-1]
	
	# create the edge header line
	for i in defaultEdgeProperties:
		edgeHeader = edgeHeader + i + ","
	
	# get the other edge properties
	for i in edgeProperties.keys():
		if not i == "node1" and not i == "node2":
			if edgeProperties[i] == type("string"):
				edgeHeader = edgeHeader + i + " VARCHAR(100),"
			elif edgeProperties[i] == type(1):
				edgeHeader = edgeHeader + i + " INT,"
			elif edgeProperties[i] == type(1.5):
				edgeHeader = edgeHeader + i + " FLOAT,"
	edgeHeader = edgeHeader[0:-1]
	
	jfc = JFileChooser()
	todo = jfc.showSaveDialog(GraphModifier.self.buttonPanel)
	if todo == JFileChooser.APPROVE_OPTION:
		file = jfc.getSelectedFile()
		filePath = file.getAbsolutePath()
		
		# write to the file
		try:
			bw = BufferedWriter(FileWriter(filePath))
			bw.write(nodeHeader + "\n")
			nodeStuff = defaultNodeProperties + nodeProperties.keys()
			# write the node stuff into the file
			for i in g.nodes:
				nodeString = ""
				for j in nodeStuff:
					nodeString = nodeString + str(eval("i." + str(j))) + ","
				nodeString = nodeString[0:-1]
				nodeString = nodeString + "\n"
				bw.write(nodeString)
			# write the edge stuff into the file
			# filter out edge porperty keys
			filteredKeys = []
			for i in edgeProperties.keys():
				if not i == "node1" and not i == "node2":
					filteredKeys.append(i)
			edgeStuff = defaultEdgeProperties + filteredKeys
			bw.write(edgeHeader + "\n")
			for i in g.edges:
				edgeString = ""
				for j in edgeStuff:
					# get node name
					if j == "node1" or j == "node2":
						edgeString = edgeString + str(eval("i." + str(j) + ".name")) + ","
					else:
						edgeString = edgeString + str(eval("i." + str(j))) + ","
				edgeString = edgeString[0:-1]
				edgeString = edgeString + "\n"
				bw.write(edgeString)
			bw.close()
			JOptionPane.showMessageDialog(GraphModifier.self.buttonPanel, "GDF created sucessfully")
		except Exception:
			JOptionPane.showMessageDialog(GraphModifier.self.buttonPanel, "Error in exporting GDF")
	GraphModifier.self.bottomPanel.setVisible(false)


#from code.activestate.com/recipes/52560
def unique(s):
    """Return a list of the elements in s, but without duplicates.

    For example, unique([1,2,3,1,2,3]) is some permutation of [1,2,3],
    unique("abcabc") some permutation of ["a", "b", "c"], and
    unique(([1, 2], [2, 3], [1, 2])) some permutation of
    [[2, 3], [1, 2]].

    For best speed, all sequence elements should be hashable.  Then
    unique() will usually work in linear time.

    If not possible, the sequence elements should enjoy a total
    ordering, and if list(s).sort() doesn't raise TypeError it's
    assumed that they do enjoy a total ordering.  Then unique() will
    usually work in O(N*log2(N)) time.

    If that's not possible either, the sequence elements must support
    equality-testing.  Then unique() will usually work in quadratic
    time.
    """

    n = len(s)
    if n == 0:
        return []

    # Try using a dict first, as that's the fastest and will usually
    # work.  If it doesn't work, it will usually fail quickly, so it
    # usually doesn't cost much to *try* it.  It requires that all the
    # sequence elements be hashable, and support equality comparison.
    u = {}
    try:
        for x in s:
            u[x] = 1
    except TypeError:
        del u  # move on to the next method
    else:
        return u.keys()

    # We can't hash all the elements.  Second fastest is to sort,
    # which brings the equal elements together; then duplicates are
    # easy to weed out in a single pass.
    # NOTE:  Python's list.sort() was designed to be efficient in the
    # presence of many duplicate elements.  This isn't true of all
    # sort functions in all languages or libraries, so this approach
    # is more effective in Python than it may be elsewhere.
    try:
        t = list(s)
        t.sort()
    except TypeError:
        del t  # move on to the next method
    else:
        assert n > 0
        last = t[0]
        lasti = i = 1
        while i < n:
            if t[i] != last:
                t[lasti] = last = t[i]
                lasti += 1
            i += 1
        return t[:lasti]

    # Brute force is all that's left.
    u = []
    for x in s:
        if x not in u:
            u.append(x)
    return u


######################## BEGIN FUNCTION CALLS ##################################
print "Starting GUESS GraphModifier (this may take a while)"
initializeGlobalVariables()
GraphModifier()
print "Finished loading GUESS GraphModifier"
######################### END FUNCTION CALLS ###################################
