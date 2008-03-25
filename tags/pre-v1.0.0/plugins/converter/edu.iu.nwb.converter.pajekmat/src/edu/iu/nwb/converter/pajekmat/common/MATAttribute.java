/**
 * 
 */
package edu.iu.nwb.converter.pajekmat.common;

/**
 * @author kelleyt
 *
 */
public class MATAttribute {
	private String attributeName;
	private String dataType;
	
	public MATAttribute(String s1, String s2){ //Construct an attribute for the file
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
