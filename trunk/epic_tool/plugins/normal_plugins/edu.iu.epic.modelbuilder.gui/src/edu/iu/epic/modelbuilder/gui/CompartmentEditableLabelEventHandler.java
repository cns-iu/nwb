package edu.iu.epic.modelbuilder.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.pswing.PSwing;

public class CompartmentEditableLabelEventHandler 
				extends PBasicInputEventHandler {
	
	private NotificationArea notificationArea;

	public CompartmentEditableLabelEventHandler(NotificationArea notificationArea) {
		this.notificationArea = notificationArea;
	}
	
	@Override
	public void mousePressed(PInputEvent inputEvent) {
		PText currentCompartmentLabel = (PText) inputEvent.getPickedNode();
		PNode compartment = currentCompartmentLabel.getParent();
		System.out.println(compartment);
		double initialCompartmentLabelWidth = currentCompartmentLabel.getWidth();
		PNode compartmentLabelEditor = createNodeLabelEditor(initialCompartmentLabelWidth,
														     currentCompartmentLabel,
														     compartment);
		currentCompartmentLabel.setVisible(false);
		compartment.addChild(compartmentLabelEditor);
	}

	private PNode createNodeLabelEditor(final double initialCompartmentLabelWidth, 
										final PText currentCompartmentLabel, 
										final PNode compartment) {  
		final String oldCompartmentLabel = currentCompartmentLabel.getText();
		final JTextField compartmentEditorJTextField = 
			new JTextField(oldCompartmentLabel);
		
		final PNode compartmentTagTransform = new PNode();

		compartmentEditorJTextField.addFocusListener(new FocusListener() {
	    	

			public void focusGained(FocusEvent e) { }

			public void focusLost(FocusEvent e) {
				String newCompartmentLabelText = compartmentEditorJTextField.getText().trim();
				
				boolean isRenameCompartmentLabelSuccessful = 
					((PCompartment) compartment).renameInMemoryCompartment(
							oldCompartmentLabel, 
							newCompartmentLabelText,
							notificationArea);
				
				/*
				 * Always rename in memory comaprtment first.. before triggering off
				 * observers (combo boxes...)
				 * */
				if (isRenameCompartmentLabelSuccessful) {
				
				currentCompartmentLabel.setText(newCompartmentLabelText);
				
				double deltaCompartmentLabelWidth = 
					currentCompartmentLabel.getWidth() - initialCompartmentLabelWidth;
				
				compartment.setWidth(compartment.getWidth() + deltaCompartmentLabelWidth);
				
				
				CompartmentIDToLableMap.addCompartmentID(
						(String) compartment.getAttribute(
								GlobalConstants.NODE_ID_ATTRIBUTE_NAME),
						newCompartmentLabelText);
				
				} else {
					
					System.out.println("rename unsuccessful. rolling it back"); 
					currentCompartmentLabel.setText(oldCompartmentLabel);	
					
				}
				
				currentCompartmentLabel.setVisible(true);
				compartmentTagTransform.removeFromParent();

				for (FocusListener currentFocusListener : 
						compartmentEditorJTextField.getFocusListeners()) {
					compartmentEditorJTextField.removeFocusListener(currentFocusListener);
				}
				

			}
	    });
	    
		PSwing compartmentLabelEditor = new PSwing(compartmentEditorJTextField);
	    compartmentTagTransform.translate(currentCompartmentLabel.getX(), 
	    								  currentCompartmentLabel.getY());
	    compartmentTagTransform.addChild(compartmentLabelEditor);
	    return compartmentTagTransform;
	}
	
}
