from edu.umd.cs.piccolo import *
from edu.umd.cs.piccolo.nodes import *
import java.util.EventListener
from edu.umd.cs.piccolo.event import *

# "Constants"
MOUSE_CLICK_DOWN = 501
MOUSE_CLICK_UP = 500

ZOOM_AMOUNT = 0.25

BUTTON_SPACING = 15
SPACE_IN_BETWEEN_BUTTONS = 25

# Generic zoom event listener class.
class ZoomInputEventListener(PInputEventListener):
	zoomAmount = 1.0
	camera = None

	def __init__(self, zoomAmount, camera):
		self.zoomAmount = zoomAmount
		self.camera = camera

	def processEvent(self, event, type):
		if (event.isMouseEvent() and event.isLeftMouseButton() and (type == MOUSE_CLICK_UP)):
			# Get a local reference to the camera.
			camera = self.camera
			# Get the center of the camera's viewing bounds.
			viewBoundsCenter = camera.getViewBounds().getCenter2D()

			# Scale about the mouse position.
			camera.scaleViewAboutPoint(self.zoomAmount, viewBoundsCenter.getX(), viewBoundsCenter.getY())

# Get the camera.
camera = vf.getDisplay().getCamera()

# Create the node buttons.
zoomInNodeButton = PImage("images/plus.png")
zoomOutNodeButton = PImage("images/minus.png")

# Position the node buttons.
zoomInNodeButton.translate(BUTTON_SPACING, BUTTON_SPACING)
zoomOutNodeButton.translate(BUTTON_SPACING, SPACE_IN_BETWEEN_BUTTONS + zoomInNodeButton.getY() + zoomInNodeButton.getHeight())

# Add the nodes to the camera.
camera.addChild(zoomInNodeButton)
camera.addChild(zoomOutNodeButton)

# Create the event listeners.
zoomInNodeButtonEventListener = ZoomInputEventListener(1.0 + ZOOM_AMOUNT, camera)
zoomOutNodeButtonEventListener = ZoomInputEventListener(1.0 - ZOOM_AMOUNT, camera)

# Finally, add the event listeners.
zoomInNodeButton.addInputEventListener(zoomInNodeButtonEventListener)
zoomOutNodeButton.addInputEventListener(zoomOutNodeButtonEventListener)