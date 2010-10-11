package edu.iu.epic.modelbuilder.gui.compartment;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import edu.iu.epic.modelbuilder.gui.transition.ComplexTransition;
import edu.iu.epic.modelbuilder.gui.transition.SimpleTransition;
import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.IDGenerator;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modelbuilder.gui.utility.PiccoloUtilities;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

/// <summary>
/// Simple event handler which applies the following actions to every node it is called on:
///   * Turn node red when the mouse goes over the node
///   * Turn node white when the mouse exits the node
///   * Drag the node, and associated edges on mousedrag
/// It assumes that the node's Tag references an ArrayList with a list of associated
/// edges where each edge is a PPath which each have a Tag that references an ArrayList
/// with a list of associated nodes.
/// </summary>
public class CompartmentMoveEventHandler extends PDragSequenceEventHandler {
	
	private PPath newSimpleTransition, newComplexTransition;
	
	private PLayer compartmentLayer; 
	private PLayer temporaryComponentsLayer;
	private PLayer transitionLayer;
	
	private Model inMemoryModel;
//	PNode currentNode;

	private PSwingCanvas mainWorkbenchCanvas;

	private IDGenerator pObjectIDGenerator;

	private NotificationArea[] notificationAreas; 
	
	
	public CompartmentMoveEventHandler(PLayer compartmentLayer,  
									   PLayer temporaryComponentsLayer,
									   PLayer transitionLayer,
									   IDGenerator pObjectIDGenerator,
									   Model inMemoryModel,
									   NotificationArea[] notificationAreas, 
									   PSwingCanvas mainWorkbenchCanvas
										  ) {
		getEventFilter().setMarksAcceptedEventsAsHandled(true);
		this.compartmentLayer = compartmentLayer;
		this.temporaryComponentsLayer = temporaryComponentsLayer;
		this.transitionLayer = transitionLayer;
		this.pObjectIDGenerator = pObjectIDGenerator;
		this.inMemoryModel = inMemoryModel;
		this.notificationAreas = notificationAreas;
		this.mainWorkbenchCanvas = mainWorkbenchCanvas;
	}
	public void mouseEntered(PInputEvent e) {
		PNode pickedNode = e.getPickedNode();
		
		String pickedNodeType = (String) pickedNode
									.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME);
		if (e.getButton() == 0 
				&& GlobalConstants.COMPARTMENT_TYPE_ATTRIBUTE_VALUE
					.equalsIgnoreCase(pickedNodeType)) {
			pickedNode.setPaint(Color.YELLOW);
		} else if (e.getButton() == 0 
				&& GlobalConstants.SIMPLE_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
					.equalsIgnoreCase(pickedNodeType)) {
			pickedNode.setPaint(Color.YELLOW);
		} else if (e.getButton() == 0 
				&& GlobalConstants.COMPLEX_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
					.equalsIgnoreCase(pickedNodeType)) {
			pickedNode.setPaint(Color.YELLOW);
		}
	}

	public void mouseExited(PInputEvent e) {
		PNode pickedNode = e.getPickedNode();
		String pickedNodeType = (String) pickedNode
									.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME);
		if (e.getButton() == 0 
				&& GlobalConstants.COMPARTMENT_TYPE_ATTRIBUTE_VALUE
					.equalsIgnoreCase(pickedNodeType)) {
			
			pickedNode.setPaint((Color) pickedNode
									.getAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME));
			
		} else if (e.getButton() == 0 
				&& GlobalConstants.SIMPLE_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
					.equalsIgnoreCase(pickedNodeType)) {
			
			pickedNode.setPaint((Color) pickedNode
									.getAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME));
			
		} else if (e.getButton() == 0 
				&& GlobalConstants.COMPLEX_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
					.equalsIgnoreCase(pickedNodeType)) {
			
			pickedNode.setPaint((Color) pickedNode
									.getAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME));
			
		} 
	}
	
	@Override
	public void mouseClicked(PInputEvent event) {
		PNode node = event.getPickedNode();
		String pickedNodeType = (String) node
									.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME);
		
		//TODO: deletwe this if not required.. currently it is doing nothing.
		if (GlobalConstants.SIMPLE_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
				.equalsIgnoreCase(pickedNodeType)) { }
		
	}
	
	@Override
	public void mouseReleased(PInputEvent e) {
		
		PNode currentPickedNode = e.getPickedNode();
		
		if  (GlobalConstants.SIMPLE_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
				.equalsIgnoreCase(((String) currentPickedNode
						.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME)))) {

			PNode destinationCompartment = getIntersectingCompartment(e.getCanvasPosition());
			
			if (destinationCompartment != null) {
				boolean isTransitionDuplicate = PiccoloUtilities
					.isTransitionDuplicate(currentPickedNode.getParent(), 
										   destinationCompartment, 
										   transitionLayer);
				
				if (!isTransitionDuplicate) {
					PNode simpleTransition = 
						new SimpleTransition(currentPickedNode.getParent(),
											 destinationCompartment,
											 pObjectIDGenerator,
											 GlobalConstants.SIMPLE_TRANSITION_RATIO_DEFAULT_VALUE,
											 inMemoryModel,
											 notificationAreas);

					transitionLayer.addChild(simpleTransition);	
				}
			}
			
			/*
			 * To make sure that if the user presses & releases the mouse on the same 
			 * compartment's "new simple transition handler" then we dont proceed to 
			 * remove the new transition from the parent because it does not exist yet. 
			 * */
			if (newSimpleTransition != null) {
				newSimpleTransition.removeFromParent();
				newSimpleTransition = null;
			}
			
			
		} else if  (GlobalConstants.COMPLEX_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
				.equalsIgnoreCase(((String) currentPickedNode
						.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME)))) {

			PNode destinationCompartment = getIntersectingCompartment(e.getCanvasPosition());
			
			if (destinationCompartment != null) {
				boolean isTransitionDuplicate = PiccoloUtilities
					.isTransitionDuplicate(currentPickedNode.getParent(), 
										   destinationCompartment, 
										   transitionLayer);
				
				if (!isTransitionDuplicate) {
					PNode complexTransition = 
						new ComplexTransition(currentPickedNode.getParent(),
											 destinationCompartment,
											 null,
											 pObjectIDGenerator,
											 inMemoryModel,
											 notificationAreas,
											 mainWorkbenchCanvas);

					transitionLayer.addChild(complexTransition);	
				}
			}
			
			/*
			 * To make sure that if the user presses & releases the mouse on the same 
			 * compartment's "new simple transition handler" then we dont proceed to 
			 * remove the new transition from the parent because it does not exist yet. 
			 * */
			if (newComplexTransition != null) {
				newComplexTransition.removeFromParent();
				newComplexTransition = null;
			}
		}
	}

	public void drag(PInputEvent event) {
		PNode node = event.getPickedNode();
		String pickedNodeType = (String) node
									.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME);
		
		
		if (GlobalConstants.COMPARTMENT_TYPE_ATTRIBUTE_VALUE.equalsIgnoreCase(pickedNodeType)) {
		node.translate(event.getDelta().width, event.getDelta().height);
		
		((PCompartment) node)
				.setInMemoryCompartmentPosition(node.getFullBoundsReference().getX(),
												node.getFullBoundsReference().getY());
		ArrayList transitions = 
			(ArrayList) node.getAttribute(GlobalConstants.COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME);
		
		/*
		 * Refreshes position of the transition along with all its child elements like
		 * Arrow, Ratio Text. 
		 * */
		PiccoloUtilities.refreshTransitions(transitions);
		
		} else if (GlobalConstants.SIMPLE_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
						.equalsIgnoreCase(pickedNodeType)) {
			
			// Note that the node's "FullBounds" must be used (instead of just the "Bound") 
			// because the nodes have non-identity transforms which must be included when
			// determining their position.
			Point2D.Double bound1 = (Point2D.Double) node.getParent().getFullBounds().getCenter2D();
			Point2D.Double bound2 = (Point2D.Double) event.getPosition();
			
			if (newSimpleTransition == null) {
				newSimpleTransition = new PPath();
				temporaryComponentsLayer.addChild(newSimpleTransition);
			}
			
			newSimpleTransition.reset();
			newSimpleTransition.moveTo((float) bound1.getX(), (float) bound1.getY());
			newSimpleTransition.lineTo((float) bound2.getX(), (float) bound2.getY());
			
		} else if (GlobalConstants.COMPLEX_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE
						.equalsIgnoreCase(pickedNodeType)) {
			
			// Note that the node's "FullBounds" must be used (instead of just the "Bound") 
			// because the nodes have non-identity transforms which must be included when
			// determining their position.
			Point2D.Double bound1 = (Point2D.Double) node.getParent().getFullBounds().getCenter2D();
			Point2D.Double bound2 = (Point2D.Double) event.getPosition();
			
			if (newComplexTransition == null) {
				newComplexTransition = new PPath();
				temporaryComponentsLayer.addChild(newComplexTransition);
			}
			
			newComplexTransition.reset();
			newComplexTransition.moveTo((float) bound1.getX(), (float) bound1.getY());
			newComplexTransition.lineTo((float) bound2.getX(), (float) bound2.getY());
			
		}
	}
	
	private PNode getIntersectingCompartment(Point2D mouseReleasePoint) {
		
		for (Iterator<PNode> compartmentsIterator = compartmentLayer.getChildrenIterator(); 
				compartmentsIterator.hasNext();) {
			PNode currentCompartment = (PNode) compartmentsIterator.next();
			Rectangle2D currentCompartmentFullBounds = currentCompartment.getFullBoundsReference(); 
			if (currentCompartmentFullBounds.contains(mouseReleasePoint)) {
				return currentCompartment;
			}
		}
		return null;
	}
	
}
