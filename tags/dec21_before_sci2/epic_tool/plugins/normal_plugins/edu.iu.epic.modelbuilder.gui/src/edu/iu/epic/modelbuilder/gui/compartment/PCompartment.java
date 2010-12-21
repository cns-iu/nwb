package edu.iu.epic.modelbuilder.gui.compartment;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import edu.iu.epic.modelbuilder.gui.editablelabel.CompartmentEditableLabelEventHandler;
import edu.iu.epic.modelbuilder.gui.editablelabel.EditableLabel;
import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDToLabelMap;
import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.iu.epic.modelbuilder.gui.utility.PiccoloUtilities;
import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.CompartmentExistsException;
import edu.iu.epic.modeling.compartment.model.exception.InvalidCompartmentNameException;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

@SuppressWarnings("serial")
public class PCompartment extends PPath {
	
	private Compartment inMemoryCompartment;
	private Model inMemoryModel;
	private CompartmentIDToLabelMap compartmentIDToLabelMap;


	public PCompartment(Shape compartmentShape, 
			   String newCompartmentID, 
			   CompartmentIDToLabelMap compartmentIDToLabelMap, 
			   Model inMemoryModel, 
			   NotificationArea notificationArea) {		
		super(compartmentShape);
		
		this.inMemoryModel = inMemoryModel;
		this.compartmentIDToLabelMap = compartmentIDToLabelMap;
		
		boolean isNewCompartmentIDAccepted = false;
		int erroneousNewLabelCounter = 0;
		Compartment newCompartment = null;
		
		while (!isNewCompartmentIDAccepted) {
			boolean errorPresent = false;
			
			try {
				newCompartment = this.inMemoryModel.addCompartment(newCompartmentID);
				
			} catch (CompartmentExistsException e) {
				notificationArea.addNotification("Compartment with name \"" 
												 + newCompartmentID + "\" already exists. ");
				errorPresent = true;
			} catch (InvalidCompartmentNameException e) {
				notificationArea.addNotification("\"" + newCompartmentID 
						 						 + "\" is an invalid compartment name.");
				errorPresent = true;
			}
			
			if (!errorPresent) {
				isNewCompartmentIDAccepted = true;
			} else {
				newCompartmentID += "_" + erroneousNewLabelCounter;
				erroneousNewLabelCounter++;
			}
		}
		
		
		this.inMemoryCompartment = newCompartment;
		
		setupCompartmentGUI(notificationArea);
		
	}
	
	public PCompartment(Shape compartmentShape, 
					   Compartment compartment,
					   CompartmentIDToLabelMap compartmentIDToLabelMap,
					   Model inMemoryModel, 
					   NotificationArea notificationArea) { 
		super(compartmentShape);
		
		this.inMemoryModel = inMemoryModel;
		this.inMemoryCompartment = compartment;
		this.compartmentIDToLabelMap = compartmentIDToLabelMap;
		
		setupCompartmentGUI(notificationArea);
	
	}

	/**
	 * @param compartment
	 * @param inMemoryModel
	 * @param notificationArea
	 */
	private void setupCompartmentGUI(NotificationArea notificationArea) {
		String compartmentID = inMemoryCompartment.getName();
		
		inMemoryCompartment.setPosition(new Point2D.Double(
											this.getFullBoundsReference().getX(),
											this.getFullBoundsReference().getY()));
		
		List<PNode> emptyTransitionPlaceHolder = new ArrayList<PNode>();
		
		this.setPaint(Color.GREEN);
		this.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, compartmentID);
		
		this.addAttribute(GlobalConstants.COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME, 
								 emptyTransitionPlaceHolder);
		
		this.addAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME, Color.GREEN);
		this.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
						  GlobalConstants.COMPARTMENT_TYPE_ATTRIBUTE_VALUE);
		this.addPropertyChangeListener(PNode.PROPERTY_FULL_BOUNDS, 
											  new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
				updateTransition((PNode) propertyChangeEvent.getSource());
			}
		});
		
		/*
		 * Creation of Complex Transition Handle
		 * */
		PNode simpleTransitionHandle = 
			PPath.createRectangle(
				0, 
				0, 
				(float) GlobalConstants.SIMPLE_TRANSITION_BUTTON_DIMENSIONS.getWidth(), 
				(float) GlobalConstants.SIMPLE_TRANSITION_BUTTON_DIMENSIONS.getHeight());
		
		simpleTransitionHandle.setPaint(GlobalConstants.SIMPLE_TRANSITION_HANDLE_COLOR);
		
		double translateSimpleTransitionHandleX = this.getX();
		double tranlateSimpleTransitionHandleY = this.getY() 
									  + this.getHeight() 
									  - GlobalConstants.SIMPLE_TRANSITION_BUTTON_DIMENSIONS
									  		.getHeight();
		simpleTransitionHandle.translate(translateSimpleTransitionHandleX, 
										 tranlateSimpleTransitionHandleY);
		
		simpleTransitionHandle.addAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME, 
											GlobalConstants.SIMPLE_TRANSITION_HANDLE_COLOR);
		
		simpleTransitionHandle.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
											"simple_translation_handle_" + compartmentID);
		
		simpleTransitionHandle.addAttribute(
				GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
				GlobalConstants.SIMPLE_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE);
		
		this.addChild(simpleTransitionHandle);
		
		
		/*
		 * Creation of Complex Transition Handle
		 * */
		PNode complexTransitionHandle 
			= PPath.createRectangle(0, 
				 0, 
				 (float) GlobalConstants.COMPLEX_TRANSITION_BUTTON_DIMENSIONS.getWidth(), 
				 (float) GlobalConstants.COMPLEX_TRANSITION_BUTTON_DIMENSIONS.getHeight());
		
		complexTransitionHandle.setPaint(GlobalConstants.COMPLEX_TRANSITION_HANDLE_COLOR);
		
		double translateComplexTransitionHandleX = translateSimpleTransitionHandleX 
												   + GlobalConstants
												   		.SIMPLE_TRANSITION_BUTTON_DIMENSIONS
												   			.getWidth();
		
		double translateComplexTransitionHandleY = this.getY() 
												   + this.getHeight() 
												   - GlobalConstants
												   		.COMPLEX_TRANSITION_BUTTON_DIMENSIONS
												   			.getHeight();
		complexTransitionHandle.translate(translateComplexTransitionHandleX, 
										  translateComplexTransitionHandleY);
		
		complexTransitionHandle.addAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME, 
											 GlobalConstants.COMPLEX_TRANSITION_HANDLE_COLOR);
		
		complexTransitionHandle.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
											"complex_translation_handle_" + compartmentID);
		
		complexTransitionHandle.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
											 GlobalConstants
											 	.COMPLEX_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE);
		
		this.addChild(complexTransitionHandle);		
		
		/*
		 * Creation of Is Secondary?
		 * */
		PNode isSecondaryHandle 
			= PPath.createRectangle(0, 
				 0, 
				 (float) GlobalConstants.IS_SECONDARY_BUTTON_DIMENSIONS.getWidth(), 
				 (float) GlobalConstants.IS_SECONDARY_BUTTON_DIMENSIONS.getHeight());
		
		double translateIsSecondaryHandleX = translateSimpleTransitionHandleX 
												   + GlobalConstants
												   		.SIMPLE_TRANSITION_BUTTON_DIMENSIONS
												   			.getWidth()
												   + GlobalConstants
												   		.COMPLEX_TRANSITION_BUTTON_DIMENSIONS
												   			.getWidth();
		
		double translateIsSecondaryHandleY = this.getY() 
												   + this.getHeight() 
												   - GlobalConstants
												   		.IS_SECONDARY_BUTTON_DIMENSIONS
												   			.getHeight();
		
		isSecondaryHandle.translate(translateIsSecondaryHandleX, 
										  translateIsSecondaryHandleY);
		
		isSecondaryHandle.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
											"is_secondary_handle_" + compartmentID);
		
		isSecondaryHandle.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
											 GlobalConstants
											 	.IS_SECONDARY_HANDLE_TYPE_ATTRIBUTE_VALUE);
		
		isSecondaryHandle.addAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME, 
									   getCompartmentTypeColor(inMemoryCompartment.isSecondary()));
		isSecondaryHandle.setPaint(getCompartmentTypeColor(inMemoryCompartment.isSecondary()));
		
		this.addChild(isSecondaryHandle);		
		
		double translateCompartmentLabelX = translateSimpleTransitionHandleX 
											+ GlobalConstants.COMPARTMENT_LABEL_X_OFFSET;
		double translateCompartmentLabelY = this.getY() 
											+ GlobalConstants.COMPARTMENT_LABEL_Y_OFFSET;
		EditableLabel compartmentLabel = 
			new EditableLabel(GlobalConstants.COMPARTMENT_LABEL_TYPE_ATTRIBUTE_VALUE, 
					 		  compartmentID,
							  translateCompartmentLabelX,
							  translateCompartmentLabelY,
							  new CompartmentEditableLabelEventHandler(this.compartmentIDToLabelMap,
									  								   notificationArea));

		compartmentIDToLabelMap.addCompartmentID(compartmentID, compartmentID);
		
		this.addChild(compartmentLabel);
	}
	
	private Color getCompartmentTypeColor(boolean isSecondary) {
		if (isSecondary) {
			return GlobalConstants.SECONDARY_COMPARTMENT_IDENTIFYING_COLOR;
		} else {
			return GlobalConstants.PRIMARY_COMPARTMENT_IDENTIFYING_COLOR;
		}
	}

	private void updateTransition(PNode node) {
		String updatingNodeType = (String) node
										.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME);
		
		if (GlobalConstants.COMPARTMENT_TYPE_ATTRIBUTE_VALUE.equalsIgnoreCase(updatingNodeType)) {

		ArrayList transitions =
			(ArrayList) node.getAttribute(GlobalConstants.COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME);
		
		/*
		 * Refreshes position of the transition along with all its child elements like
		 * Arrow, Ratio Text. 
		 * */
		PiccoloUtilities.refreshTransitions(transitions);
		
		}
		
	}

	/**
	 * @return the inMemoryCompartment
	 */
	public Compartment getInMemoryCompartment() {
		return inMemoryCompartment;
	}

	public boolean renameInMemoryCompartment(String oldCompartmentLabel,
											 String newCompartmentLabelText, 
											 NotificationArea notificationArea) {
		boolean isCompartmentLabelAccepted = true;
			
			try {
				inMemoryCompartment.setName(newCompartmentLabelText);
			} catch (InvalidCompartmentNameException e) {
				notificationArea.addNotification("\"" + newCompartmentLabelText 
												 + "\" is an invalid compartment name."
												 + " Reverting to old compartment name.");
				isCompartmentLabelAccepted = false;
			} catch (CompartmentExistsException e) {
				notificationArea.addNotification("Compartment with name \"" 
												 + newCompartmentLabelText + "\" already exists.");
				isCompartmentLabelAccepted = false;
			} 
			
			return isCompartmentLabelAccepted;
		
	}

	public void removeInMemoryCompartment() {
		inMemoryModel.removeCompartment(inMemoryCompartment);
	}

	public void setInMemoryCompartmentPosition(double xPosition, double yPosition) {
		inMemoryCompartment.setPosition(
				new Point2D.Double(xPosition, yPosition));
	}
	
}
