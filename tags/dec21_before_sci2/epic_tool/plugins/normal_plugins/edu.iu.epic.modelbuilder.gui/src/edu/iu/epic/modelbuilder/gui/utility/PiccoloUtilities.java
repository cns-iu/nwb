package edu.iu.epic.modelbuilder.gui.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.iu.epic.modelbuilder.gui.transition.ComplexTransition;
import edu.iu.epic.modelbuilder.gui.transition.SimpleTransition;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class PiccoloUtilities {
	
	public static PNode getChildComponentBasedOnGivenAttribute(PNode parentNode, 
															   String attributeKey, 
															   String attributeValue) {
		Iterator<PNode> childIterator = parentNode.getChildrenIterator(); 
		while (childIterator.hasNext()) {
			PNode childComponent = (PNode) childIterator.next();
			if (attributeValue
					.equalsIgnoreCase((String) childComponent.getAttribute(attributeKey))) {
				return childComponent;
			}
		}
		return null;
	}
	
	public static void refreshTransitions(ArrayList<PPath> transitions) {
		for (PPath currentTransition : transitions) {
			if (GlobalConstants.SIMPLE_TRANSITION_TYPE_ATTRIBUTE_VALUE
					.equalsIgnoreCase(currentTransition
										.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME)
											.toString())) {
				SimpleTransition.refreshTransition(currentTransition);
				
			} else if (GlobalConstants.COMPLEX_TRANSITION_TYPE_ATTRIBUTE_VALUE
								.equalsIgnoreCase(currentTransition
										.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME)
											.toString())) {
				ComplexTransition.refreshTransition(currentTransition);
				
			}
			
		}
	}
	
	public static boolean isTransitionDuplicate(PNode sourceCompartment,
			  									PNode targetCompartment,
			  									PLayer transitionLayer
			  									) {

		if (((String) sourceCompartment.getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME))
				.equalsIgnoreCase((String) targetCompartment
						.getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME))) {
			System.out.println("SOURCE & TARGET Compartments are same.");
			return true;
		}
		
		for (Iterator transitionIterator = transitionLayer.getChildrenIterator();
				transitionIterator.hasNext();) {
			PPath transition = (PPath) transitionIterator.next();
			
			List<PNode> compartmentsInvolved = 
					(ArrayList<PNode>) transition
							.getAttribute(GlobalConstants
									.TRANSITION_INVOLVED_COMPARTMENTS_ATTRIBUTE_NAME); 
			
			String newTransitionSourceCompartmentID = 
					(String) sourceCompartment
							.getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME);
			
			String currentTransitionSourceComprtmentID = 
					(String) compartmentsInvolved.get(0)
							.getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME);
			
			String newTransitionTargetCompartmentID = 
					(String) targetCompartment
							.getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME);
			
			String currentTransitionTargetCompartmentID = 
					(String) compartmentsInvolved.get(1)
							.getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME);
			
			if (newTransitionSourceCompartmentID
					.equalsIgnoreCase(currentTransitionSourceComprtmentID)
						&& newTransitionTargetCompartmentID
								.equalsIgnoreCase(currentTransitionTargetCompartmentID)) {
				System.out.println("Duplicate Transition Detected!");
				return true;
			}
		} 
		return false;
	}

}
