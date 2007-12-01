package edu.iu.nwb.tools.mergenodes;

import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


import org.osgi.service.log.LogService;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;


public class MergeNodes implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private LogService logger;
    
    /* 
     * Indicate the location of the input original graph.
     * The output new graph with merged nodes and the merging report
     * will always become the children of the input original graph.
     */
    private int graphIndex;
    
    /*
     * The input original graph.
     */
    private Graph orgGraph = null;

    /*
     * The input node list table.
     */
    private Table nodeListTable = null;
    
    /*
     * The output new graph with merged nodes.
     */
    private Graph newGraph = null;
    
    /*
     * Not all graphs using row number in orgGraph for the node id.
     * It will be null if the row number is the node id. Otherwise it stores
     * the column name in the node table from orgGraph that is used for the node id.
     */
    private String nodeKeyField = null;
    
    /*
     * It stores the very first column name in nodeListTable. We treat it as the node label field
     * in the node table from orgGraph.
     */
    private String nodeLabelField = null;
    
    /*
     * Keep a complete node ids of the original graph and keep them in order
     */
    private List fullNodeIDList = null;
    
    /*
     * key = NodeID of that merging node existing in the orginal graph.
     * value = merging node index value specifiied in the second last column in the nodeListTable
     */
    private Map mergingNodesMap = null;
    
    private Map mergingTable = null;
    private boolean errorInNodeListTable = false;
    private File mergingReport;
    
    public MergeNodes(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() {
    	Properties metaData = getInputParameters();
    	
    	//Validate and get the input data for nodeListTable and orgGraph
    	if (!getInputData())
    		return null;
    	if(!checkAndCompareNodeSchema())
    		return null;
    	createFullNodeIDList();
    	
    	//Process nodeListTable, return a Map, set errorInnodeListTable flag in the method.  	
        try{
        	processNodeListTable ();
        	if (mergingTable.isEmpty()){
        		logger.log(LogService.LOG_INFO, "There is no merging instruction in the node list table. \n"+
				"So there is no merging action. \n");
        		return null;
        	}
        	if (errorInNodeListTable){
        		return null;
        	}
        	if (!errorInNodeListTable) 
        		errorInNodeListTable = isErrorInNodeListTable();    
        	if (errorInNodeListTable){
        		//Do not merge node and update the graph, only generate the report
        		logger.log(LogService.LOG_ERROR, "There are errors in the node list table. \n"+
        				"Please view the \"Merging Report\" File for details. \n");
        		mergingReport = generateReport (mergingTable);
        		BasicData reportData = new BasicData(mergingReport,
    				mergingReport.getClass().getName());
        		Dictionary graphAttributes = reportData.getMetaData();
        		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        		graphAttributes.put(DataProperty.PARENT, this.data[graphIndex]);
        		graphAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        		graphAttributes.put(DataProperty.LABEL, "Merging Report");
        	
        		return new Data[] {reportData};    		
    		
        	}
        	else {
        		newGraph = updateGraphByMergingNodes(metaData);
        		BasicData newGraphData = new BasicData(newGraph,
        				newGraph.getClass().getName());
        		Dictionary graphAttributes = newGraphData.getMetaData();
        		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        		graphAttributes.put(DataProperty.PARENT, this.data[graphIndex]);
        		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
        		graphAttributes.put(DataProperty.LABEL, "Updated Network");
    		
        		logger.log(LogService.LOG_INFO, "The updated network will come soon... \n"+
				"Please view the \"Merging Report\" File for details. \n");
        		mergingReport = generateReport (mergingTable);
        		BasicData reportData = new BasicData(mergingReport,
    				mergingReport.getClass().getName());
        		graphAttributes = reportData.getMetaData();
        		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        		graphAttributes.put(DataProperty.PARENT, this.data[graphIndex]);
        		graphAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        		graphAttributes.put(DataProperty.LABEL, "Merging Report");
        		
    		
        		return new Data[] {newGraphData, reportData}; 
    		} 
        	
        }catch (Exception e){
        	logger.log (LogService.LOG_ERROR, e.toString()+"\n");
        	return null;
        }   	
        
    }
          
    /*  
     * First, validate and assign the input data to nodeListTable and orgGraph separately.
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
    			orgGraph = (Graph)data[index].getData();
    			graphIndex = index;
    		}
    		else if (dataFormat.equalsIgnoreCase("prefuse.data.Table"))
    			nodeListTable = (Table)data[index].getData();
    		else {
    			logger.log (LogService.LOG_ERROR, 
  				"Error: the data format of the input dataset is "+dataFormat+",\n"+
  				"This algorithm requires the following data formats as inputs: \n"+
  				"	prefuse.data.Graph for a graph/network and \n"+
  				"	prefuse.data.Table for a node list. \n");
    			return false;
    		}
    	}
    	if (nodeListTable==null){
    		logger.log (LogService.LOG_ERROR, 
    		"Error: This algorithm did not get prefuse.data.Table for a node list as one of the inputs. \n");
    		return false;
    	}    	
    	if(orgGraph == null){
    		logger.log (LogService.LOG_ERROR, 
    		"Error: This algorithm did not get prefuse.data.Graph for a graph/network as one of the inputs. \n");
    		return false;
    	}
    	return true;
    }
        
	/*
	 * Except the last two columns in nodeListTable, the column name and the column data type 
	 * of the first N-2 columns in nodeListTable must match the node schema of orgGraph. 
	 * No requirement on keeping the same order. 
	 * Note: that the schema in nodeListTable could be a subset of the node schema of orgGrap.
	 * Store the column name of the first column in nodeListTable.
	 * 
	 * If find any additional column in the first N-2 columns in nodeListTable, 
	 * report errors and return false.
	 */
	private boolean checkAndCompareNodeSchema(){
		boolean isMatched = true;
		Table orgNodeTable = orgGraph.getNodeTable();
		Schema orgNodeSchema = orgNodeTable.getSchema();
		Schema theNodeSchema = nodeListTable.getSchema();
		int theTotalCols = theNodeSchema.getColumnCount();
		    	
    	if (theTotalCols<3){
    		logger.log (LogService.LOG_ERROR,
    				"Error, the node list table should contains three columns at least.");
    		return isMatched = false;
    	}
    	
    	//check the data type of the last column in the nodeListTable
    	String cn_starColumn = theNodeSchema.getColumnName(theTotalCols-1);
    	String dt_starColumn = theNodeSchema. getColumnType(theTotalCols-1).getName();
    	if (!dt_starColumn.equalsIgnoreCase("java.lang.String")){
    		logger.log (LogService.LOG_ERROR,
    				"Error, the data type of the last column - "+cn_starColumn+
    				" in the node list table is "+dt_starColumn+
    				", however the algorithm expects java.lang.String.");
    		return isMatched = false;
    	}

    	//check the data type of the second last column in the nodeListTable
    	String cn_indexColumn = theNodeSchema.getColumnName(theTotalCols-2); 
    	String dt_indexColumn = theNodeSchema. getColumnType(theTotalCols-2).getName();
       	if (!dt_indexColumn.equalsIgnoreCase("java.lang.Integer")&&
       		!dt_indexColumn.equalsIgnoreCase("int")){
       		logger.log (LogService.LOG_ERROR,
       				"Error, the data type of the second last column - "+cn_indexColumn+
       				" in the node list is "+dt_indexColumn+
    				", however the algorithm expects int or java.lang.Integer.");
    	}
    	
		for (int index =0; index<theTotalCols-2; index++){			
			String theLabel = theNodeSchema.getColumnName(index); 
			int colIndex = orgNodeSchema.getColumnIndex(theLabel); 
			System.out.println (">>colName = "+theLabel);			
			System.out.println (">>colIndex = "+colIndex);
			System.out.println(">>orgColName = "+orgNodeSchema.getColumnName(colIndex));
			if(colIndex>=0){
				if (orgNodeSchema.getColumnType(theLabel).getName().equals(
						theNodeSchema.getColumnType(theLabel).getName())){
					if (index ==0)
						nodeLabelField = theLabel;
				}					
				else {
					logger.log (LogService.LOG_ERROR, "The data types of "+theLabel+
							" do not match. \n "+
							"The data type of "+theLabel+ " in the node list table is "+ 
							theNodeSchema.getColumnType(theLabel).getName()+".\n"+
					        "But the data type of "+theLabel+ 
					        " in the node schema of the original input graph is "+ 
					        orgNodeSchema.getColumnType(theLabel).getName()+".\n");
					isMatched = false;
					break;
				}
			}
			else {
				logger.log (LogService.LOG_ERROR, theLabel+
						" does not exist in the node schema of the orginal input graph. \n");
				isMatched = false;
				break;
			}
		}
		return isMatched;		
	}

    /*
     * Create an arraylist to keep a complete node ids of the original graph 
     * and keep them in order.
     */
    private void createFullNodeIDList(){
    	if (orgGraph.getNodeKeyField()!= null)
    		nodeKeyField = orgGraph.getNodeKeyField();
    	if (nodeKeyField == null){
    		/*
    		 * Deal the graph using row numbers as nodeIDs.
    		 * The order storing in a list is useful when make a grapy copy.
    		 * So use ArrayList to maintain the nodeIDs in the order
    		 * that they exist in the node table.
    		 */
    		fullNodeIDList = new ArrayList(orgGraph.getNodeCount());
    		IntIterator nodeIDs = orgGraph.nodeRows();
    		int index = 0;
    		while (nodeIDs.hasNext()){
    			int nodeID = nodeIDs.nextInt();
    			fullNodeIDList.add(index, nodeID);
    			index++;
    		}
    	}
    	else{
    		//deal the graph using values in a specified nodeKeyField as nodeIDs later
    	}
    	
    }
    /*
     * By default, take the last two columns in the table. 
     * The last column is used for specifying the primary row --
     * the value must be “*” or empty (no value).
     * The second last column is used for specifying the node index –-
     * the value must be integers such as 1, 2, 3, 4,… 
     */    
    private Map processNodeListTable () throws Exception {
    	/*
    	 * tempTable temperarily hold the row with the value of indexColumn
    	 * that occurs the very first time in the nodeListTable table
    	 * 
    	 * key = value on the second last column --indexColumn
    	 * value = Tuple, a row of the table
    	 */
    	Map tempTable = new HashMap();
    	mergingTable = new HashMap();     	
    	mergingNodesMap = new HashMap();
    	Integer nodeID;
    	
       	int totalCols = nodeListTable.getColumnCount();

    	for (int rowIndex=0; rowIndex<nodeListTable.getRowCount(); rowIndex++){
   	    	Tuple nodeRow = nodeListTable.getTuple(rowIndex);
   	    	Integer nodeIndex = (Integer)nodeRow.get(totalCols-2);
   	    	String starValue = ((String)nodeRow.get(totalCols-1)).trim();

   	    	if (!tempTable.containsKey(nodeIndex)){
   	    		tempTable.put(nodeIndex, nodeRow);    	    		
   	    	}
   	    	else {
   	    		//some nodes need to be merged
   	    		nodeID = getNodeIDFromOrgGraph(nodeRow);
	    	    if (nodeID == null){
	    	    	logger.log(LogService.LOG_ERROR, "Fail to find "+nodeRow.toString()+
	 	    			"in the orginal graph. \n");	
	    	    	errorInNodeListTable= true;
	    	    	break;
	    	    }
	    	    
   	    		if (mergingTable.containsKey(nodeIndex)){
   	    			NodeGroup nodeGroup = (NodeGroup)mergingTable.get(nodeIndex);   	    			
   	    			mergingNodesMap.put(nodeID, nodeIndex);
   	    			addANode(nodeGroup, starValue, nodeID, nodeRow);   	    			
   	    		}
   	    		else {
   	    			NodeGroup nodeGroup = new NodeGroup();
   	    			mergingNodesMap.put(nodeID, nodeIndex);
   	    			addANode(nodeGroup, starValue, nodeID, nodeRow);
   	    			
   	    			//need to add the row in tempTable to mergingTable
   	    			nodeRow = (Tuple) tempTable.get(nodeIndex);
   	    			nodeID = getNodeIDFromOrgGraph(nodeRow);
   	    			if (nodeID == null){
   	    	    		logger.log(LogService.LOG_ERROR, "Fail to find "+nodeRow.toString()+
   	 	    				"in the orginal graph. \n");	
   	    	    		errorInNodeListTable= true;
   	    	    		break;
   	    	    	}
   	    			mergingNodesMap.put(nodeID, nodeIndex);
   	    			starValue = ((String)nodeRow.get(totalCols-1)).trim();
   	    			addANode(nodeGroup, starValue, nodeID, nodeRow);
   	    			   	    			
   	    			mergingTable.put(nodeIndex, nodeGroup);    	    		
   	    		} 	    	
   	    	}
   	    }    	
    	return mergingTable;
    }  
    
    private Integer getNodeIDFromOrgGraph(Tuple nodeRow) throws Exception{    
    	Integer theNodeID = null;
    	
		Table orgNodeTable = orgGraph.getNodeTable();			
		if (nodeKeyField == null){
			IntIterator nodeIDs = orgGraph.nodeRows();    		
			while (nodeIDs.hasNext()){
				int nodeID = nodeIDs.nextInt();
				Tuple orgNode = orgNodeTable.getTuple(nodeID);  
				if (compareTuple(nodeRow, orgNode)){
					theNodeID= new Integer(nodeID);
					System.out.println(">>find theNodeID="+theNodeID);
					break;
				}    				
			}
			if (theNodeID == null){
				logger.log (LogService.LOG_ERROR, 
						"Fail to find the node = "+nodeRow.toString()+" in the orginal graph.\n");
			}
			return theNodeID;
		}
		else{
			//deal the graph using values in a specified nodeKeyField as nodeIDs later
			logger.log (LogService.LOG_WARNING,
				"Can not deal the graph using values in a specified nodeKeyField as nodeIDs yet.\n");
			return null;			
		}
    }
	
    /*
     * The first tuple from nodeListTable, the second tuple from original graph
     */
    private boolean compareTuple(Tuple nodeRow, Tuple orgNode){
    	boolean isSame = true;    	
    	Schema theNodeSchema = nodeRow.getSchema();
		int theTotalCols = theNodeSchema.getColumnCount();
		for (int index =0; index<theTotalCols-2; index++){			
			String colName = theNodeSchema.getColumnName(index); 
			Object theValue = nodeRow.get(colName);
			Object orgValue = orgNode.get(colName);
//			System.out.println(">>>theValue="+theValue.toString()+"  orgValue="+orgValue);
			if (!isSameValue(theValue, orgValue,  
					theNodeSchema.getColumnType(index).getName())){		
				isSame = false;
				break;
			}
		}			
		return isSame;
    }
    
    private boolean isSameValue (Object v1, Object v2, String dataType){
    	if (dataType.equalsIgnoreCase("java.lang.String")){
    		String theValue = ((String)v1).trim();
    		String orgValue = ((String)v2).trim();
    		if (!theValue.equals(orgValue))
    			return false;
    		else 
    			return true;
    	}
    	else if (dataType.equalsIgnoreCase("java.lang.Integer") ||
    			 dataType.equalsIgnoreCase("int")){
    		int theValue = ((Integer)v1).intValue();
    		int orgValue = ((Integer)v2).intValue();
    		if (theValue != orgValue)
    			return false;
    		else
    			return true;    			
    		
    	}
    	else {
    		logger.log(LogService.LOG_WARNING, "Can not handle "+dataType+" yet. \n");
    		return false;
    	}
    }				
    
    private void addANode(NodeGroup nodeGroup, String starValue, Integer nodeID, Tuple node){
    	if (starValue.equals("*"))
			nodeGroup.addPrimaryNodeToGroup(nodeID, node);   	    				
		else
			nodeGroup.addNodeToGroup(nodeID, node); 
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
	
	private boolean isErrorInNodeListTable(){
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
	
	/*
	 * Merge node and update the graph, as well as generate the report
	 * work with orgNetwork, mergingTable, and parameters, output is a new graph
	 */        		
	private Graph updateGraphByMergingNodes(Properties metaData){
		Table orgNodeTable = orgGraph.getNodeTable();
		Schema nodeSchema = orgNodeTable.getSchema();
		
		Table orgEdgeTable = orgGraph.getEdgeTable();
		Schema edgeSchema = orgEdgeTable.getSchema();
		
		boolean isDirected = orgGraph.isDirected(); 
		
		final Graph updatedGraph = new Graph(nodeSchema.instantiate(),
				edgeSchema.instantiate(), isDirected);
		
		Map nodeFunctions = new HashMap();
		Map edgeFunctions = new HashMap();
		getNodeEdgeFunctions(metaData, nodeFunctions, edgeFunctions);	
		
		

		constructNodes(updatedGraph, nodeSchema, nodeFunctions);
		System.out.println("Org total nodes ="+orgGraph.getNodeCount());
		System.out.println("total nodes = "+updatedGraph.getNodeCount());
		System.out.println("total edges = "+updatedGraph.getEdgeCount());
		constructEdges(updatedGraph,edgeSchema, edgeFunctions);		
		
		return updatedGraph;
	}
	
	private void getNodeEdgeFunctions(Properties metaData, Map nodeFunctions, Map edgeFunctions){
		for (final Iterator it = metaData.keySet().iterator(); it.hasNext();) {

			final String key = (String) it.next();
			String functionName = metaData.getProperty(key);
			String columnName = key.substring(key.indexOf(".")+1);

			if (key.startsWith("edge.")) {
				edgeFunctions.put(columnName, functionName);				
			}

			if (key.startsWith("node.")) {
				nodeFunctions.put(columnName, functionName);

			}
		}		
	}
	
	private void constructNodes (Graph updatedGraph, Schema nodeSchema, Map nodeFunctions){
		/*
		 * input: orgNodeTable, mergingTable, nodeFunctions
		 * output: outputGraph with updated nodes
		 */
		//first get all nodes from orgNodeTable, add new nodes to updatedGraph,
		//and copy the values over to new nodes
		Iterator nodeTuples = orgGraph.getNodes().tuples();
		while(nodeTuples.hasNext()){
			Tuple nodeTuple  = (Tuple) nodeTuples.next();
			Node newNode = updatedGraph.addNode();
			copyNodeValue(nodeTuple, newNode, nodeSchema);			
		}
			
	}
	
	private void copyNodeValue (Tuple oldNode, Node newNode, Schema nodeSchema){
		int columnCount = nodeSchema.getColumnCount();
		for(int index=0; index<columnCount; index++){
			String columnName = nodeSchema.getColumnName(index);
			Object value = oldNode.get(index);
			newNode.set(index, value);
		}
	}
	
	private void constructEdges(Graph updatedGraph, Schema edgeSchema, Map edgeFunctions){
		Iterator edgeTuples = orgGraph.getEdges().tuples();
		while(edgeTuples.hasNext()){
			Tuple edgeTuple  = (Tuple) edgeTuples.next();
//			Node newEdge = updatedGraph.addEdge(arg0, arg1);
//			copyNodeValue(edgeTuple, newEdge, edgeSchema);			
		}

	}
	

	/*
	 * This is a temp solution. I will provide a better solution later.
	 */
	private Properties getInputParameters(){
		final ClassLoader loader = getClass().getClassLoader();

		final InputStream in = loader
				.getResourceAsStream("/edu/iu/nwb/tools/mergeauthors/metadata/Operations.properties");

		final Properties metaData = new Properties();
		try {
			metaData.load(in);
		} catch (final FileNotFoundException fnfe) {
			logger.log(LogService.LOG_ERROR, fnfe.getMessage());
		} catch (final IOException ie) {
			logger.log(LogService.LOG_ERROR, ie.getMessage());
		}
		return metaData;

	}

    
}