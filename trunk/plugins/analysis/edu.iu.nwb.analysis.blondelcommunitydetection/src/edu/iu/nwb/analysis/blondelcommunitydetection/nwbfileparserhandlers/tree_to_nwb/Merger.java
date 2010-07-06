package edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.tree_to_nwb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.cishell.utilities.SetUtilities;

import edu.iu.nwb.analysis.blondelcommunitydetection.NetworkInfo;
import edu.iu.nwb.analysis.blondelcommunitydetection.Node;
import edu.iu.nwb.analysis.blondelcommunitydetection.TreeFileParsingException;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class Merger extends NWBFileWriter  {
	public static final String BASE_COMMUNITY_LEVEL_ATTRIBUTE_NAME = "blondel_community_level_";
	public static final String BASE_COMMUNITY_LABEL = "community_";
	public static final String BASE_ISOLATE_LABEL = "isolate_";
	
	private NetworkInfo networkInfo;
	private int isolateCount = 0;
	
	public Merger(File treeFile, File outputFile, NetworkInfo networkInfo)
			throws FileNotFoundException, IOException, TreeFileParsingException {
		super(outputFile);

		this.networkInfo = networkInfo;
		this.readTreeFileAndAnnotateNodes(new Scanner(treeFile));
	}
	
	@SuppressWarnings("unchecked")
	public void addNode(int id, String label, Map attributes) {
		Node node = this.networkInfo.findNodeByOriginalID(id);
		Map annotatedAttributes = new HashMap();
		annotatedAttributes.putAll(attributes);
		
		if (node != null) {
			ArrayList nodeCommunities = node.getCommunities();
			
			for (int ii = 0; ii < nodeCommunities.size(); ii++) {
				String communityLevelAttributeName = BASE_COMMUNITY_LEVEL_ATTRIBUTE_NAME + ii;
				String communityName = BASE_COMMUNITY_LABEL + nodeCommunities.get(ii);
				annotatedAttributes.put(communityLevelAttributeName, communityName);
			}
		} else {
			// Isolate nodes would not have been added to our nodes list.
			for (int ii = 0; ii < this.networkInfo.getMaxCommunityLevel(); ii++) {
				String communityLevelAttributeName = BASE_COMMUNITY_LEVEL_ATTRIBUTE_NAME + ii;
				String communityName = BASE_ISOLATE_LABEL + this.isolateCount;
				annotatedAttributes.put(communityLevelAttributeName, communityName);
				this.isolateCount++;
			}
		}

		super.addNode(id, label, annotatedAttributes);
	}

	@SuppressWarnings("unchecked")	// Raw LinkedHashMap
	public void setNodeSchema(LinkedHashMap schema) {
		for (int ii = 0; ii < this.networkInfo.getMaxCommunityLevel(); ii++) {
			String communityLevelAttributeName = BASE_COMMUNITY_LEVEL_ATTRIBUTE_NAME + ii;
			schema.put(communityLevelAttributeName, NWBFileProperty.TYPE_STRING);
		}
		
		super.setNodeSchema(schema);
	}

	private void readTreeFileAndAnnotateNodes(Scanner treeFileScanner)
			throws TreeFileParsingException {
		Map<Integer, Integer> previousMap = null;
		Map<Integer, Integer> currentMap = null;
		ArrayList<Node> nodes = this.networkInfo.getNodes();

		boolean shouldKeepReading = true;

		while (shouldKeepReading) {
			if (!this.checkForAnotherEntry(treeFileScanner)) {
				shouldKeepReading = false;
			} else {
				Integer nodeID = this.readNextNodeID(treeFileScanner);
				Integer communityID = this.readNextCommunityID(treeFileScanner);
				
				if (nodeID.intValue() == 0) {
					previousMap = currentMap;
					currentMap = new HashMap<Integer, Integer>();
				}
				
				if (previousMap != null) {
					// nodeID is a community from the previous level.
					Collection<Integer> nodesInCommunity =
						SetUtilities.getKeysOfMapEntrySetWithValue(previousMap.entrySet(), nodeID);

					for (Integer currentNodeID : nodesInCommunity) {
						currentMap.put(currentNodeID, communityID);
						Node node = (Node)nodes.get(currentNodeID.intValue());
						node.addCommunity(communityID, this.networkInfo);
					}
				} else {
					currentMap.put(nodeID, communityID);
					Node node = (Node)nodes.get(nodeID.intValue());
					node.addCommunity(communityID, this.networkInfo);
				}
			}
		}
	}

	private boolean checkForAnotherEntry(Scanner treeFileScanner) throws TreeFileParsingException {
		if (treeFileScanner.hasNext()) {
			if (!treeFileScanner.hasNextInt()) {
				throw new TreeFileParsingException(
					"A non-integer was found.  Tree files must contain only pairs of integers.");
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	private Integer readNextNodeID(Scanner treeFileScanner) throws TreeFileParsingException {
		return new Integer(treeFileScanner.nextInt());
	}
	
	private Integer readNextCommunityID(Scanner treeFileScanner) throws TreeFileParsingException {
		if (!treeFileScanner.hasNext() || !treeFileScanner.hasNextInt()) {
			throw new TreeFileParsingException(
				"A single integer was found.  Tree files must contain only pairs of integers.");
		} else {
			return new Integer(treeFileScanner.nextInt());
		}
	}
}