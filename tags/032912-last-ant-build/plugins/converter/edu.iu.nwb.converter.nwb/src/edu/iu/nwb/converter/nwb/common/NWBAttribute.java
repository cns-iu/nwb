package edu.iu.nwb.converter.nwb.common;

// TODO Move this somewhere appropriately general
public class NWBAttribute {
	private String attributeName;
	private String dataType;
	
	public NWBAttribute(String name, String type) {
		this.attributeName = name;
		this.dataType = type;
	}
	
	public String getAttrName(){
		return attributeName;
	}
	
	public String getDataType() {
		return dataType;
	}
}
