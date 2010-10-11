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

public class PCompartment extends PPath {
	
	private static final long serialVersionUID = -3166464174491867683L;
	private Compartment inMemoryCompartment;
	private Model inMemoryModel;


	public PCompartment(Shape compartmentShape, 
					   String compartmentID, 
					   Model inMemoryModel, 
					   NotificationArea notificationArea) { 
		super(compartmentShape);
		
		this.inMemoryModel = inMemoryModel;
		
		System.out.println("Observers " + CompartmentIDToLabelMap.getObservers());
		
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
		
		PNode simpleTransitionHandle = 
			PPath.createRectangle(
				0, 
				0, 
				(float) GlobalConstants.SIMPLE_TRANSITION_BUTTON_DIMENSIONS.getWidth(), 
				(float) GlobalConstants.SIMPLE_TRANSITION_BUTTON_DIMENSIONS.getHeight());
		
		simpleTransitionHandle.setPaint(Color.CYAN);
		
		double translateSimpleTransitionHandleX = this.getX();
		double tranlateSimpleTransitionHandleY = this.getY() 
									  + this.getHeight() 
									  - GlobalConstants.SIMPLE_TRANSITION_BUTTON_DIMENSIONS
									  		.getHeight();
		simpleTransitionHandle.translate(translateSimpleTransitionHandleX, 
										 tranlateSimpleTransitionHandleY);
		
		simpleTransitionHandle.addAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME, Color.CYAN);
		
		simpleTransitionHandle.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
											"simple_translation_handle_" + compartmentID);
		
		simpleTransitionHandle.addAttribute(
				GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
				GlobalConstants.SIMPLE_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE);
		
		this.addChild(simpleTransitionHandle);
		
		PNode complexTransitionHandle 
			= PPath.createRectangle(0, 
				 0, 
				 (float) GlobalConstants.COMPLEX_TRANSITION_BUTTON_DIMENSIONS.getWidth(), 
				 (float) GlobalConstants.COMPLEX_TRANSITION_BUTTON_DIMENSIONS.getHeight());
		
		complexTransitionHandle.setPaint(Color.BLUE);
		
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
		
		complexTransitionHandle.addAttribute(GlobalConstants.NODE_COLOR_ATTRIBUTE_NAME, Color.BLUE);
		
		complexTransitionHandle.addAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME, 
											"complex_translation_handle_" + compartmentID);
		
		complexTransitionHandle.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
											 GlobalConstants
											 	.COMPLEX_TRANSITION_HANDLE_TYPE_ATTRIBUTE_VALUE);
		
		this.addChild(complexTransitionHandle);		
		
		String compartmentLabelText = (String) getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME);
		
		boolean isCompartmentLabelAccepted = false;
		int erroneousNewLabelCounter = 0;
		
		while (!isCompartmentLabelAccepted) {
			boolean errorPresent = false;
			
			try {
				inMemoryCompartment = this.inMemoryModel.addCompartment(compartmentLabelText);
				setInMemoryCompartmentPosition(this.getFullBoundsReference().getX(),
								   		   	   this.getFullBoundsReference().getY());
			} catch (CompartmentExistsException e) {
				System.out.println("name already exists" + e.getMessage());
				notificationArea.addNotification("Compartment with name \"" 
												 + compartmentLabelText + "\" already exists. ");
				errorPresent = true;
			} catch (InvalidCompartmentNameException e) {
				System.out.println("invalid compartment name" + e.getMessage());
				notificationArea.addNotification("\"" + compartmentLabelText 
						 						 + "\" is an invalid compartment name.");
				errorPresent = true;
			}
			
			if (!errorPresent) {
				isCompartmentLabelAccepted = true;
			} else {
				compartmentLabelText =  (String) this
											.getAttribute(GlobalConstants.NODE_ID_ATTRIBUTE_NAME)
										+ "_" + erroneousNewLabelCounter;
				erroneousNewLabelCounter++;
			}
		}
		
		
		double translateCompartmentLabelX = translateSimpleTransitionHandleX 
											+ GlobalConstants.COMPARTMENT_LABEL_X_OFFSET;
		double translateCompartmentLabelY = this.getY() 
											+ GlobalConstants.COMPARTMENT_LABEL_Y_OFFSET;
		EditableLabel compartmentLabel = 
			new EditableLabel(GlobalConstants.COMPARTMENT_LABEL_TYPE_ATTRIBUTE_VALUE, 
							  compartmentLabelText,
							  translateCompartmentLabelX,
							  translateCompartmentLabelY,
							  new CompartmentEditableLabelEventHandler(notificationArea));

		CompartmentIDToLabelMap.addCompartmentID(compartmentID, compartmentLabelText);
		
		this.addChild(compartmentLabel);
	
	}
	
	private void updateTransition(PNode node) {

		String updatingNodeType = (String) node
										.getAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME);
		
		if (GlobalConstants.COMPARTMENT_TYPE_ATTRIBUTE_VALUE.equalsIgnoreCase(updatingNodeType)) {

		ArrayList transitions = (ArrayList) node
									.getAttribute(
											GlobalConstants.COMPARTMENT_TRANSITIONS_ATTRIBUTE_NAME);
		
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
				System.out.println("invalid compartment name" + e.getMessage());
				System.out.println("reverting to old compartment name");
				notificationArea.addNotification("\"" + newCompartmentLabelText 
												 + "\" is an invalid compartment name."
												 + " Reverting to old compartment name.");
				isCompartmentLabelAccepted = false;
			} catch (CompartmentExistsException e) {
				System.out.println("name already exists" + e.getMessage());
				System.out.println("reverting to old compartment name");
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
