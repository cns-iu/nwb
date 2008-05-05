package edu.iu.nwb.analysis.extractdirectednetfromtable.components;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;
import edu.iu.nwb.analysis.extractnetfromtable.components.AggregateFunctionMappings;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;


public class ExtractDirectedNetworkFromTable {
	


	public static GraphContainer initializeGraph(Table pdt,String sourceColumnName, String targetColumnName, Properties p, LogService log) throws InvalidColumnNameException{

		final Schema inputSchema = pdt.getSchema();

		if(inputSchema.getColumnIndex(sourceColumnName) < 0)
			throw new InvalidColumnNameException(sourceColumnName + " was not a column in this table.\n");
		
		if(inputSchema.getColumnIndex(targetColumnName) < 0)
			throw new InvalidColumnNameException(targetColumnName + " was not a column in this table.\n");

		Schema nodeSchema = createNodeSchema();
		Schema edgeSchema = createEdgeSchema();
		
		AggregateFunctionMappings nodeAggregateFunctionMap = new AggregateFunctionMappings();
		AggregateFunctionMappings edgeAggregateFunctionMap = new AggregateFunctionMappings();
		
		parseProperties(inputSchema, nodeSchema, edgeSchema, p, 
				nodeAggregateFunctionMap, edgeAggregateFunctionMap, log);	

		Graph outputGraph = new Graph(nodeSchema.instantiate(),
				edgeSchema.instantiate(), true);
		
		

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

	private static void parseProperties(Schema input, Schema nodes, Schema edges, Properties p, 
			AggregateFunctionMappings nodeFunctionMappings, 
			AggregateFunctionMappings edgeFunctionMappings, LogService log){
		
		if(p != null){
			HashSet functionNames = new HashSet(AssembleAggregateFunctions.defaultAssembly().getFunctionNames());
			HashSet columnNames = new HashSet();

			for(int i = 0; i < input.getColumnCount(); i++){
				columnNames.add(input.getColumnName(i));
			}

			for (final Iterator it = p.keySet().iterator(); it.hasNext();) {
				final String key = (String) it.next();

				String sourceColumnName = p.getProperty(key);
				final int index = sourceColumnName.lastIndexOf(".");
				final String function = sourceColumnName.substring(index + 1);
				sourceColumnName = sourceColumnName.substring(0,
						index);
				final Class columnType = input.getColumnType(sourceColumnName);
				String newColumnName = key.substring(key.indexOf(".")+1);

				if(functionNames.contains(function) && columnNames.contains(sourceColumnName) && !columnNames.contains(newColumnName)){
					if (key.startsWith("edge.")) {
						createColumn(newColumnName,sourceColumnName, function, columnType, edges);
						edgeFunctionMappings.addFunctionMapping(newColumnName, sourceColumnName, function);
					}
					if (key.startsWith("node.")) {
						createColumn(newColumnName, sourceColumnName, function, columnType,
								nodes);
						nodeFunctionMappings.addFunctionMapping(newColumnName, sourceColumnName, function);
					}
				}

				if(!functionNames.contains(function)){
					log.log(LogService.LOG_WARNING, "Unrecognized function: "+ function + ".\nContinuing with " +
					"extraction, but ignoring this specific analysis.");
				}
				if(!columnNames.contains(sourceColumnName)){
					log.log(LogService.LOG_WARNING, "Unrecognized column: "+ sourceColumnName + ".\nContinuing with " +
					"extraction, but ignoring this specific analysis.");
				}
				if(columnNames.contains(newColumnName)){
					log.log(LogService.LOG_WARNING, "The column: "+ newColumnName + " already exists." +
							"\nContinuing with " +
					"extraction, but ignoring this specific analysis.");
				}
			}

		}
	}
	
	private static void createColumn(String newColumnName, String calculateColumnName, String function, Class columnType, Schema newSchema) {	
		Class finalType = null;
		finalType = AssembleAggregateFunctions.defaultAssembly().getAggregateFunction(function, columnType).getType();
		newSchema.addColumn(newColumnName, finalType);
	}
	

	
	
	
	
	
	
	
}