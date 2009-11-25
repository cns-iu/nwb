package edu.iu.epic.modelbuilder.gui;

import java.awt.Color;

import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PText;

public class EditableLabel extends PText {
	
	private static final long serialVersionUID = 1L;
	
	public EditableLabel(String labelTypeAttributeValue,
						 String labelText, 
						 double xPosition, 
						 double yPosition, PInputEventListener editableLabelEventHandler) {
		this.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, labelTypeAttributeValue);
		this.setText(labelText);
		this.setX(xPosition);
		this.setY(yPosition);
		this.setConstrainHeightToTextHeight(true);
		this.setConstrainWidthToTextWidth(true);
		this.addInputEventListener(editableLabelEventHandler);
		
	}
	
	public String getLabel() {
		return this.getText();
	}
	public void makeEditableLabelBackgroundOpaque() {
		this.setPaint(Color.WHITE);
	}

}
