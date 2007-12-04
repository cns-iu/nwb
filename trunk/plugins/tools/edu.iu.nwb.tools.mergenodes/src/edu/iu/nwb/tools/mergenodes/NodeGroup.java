package edu.iu.nwb.tools.mergenodes;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import prefuse.data.Tuple;

public class NodeGroup {
	/*
	 * A HashMap contains a list of nodes that should be merged 
	 * key = nodeID or row number in the original graph, 
	 * value = a Tuple -- a row in the table
	 */
	private Map nodesMap;
    /*
     * Contains a list of primary node. Each element is nodeID or 
     * row number in the original graph.
     * errorFlag = true if primaryNodeList contains no element 
     * or more than one element.
     * errorFlag = false if primaryNodeList only contains one element
     */
	private List primaryNodeList;
	/*
	 * If errorFlag = true, there is no merging action for 
	 * this group of nodes.
	 */
	private boolean errorFlag;
	
	public NodeGroup (){
		nodesMap = new HashMap();
		primaryNodeList = new ArrayList();
		errorFlag = true;		
	}

	/**
	 * Add a node(the whole row) to the nodesMap
	 *
	 * @param nodeRow - a row in the Table (node with label and other attributes)
	 */
	public void addNodeToGroup (Integer NodeID, Tuple nodeRow){
	
		nodesMap.put(NodeID, nodeRow);
	}
	
	/**
	 * Add a node(the whole row) to the nodesMap and 
	 * add the same index to primaryNodeList
	 * @param nodeRow
	 * @return the value of errorFlag 
	 */
	public void addPrimaryNodeToGroup (Integer NodeID, Tuple nodeRow){
		nodesMap.put(NodeID, nodeRow);
		if (primaryNodeList.isEmpty()){
			primaryNodeList.add(NodeID);
			errorFlag = false;
		}
		else if (primaryNodeList.size()>=1){
			primaryNodeList.add(NodeID);
			errorFlag = true;
		}		
	}
	
	public int getPrimaryNodeID (){
		if (primaryNodeList.size()>1)
			return -1;
		else
			return ((Integer) primaryNodeList.get(0)).intValue();
	}
	
	public Map getNodesMap() {
		return nodesMap;
	}
	public boolean getErrorFlag(){
		return errorFlag;
	}
	
	public void setErrorFlag(boolean flag){
		this.errorFlag = flag;
	}
	
	public List getColumnValues(int columnIndex){
		List colValues = new ArrayList();
		Iterator keys = nodesMap.keySet().iterator();
		while(keys.hasNext()){
			Integer key = (Integer)keys.next();
			Tuple element = (Tuple)nodesMap.get(key);
			Object value = element.get(columnIndex);
			colValues.add(value);			
		}		
		return colValues;		
	}
	
	public List getPrimaryColValues (int columnIndex){
		List colValues = new ArrayList();		
		for (int i = 0; i<primaryNodeList.size(); i++){
			Integer key = (Integer)primaryNodeList.get(i);
			Tuple element = (Tuple)nodesMap.get(key);
			Object value = element.get(columnIndex);
			colValues.add(value);			
		}
		return colValues;
	}
	
	
	
	

}
