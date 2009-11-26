package edu.iu.epic.modelbuilder.gui.transition;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDToLableMap;
import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.IDGenerator;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modelbuilder.gui.utility.Observer;
import edu.iu.epic.modelbuilder.gui.utility.PiccoloUtilities;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

public class ComplexTransition extends PPath {

	private static final long serialVersionUID = 1L;
	private Model inMemoryModel;
	private List<Transition> inMemoryInfectionTransitions = new ArrayList<Transition>();
	
	public ComplexTransition(PNode sourceCompartment, 
							 PNode targetCompartment,
							 List<ComplexTransitionInfectionInformation> infections,
							 IDGenerator objectIDGenerator, 
							 Model inMemoryModel,
							 NotificationArea[] notificationAreas, 
							 PSwingCanvas mainWorkbenchCanvas) {
		super();
		this.inMemoryModel = inMemoryModel;
		createComplexTransition(sourceCompartment, 
							   targetCompartment,
							   objectIDGenerator,
							   notificationAreas,
							   mainWorkbenchCanvas);
		
		if (infections == null) {
			
			infections = new ArrayList<ComplexTransitionInfectionInformation>();
			
			infections.add(
					new ComplexTransitionInfectionInformation(
								null,
								null));
			
		}
		
		
		for (ComplexTransitionInfectionInformation infection : infections) {
			System.out.println(infection.getInfectorCompartmentName() + " -- " 
								+ infection.getTransitionRatio() );
			
			
			if (addInfector(sourceCompartment, 
							targetCompartment,
							infection.getInfectorCompartmentName(),
							infection.getTransitionRatio(),
							notificationAreas, 
							mainWorkbenchCanvas)) {
				//TODO: since this is the defaultbehavior of the complex transition
				//on its failure we should delete the complex transition & display 
				//an error.
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
			
			
		}
	
	}

	/**
	 * @param sourceCompartment
	 * @param targetCompartment
	 * @param notificationAreas 
	 * @param mainWorkbenchCanvas 
	 */ 
	private void createComplexTransition(final PNode sourceCompartment,
										 final PNode targetCompartment,
										 final IDGenerator pObjectIDGenerator,
										 final NotificationArea[] notificationAreas, 
										 final PSwingCanvas mainWorkbenchCanvas) {
		
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
									   GlobalConstants.COMPLEX_TRANSITION_TYPE_ATTRIBUTE_VALUE);

		String newComplexTransitionID = pObjectIDGenerator.getComplexTransitionCounter();
		this.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
				  					   newComplexTransitionID);

		PNode arrow = createArrow(targetCompartment, 
								  normalizedBounds1,
								  normalizedBounds2);
		
		this.addChild(arrow);
		
		double infectorAdderPositionX = normalizedBounds1.getX() 
										+ 0.9 
											* (arrow.getFullBoundsReference().getX() 
													- normalizedBounds1.getX())
										+ 0.5 
											* GlobalConstants.INFECTOR_ADDER_DIMENSIONS.getWidth();
		
		double infectorAdderPositionY = normalizedBounds1.getY() 
										+ 0.9 
											* (arrow.getFullBoundsReference().getY() 
													- normalizedBounds1.getY())
										+ 0.5 
											* GlobalConstants.INFECTOR_ADDER_DIMENSIONS.getHeight();
		
		PNode infectorAdderHandle = PPath.createEllipse(0, 
				 0, 
				 (float) GlobalConstants.INFECTOR_ADDER_DIMENSIONS.getWidth(), 
				 (float) GlobalConstants.INFECTOR_ADDER_DIMENSIONS.getHeight());
		infectorAdderHandle.setPaint(Color.MAGENTA);
		
		infectorAdderHandle.centerFullBoundsOnPoint(infectorAdderPositionX, infectorAdderPositionY);
		
		infectorAdderHandle.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
										 GlobalConstants.INFECTOR_ADDER_TYPE_ATTRIBUTE_VALUE);
		
		infectorAdderHandle.addAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME, Color.MAGENTA);
		
		infectorAdderHandle.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
										 "INFECTOR_ADDER_" + newComplexTransitionID);
		
		infectorAdderHandle.addInputEventListener(new PBasicInputEventHandler() {
			@Override
			public void mouseClicked(PInputEvent event) {
				addInfector(sourceCompartment, 
							targetCompartment,
							null,
							null,
							notificationAreas,
							mainWorkbenchCanvas);
			}
		});
		
		
		this.addChild(infectorAdderHandle);
		
	}

	/**
	 * @param sourceCompartment
	 * @param targetCompartment
	 * @param notificationAreas 
	 * @param mainWorkbenchCanvas 
	 * @param normalizedBounds1
	 * @param normalizedBounds2
	 * @return 
	 */ 
	private boolean addInfector(PNode sourceCompartment, 
								PNode targetCompartment,
								String infectorCompartmentName,
								String transitionRatio,
								NotificationArea[] notificationAreas, 
								PSwingCanvas mainWorkbenchCanvas) {
		
		Point2D.Double normalizedBounds1 = (Double) sourceCompartment
												.getFullBoundsReference().getCenter2D();
		
		Point2D.Double normalizedBounds2 = (Double) targetCompartment
												.getFullBoundsReference().getCenter2D();
		 
		PNode infectorInformationPanel = new InfectorInformationPanel(
											sourceCompartment,
											targetCompartment,
											infectorCompartmentName,
											transitionRatio, 
											new Point2D.Double(0, 0),
											inMemoryInfectionTransitions,
											inMemoryModel,
											notificationAreas,
											mainWorkbenchCanvas);
		
		
		
		boolean isInfectorAdditionSuccessful = 
			((InfectorInformationPanel) infectorInformationPanel).isInfectorAdditionSuccessful();

		if (isInfectorAdditionSuccessful) {

			infectorInformationPanel.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
					  GlobalConstants.INFECTOR_INFORMATION_PANEL_TYPE_ATTRIBUTE_VALUE);
			
			inMemoryInfectionTransitions.add(
					((InfectorInformationPanel) infectorInformationPanel)
							.getInMemoryInfectionTransition());
			
			this.addChild(infectorInformationPanel);
			/*
			 * Side-effects the current transition. It updates the transition attribute 
			 * that saves the infector references.
			 * */
			updateTransitionAttributeForInvolvedInfectors(infectorInformationPanel);
			
			refreshInfectorInformationPanels(this, normalizedBounds1, normalizedBounds2);
			
		}
		
		return isInfectorAdditionSuccessful;
		
	}

	/**
	 * @param transitions
	 */
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
			
			ComplexTransition.rotateArrow(bound1, bound2, arrow);
			
			Point2D.Double arrowDisplayPoint = 
				ComplexTransition.calculateArrowDisplayPosition(node2, bound1, bound2);
				
				
			if (arrowDisplayPoint != null) {
			arrow.centerFullBoundsOnPoint(arrowDisplayPoint.getX(), 
										  arrowDisplayPoint.getY());
			}
			
			

			refreshInfectorInformationPanels(transition, bound1, bound2);
			
			double infectorAdderPositionX = bound1.getX() 
											+ 0.9 
												* (arrow.getFullBoundsReference().getX() 
														- bound1.getX())
											+ 0.5 
												* GlobalConstants.INFECTOR_ADDER_DIMENSIONS
													.getWidth();
			
			double infectorAdderPositionY = bound1.getY() 
											+ 0.9 
												* (arrow.getFullBoundsReference().getY() 
														- bound1.getY())
											+ 0.5 
												* GlobalConstants.INFECTOR_ADDER_DIMENSIONS
													.getHeight();
			
			PNode infectorAdderHandle = 
				PiccoloUtilities
					.getChildComponentBasedOnGivenAttribute(
							transition, 
							GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
							GlobalConstants.INFECTOR_ADDER_TYPE_ATTRIBUTE_VALUE);
			
			infectorAdderHandle.centerFullBoundsOnPoint(infectorAdderPositionX, 
														infectorAdderPositionY);
			
	}

	/**
	 * @param transition
	 * @param bound1
	 * @param bound2
	 */
	private static void refreshInfectorInformationPanels(PPath transition,
			Point2D.Double bound1, Point2D.Double bound2) {
		List<PNode> infectors = 
				(List<PNode>) transition
						.getAttribute(GlobalConstants.TRANSITION_INVOLVED_INFECTORS_ATTRIBUTE_NAME);
		
		int numberOfInfectors = infectors.size();
		double smallestDistanceUnit = 1.0 / (numberOfInfectors + 1.0);
		
		int infectorIndex = 1;
		for (PNode currentInfector : infectors) { 
			
			double infectorConnectingPointX = bound1.getX() 
											  + (smallestDistanceUnit * infectorIndex) 
											  *  (bound2.getX() - bound1.getX());
				
			double infectorConnectingPointY = bound1.getY() 
											  + (smallestDistanceUnit * infectorIndex) 
											  *  (bound2.getY() - bound1.getY());

			((InfectorInformationPanel) currentInfector)
					.refreshInfectorInformationPanel(new Point2D.Double(
							infectorConnectingPointX,
							infectorConnectingPointY
			));
			
			infectorIndex++;
		}
	}
	
	public void removeInfectorInformationPanels(PNode currentSelectedNode) {
		System.out.println(currentSelectedNode);
		
		List<PNode> infectors = 
			(List<PNode>) currentSelectedNode
					.getAttribute(GlobalConstants.TRANSITION_INVOLVED_INFECTORS_ATTRIBUTE_NAME);
	
		for (PNode currentInfector : infectors) { 
			
			Observer infectorBoxModel = 
				((InfectorInformationPanel)currentInfector).getInfectorComboBoxModel();
			CompartmentIDToLableMap.removeObserver(infectorBoxModel);
		}
		
	}
	
	public void removeInMemoryInfectionTransitions() {
		
		//TODO: remove transition from the arraylist also
		for (Transition inMemoryInfectionTransition : inMemoryInfectionTransitions) {
			inMemoryModel.removeTransition(inMemoryInfectionTransition);
		}
		
	}
	
	private void updateTransitionAttributeForInvolvedInfectors(
			PNode infectorInformationPanel) {
		
		List<PNode> currentInvolvedInfectors = 
			(List<PNode>) this
				.getAttribute(GlobalConstants.TRANSITION_INVOLVED_INFECTORS_ATTRIBUTE_NAME);
		
		/*
		 * Infectors Attribute is not initialized yet for the complex transition.
		 * */
		if (currentInvolvedInfectors == null) {
			currentInvolvedInfectors = new ArrayList<PNode>();
			currentInvolvedInfectors.add(infectorInformationPanel);
			this.addAttribute(GlobalConstants.TRANSITION_INVOLVED_INFECTORS_ATTRIBUTE_NAME,
										   currentInvolvedInfectors);
		} else {
			currentInvolvedInfectors.add(infectorInformationPanel);
		}
	}
	
	private void updateTransitionAttributeForInvolvedCompartments(
			PNode sourceCompartment, PNode targetCompartment) {
		List<PNode> tempPNodePlaceHolder = new ArrayList<PNode>();
		
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
		 * ComplexTransition.
		 */
		tempPNodePlaceHolder = (ArrayList) sourceCompartment
				.getAttribute(GlobalConstants.COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME);
		
		
		tempPNodePlaceHolder.add(this);
		
		
		/*
		 * Updating Target PCompartment with reference to the newly added
		 * ComplexTransition.
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
		float[] defaultArrowXCoordinates = {arrowSideSize, 
											arrowSideSize + arrowSideSize * 0.5f, 
											2.0f * arrowSideSize};
		
		float[] defaultArrowYCoordinates = {arrowSideSize, 
											arrowSideSize 
												- (float) Math.pow(3.0f, 0.5) 
													* 0.5f 
													* (float) arrowSideSize, 
											arrowSideSize};

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
		
		List<Point2D.Double> edgePoints = getComplexTransitionBoundPoints(
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
	private static List<Point2D.Double> getComplexTransitionBoundPoints(
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
	 * modification to the algorithm mentioned. >Explain g<.
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
