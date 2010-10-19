package edu.iu.epic.modelbuilder.gui.utility;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

public class CompartmentIDToLabelMap {	// TODO test again
	private Map<String, String> compartmentIDToLabel = new LinkedHashMap<String, String>();
	private Set<CompartmentIDChangeObserver> compartmentIDChangeObservers =	Sets.newHashSet();
	
	public void resetCompartmentIDToLabelMap() {
		compartmentIDToLabel = new LinkedHashMap<String, String>();
		compartmentIDChangeObservers = Sets.newHashSet();
	}
	
	public Map<String, String> getCompartmentIDToLabelMap() {
		return compartmentIDToLabel;
	}

	public void addCompartmentID(String key, String value) {
		compartmentIDToLabel.put(key, value);
		notifyObservers();
	}

	public void removeCompartmentID(String key) {
		compartmentIDToLabel.remove(key);
		notifyObservers();
	}
	
	
	public void notifyObservers() {
		for (CompartmentIDChangeObserver currentObserver : compartmentIDChangeObservers) {
			currentObserver.update(compartmentIDToLabel);
		}
	}

	public void addObserver(CompartmentIDChangeObserver observerToBeAdded) {
		compartmentIDChangeObservers.add(observerToBeAdded);
	}

	public void removeObserver(CompartmentIDChangeObserver observerToBeRemoved) {
		compartmentIDChangeObservers.remove(observerToBeRemoved);
	}

	public Set<CompartmentIDChangeObserver> getObservers() {
		return compartmentIDChangeObservers;
	}	
}
