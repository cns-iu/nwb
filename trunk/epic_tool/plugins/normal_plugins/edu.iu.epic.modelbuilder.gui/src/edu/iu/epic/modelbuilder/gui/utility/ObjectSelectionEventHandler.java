package edu.iu.epic.modelbuilder.gui.utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import edu.iu.epic.modelbuilder.gui.compartment.PCompartment;
import edu.iu.epic.modelbuilder.gui.transition.ComplexTransition;
import edu.iu.epic.modelbuilder.gui.transition.SimpleTransition;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.event.PSelectionEventHandler;

public class ObjectSelectionEventHandler extends PSelectionEventHandler {

	private CompartmentIDToLabelMap compartmentIDToLabelMap;

	public ObjectSelectionEventHandler(PNode marqueeParent, 
									   List selectableParents, 
									   CompartmentIDToLabelMap compartmentIDToLabelMap) {
		super(marqueeParent, selectableParents);
		this.compartmentIDToLabelMap = compartmentIDToLabelMap;
	}
	
	public ObjectSelectionEventHandler(PNode marqueeParent, 
									   PNode selectableParent, 
									   CompartmentIDToLabelMap compartmentIDToLabelMap) {
		super(marqueeParent, selectableParent);
		this.compartmentIDToLabelMap = compartmentIDToLabelMap;
	}
	
	@Override
	public void decorateSelectedNode(PNode node) {
		
		((PPath) node).setStroke(new BasicStroke(2.1f, 
												 BasicStroke.CAP_ROUND, 
												 BasicStroke.JOIN_BEVEL));
		((PPath) node).setStrokePaint(Color.RED);
		
	}
	
	@Override
	public void undecorateSelectedNode(PNode node) {
		((PPath) node).setStroke(new BasicStroke());
		((PPath) node).setStrokePaint(Color.BLACK);
	}

	@Override
	public void keyPressed(PInputEvent e) {
		
		/* To delete all the transitions that are in relationship with the deleted 
		 * compartment, if a Compartment is selected, that is. 
		 * */
		if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
			for (Iterator<PNode> selectedNodesIterator = getSelection().iterator(); 
					selectedNodesIterator.hasNext();) {
				PNode currentSelectedNode = (PNode) selectedNodesIterator.next();
				
				if (GlobalConstants.COMPARTMENT_TYPE_ATTRIBUTE_VALUE
						.equalsIgnoreCase((String) currentSelectedNode
								.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME))) {
					
					String compartmentID = currentSelectedNode
											.getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME)
												.toString();
					
					/*
					 * This will take care of deleting all in memory transitions as well.
					 * */
					((PCompartment) currentSelectedNode).removeInMemoryCompartment();
					
					compartmentIDToLabelMap.removeCompartmentID(compartmentID);
					
					
					List<PNode> currentSelectedNodeTransitions = 
							((List<PNode>) currentSelectedNode
									.getAttribute(GlobalConstants
											.COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME));
					
					for (PNode currentTransition : currentSelectedNodeTransitions) {
						currentTransition.removeFromParent();
						currentTransition = null;
					}
					
				} else if (GlobalConstants.SIMPLE_TRANSITION_TYPE_ATTRIBUTE_VALUE
								.equalsIgnoreCase((String) currentSelectedNode
										.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME))) {
					
					System.out.println("SIMPLE TRANSITION DELETE");
					((SimpleTransition) currentSelectedNode).removeInMemoryRatioTransition();
					
				} else if (GlobalConstants.COMPLEX_TRANSITION_TYPE_ATTRIBUTE_VALUE
						.equalsIgnoreCase((String) currentSelectedNode
								.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME))) {
					
					System.out.println("COMPLEX TRANSITION DELETE");
					((ComplexTransition) currentSelectedNode)
							.removeInfectorInformationPanels(currentSelectedNode);
					
					((ComplexTransition) currentSelectedNode).removeInMemoryInfectionTransitions();
					
				}
			}
		}
		super.keyPressed(e);		
	}
	
	@Override
	protected void drag(PInputEvent e) {
		String draggedNodeType = (String) e.getPickedNode()
			.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME);
		
		/*
		 * Do not allow drag events if the picked node type is a transition because visually 
		 * that will dislodge the transition from their natural position & give an illusion 
		 * of being disconnected to all the compartments.   
		 * */
		if (draggedNodeType == null 
				|| (!GlobalConstants.SIMPLE_TRANSITION_TYPE_ATTRIBUTE_VALUE
						.equalsIgnoreCase(draggedNodeType)
					&& !GlobalConstants.COMPLEX_TRANSITION_TYPE_ATTRIBUTE_VALUE
						.equalsIgnoreCase(draggedNodeType))) {
			super.drag(e);
		} 		
	}
}
