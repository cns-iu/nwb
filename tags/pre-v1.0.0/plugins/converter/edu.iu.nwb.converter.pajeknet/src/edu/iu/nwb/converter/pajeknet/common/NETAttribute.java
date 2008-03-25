/**
 * 
 */
package edu.iu.nwb.converter.pajeknet.common;

/**
 * @author kelleyt
 *
 */
public class NETAttribute {
	private String attributeName;
	private String dataType;
	
	public NETAttribute(String s1, String s2){ //Construct an attribute for the file
		this.attributeName = s1;
		this.dataType = s2;
	}
	
	public String getAttrName(){
		return this.attributeName;
	}
	
	public String getDataType(){
		return this.dataType;
	}
}
