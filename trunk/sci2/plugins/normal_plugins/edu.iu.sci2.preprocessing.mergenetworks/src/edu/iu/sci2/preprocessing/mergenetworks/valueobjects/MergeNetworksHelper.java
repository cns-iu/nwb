package edu.iu.sci2.preprocessing.mergenetworks.valueobjects;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.sci2.preprocessing.mergenetworks.utils.MergedNetworkSchemas.NETWORK_HANDLE;

public class MergeNetworksHelper {
	
	private LinkedHashMap<String, String> finalNodeSchema = null;
	private LinkedHashMap<String, String> finalEdgeSchema = null;
	
	private Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
			oldAttributeNameToNewNodeAttributeNameAndType = null; 
	private Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
			oldAttributeNameToNewEdgeAttributeNameAndType = null;

	public Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
			getOldAttributeNameToNewNodeAttributeNameAndType() {
		
		return oldAttributeNameToNewNodeAttributeNameAndType;
	}

	public Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
			getOldAttributeNameToNewEdgeAttributeNameAndType() {
		
		return oldAttributeNameToNewEdgeAttributeNameAndType;
	}

	public LinkedHashMap<String, String> getFinalNodeSchema() {
		return finalNodeSchema;
	}

	public LinkedHashMap<String, String> getFinalEdgeSchema() {
		return finalEdgeSchema;
	}

	public void setFinalNodeSchema(LinkedHashMap<String, String> finalNodeSchema) {
		this.finalNodeSchema = finalNodeSchema;
	}

	public void setFinalEdgeSchema(LinkedHashMap<String, String> finalEdgeSchema) {
		this.finalEdgeSchema = finalEdgeSchema;
	}

	public void setOldAttributeNameToNewNodeAttributeNameAndType(
			Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
					oldAttributeNameToNewNodeAttributeNameAndType) {
		
		this.oldAttributeNameToNewNodeAttributeNameAndType = 
			oldAttributeNameToNewNodeAttributeNameAndType;
	}

	public void setOldAttributeNameToNewEdgeAttributeNameAndType(
			Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
					oldAttributeNameToNewEdgeAttributeNameAndType) {
		
		this.oldAttributeNameToNewEdgeAttributeNameAndType = 
			oldAttributeNameToNewEdgeAttributeNameAndType;
	}


}
