package edu.iu.epic.modelbuilder.gui.editablelabel;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import edu.iu.epic.modelbuilder.gui.utility.NotificationArea;
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
		currentTransitionLabel.animateToColor(Color.WHITE, 1);
		currentTransitionLabel.setVisible(false);
		PNode transition = currentTransitionLabel.getParent();
		PNode transitionLabelEditor = createNodeLabelEditor(currentTransitionLabel);
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
				saveTransitionLabel(currentTransitionLabel, 
									oldRatioText,
									transitionEditorJTextField, 
									transitionLabelTransform);				
			}
			
		});
		
		transitionEditorJTextField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				
				/*
				 * To mimic existing UX norms, save the compartment labels when the 
				 * user presses "Enter" / "Return" key.
				 * */
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					saveTransitionLabel(currentTransitionLabel, 
							oldRatioText,
							transitionEditorJTextField, 
							transitionLabelTransform);	
				}
				
			}

			public void keyReleased(KeyEvent e) { }

			public void keyTyped(KeyEvent e) { }
			
		});
		PSwing transitionLabelEditor = new PSwing(transitionEditorJTextField);
	    
		double editorPositionX = currentTransitionLabel.getFullBoundsReference().getX();
	    double editorPositionY = currentTransitionLabel.getFullBoundsReference().getY();
	    
	    transitionLabelTransform.translate(editorPositionX, 
	    								   editorPositionY);
	    transitionLabelTransform.addChild(transitionLabelEditor);
	    return transitionLabelTransform;
	}

	/**
	 * @param currentTransitionLabel
	 * @param oldRatioText
	 * @param transitionEditorJTextField
	 * @param transitionLabelTransform
	 */
	private void saveTransitionLabel(
			final PText currentTransitionLabel,
			final String oldRatioText,
			final JTextField transitionEditorJTextField,
			final PNode transitionLabelTransform) {
		double oldWidth = currentTransitionLabel.getWidth();
		String newRatioText = transitionEditorJTextField.getText().trim();
		
		if (inMemoryTransition.setRatio(newRatioText)) {

			currentTransitionLabel.setText(newRatioText);
			double newWidth = currentTransitionLabel.getWidth();
			
			double newOffsetForPositionX = -(newWidth - oldWidth);
			currentTransitionLabel.offset(newOffsetForPositionX, 0.0);
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
	
}
