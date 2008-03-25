package edu.iu.nwb.converter.prefusebibtex.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListMap {
	
	private Map map = new Hashtable();
	
	public ListMap() {
	}
	
	public void put(Object key, Object value) {
		List valueList = (List) this.map.get(key);
		if (valueList == null) {
			valueList = new ArrayList();
		}
		
		valueList.add(value);
		
		this.map.put(key, valueList);
	}
	
	public Set keySet() {
		return this.map.keySet();
	}
	
	public List get(Object key) {
		return (List) this.map.get(key);
	}
}
