from edu.umd.cs.piccolo import *
from edu.umd.cs.piccolo.nodes import *
import java.util.EventListener
from edu.umd.cs.piccolo.event import *
from java.awt.geom import *

# "Constants"
MOUSE_CLICK_UP = 500
MOUSE_CLICK_DOWN = 501
MOUSE_CLICK_DRAG_RELEASE = 502
MOUSE_OVER = 503
MOUSE_NO_LONGER_OVER = 505
MOUSE_CLICK_DRAG = 506

TOOL_BUTTON_TOP = 150
BUTTON_SPACING = 15
SPACE_IN_BETWEEN_BUTTONS = 25

MOUSE_OVER_SCALE = 1.1
MOUSE_CLICK_SCALE = 1.25

# Button type "constants".
BUTTON_TYPE_BROWSE = 1
BUTTON_TYPE_MANIPULATE_NODES = 2
BUTTON_TYPE_MANIPULATE_EDGES = 3
BUTTON_TYPE_MANIPULATE_HULLS = 4
BUTTON_TYPE_DRAW = 6

# A utility function to construct a scaled Point2D object based off of a given width, height, and scale.
def createScaledPoint2D(width, height, scale):
	return Point2D.Double(width * scale, height * scale)

# Return True if point is within bounds, or false otherwise.
def isPointInBounds(testPoint, upperLeftBoundingPoint, lowerRightBoundingPoint):
	return ((testPoint.getX() >= upperLeftBoundingPoint.getX()) and \
		(testPoint.getY() >= upperLeftBoundingPoint.getY())) and \
	       ((testPoint.getX() <= lowerRightBoundingPoint.getX()) and \
		(testPoint.getY() <= lowerRightBoundingPoint.getY()))

# A toggleable-image node class.
class TogglingPImage(PImage):
	baseImageFileName = None
	imageExtension = None
	toggleState = 0
	
	def __init__(self, baseImageFileName, imageExtension):
		# Default to off.
		self.baseImageFileName = baseImageFileName
		self.imageExtension = imageExtension
		
		PImage.__init__(self, baseImageFileName + imageExtension)

	def toggle(self):
		if (self.toggleState):
			# We're turning off.
			self.toggleState = 0
			self.setImage(self.baseImageFileName + self.imageExtension)
		else:
			# We're turning on.
			self.toggleState = 1
			self.setImage(self.baseImageFileName + "On" + self.imageExtension)

# Generic tool button event listener class.
class ToolButtonInputEventListener(PInputEventListener):
	# nodeButton is a reference up to the node button that we're listening for.
	# We need to store this reference so we can provide the visual feedback for user interaction
	# events, such as mouse-over and mouse-click.
	nodeButton = None
	# toolButtonType corresponds to the user input modes that GUESS uses for its actual (Piccolo) canvas.
	# It is used to change the current "tool" (i.e. user input mode).
	toolButtonType = None
	
	def __init__(self, nodeButton, toolButtonType):
		self.nodeButton = nodeButton
		self.toolButtonType = toolButtonType
	
	def processEvent(self, event, type):
		# Make sure currentToolNodeButton is considered to be a global.
		global currentToolNodeButton
		# Reset the scale.
		self.nodeButton.setScale(1.0)
		
		# Get the position (of the mouse) from the event relative to self.nodeButton.
		mouseEventPosition = event.getPositionRelativeTo(self.nodeButton)
		# Construct the upper-left and lower-right bounds for self.nodeButton.
		upperLeftBoundingPoint = Point2D.Double()
		lowerRightBoundingPoint = createScaledPoint2D(self.nodeButton.getWidth(), self.nodeButton.getHeight(), self.nodeButton.getScale())
		
		if (currentToolNodeButton == self.nodeButton):
			# If the current tool in use is the one that corresponds to this, return.
			return
		
		if (event.isMouseEvent() and event.isLeftMouseButton()):
			if ((type == MOUSE_CLICK_DOWN) or (type == MOUSE_CLICK_DRAG)):
				# Give some feedback that the button was clicked on.
				self.nodeButton.setScale(MOUSE_CLICK_SCALE)
			elif ((type == MOUSE_CLICK_UP) or (type == MOUSE_CLICK_DRAG_RELEASE)):
				# Also set the scale back to the mouse over scale.
				self.nodeButton.setScale(MOUSE_OVER_SCALE)
				
				if (isPointInBounds(mouseEventPosition, upperLeftBoundingPoint, lowerRightBoundingPoint)):
					# This line switches the handler for events in the display.
					VisFactory.getFactory().getDisplay().switchHandler(self.toolButtonType - 1)
					# Reset the previous tool in use.
					currentToolNodeButton.toggle()
					# Set the current tool in use to this.
					currentToolNodeButton = self.nodeButton
					# Toggle the new current tool node button.
					currentToolNodeButton.toggle()
				
				return
		elif (type == MOUSE_OVER):
			# Scale up a little bit to provide some visual feedback that the mouse is over
			self.nodeButton.setScale(MOUSE_OVER_SCALE)

# Get rid of the status bar (with the broken buttons).
def removeStatusBar():
	# First, get the main UI window.
	mainUIWindow = Guess.getMainUIWindow()
	# Get the content pane of the main UI window.
	mainUIWindowContentPane = mainUIWindow.getContentPane()
	# Get the actual components of the content pain.
	mainUIWindowContentPaneComponents = mainUIWindowContentPane.getComponents()
	
	# Reflect in the components to find the status bar so we can remove it.
	for component in mainUIWindowContentPaneComponents:
		if (isinstance(component, StatusBar)):
			# Remove it from the content pane.
			mainUIWindowContentPane.remove(component)
			# Repaint the content pane.
			mainUIWindowContentPane.repaint()
			
			# Break out of the loop (since there should only be one status bar).
			break

# Create and position a tool node button and its corresponding event listener.
# Return the newly created node button.
def createNodeButton(buttonType, baseImageFileName, fileExtension, xTranslation, yTranslation):
	# Create the node button.
	nodeButton = TogglingPImage(baseImageFileName, fileExtension)
	# Create its event listener.
	nodeButtonEventListener = ToolButtonInputEventListener(nodeButton, buttonType)
	# Add the event listener to the node button.
	nodeButton.addInputEventListener(nodeButtonEventListener)
	# Position the node button.
	nodeButton.translate(xTranslation, yTranslation)
	
	# Return the node button.
	return nodeButton

# Create the tool node buttons.
# Return the default node button.
def createNodeButtons():
	# Get the camera.
	camera = vf.getDisplay().getCamera()
	
	# We need to store the first node so we can use its height for positioning the future node buttons.
	# The first node button will also be the default node button, so it needs to be returned.
	defaultNodeButton = createNodeButton(BUTTON_TYPE_BROWSE, "images/browseTool", ".jpg", \
		BUTTON_SPACING, TOOL_BUTTON_TOP)
		
	# Add the default node button to the camera, then create and add the rest of them...
	camera.addChild(defaultNodeButton)
	
	camera.addChild(createNodeButton(BUTTON_TYPE_MANIPULATE_NODES, "images/manipulateNodesTool", ".jpg", \
		BUTTON_SPACING, TOOL_BUTTON_TOP + SPACE_IN_BETWEEN_BUTTONS + defaultNodeButton.getHeight()))
	
	camera.addChild(createNodeButton(BUTTON_TYPE_MANIPULATE_EDGES, "images/manipulateEdgesTool", ".jpg", \
		BUTTON_SPACING, TOOL_BUTTON_TOP + (SPACE_IN_BETWEEN_BUTTONS + defaultNodeButton.getHeight()) * 2))
	
	camera.addChild(createNodeButton(BUTTON_TYPE_MANIPULATE_HULLS, "images/manipulateHullsTool", ".jpg", \
		BUTTON_SPACING, TOOL_BUTTON_TOP + (SPACE_IN_BETWEEN_BUTTONS + defaultNodeButton.getHeight()) * 3))
	
	camera.addChild(createNodeButton(BUTTON_TYPE_DRAW, "images/drawTool", ".jpg", BUTTON_SPACING, \
		TOOL_BUTTON_TOP + (SPACE_IN_BETWEEN_BUTTONS + defaultNodeButton.getHeight()) * 4))
	
	return defaultNodeButton

# Remove the status bar.
removeStatusBar()

# Create the node buttons and store the reference to the one that corresponds to the default tool.
currentToolNodeButton = createNodeButtons()
# Toggle it on by default.
currentToolNodeButton.toggle()