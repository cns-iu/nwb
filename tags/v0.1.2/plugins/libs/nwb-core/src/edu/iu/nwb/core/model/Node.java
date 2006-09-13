package edu.iu.nwb.core.model;

import edu.iu.iv.common.property.Property;
import edu.iu.iv.common.property.PropertyMap;


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
		String resultString = getPropertyValue(ID) + " " + getPropertyValue(LABEL);
		
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
