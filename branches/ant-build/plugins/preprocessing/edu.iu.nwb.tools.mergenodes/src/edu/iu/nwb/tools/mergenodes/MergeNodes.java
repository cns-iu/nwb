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
    private HashMap nodeMap = new HashMap();    
    private HashMap edgeMap = new HashMap();
    private List removedNodeIDList = new ArrayList();
    private Map<Node, Integer> mergingNodesMap = new HashMap<Node, Integer>();
    private Map<Integer, NodeGroup> mergingTable = new HashMap<Integer, NodeGroup>();
    private Map nodeFunctions;
    private Map edgeFunctions;
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
        	processInputNodeListTable(inputMergeTable);

        	if (this.mergingTable.isEmpty()) {
        		String logMessage =
        			"There is no merging instruction in the node list table. \n" +
					"So there is no merging action. \n";

        		logger.log(LogService.LOG_INFO, logMessage);

        		return null;
        	}
        		
        	if (!hasErrorInNodeListTable) {
        		hasErrorInNodeListTable = isErrorInNodeListTable();
        	}

        	if (hasErrorInNodeListTable) {
        		String logMessage =
        			"There are errors in the node list table. \n"+
    				"Please view the \"Merging Report\" File for details. \n";
        		logger.log(LogService.LOG_ERROR, logMessage);
        	}
        	
        	mergingReport = generateReport(this.mergingTable);
        	BasicData reportData =
        		new BasicData(mergingReport, mergingReport.getClass().getName());
        	Dictionary<String, Object> graphAttributes = reportData.getMetadata();
        	graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
        	graphAttributes.put(DataProperty.PARENT, this.inputNetworkData);
        	graphAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        	graphAttributes.put(DataProperty.LABEL, "Merging Report");
        	
        	try {
        		Graph outputGraph =
        			updateGraphByMergingNodes(inputNetwork, aggregateFunctionProperties);
        	
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
	    	    	logger.log(LogService.LOG_ERROR, logMessage);	
	    	    	hasErrorInNodeListTable = true;

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
   	    			nodeRow = (Tuple) nodeRowsByUniqueIndex.get(nodeIndex);
   	    			node = getNodeFromInputGraph(nodeRow);

   	    			if (node == null) {
   	    				String logMessage = String.format(
   	    					"Failed to find %s in the original graph.", nodeRow.toString());
   	    	    		logger.log(LogService.LOG_ERROR, logMessage);	
   	    	    		hasErrorInNodeListTable = true;

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
		Iterator nodes = this.inputNetwork.nodes();

		while (nodes.hasNext()) {
			originalNode = (Node) nodes.next();

			if (compareTuple(nodeRow, originalNode)) {
				break;
			}
		}

		if (originalNode == null) {
			String logMessage = String.format(
				"Failed to find the node %s in the original graph.", nodeRow.toString());
			logger.log (LogService.LOG_ERROR, logMessage);
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
			tempFile = File.createTempFile("MergeNodes-", ".txt", tempDir);
		
		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString(), e);
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+
					"temp"+System.currentTimeMillis()+".txt");
		}
		return tempFile;
	}
	
	private boolean isErrorInNodeListTable(){
		boolean isError = false;
		Iterator keys = this.mergingTable.keySet().iterator();

		while (keys.hasNext()) {
			Object key = keys.next();
			NodeGroup value = (NodeGroup) this.mergingTable.get(key);

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
		Table orgNodeTable = inputNetwork.getNodeTable();
		Schema orgNodeSchema = orgNodeTable.getSchema();
		
		Iterator nodes = inputNetwork.nodes();
		while(nodes.hasNext()){
			Node originalNode = (Node)nodes.next();
			//if the node is not in the mergingNodeMap, 
			//add the node to the updated graph.
			if(!this.mergingNodesMap.containsKey(originalNode)) {
				Node newNode = updatedGraph.addNode();
				copyValue(originalNode, newNode, orgNodeSchema, 0);
				nodeMap.put(originalNode, newNode);
			}
			else{
				Integer nodeGroupIndex = this.mergingNodesMap.get(originalNode);
				NodeGroup nodeGroup = (NodeGroup) this.mergingTable.get(nodeGroupIndex);
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
					nodeMap.put(originalNode, newNode);
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
		Table edgeTable = inputNetwork.getEdgeTable();
		Schema edgeSchema = edgeTable.getSchema();
		
		Iterator edges = inputNetwork.edges();
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