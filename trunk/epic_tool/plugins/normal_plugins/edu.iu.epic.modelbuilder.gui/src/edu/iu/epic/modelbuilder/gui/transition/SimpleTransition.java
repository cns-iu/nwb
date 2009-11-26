package edu.iu.epic.modelbuilder.gui.transition;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
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
							boolean isSecondary, 
							Model inMemoryModel, 
							NotificationArea[] notificationAreas) {
		super();
		this.inMemoryModel = inMemoryModel;
		
		
		
		//TODO: do something useful with the boolean of inmemeomry addition
		createSimpleTransition(sourceCompartment, 
							   targetCompartment, 
							   pObjectIDGenerator, 
							   transitionRatio,
							   isSecondary,
							   notificationAreas);

	}

	
	/**
	 * @param sourceCompartment
	 * @param targetCompartment
	 * @param transitionRatio 
	 * @param isSecondary 
	 * @param notificationAreas 
	 * @param bound1
	 * @param bound2
	 * @return 
	 */
	private boolean createSimpleTransition(PNode sourceCompartment,
										   PNode targetCompartment,
										   IDGenerator pObjectIDGenerator, 
										   String transitionRatio, 
										   boolean isSecondary, 
										   NotificationArea[] notificationAreas) {
		
		if (transitionRatio == null 
				|| transitionRatio.equalsIgnoreCase("")) {
			transitionRatio = GlobalConstants.SIMPLE_TRANSITION_RATIO_DEFAULT_VALUE;
		}
		
		
		boolean isInMemoryTransitionAdditionSuccessful = 
					inMemoryAddRatioTransition(sourceCompartment, 
											   targetCompartment, 
											   transitionRatio,
											   isSecondary,
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
			
			PNode arrow = createArrow(targetCompartment, 
									  normalizedBounds1,
									  normalizedBounds2);
			
			this.addChild(arrow);
			
			EditableLabel simpleTransitionLabel = new EditableLabel(
					GlobalConstants.SIMPLE_TRANSITION_RATIO_ATTRIBUTE_NAME,
					transitionRatio,
					(normalizedBounds1.getX() + normalizedBounds2.getX()) * 0.5 + 5.0,
					(normalizedBounds1.getY() + normalizedBounds2.getY()) * 0.5,
					new TransitionEditableLabelEventHandler(inMemoryRatioTransition, 
															inMemoryModel,
															notificationAreas));
	
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
	 * @param isSecondary 
	 * @param notificationAreas 
	 * @return
	 */
	private boolean inMemoryAddRatioTransition(PNode sourceCompartment,
											   PNode targetCompartment, 
											   String transitionRatio, 
											   boolean isSecondary, 
											   NotificationArea[] notificationAreas) {
		boolean isInMemoryTransitionAdditionSuccessful = true;
		try {
			
			inMemoryRatioTransition = 
				inMemoryModel.addRatioTransition(
						((PCompartment) sourceCompartment).getInMemoryCompartment(), 
						((PCompartment) targetCompartment).getInMemoryCompartment(), 
						transitionRatio, 
						isSecondary);
			
		} catch (InvalidParameterExpressionException e) {
			//TODO: how best to handle this? shoulld i creata parameter definition? 
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
			
			SimpleTransition.rotateArrow(bound1, bound2, arrow);

			
			
			Point2D.Double arrowDisplayPoint = 
				SimpleTransition.calculateArrowDisplayPosition(node2, bound1, bound2);
				
				
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
			
//			System.out.println(label);
			
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

	private PNode createArrow(PNode targetCompartment,
			Point2D.Double normalizedBounds1, Point2D.Double normalizedBounds2) {
		
		PNode arrow = createDefaultArrow();

		arrow.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
						   GlobalConstants.TRANSITION_ARROW_TYPE_ATTRIBUTE_VALUE);
		/*
		 * Side-effects the orientation of the arrow.
		 * */
		rotateArrow(normalizedBounds1, normalizedBounds2, arrow);

		Point2D.Double arrowDisplayPoint = calculateArrowDisplayPosition(
				targetCompartment, normalizedBounds1, normalizedBounds2);
		
		if (arrowDisplayPoint != null) {
			arrow.centerFullBoundsOnPoint(arrowDisplayPoint.getX(), 
														   arrowDisplayPoint.getY());
			}
		
		arrow.setPickable(false);
		arrow.setPaint(Color.DARK_GRAY);
		
		return arrow;
	}

	public static void rotateArrow(Point2D.Double transitionEndpoint1,
			Point2D.Double transitionEndpoint2, PNode arrow) {
		float slope = (float) ((float) (transitionEndpoint2.getY() - transitionEndpoint1
				.getY()) / (transitionEndpoint2.getX() - transitionEndpoint1.getX()));

		double rotateByThis = Math.atan(slope) + Math.toRadians(90)  - arrow.getRotation();

		if (transitionEndpoint2.getX() < transitionEndpoint1.getX()) {
			rotateByThis += Math.toRadians(180);
		}

		arrow.rotateInPlace(rotateByThis);
	}

	private PNode createDefaultArrow() {
		int arrowSideSize = 10;
		float[] defaultArrowXCoordinates = { arrowSideSize, arrowSideSize + arrowSideSize * 0.5f, 2.0f * arrowSideSize };
		float[] defaultArrowYCoordinates = { arrowSideSize, arrowSideSize - (float) Math.pow(3.0f, 0.5) * 0.5f
						* (float) arrowSideSize, arrowSideSize };

		PNode arrow = PPath.createPolyline(defaultArrowXCoordinates, defaultArrowYCoordinates);
		return arrow;
	}

	public static Point2D.Double calculateArrowDisplayPosition(
			PNode targetCompartment, Point2D.Double normalizedBounds1,
			Point2D.Double normalizedBounds2) {
		/*
		 * It always has to be node2
		 * */
		Rectangle2D nodeBounds = targetCompartment.getFullBoundsReference();
		
		List<Point2D.Double> edgePoints = getSimpleTransitionBoundPoints(
											normalizedBounds1, normalizedBounds2);
		
		List<Point2D.Double> nodeBoundsPoints = getCompartmentBoundPoints(nodeBounds);
		
		Point2D.Double arrowDisplayPoint = getArrowDisplayPoint(edgePoints, nodeBoundsPoints);
		return arrowDisplayPoint;
	}
	
	/**
	 * @param bound1
	 * @param bound2
	 * @return
	 */
	private static List<Point2D.Double> getSimpleTransitionBoundPoints(
			Point2D.Double bound1, Point2D.Double bound2) {
		List<Point2D.Double> edgePoints = new ArrayList<Point2D.Double>();
		Point2D.Double edgePoint = new Point2D.Double(bound1.getX(), bound1.getY()); 
		edgePoints.add(edgePoint);
		
		edgePoint = new Point2D.Double(bound2.getX(), bound2.getY()); 
		edgePoints.add(edgePoint);
		return edgePoints;
	}
	/**
	 * @param nodeBounds
	 * @return
	 */
	private static List<Point2D.Double> getCompartmentBoundPoints(
			Rectangle2D nodeBounds) {
		List<Point2D.Double> nodeBoundsPoints = new ArrayList<Point2D.Double>();
		
		Point2D.Double nodeBoundPoint = new Point2D.Double(nodeBounds.getX(), nodeBounds.getY()); 
		nodeBoundsPoints.add(nodeBoundPoint);
		
		nodeBoundPoint = new Point2D.Double(nodeBounds.getX() + nodeBounds.getWidth(), 
											nodeBounds.getY()); 
		nodeBoundsPoints.add(nodeBoundPoint);
		
		nodeBoundPoint = new Point2D.Double(nodeBounds.getX() + nodeBounds.getWidth(), 
											nodeBounds.getY() + nodeBounds.getHeight()); 
		nodeBoundsPoints.add(nodeBoundPoint);

		nodeBoundPoint = new Point2D.Double(nodeBounds.getX(), 
											nodeBounds.getY() + nodeBounds.getHeight()); 
		nodeBoundsPoints.add(nodeBoundPoint);
		return nodeBoundsPoints;
	}

	/**
	 * Based on http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/ .
	 * @param edgePoints
	 * @param nodeBoundsPoints
	 * @return
	 */
	private static Double getArrowDisplayPoint(List<Double> edgePoints,
											List<Double> nodeBoundsPoints) {
		
		Point2D.Double intersectingPoint = null;
		
		for (int ii = 0; ii < 4; ii++) {
		intersectingPoint = getIntersectingPoint(edgePoints.get(0), 
												 edgePoints.get(1),
												 nodeBoundsPoints.get(ii),
												 nodeBoundsPoints.get((ii + 1) % 4));
		if (intersectingPoint != null) {
			return intersectingPoint;
		}
		
		}
		return null;
	}
	
	/**
	 * Refer to
	 * http://stackoverflow.com/questions/563198/how-do-you-detect-where
	 * -two-line-segments-intersect for the explanation. Also there is the a
	 * modification to the algorithm mentioned. >Explain g<
	 * 
	 * @param edgePoint1
	 * @param edgePoint2
	 * @param nodeSidePoint1
	 * @param nodeSidePoint2
	 * @return
	 */
	private static Double getIntersectingPoint(Double edgePoint1, Double edgePoint2,
			Double nodeSidePoint1, Double nodeSidePoint2) {

		double intersectingPointX, intersectingPointY;
		Point2D.Double intersectingPoint;

		Point2D.Double E = new Point2D.Double(edgePoint2.getX()
				- edgePoint1.getX(), edgePoint2.getY() - edgePoint1.getY());

		Point2D.Double F = new Point2D.Double(nodeSidePoint2.getX()
				- nodeSidePoint1.getX(), nodeSidePoint2.getY()
				- nodeSidePoint1.getY());

		Point2D.Double P = new Point2D.Double(-1 * E.getY(), E.getX());

		Point2D.Double Q = new Point2D.Double(-1 * F.getY(), F.getX());

		double h_denominator = (F.getX() * P.getX()) + (F.getY() * P.getY());

		double g_denominator = (E.getX() * Q.getX()) + (E.getY() * Q.getY());

		if (h_denominator == 0.0 || g_denominator == 0.0) {
			return null;
		}

		Point2D.Double A_C = new Point2D.Double(edgePoint1.getX()
				- nodeSidePoint1.getX(), edgePoint1.getY()
				- nodeSidePoint1.getY());

		double h_numerator = (A_C.getX() * P.getX()) + (A_C.getY() * P.getY());
		double g_numerator = -1.0
				* ((A_C.getX() * Q.getX()) + (A_C.getY() * Q.getY()));

		double h = h_numerator / h_denominator;
		double g = g_numerator / g_denominator;

		if ((h <= 1.0 && h >= 0.0) && (g <= 1.0 && g >= 0.0)) {
			intersectingPointX = nodeSidePoint1.getX() + F.getX() * h;
			intersectingPointY = nodeSidePoint1.getY() + F.getY() * h;
			intersectingPoint = new Point2D.Double(intersectingPointX,
					intersectingPointY);

			return intersectingPoint;
		} else {
			return null;
		}
	}

}
