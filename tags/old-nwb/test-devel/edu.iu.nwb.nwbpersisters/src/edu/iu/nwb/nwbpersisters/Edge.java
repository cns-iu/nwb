package edu.iu.nwb.nwbpersisters;

import edu.iu.nwb.nwbpersisters.Property;
import edu.iu.nwb.nwbpersisters.PropertyMap;


public class Edge extends PropertyMap {

/*
	private Map attributeMap;

	public static final String ORIGIN = "origin";
	public static final String DEST   = "dest";
	public static final String WEIGHT = "weight";
	public static final String TYPE   = "type";
*/	
	public static final Property ORIGIN = new Property("origin", String.class, 10);
	public static final Property DEST   = new Property("dest",   String.class, 20);
	public static final Property WEIGHT = new Property("weight", Double.class, 30);
	public static final Property TYPE   = new Property("type",   String.class, 40);

/*
	public Edge() {
		attributeMap = new HashMap();
	}
	
	public Collection getAttributes() {
		return attributeMap.values();
	}

	public void addAttribute(Object id, Object attribute) {
		attributeMap.put(id, attribute);
	}

	public Object getAttribute(Object id) {
		return attributeMap.get(id);
	}
*/	

	public String toString() {
		String resultString = getPropertyValue(ORIGIN) + " " + getPropertyValue(DEST);
		
		/*		Object weight = getPropertyValue(WEIGHT);
		if (weight != null) {
			resultString = resultString + " " + weight;
		}
		
		Object type = getPropertyValue(TYPE);
		if (type != null) {
			resultString = resultString + " " + type;			
		}
*/		
		return resultString;
	}
}
