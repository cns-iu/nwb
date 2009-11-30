package edu.iu.epic.modelbuilder.gui.transition;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iu.epic.modelbuilder.gui.compartment.PCompartment;
import edu.iu.epic.modelbuilder.gui.editablelabel.EditableLabel;
import edu.iu.epic.modelbuilder.gui.editablelabel.TransitionEditableLabelEventHandler;
import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDToLableMap;
import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modelbuilder.gui.utility.Observer;
import edu.iu.epic.modelbuilder.gui.utility.PiccoloUtilities;
import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.InfectionTransition;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.iu.epic.modeling.compartment.model.exception.CompartmentDoesNotExistException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterExpressionException;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.pswing.PComboBox;
import edu.umd.cs.piccolox.pswing.PSwing;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;

public class InfectorInformationPanel extends PNode {

	private static final long serialVersionUID = -76035194606028048L;
	
	private EditableLabel complexTransitionLabel;
	private PComboBox infectorComboBox;
	private Model inMemoryModel;
	private Transition inMemoryInfectionTransition;
	private boolean isInfectorAdditionSuccessful;

	private List<Transition> inMemoryInfectionTransitions;

	private NotificationArea[] notificationAreas; 

	public InfectorInformationPanel(PNode sourceCompartment, 
									PNode targetCompartment,
									String infectorCompartmentName,
									String transitionRatio, 
									Point2D.Double mainTransitionConnectingPoint, 
									List<Transition> inMemoryInfectionTransitions, 
									Model inMemoryModel,
									NotificationArea[] notificationAreas,
									PSwingCanvas mainWorkbenchCanvas) {
		super();
		this.inMemoryModel = inMemoryModel;
		this.inMemoryInfectionTransitions = inMemoryInfectionTransitions;
		this.notificationAreas = notificationAreas;
		
		if (transitionRatio == null 
				|| transitionRatio.equalsIgnoreCase("")) {
			transitionRatio = GlobalConstants.COMPLEX_TRANSITION_RATIO_DEFAULT_VALUE;
		}
		
		if (infectorCompartmentName == null
				|| infectorCompartmentName.equalsIgnoreCase("")) {

			infectorCompartmentName = 
				((EditableLabel) PiccoloUtilities.getChildComponentBasedOnGivenAttribute(
							sourceCompartment, 
							GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
							GlobalConstants.COMPARTMENT_LABEL_TYPE_ATTRIBUTE_VALUE)).getText();
			
			isInfectorAdditionSuccessful = addInMemoryInfectionTransition(
					((PCompartment) sourceCompartment).getInMemoryCompartment(),
					infectorCompartmentName,
					((PCompartment) targetCompartment).getInMemoryCompartment(),
					transitionRatio,
					inMemoryModel);
			
		} else {
			isInfectorAdditionSuccessful = addInMemoryInfectionTransition(
					((PCompartment) sourceCompartment).getInMemoryCompartment(),
					infectorCompartmentName,
					((PCompartment) targetCompartment).getInMemoryCompartment(),
					transitionRatio,
					inMemoryModel);
		}
		
		
        
		/*
		 * Go ahead with building of the infector info panel only if succeful 
		 * inmeomory was built in the first place.
		 * */
		//TODO: what to be done in the else situation?
		if (isInfectorAdditionSuccessful) {
			
			infectorComboBox = new InfectorComboBox(this, infectorCompartmentName);
		
			Map<String, PNode> infectorParentTypeToParentObject = new HashMap<String, PNode>();
			infectorParentTypeToParentObject.put("SOURCE", sourceCompartment);
			infectorParentTypeToParentObject.put("TARGET", targetCompartment);
			
			this.addAttribute(GlobalConstants.INFECTOR_PARENTS_ATTRIBUTE_NAME, 
							  infectorParentTypeToParentObject);
			
			PPath connectingEdge = new PPath(); 
			
			Point2D.Double comboBoxConnectingPoint = new Point2D.Double(
					mainTransitionConnectingPoint.getX() + 100.0,
					mainTransitionConnectingPoint.getY()
					);
			
			connectingEdge.moveTo((float) mainTransitionConnectingPoint.getX(),
								  (float) mainTransitionConnectingPoint.getY());
			
			connectingEdge.lineTo((float) comboBoxConnectingPoint.getX(),
								  (float) comboBoxConnectingPoint.getY());
			
			connectingEdge.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
					  GlobalConstants.CONNECTING_EDGE_TYPE_ATTRIBUTE_VALUE);
			
			this.addChild(connectingEdge);
			
			double labelPositionX = (mainTransitionConnectingPoint.getX() 
									+ comboBoxConnectingPoint.getX()) * 0.5 + 5.0;
			double labelPositionY = (mainTransitionConnectingPoint.getY() 
									+ comboBoxConnectingPoint.getY()) * 0.5;
			complexTransitionLabel = new EditableLabel(
					GlobalConstants.COMPLEX_TRANSITION_RATIO_ATTRIBUTE_NAME,
					transitionRatio,
					labelPositionX,
					labelPositionY,
					new TransitionEditableLabelEventHandler(inMemoryInfectionTransition, 
															inMemoryModel,
															notificationAreas));
	
			double labelWidth = complexTransitionLabel.getWidth();
			if (labelWidth + labelPositionX > comboBoxConnectingPoint.getX()
					&& comboBoxConnectingPoint.getX() > mainTransitionConnectingPoint.getX()) {
				double newLabelPositionX = labelPositionX 
										   + labelWidth 
										   - comboBoxConnectingPoint.getX() 
										   + 5.0;
				complexTransitionLabel.offset(-newLabelPositionX, 0.0);
			}
			
			complexTransitionLabel.makeEditableLabelBackgroundOpaque();
			complexTransitionLabel.moveToFront();
			this.addChild(complexTransitionLabel);
			
			PSwing infectorComboBoxWrapper = new PSwing(infectorComboBox);
			infectorComboBox.setEnvironment(infectorComboBoxWrapper, 
											mainWorkbenchCanvas);
	
	        infectorComboBoxWrapper.addAttribute(
	        		GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME,
	        		GlobalConstants.INFECTOR_COMBO_BOX_TYPE_ATTRIBUTE_VALUE);
	        
	        infectorComboBoxWrapper.translate(
	        		comboBoxConnectingPoint.getX(), 
	        		comboBoxConnectingPoint.getY() - (infectorComboBoxWrapper.getHeight() * 0.5));
	        
	        infectorComboBoxWrapper
	        	.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, 
	        							   new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
						PNode resizedComboBox = (PNode) propertyChangeEvent.getSource();
						refreshInfectorDeleterHandle(resizedComboBox);
					}
				});
	
	        this.addChild(infectorComboBoxWrapper);
	        
	        double infectorDeleterPositionX = comboBoxConnectingPoint.getX()   
											+ infectorComboBoxWrapper.getWidth()
											- GlobalConstants.INFECTOR_DELETER_DIMENSIONS
												.getWidth();
	
			double infectorDeleterPositionY = comboBoxConnectingPoint.getY()
											- 2 
												* GlobalConstants.INFECTOR_DELETER_DIMENSIONS
													.getHeight();
	
			
			PNode infectorDeleterHandle = PPath.createEllipse(
					0, 
					0, 
					(float) GlobalConstants.INFECTOR_DELETER_DIMENSIONS.getWidth(), 
					(float) GlobalConstants.INFECTOR_DELETER_DIMENSIONS.getHeight());
			
			infectorDeleterHandle.setPaint(Color.RED);
			
			infectorDeleterHandle.setX(infectorDeleterPositionX);
			infectorDeleterHandle.setY(infectorDeleterPositionY);
			
			infectorDeleterHandle.addAttribute(
						GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
						GlobalConstants.INFECTOR_DELETER_TYPE_ATTRIBUTE_VALUE); 
			
			infectorDeleterHandle.addInputEventListener(new PBasicInputEventHandler() {
				@Override
				public void mouseClicked(PInputEvent event) {
					deleteInfectorInformationPanel(event.getPickedNode().getParent());
				}
			});
			
			this.addChild(infectorDeleterHandle);
			
		}
		
			
	}

	/**
	 * @param sourceCompartment
	 * @param targetCompartment
	 * @param inMemoryModel
	 * @return
	 */
	private boolean addInMemoryInfectionTransition(Compartment sourceCompartment,
												   String infectorCompartment,
												   Compartment targetCompartment, 
												   String ratio,
												   Model inMemoryModel) {
		boolean isInfectorAdditionSuccessful = true;
		try {
			
			Compartment inMemoryInfectorCompartment = 
				inMemoryModel.getCompartment(infectorCompartment);
			
			
			inMemoryInfectionTransition = 
				inMemoryModel.addInfectionTransition(
						sourceCompartment, 
						inMemoryInfectorCompartment, 
						targetCompartment,
						ratio);
			
			System.out.println("just added complex transition " + inMemoryInfectionTransition);  
		} catch (InvalidParameterExpressionException e) {
			//TODO: how best to handle this? should i create a parameter definition? 
			//or a new parameter expression.
			//TODO: refactor this method. why infector compartments are 
			//set twice?
			
			notificationAreas[1].addNotification("\"" + ratio 
											 + "\" is an invalid parameter expression.");

			try {
			Compartment inMemoryInfectorCompartment = 
				inMemoryModel.getCompartment(infectorCompartment);
			
			
				inMemoryInfectionTransition = 
					inMemoryModel.addInfectionTransition(
							sourceCompartment, 
							inMemoryInfectorCompartment, 
							targetCompartment,
							ratio);
				
			} catch (InvalidParameterExpressionException e1) {
				isInfectorAdditionSuccessful = false;
				System.out.println(e1.getMessage());
				notificationAreas[1].addNotification("\"" + ratio 
						 						 + "\" is an invalid parameter expression.");
			} catch (CompartmentDoesNotExistException e2) {
				isInfectorAdditionSuccessful = false;
				System.out.println(e2.getMessage());
				notificationAreas[1].addNotification("Compartment with name \"" 
						 + infectorCompartment + "\" does not exist. ");
			}
			
		} catch (CompartmentDoesNotExistException e) {
			System.out.println("no such compartment exists " + e.getMessage());
			isInfectorAdditionSuccessful = false;
			notificationAreas[1].addNotification("Compartment with name \"" 
											 + infectorCompartment + "\" does not exist. ");
		}
		
		/*
		 * check if the ratio is defined yet.
		 * */
		if (isInfectorAdditionSuccessful) {
			try {
				notificationAreas[0].addAllNotifications(
						inMemoryModel.listUnboundReferencedParameters());
			} catch (InvalidParameterExpressionException exception) {
				notificationAreas[1].addNotification("Errors in testing of undefined parameters.");
			}
		}
		
		
		
		
		return isInfectorAdditionSuccessful;
	}
	
	public String getCurrentSelectedInfectorName() {
		return ((InfectorComboBox) infectorComboBox).getSelectedCompartmentName();
	}
	
	public String getCurrentRatio() {
		return complexTransitionLabel.getLabel();
	}
	
	public void handleInfectorComboBoxSelectedInfectorChangeEvent(String newlySelctedInfector) {
		
		Compartment infectorCompartment;
		try {
			infectorCompartment = inMemoryModel.getCompartment(newlySelctedInfector);
			((InfectionTransition) inMemoryInfectionTransition).setInfector(infectorCompartment);
		} catch (CompartmentDoesNotExistException e) {
			System.out.println("no cigar compartment infector change #fail");
		}

	}
	
	private void deleteInfectorInformationPanel(PNode currentInfector) {
		PNode parentComplexTransition = currentInfector.getParent();
		List<PNode> infectors = (List<PNode>) parentComplexTransition
									.getAttribute(GlobalConstants
											.TRANSITION_INVOLVED_INFECTORS_ATTRIBUTE_NAME);
		
		infectors.remove(currentInfector);
		
		currentInfector.removeFromParent();
		currentInfector = null;
		

		inMemoryModel.removeTransition(inMemoryInfectionTransition);
		inMemoryInfectionTransitions.remove(inMemoryInfectionTransition);
		
		Observer infectorBoxModel = 
			((InfectorComboBoxModel) infectorComboBox.getModel());
		
		CompartmentIDToLableMap.removeObserver(infectorBoxModel);
		
		
		//TODO: check if references were removed from appropriate attributes
		if (infectors.size() == 0) {
			
			parentComplexTransition.removeFromParent();
			
			System.out.println("--------------");
			
			parentComplexTransition = null;
			
			/*
			 * Since we have deleted the complex transition altogether, there is no need for
			 * refreshing the transition hence we return.
			 * */
			return;
		}
		ComplexTransition.refreshTransition((PPath) parentComplexTransition);
	}

	public void refreshInfectorInformationPanel(Point2D.Double mainTransitionConnectingPoint) {

		PPath connectingEdge = (PPath) PiccoloUtilities 
									.getChildComponentBasedOnGivenAttribute(this, 
											GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
											GlobalConstants.CONNECTING_EDGE_TYPE_ATTRIBUTE_VALUE);
		connectingEdge.reset();
		
		Point2D.Double comboBoxConnectingPoint = new Point2D.Double(
				mainTransitionConnectingPoint.getX() + 100.0,
				mainTransitionConnectingPoint.getY()
				);
		
		connectingEdge.moveTo((float) mainTransitionConnectingPoint.getX(),
							  (float) mainTransitionConnectingPoint.getY());
		
		connectingEdge.lineTo((float) comboBoxConnectingPoint.getX(),
							  (float) comboBoxConnectingPoint.getY());
		
		
		PNode label = PiccoloUtilities
							.getChildComponentBasedOnGivenAttribute(this, 
									GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
									GlobalConstants.COMPLEX_TRANSITION_RATIO_ATTRIBUTE_NAME);
		
		label.setX((
				mainTransitionConnectingPoint.getX() 
					+ comboBoxConnectingPoint.getX()) * 0.5 + 5.0);
		
		label.setY(
				(mainTransitionConnectingPoint.getY() 
					+ comboBoxConnectingPoint.getY()) * 0.5);
		
		PNode infectorComboBox = PiccoloUtilities
									.getChildComponentBasedOnGivenAttribute(this, 
										GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
										GlobalConstants.INFECTOR_COMBO_BOX_TYPE_ATTRIBUTE_VALUE);
		
		infectorComboBox.setOffset(comboBoxConnectingPoint.getX(), 
								  (comboBoxConnectingPoint.getY() 
										  - (infectorComboBox.getHeight() * 0.5)));
		
		
		refreshInfectorDeleterHandle(infectorComboBox);

	}

	/**
	 * @param infectorComboBox
	 */
	private void refreshInfectorDeleterHandle(
			PNode infectorComboBox) {
		PNode infectorDeleterHandle = PiccoloUtilities
										.getChildComponentBasedOnGivenAttribute(this, 
											GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
											GlobalConstants.INFECTOR_DELETER_TYPE_ATTRIBUTE_VALUE);

		double infectorDeleterPositionX = infectorComboBox.getFullBoundsReference().getX() 
										+ infectorComboBox.getWidth()
										- GlobalConstants.INFECTOR_DELETER_DIMENSIONS.getWidth();

		double infectorDeleterPositionY = infectorComboBox.getFullBoundsReference().getY() 
										  - GlobalConstants.INFECTOR_DELETER_DIMENSIONS.getHeight();

		infectorDeleterHandle.setX(infectorDeleterPositionX);
		infectorDeleterHandle.setY(infectorDeleterPositionY);
	}

	/**
	 * @return the isInfectorAdditionSuccessful
	 */
	public boolean isInfectorAdditionSuccessful() {
		return isInfectorAdditionSuccessful;
	}

	/**
	 * @return the inMemoryInfectionTransition
	 */
	public Transition getInMemoryInfectionTransition() {
		return inMemoryInfectionTransition; 
	}

	/**
	 * @return the infectorComboBoxModel
	 */
	public Observer getInfectorComboBoxModel() {
		return ((InfectorComboBoxModel) infectorComboBox.getModel());
	}
	
}
