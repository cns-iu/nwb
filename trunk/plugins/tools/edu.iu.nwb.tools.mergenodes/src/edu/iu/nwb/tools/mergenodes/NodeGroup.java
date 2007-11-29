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
	 * key = unique index,  value = a Tuple -- a row in the table
	 */
	private Map nodeGroup;
    /*
     * Contains a list of primary node. Each element is the uniqu index 
     * in the nodeGroup
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
	
	private int counter;
	
	public NodeGroup (){
		nodeGroup = new HashMap();
		primaryNodeList = new ArrayList();
		errorFlag = true;
		counter = 0;
	}

	/**
	 * Add a node(the whole row) to the nodeGroup
	 *
	 * @param nodeRow - a row in the Table (node with label and other attributes)
	 */
	public void addNodeToGroup (Tuple nodeRow){
		counter++;
		nodeGroup.put(new Integer(counter), nodeRow);
	}
	
	/**
	 * Add a node(the whole row) to the nodeGroup and 
	 * add the same index to primaryNodeList
	 * @param nodeRow
	 * @return the value of errorFlag 
	 */
	public void addPrimaryNodeToGroup (Tuple nodeRow){
		counter++;
		Integer  index = new Integer(counter);
		nodeGroup.put(index, nodeRow);
		if (primaryNodeList.isEmpty()){
			primaryNodeList.add(index);
			errorFlag = false;
		}
		else if (primaryNodeList.size()>=1){
			primaryNodeList.add(index);
			errorFlag = true;
		}		
	}	
	
	public boolean getErrorFlag(){
		return errorFlag;
	}
	
	public void setErrorFlag(boolean flag){
		this.errorFlag = flag;
	}
	
	public List getColumnValues(int columnIndex){
		List colValues = new ArrayList();
		Iterator keys = nodeGroup.keySet().iterator();
		while(keys.hasNext()){
			Integer key = (Integer)keys.next();
			Tuple element = (Tuple)nodeGroup.get(key);
			Object value = element.get(columnIndex);
			colValues.add(value);			
		}		
		return colValues;
		
	}
	
	public List getPrimaryColValues (int columnIndex){
		List colValues = new ArrayList();		
		for (int i = 0; i<primaryNodeList.size(); i++){
			Integer key = (Integer)primaryNodeList.get(i);
			Tuple element = (Tuple)nodeGroup.get(key);
			Object value = element.get(columnIndex);
			colValues.add(value);			
		}
		return colValues;
	}
	
	
	
	

}
