package edu.iu.epic.modelbuilder.gui.utility;

import java.util.Map;

public interface Observer {
	
	public void update(Map<String, String> compartmentIDToLabel);
	
}
