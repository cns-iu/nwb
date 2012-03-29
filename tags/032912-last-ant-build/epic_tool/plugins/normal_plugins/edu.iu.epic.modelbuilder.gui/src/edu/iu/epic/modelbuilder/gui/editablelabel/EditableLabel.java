package edu.iu.epic.modelbuilder.gui.editablelabel;

import java.awt.Color;

import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PText;

@SuppressWarnings("serial")
public class EditableLabel extends PText {
	
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
