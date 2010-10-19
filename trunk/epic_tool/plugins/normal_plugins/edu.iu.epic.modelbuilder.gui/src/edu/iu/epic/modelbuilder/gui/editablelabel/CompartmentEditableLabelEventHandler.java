package edu.iu.epic.modelbuilder.gui.editablelabel;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import edu.iu.epic.modelbuilder.gui.compartment.PCompartment;
import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDToLabelMap;
import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.pswing.PSwing;

public class CompartmentEditableLabelEventHandler 
				extends PBasicInputEventHandler {
	
	private NotificationArea notificationArea;
	private CompartmentIDToLabelMap compartmentIDToLabelMap;

	public CompartmentEditableLabelEventHandler(CompartmentIDToLabelMap compartmentIDToLabelMap, 
												NotificationArea notificationArea) {
		this.notificationArea = notificationArea;
		this.compartmentIDToLabelMap = compartmentIDToLabelMap;
	}
	
	@Override
	public void mousePressed(PInputEvent inputEvent) {
		PText currentCompartmentLabel = (PText) inputEvent.getPickedNode();
		PNode compartment = currentCompartmentLabel.getParent();
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
				saveCompartmentLabel(initialCompartmentLabelWidth,
									 currentCompartmentLabel, 
									 compartment,
									 oldCompartmentLabel, 
									 compartmentEditorJTextField,
									 compartmentTagTransform);
			}
	    });
		
		compartmentEditorJTextField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) { 
				
				/*
				 * To mimic existing UX norms, save the compartment labels when the 
				 * user presses "Enter" / "Return" key.
				 * */
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					saveCompartmentLabel(initialCompartmentLabelWidth,
							 currentCompartmentLabel, 
							 compartment,
							 oldCompartmentLabel, 
							 compartmentEditorJTextField,
							 compartmentTagTransform);
				}
			}

			public void keyReleased(KeyEvent e) { }

			public void keyTyped(KeyEvent e) { }
			
		});
	    
		PSwing compartmentLabelEditor = new PSwing(compartmentEditorJTextField);
	    compartmentTagTransform.translate(currentCompartmentLabel.getX(), 
	    								  currentCompartmentLabel.getY());
	    compartmentTagTransform.addChild(compartmentLabelEditor);
	    return compartmentTagTransform;
	}

	/**
	 * @param initialCompartmentLabelWidth
	 * @param currentCompartmentLabel
	 * @param compartment
	 * @param oldCompartmentLabel
	 * @param compartmentEditorJTextField
	 * @param compartmentTagTransform
	 */
	private void saveCompartmentLabel(
			final double initialCompartmentLabelWidth,
			final PText currentCompartmentLabel,
			final PNode compartment, final String oldCompartmentLabel,
			final JTextField compartmentEditorJTextField,
			final PNode compartmentTagTransform) {
		String newCompartmentLabelText = compartmentEditorJTextField.getText().trim();
		
		boolean isRenameCompartmentLabelSuccessful = 
			((PCompartment) compartment).renameInMemoryCompartment(
					oldCompartmentLabel, 
					newCompartmentLabelText,
					notificationArea);
		
		/*
		 * Always rename in memory compartment first.. before triggering off
		 * observers (combo boxes...)
		 * */
		if (isRenameCompartmentLabelSuccessful) {
		
		currentCompartmentLabel.setText(newCompartmentLabelText);
		
		double deltaCompartmentLabelWidth = 
			currentCompartmentLabel.getWidth() - initialCompartmentLabelWidth;
		
		double compartmentNewWidth = compartment.getWidth() + deltaCompartmentLabelWidth;
		
		int numOfHandles = 3;
		
		if (compartmentNewWidth < (GlobalConstants.DEFAULT_HANDLE_WIDTH * numOfHandles)) {
			compartmentNewWidth = GlobalConstants.DEFAULT_HANDLE_WIDTH * numOfHandles;
		}
		
		compartment.setWidth(compartmentNewWidth);
		
		
		compartmentIDToLabelMap.addCompartmentID(
				(String) compartment.getAttribute(
						GlobalConstants.NODE_ID_ATTRIBUTE_NAME),
				newCompartmentLabelText);
		
		} else {
			currentCompartmentLabel.setText(oldCompartmentLabel);	
		}
		
		currentCompartmentLabel.setVisible(true);
		compartmentTagTransform.removeFromParent();
	
		for (FocusListener currentFocusListener 
				: compartmentEditorJTextField.getFocusListeners()) {
			compartmentEditorJTextField.removeFocusListener(currentFocusListener);
		}
	}
	
}
