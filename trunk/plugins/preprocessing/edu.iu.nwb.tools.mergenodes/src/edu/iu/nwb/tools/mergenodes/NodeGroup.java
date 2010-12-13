package edu.iu.nwb.tools.mergenodes;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import prefuse.data.Node;
import prefuse.data.Tuple;

public class NodeGroup {
	/*
	 * A list of nodes in the original graph that should be merged 
	 */
	private Set nodesSet;
    /*
     * Contains a list of primary node. Each element is a node in the 
     * original graph.
     * errorFlag = true if primaryNodeList contains no element 
     * or more than one element.
     * errorFlag = false if primaryNodeList only contains one element
     */
	private Set primaryNodesSet;
	/*
	 * If errorFlag = true, there is no merging action for 
	 * this group of nodes.
	 */
	private boolean errorFlag;
	
	public NodeGroup (){
		nodesSet = new HashSet();
		primaryNodesSet = new HashSet();
		errorFlag = true;		
	}

	/**
	 * Add a node(the whole row) to the nodesMap
	 *
	 * @param nodeRow - a row in the Table (node with label and other attributes)
	 */
	public void addNodeToGroup (Node node){
	
		nodesSet.add(node);
	}
	
	/**
	 * Add a node(the whole row) to the nodesMap and 
	 * add the same index to primaryNodeList
	 * @param nodeRow
	 * @return the value of errorFlag 
	 */
	public void addPrimaryNodeToGroup (Node primaryNode){
		nodesSet.add(primaryNode);
		if (primaryNodesSet.isEmpty()){
			primaryNodesSet.add(primaryNode);
			errorFlag = false;
		}
		else if (primaryNodesSet.size()>=1){
			primaryNodesSet.add(primaryNode);
			errorFlag = true;
		}		
	}
	
	public Node getPrimaryNode (){
		if ((primaryNodesSet.size() > 1) || (primaryNodesSet.isEmpty())) {
			return null;
		} else {
			return (Node) primaryNodesSet.iterator().next();
		}		
	}
	
	public Set getNodesSet() {
		return nodesSet;
	}
	public boolean getErrorFlag(){
		return errorFlag;
	}
	
	public void setErrorFlag(boolean flag){
		this.errorFlag = flag;
	}
	
	public List getColumnValues(int columnIndex){
		List colValues = new ArrayList();
		Iterator nodes = nodesSet.iterator();
		while(nodes.hasNext()){
			Node theNode = (Node) nodes.next();
			Object value = theNode.get(columnIndex);
			colValues.add(value);			
		}		
		return colValues;		
	}
	
	public List getPrimaryColValues (int columnIndex){
		List colValues = new ArrayList();	
		Iterator nodes = primaryNodesSet.iterator();
		while(nodes.hasNext()){
			Node theNode = (Node) nodes.next();
			Object value = theNode.get(columnIndex);
			colValues.add(value);			
		}		
		return colValues;
	}
	

}
