package edu.iu.nwb.analysis.symmetrize;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.symmetrize.aggregations.Aggregator;
import edu.iu.nwb.analysis.symmetrize.aggregations.Average;
import edu.iu.nwb.analysis.symmetrize.aggregations.FirstAlphabetically;
import edu.iu.nwb.analysis.symmetrize.aggregations.LastAlphabetically;
import edu.iu.nwb.analysis.symmetrize.aggregations.Maximum;
import edu.iu.nwb.analysis.symmetrize.aggregations.Minimum;
import edu.iu.nwb.analysis.symmetrize.aggregations.Sum;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;

public class Symmetrize implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    static final String PREFIX = "attribute.";
    private static Map aggregators;
    
    static {
    	aggregators = new HashMap();
    	aggregators.put("max", new Maximum());
    	aggregators.put("min", new Minimum());
    	aggregators.put("average", new Average());
    	aggregators.put("sum", new Sum());
    	aggregators.put("first alphabetically", new FirstAlphabetically());
    	aggregators.put("last alphabetically", new LastAlphabetically());
    }
    
    public Symmetrize(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	Graph graph = (Graph) data[0].getData();
		
    	LogService logger = (LogService)context.getService(LogService.class.getName());
    	
    	boolean matrix = ((Boolean) parameters.get("matrix")).booleanValue();
    	boolean bare = parameters.get("bare") != null;
    	
    	
    	if(matrix) {
    		logger.log(LogService.LOG_INFO, "You requested this graph be treated as a matrix. Missing directed edges will " +
    				"be treated as edges with zero and empty string-valued attributes");
    	}
		
    	if(!graph.isDirected()) {
    		logger.log(LogService.LOG_WARNING, "The graph is already undirected. Parallel edges will still be merged, but " +
    				"check your data if you expected this graph to be directed\n\n");
    	}
    	
    	if(!matrix && bare) {
    		logger.log(LogService.LOG_INFO, "No edge attributes to symmetrize. " +
    				"Edges (directed or undirected) between the same pair of nodes will still be " +
    				"replaced with a single undirected edge");
    	}
    	
    	
		Schema oldSchema = graph.getEdgeTable().getSchema();
		
		
		String sourceField = graph.getEdgeSourceField();
		String targetField = graph.getEdgeTargetField();
		
		
		
		Map attributes = makeAggregatorMap(oldSchema, sourceField, targetField);
		
		//construct the new schema
		Schema schema = new Schema();
		Iterator columns = attributes.keySet().iterator();
		while(columns.hasNext()) {
			String column = (String) columns.next();
			Class klass = (Class) attributes.get(column);
			schema.addColumn(column, klass);
		}
		
		if(matrix && bare) {
			schema.addColumn("weight", Integer.class, new Integer(1));
			this.parameters.put(PREFIX + "weight", parameters.get("bare"));
		}
		
		//empty tables
		Table nodeTable = graph.getNodeTable().getSchema().instantiate();
		Table edgeTable = schema.instantiate();
		
		//copy over nodes
		Iterator nodes = graph.getNodeTable().iterator();
		while(nodes.hasNext()) {
			nodeTable.addTuple(graph.getNodeTable().getTuple(((Integer) nodes.next()).intValue()));
			
		}
		
		
		//to map pairs of nodes onto groups of edges in the original graph
		Map groupings = new HashMap();
		
		//already?
		boolean symmetrized = true;
		
		
		//fill groupings
		Iterator edges = graph.getEdgeTable().iterator();
		while(edges.hasNext()) {
			Tuple tuple = graph.getEdgeTable().getTuple(((Integer) edges.next()).intValue());
			
			Set key = new HashSet();
			key.add(tuple.get(sourceField));
			key.add(tuple.get(targetField));
			
			if(groupings.containsKey(key)) {
				//found a second edge between two nodes
				symmetrized = false;
			} else {
				groupings.put(key, new ArrayList());
			}
			((List) groupings.get(key)).add(tuple);
		}
		
		if(symmetrized) {
			
			logger.log(LogService.LOG_WARNING, "This graph is already symmetrized. No new graph will be returned.");
			
			return new Data[]{};
		}
		
		System.err.println("There are " + groupings.size() + " groups");
		
		Iterator groups = groupings.keySet().iterator();
		while(groups.hasNext()) {
			Set key = (Set) groups.next();
			Object source;
			Object target;
			if(key.size() == 1) { //self loop
				source = key.toArray()[0];
				target = source;
			} else {
				Object[] parts = key.toArray(); //guaranteed to be size 2
				source = parts[0]; //doesn't matter the order because this is undirected
				target = parts[1];
			}
			
			List group = (List) groupings.get(key);
			
			if(!source.equals(target) && matrix) {
				boolean left = false;
				boolean right = false;
				Iterator tuples = group.iterator();
				while(tuples.hasNext()) {
					Tuple tuple = (Tuple) tuples.next();
					if(source.equals(tuple.get(sourceField))) {
						left = true;
					} else {
						right = true;
					}
				}
				if(!left || !right) {
					group.add(emptyTuple(schema));
				}
			}
			
			int row = edgeTable.addRow();
			edgeTable.set(row, sourceField, source);
			edgeTable.set(row, targetField, target);
			
			for(int attribute = 0; attribute < schema.getColumnCount(); attribute++) {
				String name = schema.getColumnName(attribute);
				Object aggregate = this.parameters.get(PREFIX + name);
				if(aggregate != null) {
					List values = new ArrayList();
					Iterator tuples = group.iterator();
					while(tuples.hasNext()) {
						Tuple tuple = (Tuple) tuples.next();
						values.add(tuple.get(name));
					}
					Object newValue = ((Aggregator) aggregators.get(aggregate)).aggregate(values);
					edgeTable.set(row, attribute, newValue);
				}
			}
		}
		
		Table realEdgeTable = edgeTable.getSchema().instantiate();
		
		if(matrix) {
			Iterator iter = edgeTable.tuples();
			while(iter.hasNext()) {
				Tuple tuple = (Tuple) iter.next();
				if(!isEmptyTuple(tuple)) {
					realEdgeTable.addTuple(tuple);
				}
			}
		}
		
		
		
		
		Graph resultGraph = new Graph(
				nodeTable,
				realEdgeTable,
				false,
				graph.getNodeKeyField(),
				graph.getEdgeSourceField(),
				graph.getEdgeTargetField());
		
		
		
		Data result = new BasicData(resultGraph, Graph.class.getName());
		Dictionary metadata = result.getMetadata();
		metadata.put(DataProperty.LABEL, "Symmetric Graph");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		
		return new Data[] { result };
    }

	private Map makeAggregatorMap(Schema oldSchema, String sourceField,
			String targetField) {
		//we'll keep the source and target for the new schema
		Map attributes = new HashMap();
		attributes.put(sourceField, oldSchema.getColumnType(sourceField));
		attributes.put(targetField, oldSchema.getColumnType(targetField));
		
		
		
		//we'll keep any attributes that are not dropped
		Enumeration keys = parameters.keys();
		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if(key.startsWith(PREFIX) && !"drop".equals(parameters.get(key))) {
				String attribute = key.substring(PREFIX.length());
				Aggregator aggregator = (Aggregator) aggregators.get(parameters.get(key));
				//the type will depend on the aggregation used and the original type
				attributes.put(attribute, aggregator.getType(oldSchema.getColumnType(attribute)));
			}
		}
		return attributes;
	}

	private Tuple emptyTuple(Schema schema) {
		Table table = schema.instantiate();
		Tuple row = table.getTuple(table.addRow());
		for(int attribute = 0; attribute < schema.getColumnCount(); attribute++) {
			String name = schema.getColumnName(attribute);
			Object aggregate = this.parameters.get(PREFIX + name);
			if(aggregate != null) {
				if(row.canGet(name, Number.class)) {
					if(row.canSetInt(name)) {
						row.setInt(name, 0);
					} else if(row.canSetFloat(name)) {
						row.setFloat(name, 0);
					}
				} else if(row.canSetString(name)){
					row.set(name, "");
				}
			}
		}
		return row;
	}
	
	private boolean isEmptyTuple(Tuple tuple) {
		Schema schema = tuple.getSchema();
		for(int attribute = 0; attribute < schema.getColumnCount(); attribute++) {
			String name = schema.getColumnName(attribute);
			Object aggregate = this.parameters.get(PREFIX + name);
			if(aggregate != null) {
				if(tuple.canGet(name, Number.class) || tuple.canGet(name, double.class)) {
					if(((Number) tuple.get(name)).doubleValue() != 0.0) {
						return false;
					}
				} else if(tuple.canGetString(name) && "".equals(tuple.getString(name))){
					return false;
				}
			}
		}
		
		
		return true;
	}
}