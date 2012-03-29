package edu.iu.epic.modelbuilder.gui.transition;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;

import edu.iu.epic.modelbuilder.gui.utility.CompartmentIDChangeObserver;

@SuppressWarnings("serial")
public class InfectorComboBoxModel extends DefaultComboBoxModel implements CompartmentIDChangeObserver {

	private Map<Integer, String> initialIndexToCompartmentID;
	
	public InfectorComboBoxModel(Map<String, String> initialCompartmentIDToLabel) {
		this.initialIndexToCompartmentID = new HashMap<Integer, String>();
		
		int index = 0;
		for (Entry<String, String> currentMapping : initialCompartmentIDToLabel.entrySet()) {
			this.addElement(currentMapping.getValue());
			initialIndexToCompartmentID.put(index, currentMapping.getKey());
			index++;
		}
	}

	public void update(Map<String, String> compartmentIDToLabel) {
		String currentSelectedInfectorLabel = this.getSelectedItem().toString();
		int currentSelectedInfectorIndex = this.getIndexOf(currentSelectedInfectorLabel);
		String currentSelectedInfectorID =
			initialIndexToCompartmentID.get(currentSelectedInfectorIndex);

		/*
		 * Set the default new combo box index as the First option.
		 * */
		int newInfectorComboBoxIndex = 0; 
		
		/*
		 * If the currently (before refreshing the combo box) selected option is also available in 
		 * the new options set, then set the new selected index belonging to this option.
		 * */
		if (compartmentIDToLabel.containsKey(currentSelectedInfectorID)) {
			int index = 0;
			for (Entry<String, String> currentMapping : compartmentIDToLabel.entrySet()) {
				if (currentMapping.getKey().equalsIgnoreCase(currentSelectedInfectorID)) {
					newInfectorComboBoxIndex = index;
					break;
				}
				index++;
			}
		} else if (currentSelectedInfectorIndex < (this.getSize() - 1)) {
			/*
			 * If the current selected index is still within the size of the new option set
			 * then retain the selected index. Else go with the default index. 
			 * */
			newInfectorComboBoxIndex = currentSelectedInfectorIndex;
		} 
		
		this.removeAllElements();
		initialIndexToCompartmentID = new HashMap<Integer, String>();
		
		int index = 0;
		for (Entry<String, String> currentMapping : compartmentIDToLabel.entrySet()) {
			this.addElement(currentMapping.getValue());
			initialIndexToCompartmentID.put(index, currentMapping.getKey());
			index++;
		}

		this.setSelectedItem(this.getElementAt(newInfectorComboBoxIndex));
	}
	
}
