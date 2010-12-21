package edu.iu.scipolicy.preprocessing.mergenetworks.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.scipolicy.preprocessing.mergenetworks.MergeNetworks;

/**
 * 
 * This is used to merge the metadata of the input networks and output a unified schema for both 
 * nodes & edges.
 * @author cdtank
 *
 */
public class MergedNetworkSchemas {
	
	private String firstNetworkCollisionResolvingPrefix;
	private String secondNetworkCollisionResolvingPrefix;
	private Set<String> nodeAttributeNamesToBeIgnored;
	private Set<String> edgeAttributeNamesToBeIgnored;
	
	public static enum NETWORK_HANDLE {
		FIRST,
		SECOND
	}
	
	public MergedNetworkSchemas(Set<String> nodeAttributeNamesToBeIgnored,
							   Set<String> edgeAttributeNamesToBeIgnored,
							   String firstNetworkCollisionResolvingPrefix, 
							   String secondNetworkCollisionResolvingPrefix) {
		this.firstNetworkCollisionResolvingPrefix = firstNetworkCollisionResolvingPrefix;
		this.secondNetworkCollisionResolvingPrefix = secondNetworkCollisionResolvingPrefix;
		this.nodeAttributeNamesToBeIgnored = nodeAttributeNamesToBeIgnored;
		this.edgeAttributeNamesToBeIgnored = edgeAttributeNamesToBeIgnored;
	}
	
	/**
	 * This method is used to get a map of old node attribute names to new (resolved) attributes (
	 * name & data type). 
	 * @param secondNetworkNodeSchema 
	 * @param firstNetworkNodeSchema 
	 * @return
	 */
	public Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
			getOldAttributeToResolvedNodeSchemaDefinitions(
					List<NWBAttribute> firstNetworkNodeSchema, 
					List<NWBAttribute> secondNetworkNodeSchema) {
		
		Map<NETWORK_HANDLE, Map<String, NWBAttribute>> resolvedNodeSchemas = 
				new HashMap<NETWORK_HANDLE, Map<String, NWBAttribute>>();
		resolvedNodeSchemas.put(NETWORK_HANDLE.FIRST, new LinkedHashMap<String, NWBAttribute>());
		resolvedNodeSchemas.put(NETWORK_HANDLE.SECOND, new LinkedHashMap<String, NWBAttribute>());
		
		Set<List<NWBAttribute>> allNodeSchemas = new HashSet<List<NWBAttribute>>();
		allNodeSchemas.add(firstNetworkNodeSchema);
		allNodeSchemas.add(secondNetworkNodeSchema);
		
		Map<String, NWBAttribute> firstNetworkOldAttributeNameToNewAttribute = 
				resolvedNodeSchemas.get(NETWORK_HANDLE.FIRST);
		
		Map<String, NWBAttribute> secondNetworkOldAttributeNameToNewAttribute = 
				resolvedNodeSchemas.get(NETWORK_HANDLE.SECOND);
		
		firstNetworkOldAttributeNameToNewAttribute.putAll(getResolvedSchemaDefinitions(
										firstNetworkNodeSchema,
										allNodeSchemas,
										nodeAttributeNamesToBeIgnored, 
										firstNetworkCollisionResolvingPrefix));
		
		secondNetworkOldAttributeNameToNewAttribute.putAll(getResolvedSchemaDefinitions(
				secondNetworkNodeSchema,
				allNodeSchemas,
				nodeAttributeNamesToBeIgnored, 
				secondNetworkCollisionResolvingPrefix));
		
		return resolvedNodeSchemas;
	}

	public LinkedHashMap<String, String> getFinalNodeSchema(
			Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
					oldAttributeToResolvedNodeSchemaDefinitions) {
		
		LinkedHashMap<String, String> resolvedNodeSchema = new LinkedHashMap<String, String>();
		
		for (Map.Entry<NETWORK_HANDLE, Map<String, NWBAttribute>> currentNetworkNodeSchema 
				: oldAttributeToResolvedNodeSchemaDefinitions.entrySet()) {
			
			Map<String, NWBAttribute> oldToResolvedAttribute = currentNetworkNodeSchema.getValue();
			
			for (Map.Entry<String, NWBAttribute> currentOldToResolvedAttributeName 
					: oldToResolvedAttribute.entrySet()) {
				
				resolvedNodeSchema.put(
						currentOldToResolvedAttributeName.getValue().getAttrName(),
						currentOldToResolvedAttributeName.getValue().getDataType());
				
			}
			
		}
		
		return resolvedNodeSchema;
	}

	public Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
			getOldAttributeToResolvedEdgeSchemaDefinitions(
					List<NWBAttribute> firstNetworkEdgeSchema, 
					List<NWBAttribute> secondNetworkEdgeSchema) {
		
		Map<NETWORK_HANDLE, Map<String, NWBAttribute>> resolvedEdgeSchema = 
				new HashMap<NETWORK_HANDLE, Map<String, NWBAttribute>>();
		resolvedEdgeSchema.put(NETWORK_HANDLE.FIRST, new LinkedHashMap<String, NWBAttribute>());
		resolvedEdgeSchema.put(NETWORK_HANDLE.SECOND, new LinkedHashMap<String, NWBAttribute>());
		
		Set<List<NWBAttribute>> allEdgeSchemas = new HashSet<List<NWBAttribute>>();
		allEdgeSchemas.add(firstNetworkEdgeSchema);
		allEdgeSchemas.add(secondNetworkEdgeSchema);
		
		Map<String, NWBAttribute> firstNetworkOldAttributeNameToNewAttribute = 
				resolvedEdgeSchema.get(NETWORK_HANDLE.FIRST);
		Map<String, NWBAttribute> secondNetworkOldAttributeNameToNewAttribute = 
				resolvedEdgeSchema.get(NETWORK_HANDLE.SECOND);
		
		firstNetworkOldAttributeNameToNewAttribute.putAll(getResolvedSchemaDefinitions(
										firstNetworkEdgeSchema,
										allEdgeSchemas,
										edgeAttributeNamesToBeIgnored, 
										firstNetworkCollisionResolvingPrefix));
		
		secondNetworkOldAttributeNameToNewAttribute.putAll(getResolvedSchemaDefinitions(
				secondNetworkEdgeSchema,
				allEdgeSchemas,
				edgeAttributeNamesToBeIgnored, 
				secondNetworkCollisionResolvingPrefix));
		
		return resolvedEdgeSchema;
		
	}

	public LinkedHashMap<String, String> getFinalEdgeSchema(
			Map<NETWORK_HANDLE, Map<String, NWBAttribute>> 
					oldAttributeToResolvedEdgeSchemaDefinitions) {
		
		LinkedHashMap<String, String> resolvedEdgeSchema = new LinkedHashMap<String, String>();
		
		for (Map.Entry<NETWORK_HANDLE, Map<String, NWBAttribute>> currentNetworkNodeSchema 
				: oldAttributeToResolvedEdgeSchemaDefinitions.entrySet()) {
			
			Map<String, NWBAttribute> oldToResolvedAttribute = currentNetworkNodeSchema.getValue();
			
			for (Map.Entry<String, NWBAttribute> currentOldToResolvedAttributeName 
					: oldToResolvedAttribute.entrySet()) {
				
				
				resolvedEdgeSchema.put(currentOldToResolvedAttributeName.getValue().getAttrName(),
									   currentOldToResolvedAttributeName.getValue().getDataType());
				
			}
			
			/*
			 * Add special columns that indicate whether a given edge was in each of the input networks.
			 */
			resolvedEdgeSchema.put(MergeNetworks.IS_PRESENT_IN_NETWORK_PREFIX 
					+ firstNetworkCollisionResolvingPrefix, "string");
			resolvedEdgeSchema.put(MergeNetworks.IS_PRESENT_IN_NETWORK_PREFIX 
					+ secondNetworkCollisionResolvingPrefix, "string");
		}
		return resolvedEdgeSchema;
	}
	
	
	private Map<String, NWBAttribute> getResolvedSchemaDefinitions(
				List<NWBAttribute> originalSchema,
				Set<List<NWBAttribute>> allSchemas, 
				Set<String> attributeNamesToBeIgnored,
				String collisionResolvingPrefix) {
		
		Map<String, NWBAttribute> resolvedSchemaDefinitions = 
				new LinkedHashMap<String, NWBAttribute>();
		
		for (NWBAttribute currentAttribute : originalSchema) {
			
			String currentAttributeName = currentAttribute.getAttrName();
			if (isAttributePresentInOtherSchemas(currentAttributeName, originalSchema, allSchemas) 
					&& !attributeNamesToBeIgnored.contains(currentAttributeName)) {
				
				resolvedSchemaDefinitions.put(
						currentAttributeName,
						new NWBAttribute(
								getResolvedAttributeName(currentAttributeName,
														 collisionResolvingPrefix), 
								currentAttribute.getDataType()));
			} else {
				resolvedSchemaDefinitions.put(
						currentAttributeName, 						
						new NWBAttribute(currentAttributeName, currentAttribute.getDataType()));
			}
		}
		
		return resolvedSchemaDefinitions;
	}

	/**
	 * @param currentAttributeName
	 * @param collisionResolvingPrefix 
	 * @return
	 */
	private String getResolvedAttributeName(String currentAttributeName, 
											String collisionResolvingPrefix) {
		return collisionResolvingPrefix + "_" + currentAttributeName;
	}

	private boolean isAttributePresentInOtherSchemas(String attrName, 
													 List<NWBAttribute> originalSchema, 
													 Set<List<NWBAttribute>> allNodeSchemas) {

		for (List<NWBAttribute> currentSchema : allNodeSchemas) {
			
			if (currentSchema != originalSchema) {
				
				for (NWBAttribute currentAttribute : currentSchema) {
					if (attrName.equalsIgnoreCase(currentAttribute.getAttrName())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
