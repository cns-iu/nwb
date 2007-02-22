package edu.iu.nwb.analysis.multipartitejoining;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.expression.parser.ExpressionParser;

/**
 * @author Russell Duhon
 *
 */
public class JoiningAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
	private LogService log;
    
    //private Schema edgeWeightSchema = new Schema(new String[]{"weight", "source", "target"}, new Class[]{int.class, long.class, long.class});
    

    /**
     * Construct with the appropriate parameters
     * @param data This contains the input graph
     * @param parameters This contains r and q
     * @param context And this allows access to some additional capabilities
     */
    public JoiningAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	
    	log = (LogService) context.getService(LogService.class.getName());
    	
		Graph inputGraph = (Graph) data[0].getData();
    	
    	String key = (String) parameters.get("key");
    	String type = (String) parameters.get("type");
    	String join = (String) parameters.get("join");
    	File metadataFile = (File) parameters.get("metadata");
    	
    	Properties metadata = new Properties();
    	try {
			metadata.load(new FileInputStream(metadataFile));
		} catch (FileNotFoundException e) {
			log.log(LogService.LOG_ERROR, "The file " + metadataFile.getAbsolutePath() + " was not found.");
			return null;
		} catch (IOException e) {
			log.log(LogService.LOG_ERROR, "There was a problem loading the file " + metadataFile.getAbsolutePath(), e);
			return null;
		}
		
		Schema inputNodeSchema = inputGraph.getNodeTable().getSchema();
		
		Schema outputNodeSchema = enhanceNodeSchema(inputNodeSchema, metadata);
		Schema outputEdgeSchema = createEdgeSchema(inputNodeSchema, metadata);
		
		Graph outputGraph = new Graph(outputNodeSchema.instantiate(), outputEdgeSchema.instantiate(), inputGraph.isDirected());
		
		
		Iterator typeIterator = inputGraph.tuples(ExpressionParser.predicate("[" + key + "] = \"" + type + "\""));
		Iterator joinIterator = inputGraph.tuples(ExpressionParser.predicate("[" + key + "] = \"" + join + "\""));
		
		
		Vector typeNodes = new Vector();
		while(typeIterator.hasNext()) {
			typeNodes.add(typeIterator.next());
		}
		
		while(joinIterator.hasNext()) {
			Node joinNode = (Node) joinIterator.next();
			
			Iterator neighbors = joinNode.neighbors();
			Stack typeNeighbors = new Stack();
			while(neighbors.hasNext()) {
				Node neighbor = (Node) neighbors.next();
				if(typeNodes.contains(neighbor)) {
					typeNeighbors.push(neighbor);
				}
			}
			
			while(!typeNeighbors.isEmpty()) {
				Node first = moveTo((Node) typeNeighbors.pop(), outputGraph);
				for(Iterator rest = typeNeighbors.iterator(); rest.hasNext(); ) {
					Node second = moveTo((Node) rest.next(), outputGraph);
					fold(first, second, joinNode, metadata);
					
				}
				
			}
			
			
			
		}
		
		
		
		
		
		
    	
    	return null;
    }
    
    private Schema createEdgeSchema(Schema joinSchema, Properties metadata) {
		Schema output = new Schema();
		output.addColumn("source", long.class);
		output.addColumn("target", long.class);
		
		boolean weightSpecifiedFlag = false;
		
		for(Enumeration keys = metadata.propertyNames(); keys.hasMoreElements(); ) {
			String key = (String) keys.nextElement();
			if(key.startsWith("edge.")) {
				String realKey = key.split(".")[1];
				if(realKey.equals("weight")) {
					weightSpecifiedFlag = true;
				} else if(realKey.equals("source") || realKey.equals("target")) {
					log.log(LogService.LOG_WARNING, "The metadata key " + realKey + " is not allowed for edges and will be ignored.");
				} else {
					String fromKey = metadata.getProperty(key).split(".")[0];
					output.addColumn(realKey, joinSchema.getColumnType(fromKey));
				}
			}
		}
		
		if(!weightSpecifiedFlag) {
			output.addColumn("weight", int.class, new Integer(1));
		}
		
		return output;
	}

	private void fold(Node first, Node second, Node joinNode, Properties metadata) {
		
		boolean weightSpecifiedFlag = false;
		
		for(Enumeration keys = metadata.propertyNames(); keys.hasMoreElements(); ) {
			String key = (String) keys.nextElement();
			
			String[] keyArray = (String[]) metadata.getProperty(key).split(".");
			String sourceField = keyArray[0];
			String operation = keyArray[1];
			
			if(key.startsWith("edge.")) {
				
				
				
				Edge edge = first.getGraph().getEdge(first, second);
				if(edge == null) {
					edge = first.getGraph().addEdge(first, second);
				}
				
				String realKey = key.split(".")[1];
				
				if(realKey.equals("weight")) {
					weightSpecifiedFlag = true;
				}
				
				foldField(operation, sourceField, realKey, joinNode, edge);
				
				
				
				
				
			} else {
				
				foldField(operation, sourceField, key, joinNode, first);
				foldField(operation, sourceField, key, joinNode, second);
			}
			
		}
		
		if(!weightSpecifiedFlag) {
			
		}
	}

	private Schema enhanceNodeSchema(Schema schema, Properties metadata) {
    	Schema output = new Schema();
    	for(Enumeration keys = metadata.propertyNames(); keys.hasMoreElements();) {
    		String key = (String) keys.nextElement();
    		if(!key.startsWith("edge.")) {
    			if(schema.getColumnIndex(key) != -1) {
    				log.log(LogService.LOG_WARNING, "The metadata key " + key + " is already in use for this graph. It will be overwritten with the aggregated values");
    			}
    			String fromKey = (String) metadata.getProperty(key).split(".")[0];
    		
    			output.addColumn(key, schema.getColumnType(fromKey));
    		}
    	}
    	
    	for(int ii = 0; ii < schema.getColumnCount(); ii++) {
    		String columnName = schema.getColumnName(ii);
			if(output.getColumnIndex(columnName) != -1) {
    			output.addColumn(columnName, schema.getColumnType(columnName), schema.getDefault(columnName));
    		}
    	}
    	
    	return output;
    }
    
    private Node moveTo(Node oldNode, Graph newGraph) {
    	Node newNode = newGraph.addNode();
    	
    	Schema newSchema = newGraph.getNodeTable().getSchema();
    	
    	for(int columnIndex = 0; columnIndex < oldNode.getColumnCount(); columnIndex++) {
    		String columnName = oldNode.getColumnName(columnIndex);
    		if(newSchema.getColumnIndex(columnName) == -1) {
    			newNode.set(columnName, oldNode.get(columnIndex));
    		}
    	}
    	
    	return newNode;
    }
    
    private void foldField(String operation, String fromField, String toField, Node join, Tuple assign) {
    	
    }
    
    private void increase(Tuple tuple, String field, Number amount) {
    	Class columnType = tuple.getColumnType(field);
		
    	
    }
}