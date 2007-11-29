package edu.iu.nwb.tools.mergenodes;

import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


import org.osgi.service.log.LogService;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Table;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Tuple;


public class MergeNodes implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private LogService logger;
    
    private Graph orgNetwork = null, newNetwork=null;
    private int networkIndex;
    private Table nodeList = null;
    private Map mergingTable = null;
    private boolean errorInNodeList = false;
    private File mergingReport;
    
    public MergeNodes(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() {
    	
    	//First, validate and get the input data for nodeLost and orgNetwork
    	if (!getInputData())
    		return null;
    	
    	//second process nodeList, return a Map, set errorInNodeList flag in the method.  	
        try{
        	processNodeList (nodeList);
        	if (mergingTable.isEmpty()){
        		logger.log(LogService.LOG_INFO, "There is no merging instruction in the node list table. \n"+
				"So there is no merging action. \n");
        		return null;
        	}
        	errorInNodeList = isErrorInNodeList();    
        	if (errorInNodeList){
        		//Do not merge node and update the graph, only generate the report
        		logger.log(LogService.LOG_ERROR, "There are errors in the node list table. \n"+
        				"Please view the \"Merging Report\" File for details. \n");
        		mergingReport = generateReport (mergingTable);
        		BasicData reportData = new BasicData(mergingReport,
    				mergingReport.getClass().getName());
        		Dictionary graphAttributes = reportData.getMetaData();
        		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        		graphAttributes.put(DataProperty.PARENT, this.data[networkIndex]);
        		graphAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        		graphAttributes.put(DataProperty.LABEL, "Merging Report");
        	
        		return new Data[] {reportData};    		
    		
        	}
        	else {
        		//Merge node and update the graph, as well as generate the report
/*        		newNetwork = updateGraphByMergingNodes(orgNetwork, mergingTable, parameters);
        		BasicData newNetworkData = new BasicData(newNetwork,
    				newNetwork.getClass().getName());
        		Dictionary graphAttributes = newNetworkData.getMetaData();
        		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        		graphAttributes.put(DataProperty.PARENT, this.data[networkIndex]);
        		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
        		graphAttributes.put(DataProperty.LABEL, "Updated Network");
*/    		
        		logger.log(LogService.LOG_INFO, "The updated network will come soon... \n"+
				"Please view the \"Merging Report\" File for details. \n");
        		mergingReport = generateReport (mergingTable);
        		BasicData reportData = new BasicData(mergingReport,
    				mergingReport.getClass().getName());
        		Dictionary graphAttributes = reportData.getMetaData();
        		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        		graphAttributes.put(DataProperty.PARENT, this.data[networkIndex]);
        		graphAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        		graphAttributes.put(DataProperty.LABEL, "Merging Report");
        		
    		
        		return new Data[] {reportData}; 
    		} 
        	
        }catch (Exception e){
        	logger.log (LogService.LOG_ERROR, e.toString()+"\n");
        	return null;
        }   	
        
    }
    
    /*  
     * First, validate and get the input data for nodeList and orgNetwork
     */
    private boolean getInputData(){
    	if (data.length != 2) {
      	  logger.log (LogService.LOG_ERROR, 
				"Error: This algorithm requires two datasets as inputs: a graph/network "+
				"and a node list with the instruction of merging nodes. \n");
      	  return false;
        }    	
    	for (int index =0; index<2; index++){
    		String dataFormat = data[index].getData().getClass().getName();    		
    		if (dataFormat.equalsIgnoreCase("prefuse.data.Graph")){
    			orgNetwork = (Graph)data[index].getData();
    			networkIndex = index;
    		}
    		else if (dataFormat.equalsIgnoreCase("prefuse.data.Table"))
    			nodeList = (Table)data[index].getData();
    		else {
    			logger.log (LogService.LOG_ERROR, 
  				"Error: the data format of the input dataset is "+dataFormat+",\n"+
  				"This algorithm requires the following data formats as inputs: \n"+
  				"	prefuse.data.Graph for a graph/network and \n"+
  				"	prefuse.data.Table for a node list. \n");
    			return false;
    		}
    	}
    	if (nodeList==null){
    		logger.log (LogService.LOG_ERROR, 
    		"Error: This algorithm did not get prefuse.data.Table for a node list as one of the inputs. \n");
    		return false;
    	}    	
    	if(orgNetwork == null){
    		logger.log (LogService.LOG_ERROR, 
    		"Error: This algorithm did not get prefuse.data.Graph for a graph/network as one of the inputs. \n");
    		return false;
    	}
    	return true;
    }

    /*
     * By default, take the last two columns in the table. 
     * The last column is used for specifying the primary row --
     * the value must be “*” or empty (no value).
     * The second last column is used for specifying the node index –-
     * the value must be integers such as 1, 2, 3, 4,… 
     */    
    private Map processNodeList (Table nodeList) throws Exception {
    	/*
    	 * tempTable temperarily hold the row with the value of indexColumn
    	 * that occurs the very first time in the nodeList table
    	 * 
    	 * key = value on the second last column --indexColumn
    	 * value = Tuple, a row of the table
    	 */
    	Map tempTable = new HashMap();
    	mergingTable = new HashMap(); 
    	
    	int totalCols = nodeList.getColumnCount();
    	if (totalCols<3){
    		throw new Exception ("Error, the node list at least should contains three columns.");
    	}
    	Schema nodeListSchema = nodeList.getSchema();
    	//get the last column name and data type
//    	String starColumn = nodeListSchema.getColumnName(totalCols-1); 
    	String dt_starColumn = nodeListSchema. getColumnType(totalCols-1).getName();
    	if (!dt_starColumn.equalsIgnoreCase("java.lang.String")){
    		throw new Exception ("Error, the data type of the last column in the node list is "+dt_starColumn+
    				", however the algorithm expects java.lang.String.");
    	}

    	//get the second last column name and data type
//    	String indexColumn = nodeListSchema.getColumnName(totalCols-2); 
/*    	String dt_indexColumn = nodeListSchema. getColumnType(totalCols-2).getName();
       	if (!dt_indexColumn.equalsIgnoreCase("java.lang.Integer")){
    		throw new Exception ("Error, the data type of the second last column in the node list is "+dt_indexColumn+
    				", however the algorithm expects java.lang.Integer.");
    	}
*/  
   	    for (int rowIndex=0; rowIndex<nodeList.getRowCount(); rowIndex++){
   	    	Tuple nodeRow = nodeList.getTuple(rowIndex);
   	    	Integer nodeIndex = (Integer)nodeRow.get(totalCols-2);
   	    	String starValue = ((String)nodeRow.get(totalCols-1)).trim();
   	    	
   	    	if (!tempTable.containsKey(nodeIndex)){
   	    		tempTable.put(nodeIndex, nodeRow);   	    		
   	    	}
   	    	else {
   	    		//some nodes need to be merged
   	    		if (mergingTable.containsKey(nodeIndex)){
   	    			NodeGroup nodeGroup = (NodeGroup)mergingTable.get(nodeIndex);
   	    			addANode(nodeGroup, starValue, nodeRow);   	    			    	    						
   	    		}
   	    		else {
   	    			NodeGroup nodeGroup = new NodeGroup();
   	    			addANode(nodeGroup, starValue, nodeRow);
   	    			
   	    			//need to add the row in tempTable to mergingTable
   	    			nodeRow = (Tuple) tempTable.get(nodeIndex);
   	    			starValue = ((String)nodeRow.get(totalCols-1)).trim();
   	    			addANode(nodeGroup, starValue, nodeRow);
   	    			
   	    			mergingTable.put(nodeIndex, nodeGroup);    	    		
   	    		} 	    	
   	    	}
   	    }    	
    	return mergingTable;
    }  
    
    private void addANode(NodeGroup nodeGroup, String starValue, Tuple node){
    	if (starValue.equals("*"))
			nodeGroup.addPrimaryNodeToGroup(node);   	    				
		else
			nodeGroup.addNodeToGroup(node); 
    }
    
    private File generateReport (Map MergingTable) throws IOException{
    	File report = getTempFile();
    	PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(report)));
    	Iterator keys = MergingTable.keySet().iterator();
    	StringBuffer errorMsg = new StringBuffer();
    	while (keys.hasNext()){
    		Integer key = (Integer)keys.next();
    		NodeGroup nodeGroup = (NodeGroup)MergingTable.get(key);
    		if(!nodeGroup.getErrorFlag()){
    			//add summary to mergingMsg
    			/*
    			  
    			  Merge Albert, R
    			 		Albet, R 
    			        Albett, R
    			  Using Albert, R for the node label (primary key).
    			  */
    			Iterator labels = nodeGroup.getColumnValues(0).iterator();
    			Iterator keyLabels = nodeGroup.getPrimaryColValues(0).iterator();
 	
    			out.println("Merge "+getStringList(labels)+".");
    			out.println("Use "+getStringList(keyLabels)+
    					" to represent this merged group.");  
    			out.println();
    		}
    		else {
    			//add summary to errorMsg
    			/*Error: 
				Fail to merge Barabasi, AL
          			Barabasi, A
          			Barabasii, AL
            	Reason: Specify more than one primary key.

    			*/
    			Iterator labels = nodeGroup.getColumnValues(0).iterator();
    			List primaryKeys = nodeGroup.getPrimaryColValues(0);
 	
    			errorMsg.append("Error: Fail to merge "+getStringList(labels)+". \n");
    			if (primaryKeys.size()==0)
    				errorMsg.append("Reason: didn't specify primary item for this group. \n");
    			else if (primaryKeys.size()>1)
    				errorMsg.append("Reason: Have specified more than one primary item for this group. \n");  
    		}
    	} 

    	out.println(errorMsg.toString());
    	out.close();
    	return report;
    }
    
    private String getStringList (Iterator iterator){
    	StringBuffer temp = new StringBuffer();
    	String value = (String)iterator.next();
		temp.append("\""+value+"\"");
    	while (iterator.hasNext()){
    		temp.append(", ");
    		value = (String)iterator.next();
    		temp.append("\""+value+"\"");
    	}
    	return temp.toString();
    }
	private File getTempFile(){
		File tempFile;
    
		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		try{
			tempFile = File.createTempFile("NWB-Session-", ".txt", tempDir);
		
		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+
					"temp"+System.currentTimeMillis()+".txt");

		}
		return tempFile;
	}
	
	private boolean isErrorInNodeList(){
		boolean isError = false;
		Iterator keys = mergingTable.keySet().iterator();
		while (keys.hasNext()){
			Object key = keys.next();
			NodeGroup value = (NodeGroup)mergingTable.get(key);
			if (value.getErrorFlag()){
				isError = true;
				break;
			}				
		}
		return isError;
			
	}

    
}