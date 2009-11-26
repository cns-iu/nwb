package edu.iu.epic.modelbuilder.gui.utility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompartmentIDToLableMap{
	
	private static Map<String, String> compartmentIDToLable = new LinkedHashMap<String, String>();
	private static List<Observer> observers = new ArrayList<Observer>();
	
	public static void resetCompartmentIDToLableMap() {
		compartmentIDToLable = new LinkedHashMap<String, String>();
	}
	
	public static Map<String, String> getCompartmentIDToLableMap() {
		return compartmentIDToLable;
	}

	public static void addCompartmentID(String key, String value) {
		compartmentIDToLable.put(key, value);
		notifyObservers();
	}

	public static void removeCompartmentID(String key) {
		compartmentIDToLable.remove(key);
		notifyObservers();
	}
	
	public static void notifyObservers() {
		for (Observer currentObserver : observers) {
			currentObserver.update(compartmentIDToLable);
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
