package edu.iu.sci2.preprocessing.mergenetworks.valueobjects;

import java.util.List;

import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;

public class NetworkMetadata {
	
	private List<NWBAttribute> nodeSchema;
	private List<NWBAttribute> edgeSchema;

	/**
	 * This constructor is used to handle usage of List of NWBAttributes instead of the 
	 * standard LinkedHashMap. The long term fix should be to change {@link ValidateNWBFile}
	 * to use LinkedHashMap instead of a List of NWBAttributes.  
	 * @param nodeAttrList
	 * @param edgeAttrList
	 */
	public NetworkMetadata(List<NWBAttribute> nodeAttrList, 
						   List<NWBAttribute> edgeAttrList) {
		
		this.edgeSchema = edgeAttrList;
		this.nodeSchema = nodeAttrList;
		
	}
	
	public List<NWBAttribute> getNodeSchema() {
		return nodeSchema;
	}

	public List<NWBAttribute> getEdgeSchema() {
		return edgeSchema;
	}
	
}
