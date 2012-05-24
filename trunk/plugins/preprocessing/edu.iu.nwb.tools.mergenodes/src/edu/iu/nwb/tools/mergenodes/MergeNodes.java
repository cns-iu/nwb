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
import java.util.Set;

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
    private Data inputNetworkData;
    private Graph inputNetwork;
    private Table inputMergeTable;
    private LogService logger;
    private String aggregateFunctionFileName;

    private boolean isDirected;
    private Map<Node, Node> nodeMap = new HashMap<Node, Node>();    
    private Map<Object, Edge> edgeMap = new HashMap<Object, Edge>();
    private Map<Node, Integer> mergingNodesMap = new HashMap<Node, Integer>();
    private Map<Integer, NodeGroup> mergingTable = new HashMap<Integer, NodeGroup>();
    private Map<String, String> nodeFunctions;
    private Map<String, String> edgeFunctions;
    private boolean hasErrorInNodeListTable = false;
    private File mergingReport;

    public MergeNodes(
    		Data inputNetworkData,
    		Graph inputNetwork,
    		Table inputMergeTable,
    		LogService logger,
    		String aggregateFunctionFileName) {
        this.inputNetworkData = inputNetworkData;
        this.inputNetwork = inputNetwork;
        this.inputMergeTable = inputMergeTable;
        this.logger = logger;
        this.aggregateFunctionFileName = aggregateFunctionFileName;
        this.isDirected = this.inputNetwork.isDirected();
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Properties aggregateFunctionProperties = getAggregateFunctionProperties();

        try {
        	processInputNodeListTable(this.inputMergeTable);

        	if (this.mergingTable.isEmpty()) {
        		String logMessage =
        			"There is no merging instruction in the node list table. \n" +
					"So there is no merging action. \n";

        		this.logger.log(LogService.LOG_INFO, logMessage);

        		return null;
        	}
        		
        	if (!this.hasErrorInNodeListTable) {
        		this.hasErrorInNodeListTable = isErrorInNodeListTable();
        	}

        	if (this.hasErrorInNodeListTable) {
        		String logMessage =
        			"There are errors in the node list table. \n"+
    				"Please view the \"Merging Report\" File for details. \n";
        		this.logger.log(LogService.LOG_ERROR, logMessage);
        	}
        	
        	this.mergingReport = generateReport(this.mergingTable);
        	BasicData reportData =
        		new BasicData(this.mergingReport, this.mergingReport.getClass().getName());
        	Dictionary<String, Object> graphAttributes = reportData.getMetadata();
        	graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        	graphAttributes.put(DataProperty.PARENT, this.inputNetworkData);
        	graphAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        	graphAttributes.put(DataProperty.LABEL, "Merging Report");
        	
        	try {
        		Graph outputGraph =
        			updateGraphByMergingNodes(this.inputNetwork, aggregateFunctionProperties);
        	
        		BasicData outputGraphData =
        			new BasicData(outputGraph, outputGraph.getClass().getName());
        		graphAttributes = outputGraphData.getMetadata();
        		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        		graphAttributes.put(DataProperty.PARENT, this.inputNetworkData);
        		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
        		graphAttributes.put(DataProperty.LABEL, "Updated Network");

        		return new Data[] {outputGraphData, reportData}; 
        	} catch (Exception e) {
        		throw new RuntimeException(e.getMessage(), e);
//        		logger.log (LogService.LOG_ERROR, e.toString() + "\n", e);
        	}      	
        } catch (Exception e) {
        	throw new RuntimeException(e.getMessage(), e);
//        	throw new AlgorithmExecutionException(e.getMessage(), e);
        }
    }

    private Properties getAggregateFunctionProperties() throws AlgorithmExecutionException {
    	if (this.aggregateFunctionFileName != null) {
    		// TODO: Custom exception type for this.
			return PropertyHandler.getProperties(this.aggregateFunctionFileName, this.logger);			
		} else {
			return null;
		}
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
    private void processInputNodeListTable(Table nodeTable) throws Exception {
    	Map<Integer, Tuple> nodeRowsByUniqueIndex = new HashMap<Integer, Tuple>();
       	int totalCols = nodeTable.getColumnCount();

    	for (int rowIndex = 0; rowIndex < nodeTable.getRowCount(); rowIndex++) {
   	    	Tuple nodeRow = nodeTable.getTuple(rowIndex);
   	    	Integer nodeIndex = (Integer) nodeRow.get(totalCols - 2);
   	    	String starValue = ((String) nodeRow.get(totalCols - 1)).trim();

   	    	if (!nodeRowsByUniqueIndex.containsKey(nodeIndex)) {
   	    		nodeRowsByUniqueIndex.put(nodeIndex, nodeRow);    	    		
   	    	} else {
   	    		Node node = getNodeFromInputGraph(nodeRow);

	    	    if (node == null) {
	    	    	String logMessage = String.format(
	    	    		"Failed to find %s in the original graph.", nodeRow.toString());
	    	    	this.logger.log(LogService.LOG_ERROR, logMessage);	
	    	    	this.hasErrorInNodeListTable = true;

	    	    	break;
	    	    }
	    	    
   	    		if (this.mergingTable.containsKey(nodeIndex)) {
   	    			NodeGroup nodeGroup = this.mergingTable.get(nodeIndex); 
   	    			this.mergingNodesMap.put(node, nodeIndex);
   	    			addANode(nodeGroup, starValue, node);   	    			
   	    		}
   	    		else {
   	    			NodeGroup nodeGroup = new NodeGroup();   	    			
   	    			this.mergingNodesMap.put(node, nodeIndex);
//   	    			printMergingNodesMap();
   	    			addANode(nodeGroup, starValue, node);
   	    			
   	    			//need to add the row in tempTable to mergingTable
   	    			nodeRow = nodeRowsByUniqueIndex.get(nodeIndex);
   	    			node = getNodeFromInputGraph(nodeRow);

   	    			if (node == null) {
   	    				String logMessage = String.format(
   	    					"Failed to find %s in the original graph.", nodeRow.toString());
   	    	    		this.logger.log(LogService.LOG_ERROR, logMessage);	
   	    	    		this.hasErrorInNodeListTable = true;

   	    	    		break;
   	    	    	}

   	    			this.mergingNodesMap.put(node, nodeIndex); 
   	    			starValue = ((String) nodeRow.get(totalCols - 1)).trim();
   	    			addANode(nodeGroup, starValue, node);
   	    			   	    			
   	    			this.mergingTable.put(nodeIndex, nodeGroup);    	    		
   	    		} 	    	
   	    	}
   	    }    	    	
    }  
    
    private Node getNodeFromInputGraph(Tuple nodeRow) throws Exception {
    	Node originalNode = null;
		Iterator<Node> nodes = this.inputNetwork.nodes();

		while (nodes.hasNext()) {
			originalNode = nodes.next();

			if (compareTuple(nodeRow, originalNode)) {
				break;
			}
		}

		if (originalNode == null) {
			String logMessage = String.format(
				"Failed to find the node %s in the original graph.", nodeRow.toString());
			this.logger.log (LogService.LOG_ERROR, logMessage);
		}

		return originalNode;		
    }
	
    /*
     * The first tuple from inputMergeTable, the second tuple from original graph
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
            this.logger.log(LogService.LOG_WARNING, "Can not handle "+dataType+" yet. \n");
            return false;
        }
    }         

    
    private void addANode(NodeGroup nodeGroup, String starValue, Node node){
    	if (starValue.equals("*"))
			nodeGroup.addPrimaryNodeToGroup(node);   	    				
		else
			nodeGroup.addNodeToGroup(node); 
    }
    
    private File generateReport (Map<Integer, NodeGroup> MergingTable) throws IOException{
    	File report = getTempFile();
    	PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(report)));
    	Iterator<Integer> keys = MergingTable.keySet().iterator();
    	StringBuffer errorMsg = new StringBuffer();
    	while (keys.hasNext()){
    		Integer key = keys.next();
    		NodeGroup nodeGroup = MergingTable.get(key);
    		if(!nodeGroup.getErrorFlag()){
    			//add summary to mergingMsg
    			/*
    			  
    			  Merge Albert, R
    			 		Albet, R 
    			        Albett, R
    			  Using Albert, R for the node label (primary key).
    			  */
    			Iterator<?> labels = nodeGroup.getColumnValues(0).iterator();
    			Iterator<?> keyLabels = nodeGroup.getPrimaryColValues(0).iterator();
 	
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
    			Iterator<?> labels = nodeGroup.getColumnValues(0).iterator();
    			List<?> primaryKeys = nodeGroup.getPrimaryColValues(0);
 	
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
    
    private String getStringList (Iterator<?> iterator){
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
			tempFile = File.createTempFile("MergeNodes-", ".txt", tempDir);
		
		}catch (IOException e){
			this.logger.log(LogService.LOG_ERROR, e.toString(), e);
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+
					"temp"+System.currentTimeMillis()+".txt");
		}
		return tempFile;
	}
	
	private boolean isErrorInNodeListTable(){
		boolean isError = false;
		Iterator<Integer> keys = this.mergingTable.keySet().iterator();

		while (keys.hasNext()) {
			Object key = keys.next();
			NodeGroup value = this.mergingTable.get(key);

			if (value.getErrorFlag()) {
				isError = true;

				break;
			}				
		}

		return isError;			
	}

	private void copyValue(Tuple orgTuple, Tuple newTuple, Schema theSchema, int startingIndex) {
		int columnCount = theSchema.getColumnCount();

		for(; startingIndex < columnCount; startingIndex++) {
			Object value = orgTuple.get(startingIndex);
			newTuple.set(startingIndex, value);
		}
	}
	
	/*
	 * Merge node and update the graph.
	 * Work with orgNetwork, fullNodeIDList, mergingNodesMap,
	 * mergingTable, and parameters, output is a new graph
	 */        		
	private Graph updateGraphByMergingNodes(
			Graph theInputGraph, Properties aggFunctionKeyValuePairs) throws Exception {
		if (aggFunctionKeyValuePairs != null) {
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
				edgeSchema.instantiate(), this.isDirected);

		return theOutputGraph;
	}
	
	private void copyAndMergeNodes(Graph updatedGraph) throws Exception {
		/*
		 * Store nodes that should be merged but not a primary node
		 * key = this type of node
		 * value = the corresponding primary node
		 */
		HashMap<Node, Node> leftNodes = new HashMap<Node, Node>();
		Table orgNodeTable = this.inputNetwork.getNodeTable();
		Schema orgNodeSchema = orgNodeTable.getSchema();
		
		Iterator<Node> nodes = this.inputNetwork.nodes();
		while(nodes.hasNext()){
			Node originalNode = nodes.next();
			//if the node is not in the mergingNodeMap, 
			//add the node to the updated graph.
			if(!this.mergingNodesMap.containsKey(originalNode)) {
				Node newNode = updatedGraph.addNode();
				copyValue(originalNode, newNode, orgNodeSchema, 0);
				this.nodeMap.put(originalNode, newNode);
			}
			else{
				Integer nodeGroupIndex = this.mergingNodesMap.get(originalNode);
				NodeGroup nodeGroup = this.mergingTable.get(nodeGroupIndex);
				Node primaryNode = nodeGroup.getPrimaryNode();

				if (primaryNode == null) {
					String format =
						"Error: Unable to find the primary node for the node group that " +
						"contains %s";
					String exceptionMessage = String.format(format, originalNode.toString());
					throw new Exception(exceptionMessage);
				}

				if (primaryNode == originalNode) {
					Node newNode = updatedGraph.addNode();
					copyValue(originalNode, newNode, orgNodeSchema, 0);
					this.nodeMap.put(originalNode, newNode);
				} else {
					//store primaryNode != orgNode
					leftNodes.put(originalNode,primaryNode);
				}
			}
		}
		if (!leftNodes.isEmpty()){
			//handle primaryNode != orgNode
			nodes = leftNodes.keySet().iterator();
			while(nodes.hasNext()){
				Node orgNode = nodes.next();
				Node primaryNode = leftNodes.get(orgNode);
				Node newNode = this.nodeMap.get(primaryNode);
				updateValues(newNode, orgNode, "node");
				this.nodeMap.put(orgNode, newNode);				
			}
		}		
	}
	
	private void copyAndMergeEdges(Graph updatedGraph) throws Exception {
//		printMergingNodesMap();
//		printNodeMap();
		Table edgeTable = this.inputNetwork.getEdgeTable();
		Schema edgeSchema = edgeTable.getSchema();
		
		Iterator<?> edges = this.inputNetwork.edges();
		while(edges.hasNext()){
			Edge orgEdge = (Edge) edges.next();
			Node orgSourceNode = orgEdge.getSourceNode();
			Node orgTargetNode = orgEdge.getTargetNode();
			Node newSourceNode = this.nodeMap.get(orgSourceNode);
			Node newTargetNode = this.nodeMap.get(orgTargetNode);
			Object newEdgeKey = null;
			
			if(this.isDirected){
				DirectedEdge newEdgeSet= new DirectedEdge(newSourceNode, newTargetNode);
				newEdgeKey = newEdgeSet;
			}			
			else{
				//deal undirected
				Set<Node> newEdgeSet = new HashSet<Node>();
				newEdgeSet.add(newSourceNode);
				newEdgeSet.add(newTargetNode);
				newEdgeKey = newEdgeSet; 
			}
			
			if(this.edgeMap.containsKey(newEdgeKey)){
				Edge theEdge = this.edgeMap.get(newEdgeKey);
				updateValues(theEdge, orgEdge, "edge");		
			}
			else{
				Edge newEdge = updatedGraph.addEdge(newSourceNode, newTargetNode);
				copyValue(orgEdge, newEdge, edgeSchema, 2);
				this.edgeMap.put(newEdgeKey, newEdge);
			}						
		}	 
	}


	/**
	 * @param primaryTuple
	 * 		The row into which another row is being merged
	 * @param theTuple
	 * 		The row to merge into the primaryTuple
	 * @param tag
	 * 		Either "edge" or "node": identifies whether to set an edge or node ... ??
	 */
	private boolean updateValues (Tuple primaryTuple, Tuple theTuple, String tag){
		boolean isSuccessful = true;
		Schema theSchema = primaryTuple.getSchema();
		Map<String, String> functionMap = null;
		int startingColumnIndex = 0;
		if (tag.equalsIgnoreCase("node")) {
			startingColumnIndex = 1;
			functionMap = this.nodeFunctions;
		} else if (tag.equalsIgnoreCase("edge")) {
			startingColumnIndex = 2;
			functionMap = this.edgeFunctions;
		} else {
			throw new IllegalArgumentException("Attributes can only be added to 'edge' or 'node', not " + tag);
		}
		/*
		 * Note: aggregationFunctions are not always required if there's no node 
		 * and edge attributes in the network.
		 */
		if (functionMap != null){
			
			for (int i = startingColumnIndex; i < theSchema.getColumnCount(); i++) {
				final String columnName = theSchema.getColumnName(i);
				final Class<?> type = theSchema.getColumnType(i);

				final String functionName = functionMap.get(columnName
						.toLowerCase());
				if (functionName == null) {
					isSuccessful = false;
					this.logger
							.log(LogService.LOG_ERROR,
									"No function was mapped for the column '"
											+ columnName
											+ "'.  Please review the aggregate function file.  It is very likely that data was discarded during the merge!");
					continue;
				}
				try {
					UtilityFunction theFunction = getFunction(functionName,
							type);
					if (theFunction != null) {
						Object primaryValue = primaryTuple.get(i);
						Object theValue = theTuple.get(i);
						Object newValue = theFunction.operate(primaryValue,
								theValue);
						primaryTuple.set(i, newValue);
					}
				} catch (UnsupportedTypeForFunctionException e) {
					this.logger.log(LogService.LOG_ERROR,
							e.getLocalizedMessage(), e);
					isSuccessful = false;
					continue;
				}
			}
		}
		return isSuccessful;
	}	
	
	private void createUtilityFunctionMap(Properties aggFunctionKeyValuePairs){
	    //key= the column/attribute name for nodes or edges
	    //value = function name such as sum, max, ignore, etc.
	 	this.nodeFunctions = new HashMap<String, String>();
		this.edgeFunctions = new HashMap<String, String>();
		Enumeration<?> names = aggFunctionKeyValuePairs.propertyNames();
		while (names.hasMoreElements()) {

			final String key = (String) names.nextElement();			
			String value = aggFunctionKeyValuePairs.getProperty(key);
			String columnName = key.substring(key.indexOf(".")+1);
			String functionName = value.substring(value.indexOf(".")+1);
			if (key.startsWith("edge.")) {
				this.edgeFunctions.put(columnName.toLowerCase(), functionName);				
			}
			else if (key.startsWith("node.")) {
				this.nodeFunctions.put(columnName.toLowerCase(), functionName);
			}
		}
	}
	
	private static UtilityFunction getFunction(String functionName,
			Class<?> type) throws UnsupportedTypeForFunctionException {

		final String errorMsg = new StringBuilder().append("Can not support ")
				.append(functionName).append(" for ").append(type.getName())
				.append(" yet.").append("\n").toString();

		final String name = functionName.toLowerCase();

		if (name.equals("count")) {
			return new Count();
		} else if (name.equals("arithmeticmean")) {
			throw new UnsupportedTypeForFunctionException(errorMsg);
		} else if (name.equals("sum")) {
			if (type.equals(int.class) || type.equals(Integer.class)) {
				return new IntegerSum();
			} else if (type.equals(double.class) || type.equals(Double.class)) {
				throw new UnsupportedTypeForFunctionException(errorMsg);
			} else if (type.equals(float.class) || type.equals(Float.class)) {
				throw new UnsupportedTypeForFunctionException(errorMsg);
			}
		} else if (name.equals("max")) {
			if (type.equals(int.class) || type.equals(Integer.class)) {
				throw new UnsupportedTypeForFunctionException(errorMsg);
			} else if (type.equals(double.class) || type.equals(Double.class)) {
				throw new UnsupportedTypeForFunctionException(errorMsg);
			} else if (type.equals(float.class) || type.equals(Float.class)) {
				throw new UnsupportedTypeForFunctionException(errorMsg);
			}
		} else if (name.equals("min")) {
			if (type.equals(int.class) || type.equals(Integer.class)) {
				throw new UnsupportedTypeForFunctionException(errorMsg);
			} else if (type.equals(double.class) || type.equals(Double.class)) {
				throw new UnsupportedTypeForFunctionException(errorMsg);
			} else if (type.equals(float.class) || type.equals(Float.class)) {
				throw new UnsupportedTypeForFunctionException(errorMsg);
			}
		} else if (name.equals("ignore")) {
			return null;
		}
		return null;
	}

	/**
	 * Represents errors that occur when no function class (e.g. Sum) could be
	 * found to support a type (e.g. Integer.class).
	 */
	public static class UnsupportedTypeForFunctionException extends Exception {
		private static final long serialVersionUID = -2285330745413728087L;

		/**
		  * @see Exception#Exception()
		  */
		 public UnsupportedTypeForFunctionException() {
			 super();
		 }
		 
		 /**
		  * @see Exception#Exception(String) 
		  */
		 public UnsupportedTypeForFunctionException(String message) {
			 super(message);			
		 }
		 
		 /**
		  * @see Exception#Exception(Throwable)
		  */
		 public UnsupportedTypeForFunctionException(Throwable cause) {
			 super(cause);			
		 }
		 
		 /**
		  * @see Exception#Exception(String, Throwable)
		  */
		 public UnsupportedTypeForFunctionException(String message, Throwable cause) {
			 super(message, cause);
		 }
	 }
	
//	private void printMergingNodesMap() {
//		Iterator<Node> keys = this.mergingNodesMap.keySet().iterator();
//		while (keys.hasNext()){
//			Node node = keys.next();
////			System.out.println(">>node ="+node.get(nodeLabelField));
//		}
//	}
	
//	private void printEdges(Graph updatedGraph){
//		Iterator edges = updatedGraph.edges();
//		while(edges.hasNext()){
//			Edge edge = (Edge)edges.next();
//			int source = edge.getSourceNode().getRow();
//			int target = edge.getTargetNode().getRow();
////			System.out.println(""+source+"\t"+target);
//		}
//		
//	}
	
//	private void printNodeMap(){
////		System.out.println(">>in print nodeMap, size="+nodeMap.size());
//		
//		Iterator keys = nodeMap.keySet().iterator();
//		while(keys.hasNext()){
//			Node orgNode = (Node)keys.next();
//			Node newNode = (Node)nodeMap.get(orgNode);
////			System.out.println("orgNode= "+orgNode.get(nodeLabelField)+
////					",  newNode = "+newNode.get(nodeLabelField));
//		}
//	}
}