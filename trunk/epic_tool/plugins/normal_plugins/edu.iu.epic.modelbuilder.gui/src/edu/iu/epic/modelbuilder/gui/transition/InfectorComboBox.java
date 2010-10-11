package edu.iu.epic.modelbuilder.gui.transition;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDToLabelMap;
import edu.umd.cs.piccolox.pswing.PComboBox;

public class InfectorComboBox extends PComboBox {

	private static final long serialVersionUID = -2697730594337025625L;

	public InfectorComboBox(final InfectorInformationPanel infectorInformationPanel, 
							String infectorCompartmentName) {
		
		super();
		
		InfectorComboBoxModel infectorComboBoxModel = 
			new InfectorComboBoxModel(CompartmentIDToLabelMap.getCompartmentIDToLabelMap());
		
		this.setModel(infectorComboBoxModel);
		
		if (infectorCompartmentName != null 
				&& !infectorCompartmentName.equalsIgnoreCase("")) {
			this.setSelectedItem(infectorCompartmentName);
		}
		
		
		this.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				
				if (e.getStateChange() == ItemEvent.SELECTED) {
					System.out.println("item state chanege fired");
					infectorInformationPanel
						.handleInfectorComboBoxSelectedInfectorChangeEvent(e.getItem().toString());
				}
			}
		});
	}
	
	public String getSelectedCompartmentName() {
		return (String) this.getSelectedItem();
	}

}
