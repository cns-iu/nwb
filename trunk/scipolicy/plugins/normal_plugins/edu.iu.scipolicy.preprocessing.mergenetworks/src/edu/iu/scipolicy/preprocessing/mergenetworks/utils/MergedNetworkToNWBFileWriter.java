package edu.iu.scipolicy.preprocessing.mergenetworks.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.Edge;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.MergeNetworksHelper;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.Node;

public class MergedNetworkToNWBFileWriter {
	
	public static File writeMergedNetworkToNWBFile(
			MergeNetworksHelper mergeNetworksHelper, 
			Map<Object, Node> nodeIDToNodeObject, 
			Map<String, Edge> edgeIdentifierToEdgeObject) throws IOException {

		
		File outputNWBFile = NWBFileUtilities.createTemporaryNWBFile();
		NWBFileWriter nwbFileWriter = new NWBFileWriter(outputNWBFile);
		
		nwbFileWriter.setNodeSchema(mergeNetworksHelper.getFinalNodeSchema());
		
		for (Map.Entry<Object, Node> currentNodeIdentifierToNode : nodeIDToNodeObject.entrySet()) {
			
			Node currentNode = currentNodeIdentifierToNode.getValue();
			
			nwbFileWriter.addNode(currentNode.getID(), 
								  currentNode.getLabel(), 
								  currentNode.getAttributes());
		}
		
		nwbFileWriter.setUndirectedEdgeSchema(mergeNetworksHelper.getFinalEdgeSchema());
		
		for (Map.Entry<String, Edge> currentEdgeIdentifierToEdge 
				: edgeIdentifierToEdgeObject.entrySet()) {
			
			Edge currentEdge = currentEdgeIdentifierToEdge.getValue();
			
			nwbFileWriter.addUndirectedEdge(currentEdge.getSourceID(),
											currentEdge.getTargetID(), 
											currentEdge.getAttributes());
			
		}
		
		nwbFileWriter.finishedParsing();
		
		return outputNWBFile;
	}
}
