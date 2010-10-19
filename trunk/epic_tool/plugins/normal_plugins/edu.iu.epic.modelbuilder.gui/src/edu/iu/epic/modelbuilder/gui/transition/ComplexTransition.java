package edu.iu.epic.modelbuilder.gui.transition;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDToLabelMap;
import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.IDGenerator;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDChangeObserver;
import edu.iu.epic.modelbuilder.gui.utility.PiccoloUtilities;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

@SuppressWarnings("serial")
public class ComplexTransition extends PPath {

	private Model inMemoryModel;
	private List<Transition> inMemoryInfectionTransitions = new ArrayList<Transition>();
	private CompartmentIDToLabelMap compartmentIDToLabelMap;
	
	public ComplexTransition(boolean createOnlyGUI, 
							 PNode sourceCompartment, 
							 PNode targetCompartment,
							 List<ComplexTransitionInfectionInformation> infections,
							 IDGenerator objectIDGenerator, 
							 CompartmentIDToLabelMap compartmentIDToLabelMap, 
							 Model inMemoryModel,
							 NotificationArea[] notificationAreas, 
							 PSwingCanvas mainWorkbenchCanvas) {
		super();
		this.inMemoryModel = inMemoryModel;
		this.compartmentIDToLabelMap = compartmentIDToLabelMap;
		
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
								null,
								null));
			
		}
		
		
		for (ComplexTransitionInfectionInformation infection : infections) {
			
			if (addInfector(createOnlyGUI,
							sourceCompartment, 
							targetCompartment,
							infection,
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

		PNode arrow = new Arrow(targetCompartment, 
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
				addInfector(false,
							sourceCompartment, 
							targetCompartment,
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
	 * @param infection 
	 * @param notificationAreas 
	 * @param mainWorkbenchCanvas 
	 * @param normalizedBounds1
	 * @param normalizedBounds2
	 * @return 
	 */ 
	private boolean addInfector(boolean createOnlyGUI,
								PNode sourceCompartment, 
								PNode targetCompartment,
								ComplexTransitionInfectionInformation infection, 
								NotificationArea[] notificationAreas, 
								PSwingCanvas mainWorkbenchCanvas) {
		
		Point2D.Double normalizedBounds1 = (Double) sourceCompartment
												.getFullBoundsReference().getCenter2D();
		
		Point2D.Double normalizedBounds2 = (Double) targetCompartment
												.getFullBoundsReference().getCenter2D();
		 
		PNode infectorInformationPanel = new InfectorInformationPanel(
											createOnlyGUI,
											sourceCompartment,
											targetCompartment,
											infection,
											new Point2D.Double(0, 0),
											inMemoryInfectionTransitions,
											compartmentIDToLabelMap,
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
			
			Arrow.rotateArrow(bound1, bound2, arrow);
			
			Point2D.Double arrowDisplayPoint = 
				Arrow.calculateArrowDisplayPosition(node2, bound1, bound2);
				
				
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
			
			CompartmentIDChangeObserver infectorBoxModel = 
				((InfectorInformationPanel) currentInfector).getInfectorComboBoxModel();
			compartmentIDToLabelMap.removeObserver(infectorBoxModel);
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



}
