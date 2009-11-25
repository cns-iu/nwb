package edu.iu.epic.modelbuilder.gui;

import java.util.Map;

public interface Observer {
	
	public void update(Map<String, String> compartmentIDToLable);
	
}
