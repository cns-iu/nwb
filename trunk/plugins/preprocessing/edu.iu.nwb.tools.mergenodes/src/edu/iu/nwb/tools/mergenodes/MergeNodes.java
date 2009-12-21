package edu.iu.nwb.tools.mergenodes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.nwb.analysis.extractnetfromtable.components.PropertyHandler;


public class MergeNodes implements Algorithm {
    Data[] inData;
    Dictionary parameters;
    CIShellContext context;
    
    private LogService logger;    

    /*
     * The input node list table.
     */
    private Table inputNodeListTable = null;
    
    /*
     * The input original graph.
     */
    private Graph inputGraph = null;
    
    /*
     * Indicate if the inputGraph is a directed network or
     * an undirected network. If it is true, it is a directed one.
     */
    private boolean isDirected;

    /* 
     * Indicate the location of the input original graph in the input Data[]
     * The output new graph with merged nodes and the merging report
     * will always become the children of the input original graph.
     */
    private int graphIndex;
    
    /*
     * The output new graph with merged nodes.
     */
    private Graph outputGraph = null;
    
    /*
     * Not all graphs using row number in inputGraph for the node id.
     * It will be null if the row number is the node id. Otherwise it stores
     * the column name in the node table from inputGraph that is used for the node id.
     */
    private String nodeKeyField = null;
    
    /*
     * It stores the very first column name in inputNodeListTable. We treat it as the node label field
     * in the node table from inputGraph.
     */
    private String nodeLabelField = null;
    
    private HashMap nodeMap = new HashMap();    
    private HashMap edgeMap = new HashMap();
        
    /*
     * 
     */
    private List removedNodeIDList = new ArrayList();
    
    /*
     * key = node in the original graph that should be merged.
     * value = merging node index value specified in the second last column in the inputNodeListTable
     */
    private Map mergingNodesMap = null;
    
    private Map mergingTable, nodeFunctions, edgeFunctions;
    private boolean hasErrorInNodleListTable = false;
    private File mergingReport;
    
    
    public MergeNodes(Data[] data, Dictionary parameters, CIShellContext context) {
        this.inData = data;
        this.parameters = parameters;
        this.context = context;
        logger = (LogService)context.getService(LogService.class.getName());
    }

    //??? Should return null or not
    public Data[] execute() throws AlgorithmExecutionException {
    	Dictionary graphAttributes;
    	Properties aggFunctionKeyValuePairs = null;
    	
		if(parameters.get("aff") != null){
			aggFunctionKeyValuePairs = PropertyHandler.getProperties(
								(String)parameters.get("aff"),this.logger);			
		}

    	//First, validate and assign the input data to inputNodeListTable 
    	//and inputGraph separately.
    	if (!getInputData(inData))
    		return null;
    	if(!checkAndCompareNodeSchema(inputGraph, inputNodeListTable)){
    		return null;
    	}
   	
    	//Process inputNodeListTable, return a Map, set hasErrorInNodleListTable 
    	//flag in the method.  	
        try{
        	processInputNodeListTable (inputNodeListTable);
        	if (mergingTable.isEmpty()){
        		logger.log(LogService.LOG_INFO, 
        			"There is no merging instruction in the node list table. \n"+
					"So there is no merging action. \n");
        		return null;
        	}
        		
        	if (!hasErrorInNodleListTable)
        	{
        		hasErrorInNodleListTable = isErrorInNodleListTable();
        	}
        	if (hasErrorInNodleListTable)
        	{
        		//Do not merge node and update the graph, only generate the report
        		logger.log(LogService.LOG_ERROR, "There are errors in the node list table. \n"+
        				"Please view the \"Merging Report\" File for details. \n");
        	}
        	
        	mergingReport = generateReport (mergingTable);
        	BasicData reportData = new BasicData(mergingReport,
    				mergingReport.getClass().getName());
        	graphAttributes = reportData.getMetadata();
        	graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        	graphAttributes.put(DataProperty.PARENT, this.inData[graphIndex]);
        	graphAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        	graphAttributes.put(DataProperty.LABEL, "Merging Report");
        	
        	try {
        		outputGraph = updateGraphByMergingNodes(inputGraph, aggFunctionKeyValuePairs);
        	
        		BasicData outputGraphData = new BasicData(outputGraph,
        			outputGraph.getClass().getName());
        		graphAttributes = outputGraphData.getMetadata();
        		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        		graphAttributes.put(DataProperty.PARENT, this.inData[graphIndex]);
        		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
        		graphAttributes.put(DataProperty.LABEL, "Updated Network");
        		return new Data[] {outputGraphData, reportData}; 
        	}
        	catch (Exception e){
        		logger.log (LogService.LOG_ERROR, e.toString() + "\n", e);
        	}
        	return new Data[] {reportData};         	
        }catch (Exception e){
        	throw new AlgorithmExecutionException(e.getMessage(),e);
        }
    }
          
    /*  
     * Process input Data[]. Expect two input data:
     * 		a graph/network (the data type is prefuse.data.Graph) and 
     * 		a node list/table (the data type is prefuse.data.Table) 
     * 		from the graph that contains the instructions about which 
     * 		nodes should be merged.
     */
    private boolean getInputData(Data[] inputData){
    	inputNodeListTable = null;
    	inputGraph = null;
    	//Make sure there are two input data
    	if (inputData.length != 2) {
      	  logger.log (LogService.LOG_ERROR, 
				"Error: This algorithm requires two datasets as inputs: a graph/network "+
				"and a node list with the instruction of merging nodes. \n");
      	  return false;
        }
    	
    	//Make sure that the input data are either prefuse.data.Graph or prefuse.data.Table.
    	//Can not be any other data type.
    	for (int index =0; index<2; index++){
    		String dataFormat = inputData[index].getData().getClass().getName();    		
    		if (dataFormat.equalsIgnoreCase("prefuse.data.Graph")){
    			inputGraph = (Graph)inputData[index].getData();
    			graphIndex = index;
    			isDirected = inputGraph.isDirected();
    		}
    		else if (dataFormat.equalsIgnoreCase("prefuse.data.Table"))
    			inputNodeListTable = (Table)inputData[index].getData();
    		else {
    			logger.log (LogService.LOG_ERROR, 
  				"Error: the data format of the input dataset is "+dataFormat+",\n"+
  				"This algorithm requires the following data formats as inputs: \n"+
  				"	prefuse.data.Graph for a graph/network and \n"+
  				"	prefuse.data.Table for a node list. \n");
    			return false;
    		}
    	}
    	
    	//Two input data can not be both prefuse.data.Graph.
    	if (inputNodeListTable==null){
    		logger.log (LogService.LOG_ERROR, 
    		"Error: This algorithm did not get prefuse.data.Table for a node list as one of the inputs. \n");
    		return false;
    	}
    	
    	//Two input data can not be both prefuse.data.Table.
    	if(inputGraph == null){
    		logger.log (LogService.LOG_ERROR, 
    		"Error: This algorithm did not get prefuse.data.Graph for a graph/network as one of the inputs. \n");
    		return false;
    	}
    	return true;
    }
        
	/*
	 * Except the last two columns in inputNodeListTable, the column name and the column data type 
	 * of the first N-2 columns in inputNodeListTable must match the node schema of inputGraph. 
	 * No requirement on keeping the same order. 
	 * Note: that the schema in inputNodeListTable could be a subset of the node schema of orgGrap.
	 * Store the column name of the first column in inputNodeListTable.
	 * 
	 * If find any additional column in the first N-2 columns in inputNodeListTable, 
	 * report errors and return false.
	 */
	private boolean checkAndCompareNodeSchema(Graph theInputGraph, Table theInputNodeListTable){
		boolean isMatched = true;
		Table orgNodeTable = theInputGraph.getNodeTable();
		Schema orgNodeSchema = orgNodeTable.getSchema();
		Schema theNodeSchema = theInputNodeListTable.getSchema();
		int theTotalCols = theNodeSchema.getColumnCount();
		    	
    	if (theTotalCols<3){
    		logger.log (LogService.LOG_ERROR,
    				"Error, the node list table should contains three columns at least.");
    		return isMatched = false;
    	}
    	
    	//check the data type of the last column in the inputNodeListTable, and it must be String
    	String cn_starColumn = theNodeSchema.getColumnName(theTotalCols-1);
    	String dt_starColumn = theNodeSchema. getColumnType(theTotalCols-1).getName();
    	if (!dt_starColumn.equalsIgnoreCase("java.lang.String")){
    		logger.log (LogService.LOG_ERROR,
    				"Error, the data type of the last column - "+cn_starColumn+
    				" in the node list table is "+dt_starColumn+
    				", however the algorithm expects java.lang.String.");
    		return isMatched = false;
    	}

    	//check the data type of the second last column in the inputNodeListTable, and it must be
    	//Integer.
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
			String graphLabel = theLabel;
			int colIndex = orgNodeSchema.getColumnIndex(theLabel); 			
			if (colIndex==-1) 
			{
				graphLabel = theLabel.toLowerCase();
				colIndex = orgNodeSchema.getColumnIndex(graphLabel);				
				
			}
			if(colIndex>=0){
				if (orgNodeSchema.getColumnType(graphLabel).getName().equalsIgnoreCase(
						theNodeSchema.getColumnType(theLabel).getName())){
					if (index ==0)
						nodeLabelField = theLabel;
				}					
				else {
					logger.log (LogService.LOG_WARNING, "The data types of "+theLabel+
							" do not match. \n"+
							"The data type of "+theLabel+ " in the node list table is "+ 
							theNodeSchema.getColumnType(theLabel).getName()+".\n"+
					        "But the data type of "+theLabel+ 
					        " in the node schema of the original input graph is "+ 
					        orgNodeSchema.getColumnType(graphLabel).getName()+".\n");
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
     * By default, take the last two columns in the table. 
     * The last column is used for specifying the primary row --
     * the value must be * or empty (no value).
     * The second last column is used for specifying the node index
     * the value must be integers such as 1, 2, 3, 4, etc. 
     * 
     * Output is a mergingTable and mergingNodesMap
     */    
    private void processInputNodeListTable (Table theInputNodeListTable) throws Exception {
    	/*
    	 * tempTable temporarily hold the row with the value of indexColumn
    	 * that occurs the very first time in the inputNodeListTable table
    	 * 
    	 * key = value on the second last column --indexColumn
    	 * value = tuple, a row of the table
    	 */
    	Map tempTable = new HashMap();
        /*
         * key = value on the second last column --indexColumn.
         * value = a NodeGroup Object
         */
    	mergingTable = new HashMap(); 
        /*
         * key = node in the original graph
         * value = merging node index value specified in the second last column in the inputNodeListTable
         */
       	mergingNodesMap = new HashMap();
    	Node node;
    	
       	int totalCols = theInputNodeListTable.getColumnCount();
    	for (int rowIndex=0; rowIndex<theInputNodeListTable.getRowCount(); rowIndex++){
   	    	Tuple nodeRow = theInputNodeListTable.getTuple(rowIndex);
   	    	Integer nodeIndex = (Integer)nodeRow.get(totalCols-2);
   	    	String starValue = ((String)nodeRow.get(totalCols-1)).trim();
   	    	if (!tempTable.containsKey(nodeIndex)){
   	    		
   	    		tempTable.put(nodeIndex, nodeRow);    	    		
   	    	}
   	    	else {
   	    		//some nodes need to be merged
   	    		node = getNodeFromInputGraph(nodeRow);
	    	    if (node == null){
	    	    	logger.log(LogService.LOG_ERROR, "Fail to find "+nodeRow.toString()+
	 	    			"in the orginal graph. \n");	
	    	    	hasErrorInNodleListTable= true;
	    	    	break;
	    	    }
	    	    
   	    		if (mergingTable.containsKey(node)){
   	    			NodeGroup nodeGroup = (NodeGroup)mergingTable.get(node); 
   	    			mergingNodesMap.put(node, nodeIndex);
   	    			addANode(nodeGroup, starValue, node);   	    			
   	    		}
   	    		else {
   	    			NodeGroup nodeGroup = new NodeGroup();   	    			
   	    			mergingNodesMap.put(node, nodeIndex);
//   	    			printMergingNodesMap();
   	    			addANode(nodeGroup, starValue, node);
   	    			
   	    			//need to add the row in tempTable to mergingTable
   	    			nodeRow = (Tuple) tempTable.get(nodeIndex);
   	    			node = getNodeFromInputGraph(nodeRow);
   	    			if (node == null){
   	    	    		logger.log(LogService.LOG_ERROR, "Fail to find "+nodeRow.toString()+
   	 	    				"in the orginal graph. \n");	
   	    	    		hasErrorInNodleListTable= true;
   	    	    		break;
   	    	    	}
   	    			mergingNodesMap.put(node, nodeIndex);
//   	    			printMergingNodesMap(); 
   	    			starValue = ((String)nodeRow.get(totalCols-1)).trim();
   	    			addANode(nodeGroup, starValue, node);
   	    			   	    			
   	    			mergingTable.put(nodeIndex, nodeGroup);    	    		
   	    		} 	    	
   	    	}
   	    }    	    	
    }  
    
    private Node getNodeFromInputGraph(Tuple nodeRow) throws Exception{
    	
    	Node orgNode = null;
		Iterator nodes = inputGraph.nodes();
		while(nodes.hasNext()){
			orgNode = (Node)nodes.next();
			if (compareTuple(nodeRow, orgNode))
				break;
		}
		if(orgNode == null)
			logger.log (LogService.LOG_ERROR, 
				"Fail to find the node = "+nodeRow.toString()+" in the orginal graph.\n");
		return orgNode;		
    }
	
    /*
     * The first tuple from inputNodeListTable, the second tuple from original graph
     */
    private boolean compareTuple(Tuple nodeRow, Tuple orgNode){
    	boolean isSame = true;    	
    	Schema theNodeSchema = nodeRow.getSchema();
    	Schema orgNodeSchema = orgNode.getSchema();
    	
		int theTotalCols = theNodeSchema.getColumnCount();
		for (int index =0; index<theTotalCols-2; index++){			
			String colName = theNodeSchema.getColumnName(index); 
			
			Object theValue = nodeRow.get(colName);
			Object orgValue = null;
			
			int colIndex = orgNodeSchema.getColumnIndex(colName); 			
			if (colIndex==-1) 
			{
				colIndex = orgNodeSchema.getColumnIndex(colName.toLowerCase());		
				
			}
			
			if(colIndex ==-1){
				//log
			}
			else {
				orgValue = orgNode.get(colIndex);
			}
			//compare if the values of each column between the table and 
			//the original graph are same. 
			if (!isSameValue(theValue, orgValue,  
					theNodeSchema.getColumnType(index).getName())){	
				isSame = false;
				break;
			}
		}			
		return isSame;
    }
    
    private boolean isSameValue (Object v1, Object v2, String dataType){
        
        //handle case of one or both values being null
        
        boolean v1IsNull = v1 == null;
        boolean v2IsNull = v2 == null;
        
        if (v1IsNull && v2IsNull) {
            //two nulls are equal
            return true;
        } 
        if (v1IsNull || v2IsNull) {
            //null never equals non-null
            return false;
        }
        
        // handle normal case where neither is null
        
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

    
    private void addANode(NodeGroup nodeGroup, String starValue, Node node){
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
			logger.log(LogService.LOG_ERROR, e.toString(), e);
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+
					"temp"+System.currentTimeMillis()+".txt");
		}
		return tempFile;
	}
	
	private boolean isErrorInNodleListTable(){
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
	
	
	private void copyValue (Tuple orgTuple, Tuple newTuple, Schema theSchema, int startingIndex){
		int columnCount = theSchema.getColumnCount();
		for(; startingIndex<columnCount; startingIndex++){
			Object value = orgTuple.get(startingIndex);
			newTuple.set(startingIndex, value);
		}
	}
	
	/*
	 * Merge node and update the graph.
	 * Work with orgNetwork, fullNodeIDList, mergingNodesMap,
	 * mergingTable, and parameters, output is a new graph
	 */        		
	private Graph updateGraphByMergingNodes(Graph theInputGraph, Properties aggFunctionKeyValuePairs) throws Exception {
		if (aggFunctionKeyValuePairs != null){
			createUtilityFunctionMap(aggFunctionKeyValuePairs);
		}
		Graph updatedGraph = createOutputGraph(theInputGraph);
		copyAndMergeNodes(updatedGraph);
		copyAndMergeEdges(updatedGraph);
			
		return updatedGraph;
	}
	
	/*
	 * Create an empty output Graph with the same nodeSchema 
	 * and edgeSchem of the original input Graph
	 */
	private Graph createOutputGraph(Graph theInputGraph){
		Table orgNodeTable = theInputGraph.getNodeTable();
		Schema nodeSchema = orgNodeTable.getSchema();
		
		Table orgEdgeTable = theInputGraph.getEdgeTable();
		Schema edgeSchema = orgEdgeTable.getSchema();
		
		Graph theOutputGraph = new Graph(nodeSchema.instantiate(),
				edgeSchema.instantiate(), isDirected);

		return theOutputGraph;
	}
	
	private void copyAndMergeNodes(Graph updatedGraph) throws Exception {
		/*
		 * Store nodes that should be merged but not a primary node
		 * key = this type of node
		 * value = the corresponding primary node
		 */
		HashMap leftNodes = new HashMap();
		Table orgNodeTable = inputGraph.getNodeTable();
		Schema orgNodeSchema = orgNodeTable.getSchema();
		
		Iterator nodes = inputGraph.nodes();
		while(nodes.hasNext()){
			Node orgNode = (Node)nodes.next();
			//if the node is not in the mergingNodeMap, 
			//add the node to the updated graph.
			if(!mergingNodesMap.containsKey(orgNode)){
				Node newNode = updatedGraph.addNode();
				copyValue(orgNode, newNode, orgNodeSchema, 0);
				nodeMap.put(orgNode, newNode);
			}
			else{
				Integer nodeGroupIndex = (Integer)mergingNodesMap.get(orgNode);
				NodeGroup nodeGroup = (NodeGroup)mergingTable.get(nodeGroupIndex);
				Node primaryNode = nodeGroup.getPrimaryNode();
				if(primaryNode == null)
					throw new Exception("Error, can not get the primary node for the node group"+
						" that contains " + orgNode.toString());
				if(primaryNode == orgNode){
					Node newNode = updatedGraph.addNode();
					copyValue(orgNode, newNode, orgNodeSchema, 0);
					nodeMap.put(orgNode, newNode);
				}
				else {
					//store primaryNode != orgNode
					leftNodes.put(orgNode,primaryNode);
				}
			}
		}
		if (!leftNodes.isEmpty()){
			//handle primaryNode != orgNode
			nodes = leftNodes.keySet().iterator();
			while(nodes.hasNext()){
				Node orgNode = (Node)nodes.next();
				Node primaryNode = (Node)leftNodes.get(orgNode);
				Node newNode = (Node)nodeMap.get(primaryNode);
				updateValues(newNode, orgNode, "node");
				nodeMap.put(orgNode, newNode);				
			}
		}		
	}
	
	private void copyAndMergeEdges(Graph updatedGraph) throws Exception {
//		printMergingNodesMap();
//		printNodeMap();
		Table edgeTable = inputGraph.getEdgeTable();
		Schema edgeSchema = edgeTable.getSchema();
		
		Iterator edges = inputGraph.edges();
		while(edges.hasNext()){
			Edge orgEdge = (Edge) edges.next();
			Node orgSourceNode = orgEdge.getSourceNode();
			Node orgTargetNode = orgEdge.getTargetNode();
			Node newSourceNode = (Node) nodeMap.get(orgSourceNode);
			Node newTargetNode = (Node) nodeMap.get(orgTargetNode);
			Object newEdgeKey = null;
			
			if(isDirected){
				DirectedEdge newEdgeSet= new DirectedEdge(newSourceNode, newTargetNode);
				newEdgeKey = newEdgeSet;
			}			
			else{
				//deal undirected
				HashSet newEdgeSet = new HashSet();
				newEdgeSet.add(newSourceNode);
				newEdgeSet.add(newTargetNode);
				newEdgeKey = newEdgeSet; 
			}
			
			if(edgeMap.containsKey(newEdgeKey)){
				Edge theEdge = (Edge) edgeMap.get(newEdgeKey);
				updateValues(theEdge, orgEdge, "edge");		
			}
			else{
				Edge newEdge = updatedGraph.addEdge(newSourceNode, newTargetNode);
				copyValue(orgEdge, newEdge, edgeSchema, 2);
				edgeMap.put(newEdgeKey, newEdge);
			}						
		}	 
	}


	private boolean updateValues (Tuple primaryTuple, Tuple theTuple, String tag){
		boolean isSuccessful = true;
		Schema theSchema = primaryTuple.getSchema();
//		Schema theSchema = theTuple.getSchema();
		Map functionMap = null;
		int k =0;
		if (tag.equalsIgnoreCase("node")){
			k = 1;
			functionMap=nodeFunctions;
		}			
		else {
			//assume if (tag.equalsIgnoreCase("edge"))
			k = 2;
			functionMap=edgeFunctions;
		}
		/*
		 * Note: aggregationFunctions are not always required if there's no node 
		 * and edge attributes in the network.
		 */
		if (functionMap != null){
			
			for (; k < theSchema.getColumnCount(); k++) {
				final String cn = theSchema.getColumnName(k);
				final Class dt = theSchema.getColumnType(k);
				
				final String fn = (String) functionMap.get(cn.toLowerCase());
				try{
					UtilityFunction theFunction = getFunction(fn, dt);	
					if (theFunction != null){
						Object primaryValue = primaryTuple.get(k);
						Object theValue = theTuple.get(k);
						Object newValue = theFunction.operate(primaryValue, theValue);
						primaryTuple.set(k, newValue);	
					}
				}catch (Exception e){
					logger.log(LogService.LOG_ERROR, e.toString(), e);
					isSuccessful = false;
					break;
				}
			}
		}
		return isSuccessful;
	}	
	
	private void createUtilityFunctionMap(Properties aggFunctionKeyValuePairs){
	    //key= the column/attribute name for nodes or edges
	    //value = function name such as sum, max, ignore, etc.
	 	nodeFunctions = new HashMap();
		edgeFunctions = new HashMap();
		Enumeration names = aggFunctionKeyValuePairs.propertyNames();
		while (names.hasMoreElements()) {

			final String key = (String) names.nextElement();			
			String value = aggFunctionKeyValuePairs.getProperty(key);
			String columnName = key.substring(key.indexOf(".")+1);
			String functionName = value.substring(value.indexOf(".")+1);
			if (key.startsWith("edge.")) {
				edgeFunctions.put(columnName.toLowerCase(), functionName);				
			}
			else if (key.startsWith("node.")) {
				nodeFunctions.put(columnName.toLowerCase(), functionName);
			}
		}
	}
	
	private UtilityFunction getFunction(String functionName, 
			Class type) throws Exception{
		final String errorMsg = "Can not support "+functionName+" for "+
			type.getName()+" yet.\n";
		final String name = functionName.toLowerCase();
		if (name.equals("count")) {
			return new Count();
		} else if (name.equals("arithmeticmean")) {
				throw new Exception(errorMsg);
				//return new DoubleArithmeticMean();
		} else if (name.equals("sum")) {
				if (type.equals(int.class) || type.equals(Integer.class)) {
					return new IntegerSum();
				}
				if (type.equals(double.class) || type.equals(Double.class)) {
					throw new Exception(errorMsg);
					//return new DoubleSum();
				}
				if (type.equals(float.class) || type.equals(Float.class)) {
					throw new Exception(errorMsg);
					//return new FloatSum();
				}
		} else if (name.equals("max")) {
				if (type.equals(int.class) || type.equals(Integer.class)) {
					throw new Exception(errorMsg);
					//return new IntegerMax();
				}
				if (type.equals(double.class) || type.equals(Double.class)) {
					throw new Exception(errorMsg);
					//return new DoubleMax();
				}
				if (type.equals(float.class) || type.equals(Float.class)) {
					throw new Exception(errorMsg);
					//return new FloatMax();
				}
		} else if (name.equals("min")) {
				if (type.equals(int.class) || type.equals(Integer.class)) {
					throw new Exception(errorMsg);
					//return new IntegerMin();
				}
				if (type.equals(double.class) || type.equals(Double.class)) {
					throw new Exception(errorMsg);
					//return new DoubleMin();
				}
				if (type.equals(float.class) || type.equals(Float.class)) {
					throw new Exception(errorMsg);
					//return new FloatMin();
				}
		}
		else if (name.equals("ignore"))
			return null;
		return null;
	}
	
	private void printMergingNodesMap(){
		Iterator keys = mergingNodesMap.keySet().iterator();
		while (keys.hasNext()){
			Node node = (Node)keys.next();
//			System.out.println(">>node ="+node.get(nodeLabelField));
		}
	}
	
	private void printEdges(Graph updatedGraph){
		Iterator edges = updatedGraph.edges();
		while(edges.hasNext()){
			Edge edge = (Edge)edges.next();
			int source = edge.getSourceNode().getRow();
			int target = edge.getTargetNode().getRow();
//			System.out.println(""+source+"\t"+target);
		}
		
	}
	
	private void printNodeMap(){
//		System.out.println(">>in print nodeMap, size="+nodeMap.size());
		
		Iterator keys = nodeMap.keySet().iterator();
		while(keys.hasNext()){
			Node orgNode = (Node)keys.next();
			Node newNode = (Node)nodeMap.get(orgNode);
//			System.out.println("orgNode= "+orgNode.get(nodeLabelField)+
//					",  newNode = "+newNode.get(nodeLabelField));
		}
	}
}