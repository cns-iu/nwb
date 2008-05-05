package edu.iu.nwb.analysis.extractdirectednetfromtable.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.osgi.service.log.LogService;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.column.Column;
import edu.iu.nwb.analysis.extractnetfromtable.components.AggregateFunctionMappings;

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
		final Pattern p = Pattern.compile("\\Q" + splitString + "\\E");
		boolean dupValues = false;
		String error;
		final HashMap dupValuesErrorMessages = new HashMap();
		Column sourceColumn = this.table.getColumn(sourceColumnName);
		Column targetColumn = this.table.getColumn(targetColumnName);
		Node node1;
		Node node2;
		Edge edge;
		for (Iterator it = this.table.rows(); it.hasNext();){

			int row = ((Integer)it.next()).intValue();
			final String sourceString = (String) sourceColumn.get(row);
			final String targetString = (String) targetColumn.get(row);

			Set seenSource = new HashSet();
			Set seenTarget;

			if(sourceString != null && targetString != null){ //ensure we have values to extract

				final String[] sources = p.split(sourceString);
				final String[] targets = p.split(targetString);

				for (int i = 0; i < sources.length; i++) {
					if(seenSource.add(sources[i])){ 
						node1 = NodeContainer.mutateNode(sources[i],this.graph,this.table,row,this.nodeMap);

						seenTarget = new HashSet();
						for (int j = 0; j < targets.length; j++) {
							if(seenTarget.add(targets[j])){
								node2 = NodeContainer.mutateNode(targets[j],this.graph,this.table,row,this.nodeMap);
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


}
