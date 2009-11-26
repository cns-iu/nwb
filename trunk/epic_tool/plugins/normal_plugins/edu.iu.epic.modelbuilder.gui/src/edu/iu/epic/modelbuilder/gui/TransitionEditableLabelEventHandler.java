package edu.iu.epic.modelbuilder.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterExpressionException;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.pswing.PSwing;

public class TransitionEditableLabelEventHandler 
	extends PBasicInputEventHandler {
	
	private Transition inMemoryTransition;
	private NotificationArea[] notificationAreas;
	private Model inMemoryModel; 

	public TransitionEditableLabelEventHandler(
			Transition inMemoryTransition,
			Model inMemoryModel,
			NotificationArea[] notificationAreas) {
		this.inMemoryTransition = inMemoryTransition;
		this.inMemoryModel = inMemoryModel;
		this.notificationAreas = notificationAreas;
	}

	@Override
	public void mousePressed(PInputEvent inputEvent) {
		PText currentTransitionLabel = (PText) inputEvent.getPickedNode();
		PNode transition = currentTransitionLabel.getParent();
		PNode transitionLabelEditor = createNodeLabelEditor(currentTransitionLabel);
		currentTransitionLabel.setVisible(false);
		transition.addChild(transitionLabelEditor);
	}

	private PNode createNodeLabelEditor(final PText currentTransitionLabel) { 
		final String oldRatioText = currentTransitionLabel.getText();
		final JTextField transitionEditorJTextField = 
			new JTextField(oldRatioText);
		final PNode transitionLabelTransform = new PNode();
		transitionEditorJTextField.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) { }

			public void focusLost(FocusEvent e) {
				String newRatioText = transitionEditorJTextField.getText().trim();
				
				//TODO: what if the ratio rename fails? currently setratio does not 
				//return a boolean. asked joseph to looked into it.
				if (inMemoryTransition.setRatio(newRatioText)) {
					currentTransitionLabel.setText(newRatioText);
					
					try {
						notificationAreas[0].addAllNotifications(
								inMemoryModel.listUnboundReferencedParameters());
					} catch (InvalidParameterExpressionException exception) {
						notificationAreas[1]
						       .addNotification("Errors in testing of undefined parameters.");
					}
					
				} else {
					currentTransitionLabel.setText(oldRatioText);
					notificationAreas[1].addNotification("\"" + newRatioText 
	 												 + "\" is an invalid parameter expression."
	 												 + " Reverting to old parameter expression.");
					
				}
				
				currentTransitionLabel.setVisible(true);
				transitionLabelTransform.removeFromParent();				
			}
			
		});
		
		PSwing transitionLabelEditor = new PSwing(transitionEditorJTextField);
	    
	    transitionLabelTransform.translate(currentTransitionLabel.getX(), 
	    								   currentTransitionLabel.getY());
	    transitionLabelTransform.addChild(transitionLabelEditor);
	    return transitionLabelTransform;
	}
	
}
