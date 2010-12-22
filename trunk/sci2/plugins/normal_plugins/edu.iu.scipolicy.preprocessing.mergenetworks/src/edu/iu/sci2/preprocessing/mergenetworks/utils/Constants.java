package edu.iu.sci2.preprocessing.mergenetworks.utils;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class Constants {
	
	public static final Set<String> NWB_FORMAT_MANDATED_NODE_ATTRIBUTES = new HashSet<String>() { {
		add("id");
		add("label");
	} }; 
	
	public static final Set<String> NWB_FORMAT_MANDATED_EDGE_ATTRIBUTES = new HashSet<String>() { {
		add("source");
		add("target");
	} };

	public static final int MAX_PREFIX_DISPLAY_TEXT_LENGTH = 10; 

}
