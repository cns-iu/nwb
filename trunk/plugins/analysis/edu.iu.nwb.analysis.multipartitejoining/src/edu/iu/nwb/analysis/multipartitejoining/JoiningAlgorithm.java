package edu.iu.nwb.analysis.multipartitejoining;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import cern.colt.function.ObjectFunction;
import cern.colt.function.ObjectObjectFunction;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix3D;
import cern.colt.matrix.impl.SparseObjectMatrix3D;

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
	private Map transferred;
	
	private class IntegerAdder implements ObjectObjectFunction {
		public Object apply(Object aggregate, Object current) {
			return new Long(((Number) aggregate).longValue() + ((Number) current).longValue());
		}
	}
	
	private class DoubleAdder implements ObjectObjectFunction {
		public Object apply(Object aggregate, Object current) {
			return new Double(((Number) aggregate).doubleValue() + ((Number) current).doubleValue());
		}
	}
	private class LongMaker implements ObjectFunction {
		
		private String field;

		public LongMaker(String field) {
			this.field = field;
		}
		
		public Object apply(Object current) {
			if(current == null) {
				return new Long(0);
			} else {
				return new Long(((Tuple) current).getLong(field));
			}
		}
	}
	
	private class IntegerMaker implements ObjectFunction {
		
		private String field;

		public IntegerMaker(String field) {
			this.field = field;
		}
		
		public Object apply(Object current) {
			if(current == null) {
				return new Integer(0);
			} else {
				return new Integer(((Tuple) current).getInt(field));
			}
		}
	}
	
	private class DoubleMaker implements ObjectFunction {
		
		private String field;

		public DoubleMaker(String field) {
			this.field = field;
		}
		
		public Object apply(Object current) {
			if(current == null) {
				return new Double(0);
			} else {
				return new Double(((Tuple) current).getDouble(field));
			}
		}
	}
	
	private class FloatMaker implements ObjectFunction {
		
		private String field;

		public FloatMaker(String field) {
			this.field = field;
		}
		
		public Object apply(Object current) {
			if(current == null) {
				return new Float(0);
			} else {
				return new Float(((Tuple) current).getFloat(field));
			}
		}
	}
	
	
    
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
        this.transferred = new HashMap();
    }

    public Data[] execute() {
    	
    	log = (LogService) context.getService(LogService.class.getName());
    	
		Graph inputGraph = (Graph) data[0].getData();
    	
    	String key = (String) parameters.get("key");
    	String type = (String) parameters.get("type");
    	String join = (String) parameters.get("join");
    	File metadataFile = new File((String)parameters.get("metadata"));
    	
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
		
		Iterator joinTemp = inputGraph.tuples(ExpressionParser.predicate("[" + key + "] = \"" + join + "\""));
		int totalJoinNodes = 0;
		while(joinTemp.hasNext()) {
			totalJoinNodes += 1;
			joinTemp.next();
		}
		
		int nodeCount = inputGraph.getNodeCount();
		ObjectMatrix3D matrix = new SparseObjectMatrix3D(totalJoinNodes, nodeCount, nodeCount);
		
		
		Iterator typeIterator = inputGraph.tuples(ExpressionParser.predicate("[" + key + "] = \"" + type + "\""));
		Iterator joinIterator = inputGraph.tuples(ExpressionParser.predicate("[" + key + "] = \"" + join + "\""));
		
		
		Vector typeNodes = new Vector();
		while(typeIterator.hasNext()) {
			typeNodes.add(typeIterator.next());
		}
		
		int currentJoinNodeLocation = 0;
		while(joinIterator.hasNext()) {
			Node joinNode = (Node) joinIterator.next();
			currentJoinNodeLocation += 1;
			
			Iterator neighbors = joinNode.neighbors();
			Stack typeNeighbors = new Stack();
			while(neighbors.hasNext()) {
				Node neighbor = (Node) neighbors.next();
				if(typeNodes.contains(neighbor)) {
					typeNeighbors.push(neighbor);
				}
			}
			
			while(!typeNeighbors.isEmpty()) {
				Node first = (Node) typeNeighbors.pop();//moveTo((Node) typeNeighbors.pop(), outputGraph);
				for(Iterator rest = typeNeighbors.iterator(); rest.hasNext(); ) {
					Node second = (Node) rest.next();//moveTo((Node) rest.next(), outputGraph);
					
					matrix.setQuick(currentJoinNodeLocation, first.getRow(), second.getRow(), joinNode);
					
					//fold(first, second, joinNode, metadata);
				}
			}
		}
		
		for(int row = 0; row < matrix.rows(); row++) {
			for(int column = 0; column < matrix.columns(); column++) {
				ObjectMatrix1D joins = matrix.viewRow(row).viewColumn(column);
				if(joins.cardinality() != 0) {
					Node first = moveTo(inputGraph.getNode(row), outputGraph);
					Node second = moveTo(inputGraph.getNode(column), outputGraph);
					fold(first, second, joins, metadata);
				}
			}
		}
		
		
		
		Data outputData = new BasicData(outputGraph, Graph.class.getName());
		Dictionary attributes = outputData.getMetaData();
		attributes.put(DataProperty.MODIFIED, new Boolean(true));
		attributes.put(DataProperty.PARENT, this.data[0]);
		attributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		attributes.put(DataProperty.LABEL, "Graph of a join of " + type + " nodes across " + join + " nodes.");
    	
    	return new Data[]{outputData};
    }
    
    private Schema createEdgeSchema(Schema joinSchema, Properties metadata) {
		Schema output = new Schema();
		output.addColumn("source", int.class);
		output.addColumn("target", int.class);
		
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

	private void fold(Node first, Node second, ObjectMatrix1D joins, Properties metadata) {
		
		boolean weightSpecifiedFlag = false;
		
		Edge edge = first.getGraph().getEdge(first, second);
		if(edge == null) {
			edge = first.getGraph().addEdge(first, second);
		}
		
		for(Enumeration keys = metadata.propertyNames(); keys.hasMoreElements(); ) {
			String key = (String) keys.nextElement();
			
			String[] keyArray = (String[]) metadata.getProperty(key).split(".");
			String sourceField = keyArray[0];
			String operation = keyArray[1];
			
			if(key.startsWith("edge.")) {
				
				
				
				
				
				String realKey = key.split(".")[1];
				
				if(realKey.equals("weight")) {
					weightSpecifiedFlag = true;
				}
				
				foldField(operation, sourceField, realKey, joins, edge);
				
				
				
				
				
			} else {
				
				foldField(operation, sourceField, key, joins, first);
				foldField(operation, sourceField, key, joins, second);
			}
			
		}
		
		if(!weightSpecifiedFlag) {
			edge.setInt("weight", number(joins));
		}
	}

	private int number(ObjectMatrix1D joins) {
		return ((Number) joins.aggregate(new IntegerAdder(), new ObjectFunction() {
			public Object apply(Object current) {
				if(current == null) {
					return new Integer(0);
				} else {
					return new Integer(1);
				}
			}
		})).intValue();
	}
	
	private double doubleSum(ObjectMatrix1D joins, final String field) {
		return ((Number) joins.aggregate(new DoubleAdder(), new DoubleMaker(field))).doubleValue();
	}
	
	private float floatSum(ObjectMatrix1D joins, final String field) {
		return ((Number) joins.aggregate(new DoubleAdder(), new FloatMaker(field))).floatValue();
	}
	
	private int integerSum(ObjectMatrix1D joins, final String field) {
		return ((Number) joins.aggregate(new IntegerAdder(), new IntegerMaker(field))).intValue();
	}
	
	private long longSum(ObjectMatrix1D joins, final String field) {
		return ((Number) joins.aggregate(new IntegerAdder(), new LongMaker(field))).longValue();
	}

	private void foldField(String operation, String sourceField, String key, ObjectMatrix1D joins, Tuple tuple) {
		if("sum".equals(operation)) {
			if(tuple.canSetDouble(key)) {
				tuple.setDouble(key, doubleSum(joins, sourceField));
			} else if(tuple.canSetFloat(key)) {
				tuple.setFloat(key, floatSum(joins, sourceField));
			} else if(tuple.canSetLong(key)) {
				tuple.setLong(key, longSum(joins, sourceField));
			} else if(tuple.canSetInt(key)) {
				tuple.setFloat(key, integerSum(joins, sourceField));
			}
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
    		
			if(output.getColumnIndex(columnName) == -1) {
    			output.addColumn(columnName, schema.getColumnType(columnName), schema.getDefault(columnName));
    		}
    	}
    	
    	return output;
    }
    
	private Node moveTo(Node oldNode, Graph newGraph) {
		if(this.transferred.keySet().contains(oldNode)) {
			return (Node) this.transferred.get(oldNode);
		} else {
			Node newNode = newGraph.addNode();

			Schema newSchema = newGraph.getNodeTable().getSchema();

			for(int columnIndex = 0; columnIndex < oldNode.getColumnCount(); columnIndex++) {
				String columnName = oldNode.getColumnName(columnIndex);
				if(newSchema.getColumnIndex(columnName) != -1) {
					newNode.set(columnName, oldNode.get(columnIndex));
				}
			}
			this.transferred.put(oldNode, newNode);
			return newNode;
		}
	}
}