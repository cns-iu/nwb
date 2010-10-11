package edu.iu.epic.modelbuilder.gui.utility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// TODO: This class should be refactored to not be static (so it's used for instance variables).
// Think of the concurrency!
public class CompartmentIDToLabelMap{
	
	private static Map<String, String> compartmentIDToLabel = new LinkedHashMap<String, String>();
	private static List<Observer> observers = new ArrayList<Observer>();
	
	public static void resetCompartmentIDToLabelMap() {
		compartmentIDToLabel = new LinkedHashMap<String, String>();
		observers = new ArrayList<Observer>();
	}
	
	public static Map<String, String> getCompartmentIDToLabelMap() {
		return compartmentIDToLabel;
	}

	public static void addCompartmentID(String key, String value) {
		compartmentIDToLabel.put(key, value);
		notifyObservers();
	}

	public static void removeCompartmentID(String key) {
		compartmentIDToLabel.remove(key);
		notifyObservers();
	}
	
	public static void notifyObservers() {
		for (Observer currentObserver : observers) {
			currentObserver.update(compartmentIDToLabel);
		}
	}

	public static void addObserver(Observer observerToBeAdded) {
		observers.add(observerToBeAdded);
	}

	public static void removeObserver(Observer observerToBeRemoved) {
		observers.remove(observerToBeRemoved);
	}

	/**
	 * @return the observers
	 */
	public static List<Observer> getObservers() {
		return observers;
	}	
	
	
}
