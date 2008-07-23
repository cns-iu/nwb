package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.column.Column;

public class GraphContainer {
	private Graph graph;
	private Table table;
	private AggregateFunctionMappings nodeMap;
	private AggregateFunctionMappings edgeMap;

	public GraphContainer(Graph g, Table t, AggregateFunctionMappings nodeFunctionMap, AggregateFunctionMappings edgeFunctionMap){
		this.graph = g;
		this.table = t;
		this.nodeMap = nodeFunctionMap;
		this.edgeMap = edgeFunctionMap;
	}

	public Graph buildGraph(String sourceColumnName, String targetColumnName, String splitString, LogService log){
		if(this.graph.isDirected()){
			return buildDirectedGraph(sourceColumnName,targetColumnName,splitString,log);
		}else{
			return buildUndirectedGraph(sourceColumnName,targetColumnName,splitString,log);
		}
	}
	
	private Graph buildUndirectedGraph(String sourceColumnName, String targetColumnName, String splitString, LogService log){
		boolean dupValues = false;
		final HashMap dupValuesErrorMessages = new HashMap();
		
		
		
		for (Iterator it = this.table.rows(); it.hasNext();){
			Node node1 = null;
			Node node2 = null;
			int row = ((Integer)it.next()).intValue();
			final String s = (String) this.table.get(row, targetColumnName);

			Set seenObject = new HashSet();

			if(s != null){ //no values to extract from
				final Pattern p = Pattern.compile("\\Q" + splitString + "\\E");
				final String[] objects = p.split(s);

				for (int i = objects.length - 1; i >= 0; i--) {
					if(seenObject.add(objects[i])){ //no duplicate nodes.
						node1 = NodeContainer.mutateNode(objects[i], this.graph, this.table, row, this.nodeMap, AggregateFunctionMappings.SOURCEANDTARGET);
					}
					node1 = this.graph.getNode(this.nodeMap.getFunctionRow(objects[i]).getRowNumber());
					for (int j = 0; j < i; j++) {
						if(!objects[j].equals(objects[i])){
							if(seenObject.add(objects[j])){ //no duplicate nodes.
								node2 = NodeContainer.mutateNode(objects[j], this.graph, this.table, row, this.nodeMap, AggregateFunctionMappings.SOURCEANDTARGET);
								
							}
							//create or modify an edge as necessary
							node2 = this.graph.getNode(this.nodeMap.getFunctionRow(objects[j]).getRowNumber());
							EdgeContainer.mutateEdge(node1, node2, this.graph, this.table, row, this.edgeMap);
						}else{
							dupValues = true; //detected a self-loop
						}
					}
				}
				if(dupValues){
					//String title = (String)pdt.get(row,pdt.getColumnNumber("TI"));
					String title = "unknown";
					//ExtractNetworkFromTable.addDuplicateValueErrorMessage(title, columnName, dupValuesErrorMessages);
					//dupValues = false;
				}
			}
			else{
				//String title = (String)pdt.get(row,pdt.getColumnNumber("TI"));
				String title = "unknown";
				//ExtractNetworkFromTable.printNoValueToExtractError(title, columnName, this.log);
			}
		}
		for(Iterator dupIter = dupValuesErrorMessages.keySet().iterator(); dupIter.hasNext();){
			log.log(LogService.LOG_WARNING, (String)dupValuesErrorMessages.get(dupIter.next()));
		}
		
		return this.graph;
	}
	
	private Graph buildDirectedGraph(String sourceColumnName, String targetColumnName, String splitString, LogService log){
		final Pattern p = Pattern.compile("\\Q" + splitString + "\\E");
		final HashMap dupValuesErrorMessages = new HashMap();
		Column sourceColumn = this.table.getColumn(sourceColumnName);
		Column targetColumn = this.table.getColumn(targetColumnName);
		Node node1;
		Node node2;
		for (Iterator it = this.table.rows(); it.hasNext();){

			int row = ((Integer)it.next()).intValue();
			final String sourceString = (String) sourceColumn.get(row);
			final String targetString = (String) targetColumn.get(row);

			Set seenSource = new HashSet();
			Set seenTarget;// = seenSource;

			if(sourceString != null && targetString != null){ //ensure we have values to extract

				final String[] sources = p.split(sourceString);
				final String[] targets = p.split(targetString);

				for (int i = 0; i < sources.length; i++) {
					if(seenSource.add(sources[i])){ 
						node1 = NodeContainer.mutateNode(sources[i],this.graph,this.table,row,this.nodeMap,AggregateFunctionMappings.SOURCE);
							seenTarget = new HashSet();
						
						for (int j = 0; j < targets.length; j++) {
								if(seenTarget.add(targets[j])){
									node2 = NodeContainer.mutateNode(targets[j],this.graph,this.table,row,this.nodeMap,AggregateFunctionMappings.TARGET);
								
									EdgeContainer.mutateEdge(node1,node2,this.graph,this.table,row,this.edgeMap);
							}
						}
					}
				}
		}
		else{
				//String title = (String)pdt.get(row,pdt.getColumnNumber("TI"));
				String title = "unknown";
				//ExtractNetworkFromTable.printNoValueToExtractError(title, sourceColumnName, log);
			}
		}
		for(Iterator dupIter = dupValuesErrorMessages.keySet().iterator(); dupIter.hasNext();){
			log.log(LogService.LOG_WARNING, (String)dupValuesErrorMessages.get(dupIter.next()));
		}


		return this.graph;
	}
	
	public static GraphContainer initializeGraph(Table pdt,String sourceColumnName, String targetColumnName, boolean isDirected,Properties p, LogService log) throws InvalidColumnNameException{

		final Schema inputSchema = pdt.getSchema();

		if(inputSchema.getColumnIndex(sourceColumnName) < 0)
			throw new InvalidColumnNameException(sourceColumnName + " was not a column in this table.\n");
		
		if(inputSchema.getColumnIndex(targetColumnName) < 0)
			throw new InvalidColumnNameException(targetColumnName + " was not a column in this table.\n");

		Schema nodeSchema = createNodeSchema();
		Schema edgeSchema = createEdgeSchema();
		
		AggregateFunctionMappings nodeAggregateFunctionMap = new AggregateFunctionMappings();
		AggregateFunctionMappings edgeAggregateFunctionMap = new AggregateFunctionMappings();
		
		AggregateFunctionMappings.parseProperties(inputSchema, nodeSchema, edgeSchema, p, 
				nodeAggregateFunctionMap, edgeAggregateFunctionMap, log);	

		Graph outputGraph = new Graph(nodeSchema.instantiate(),
				edgeSchema.instantiate(), isDirected);
		
		

		return new GraphContainer(outputGraph,pdt,nodeAggregateFunctionMap,edgeAggregateFunctionMap);

	}

	private static Schema createNodeSchema(){
		Schema nodeSchema = new Schema();
		nodeSchema.addColumn("label", String.class);
		return nodeSchema;
	}

	private static Schema createEdgeSchema(){
		Schema edgeSchema = new Schema();
		edgeSchema.addColumn("source",int.class);
		edgeSchema.addColumn("target",int.class);
		return edgeSchema;
	}
}
