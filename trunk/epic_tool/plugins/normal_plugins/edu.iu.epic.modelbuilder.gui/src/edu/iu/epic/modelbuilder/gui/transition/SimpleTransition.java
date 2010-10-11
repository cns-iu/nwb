package edu.iu.epic.modelbuilder.gui.transition;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import edu.iu.epic.modelbuilder.gui.compartment.PCompartment;
import edu.iu.epic.modelbuilder.gui.editablelabel.EditableLabel;
import edu.iu.epic.modelbuilder.gui.editablelabel.TransitionEditableLabelEventHandler;
import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.IDGenerator;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modelbuilder.gui.utility.PiccoloUtilities;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterExpressionException;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * @author cdtank
 *
 */
public class SimpleTransition extends PPath {

	private static final long serialVersionUID = 1L;
	private Model inMemoryModel;
	private Transition inMemoryRatioTransition;
	
	public SimpleTransition(PNode sourceCompartment, 
							PNode targetCompartment,
							IDGenerator pObjectIDGenerator,
							String transitionRatio,
							Model inMemoryModel, 
							NotificationArea[] notificationAreas) {
		super();
		this.inMemoryModel = inMemoryModel;
		
		
		
		//TODO: do something useful with the boolean of inmemeomry addition
		createSimpleTransition(sourceCompartment, 
							   targetCompartment, 
							   pObjectIDGenerator, 
							   transitionRatio,
							   notificationAreas);

	}

	
	/**
	 * @param sourceCompartment
	 * @param targetCompartment
	 * @param transitionRatio 
	 * @param notificationAreas 
	 * @param bound1
	 * @param bound2
	 * @return 
	 */
	private boolean createSimpleTransition(PNode sourceCompartment,
										   PNode targetCompartment,
										   IDGenerator pObjectIDGenerator, 
										   String transitionRatio, 
										   NotificationArea[] notificationAreas) {
		
		if (transitionRatio == null 
				|| transitionRatio.equalsIgnoreCase("")) {
			transitionRatio = GlobalConstants.SIMPLE_TRANSITION_RATIO_DEFAULT_VALUE;
		}
		
		
		boolean isInMemoryTransitionAdditionSuccessful = 
					inMemoryAddRatioTransition(sourceCompartment, 
											   targetCompartment, 
											   transitionRatio,
											   notificationAreas);
		
		/*
		 * Only if in memory addition was successful then go ahead and create the gui.
		 * */
		if (isInMemoryTransitionAdditionSuccessful) {
		
			Point2D.Double normalizedBounds1 = (Point2D.Double) sourceCompartment
					.getFullBoundsReference()
							.getCenter2D();
			
			Point2D.Double normalizedBounds2 = (Point2D.Double) targetCompartment
					.getFullBoundsReference()
							.getCenter2D();		
			
			this.moveTo((float) normalizedBounds1.getX(),
			(float) normalizedBounds1.getY());
			this.lineTo((float) normalizedBounds2.getX(),
			(float) normalizedBounds2.getY());
			
			this.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
							  GlobalConstants.SIMPLE_TRANSITION_TYPE_ATTRIBUTE_VALUE);
			
			this.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
							  pObjectIDGenerator.getSimpleTransitionCounter());
			
			PNode arrow = new Arrow(targetCompartment, 
									normalizedBounds1,
									normalizedBounds2);
			
			this.addChild(arrow);
			
			double labelPositionX = (normalizedBounds1.getX() 
									+ normalizedBounds2.getX()) * 0.5 + 5.0;
			double labelPositionY = (normalizedBounds1.getY() 
									+ normalizedBounds2.getY()) * 0.5;
			
			EditableLabel simpleTransitionLabel = new EditableLabel(
					GlobalConstants.SIMPLE_TRANSITION_RATIO_ATTRIBUTE_NAME,
					transitionRatio,
					labelPositionX,
					labelPositionY,
					new TransitionEditableLabelEventHandler(inMemoryRatioTransition, 
															inMemoryModel,
															notificationAreas));
	
			double labelWidth = simpleTransitionLabel.getWidth();
			
			if (labelWidth + labelPositionX > normalizedBounds2.getX()
					&& normalizedBounds2.getX() > normalizedBounds1.getX()) {
				double newLabelPositionX = labelPositionX 
										   + labelWidth 
										   - normalizedBounds2.getX() + 5.0;
				simpleTransitionLabel.offset(-newLabelPositionX, 0.0);
			}
			
			simpleTransitionLabel.makeEditableLabelBackgroundOpaque();
			simpleTransitionLabel.moveToFront();
			this.addChild(simpleTransitionLabel);
			
			/*
			 * Side-effects the compartments involved in the transition. It updates the 
			 * compartment attribute that saves the transition that are in relationship
			 * with the compartment.  
			 * */
			updateRelatedCompartmentAttributes(sourceCompartment, targetCompartment);
	
			/*
			 * Side-effects the current transition. It updates the transition attribute 
			 * that saves the sourceCompartment - targetCompartment relationship.
			 * */
			updateTransitionAttributeForInvolvedCompartments(sourceCompartment,
					targetCompartment);
		
		}
		
		return isInMemoryTransitionAdditionSuccessful;
	}


	/**
	 * @param sourceCompartment
	 * @param targetCompartment
	 * @param transitionRatio
	 * @param notificationAreas 
	 * @return
	 */
	private boolean inMemoryAddRatioTransition(PNode sourceCompartment,
											   PNode targetCompartment, 
											   String transitionRatio, 
											   NotificationArea[] notificationAreas) {
		boolean isInMemoryTransitionAdditionSuccessful = true;
		try {
			
			inMemoryRatioTransition = 
				inMemoryModel.addRatioTransition(
						((PCompartment) sourceCompartment).getInMemoryCompartment(), 
						((PCompartment) targetCompartment).getInMemoryCompartment(), 
						transitionRatio);
			
		} catch (InvalidParameterExpressionException e) {
			//TODO: how best to handle this? should i create a parameter definition? 
			//or a new parameter expression. in this specific case it will NEVER happen
			//because the default value of ratio is used which is legal.
			System.out.println("invalid parameetr expression");
			isInMemoryTransitionAdditionSuccessful = false;
			notificationAreas[1].addNotification("\"" + transitionRatio 
					 						+ "\" is an invalid parameter expression.");
			
		}
		
		/*
		 * check if the ratio is defined yet.
		 * */
		if (isInMemoryTransitionAdditionSuccessful) {
			try {
				notificationAreas[0].addAllNotifications(
						inMemoryModel.listUnboundReferencedParameters());
			} catch (InvalidParameterExpressionException exception) {
				notificationAreas[1].addNotification("Errors in testing of undefined parameters.");
			}
		}
		
		return isInMemoryTransitionAdditionSuccessful;
	}

	
	
	public static void refreshTransition(PPath transition) {
			
			ArrayList nodes = 
				(ArrayList) transition
					.getAttribute(GlobalConstants.TRANSITION_INVOLVED_COMPARTMENTS_ATTRIBUTE_NAME);
			PNode node1 = (PNode) nodes.get(0);
			PNode node2 = (PNode) nodes.get(1);
			
			transition.reset();
			// Note that the node's "FullBounds" must be used (instead of just the "Bound") 
			// because the nodes have non-identity transforms which must be included when
			// determining their position.
			Point2D.Double bound1 = (Point2D.Double) node1.getFullBounds().getCenter2D();
			Point2D.Double bound2 = (Point2D.Double) node2.getFullBounds().getCenter2D();

			transition.moveTo((float) bound1.getX(), (float) bound1.getY());
			transition.lineTo((float) bound2.getX(), (float) bound2.getY());
			
			PNode arrow = PiccoloUtilities
								.getChildComponentBasedOnGivenAttribute(
										transition, 
										GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
										GlobalConstants.TRANSITION_ARROW_TYPE_ATTRIBUTE_VALUE);
			
			Arrow.rotateArrow(bound1, bound2, arrow);

			
			
			Point2D.Double arrowDisplayPoint = 
				Arrow.calculateArrowDisplayPosition(node2, bound1, bound2);
				
				
			if (arrowDisplayPoint != null) {
			arrow.centerFullBoundsOnPoint(arrowDisplayPoint.getX(), 
														   arrowDisplayPoint.getY());
			}
			
			
			PNode label = 
				PiccoloUtilities
						.getChildComponentBasedOnGivenAttribute(
								transition, 
								GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
								GlobalConstants.SIMPLE_TRANSITION_RATIO_ATTRIBUTE_NAME);
			
			label.setX((bound1.getX() + bound2.getX()) * 0.5 + 5.0);
			label.setY((bound1.getY() + bound2.getY()) * 0.5);
			
	}
	
	public void removeInMemoryRatioTransition() {
		inMemoryModel.removeTransition(inMemoryRatioTransition);
		System.out.println("SIMPLE i was deleted " + inMemoryRatioTransition);
		for (Transition name : inMemoryModel.getTransitions()) {
			System.out.println("RATIO transition name > " + name );
		}
	}
	
	private void updateTransitionAttributeForInvolvedCompartments(
			PNode sourceCompartment, PNode targetCompartment) {
		List<PNode> tempPNodePlaceHolder;
		tempPNodePlaceHolder = new ArrayList<PNode>();
		
		tempPNodePlaceHolder.add(sourceCompartment);
		tempPNodePlaceHolder.add(targetCompartment);
		
		this.addAttribute(GlobalConstants.TRANSITION_INVOLVED_COMPARTMENTS_ATTRIBUTE_NAME,
						  tempPNodePlaceHolder);
	}

	private void updateRelatedCompartmentAttributes(PNode sourceCompartment,
			PNode targetCompartment) {
		List<PNode> tempPNodePlaceHolder;
		/*
		 * Updating Source PCompartment with reference to the newly added
		 * SimpleTransition.
		 */
		tempPNodePlaceHolder = (ArrayList) sourceCompartment
				.getAttribute(GlobalConstants.COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME);
		
		
		tempPNodePlaceHolder.add(this);
		
		
		/*
		 * Updating Target PCompartment with reference to the newly added
		 * SimpleTransition.
		 */
		
		
		tempPNodePlaceHolder = (ArrayList) targetCompartment
				.getAttribute(GlobalConstants.COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME);
		
		
		tempPNodePlaceHolder.add(this);
	}

}
