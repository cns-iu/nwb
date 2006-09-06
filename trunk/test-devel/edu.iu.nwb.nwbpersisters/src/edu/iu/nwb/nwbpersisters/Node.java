package edu.iu.nwb.nwbpersisters;

import edu.iu.nwb.nwbpersisters.Property;
import edu.iu.nwb.nwbpersisters.PropertyMap;


public class Node extends PropertyMap {
	
	public static final Property ID     = new Property("node_id", String.class, 10);
	public static final Property LABEL  = new Property("label",   String.class, 20);
	public static final Property WEIGHT = new Property("weight",  Double.class, 30);
	public static final Property TYPE   = new Property("type",    String.class, 40);
	
/*	private Map attributeMap;
	
	public static final String NUMBER = "node#";
	public static final String LABEL  = "label";
	public static final String WEIGHT = "weight";
	public static final String TYPE   = "type";

	public Node() {
		attributeMap = new HashMap();
	}

	public Collection getAttributes() {
		return attributeMap.values();
	}
	
	public Collection getIds() {
		return attributeMap.keySet();
	}
	
	public Object getAttribute(Object id) {
		return attributeMap.get(id);
	}

	public void addAttribute(Object id, Object attribute) {
		attributeMap.put(id, attribute);
	}
*/
	
	public String toString() {
		String resultString = (String)getPropertyValue(ID);
		
		String label = (String)getPropertyValue(LABEL);
		if (label.charAt(0) == '"') {
			resultString += " " + label;
		}
		else {
			resultString += " " + "\"" + label + "\"";			
		}
		
//		String resultString = (String)getPropertyValue(ID);
		Object weight = getPropertyValue(WEIGHT);
		if (weight != null) {
			resultString = resultString + " " + weight;
		}

		Object type = getPropertyValue(TYPE);
		if (type != null) {
			resultString = resultString + " " + type;			
		}
	
		return resultString;
	}
}
