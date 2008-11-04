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
					# Reset the previous tool in use. (TODO: change image, but for now transparency will do.)
					currentToolNodeButton.setTransparency(1.0)
					# Set the current tool in use to this.
					currentToolNodeButton = self.nodeButton
					# Set the transparency for the current tool in use to reflect that it's not a valid button.
					# (TODO: change image.)
					currentToolNodeButton.setTransparency(0.5)
				
				return
		elif (type == MOUSE_OVER):
			# Scale up a little bit to provide some visual feedback that the mouse is over
			self.nodeButton.setScale(MOUSE_OVER_SCALE)

		return

# Get rid of the status bar (with the broken buttons).
# First, get the main UI window.
mainUIWindow = Guess.getMainUIWindow()
# Get the content pane of the main UI window.
mainUIWindowContentPane = mainUIWindow.getContentPane()
# Get the actual components of the content pain.
mainUIWindowContentPaneComponents = mainUIWindowContentPane.getComponents()

# Reflect in the components to find the status bar so we can remove it.
for i in range(0, mainUIWindowContentPaneComponents.size()):
	# The current component.
	component = mainUIWindowContentPaneComponents[i]
	
	if (component.getClass() == StatusBar):
		# Remove it from the content pane.
		mainUIWindowContentPane.remove(component)
		# Nullify our local array.
		mainUIWindowContentPaneComponents[i] = None
		# Repaint the content pane.
		mainUIWindowContentPane.repaint()
		
		# Break out of the loop (since there should only be one status bar).
		break
			
# Get the camera.
camera = vf.getDisplay().getCamera()

# Create the tool node buttons.
browseToolNodeButton = PImage("images/browseTool.jpg")	# TODO: Actually default the image to the in-use version.
manipulateNodesToolNodeButton = PImage("images/manipulateNodesTool.jpg")
manipulateEdgesToolNodeButton = PImage("images/manipulateEdgesTool.jpg")
manipulateHullsToolNodeButton = PImage("images/manipulateHullsTool.jpg")
drawToolNodeButton = PImage("images/drawTool.jpg")
# This is just a reference to the node button that corresponds to the tool currently in use.
currentToolNodeButton = browseToolNodeButton
currentToolNodeButton.setTransparency(0.5)

# Position the node buttons.
browseToolNodeButton.translate(BUTTON_SPACING, TOOL_BUTTON_TOP)
manipulateNodesToolNodeButton.translate(BUTTON_SPACING, TOOL_BUTTON_TOP + SPACE_IN_BETWEEN_BUTTONS + browseToolNodeButton.getHeight())
manipulateEdgesToolNodeButton.translate(BUTTON_SPACING, TOOL_BUTTON_TOP + (SPACE_IN_BETWEEN_BUTTONS + manipulateNodesToolNodeButton.getHeight()) * 2)
manipulateHullsToolNodeButton.translate(BUTTON_SPACING, TOOL_BUTTON_TOP + (SPACE_IN_BETWEEN_BUTTONS + manipulateEdgesToolNodeButton.getHeight()) * 3)
drawToolNodeButton.translate(BUTTON_SPACING, TOOL_BUTTON_TOP + (SPACE_IN_BETWEEN_BUTTONS + manipulateHullsToolNodeButton.getHeight()) * 4)

# Add the nodes to the camera.
camera.addChild(browseToolNodeButton)
camera.addChild(manipulateNodesToolNodeButton)
camera.addChild(manipulateEdgesToolNodeButton)
camera.addChild(manipulateHullsToolNodeButton)
camera.addChild(drawToolNodeButton)

# Create the event listeners.
browseToolNodeButtonEventListener = ToolButtonInputEventListener(browseToolNodeButton, BUTTON_TYPE_BROWSE)
manipulateNodesToolNodeButtonEventListener = ToolButtonInputEventListener(manipulateNodesToolNodeButton, BUTTON_TYPE_MANIPULATE_NODES)
manipulateEdgesToolNodeButtonEventListener = ToolButtonInputEventListener(manipulateEdgesToolNodeButton, BUTTON_TYPE_MANIPULATE_EDGES)
manipulateHullsToolNodeButtonEventListener = ToolButtonInputEventListener(manipulateHullsToolNodeButton, BUTTON_TYPE_MANIPULATE_HULLS)
drawToolNodeButtonEventListener = ToolButtonInputEventListener(drawToolNodeButton, BUTTON_TYPE_DRAW)

# Finally, add the event listeners.
browseToolNodeButton.addInputEventListener(browseToolNodeButtonEventListener)
manipulateNodesToolNodeButton.addInputEventListener(manipulateNodesToolNodeButtonEventListener)
manipulateEdgesToolNodeButton.addInputEventListener(manipulateEdgesToolNodeButtonEventListener)
manipulateHullsToolNodeButton.addInputEventListener(manipulateHullsToolNodeButtonEventListener)
drawToolNodeButton.addInputEventListener(drawToolNodeButtonEventListener)