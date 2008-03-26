package edu.iu.nwb.analysis.multipartitejoining;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;

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
import prefuse.data.Tuple;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.tuple.TableTuple;
import prefuse.data.tuple.TupleSet;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import cern.colt.matrix.ObjectMatrix3D;
import cern.colt.matrix.impl.SparseObjectMatrix2D;
import cern.colt.matrix.impl.SparseObjectMatrix3D;

/**
 * @author Russell Duhon
 *
 */
public class JoiningAlgorithm implements Algorithm {
    private static final String WEIGHT_COLUMN = "weight";
	private static final String SEPARATOR = "\\.";
	private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
	private LogService log;
	private Map transferred;
	
	private static Map typeMap = new HashMap();
	
	static {
		typeMap.put("count", int.class);
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

    public Data[] execute() throws AlgorithmExecutionException {
    	
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
			throw new AlgorithmExecutionException("The file " + metadataFile.getAbsolutePath() + " was not found.", e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("There was a problem loading the file " + metadataFile.getAbsolutePath(), e);
		}
		
		Schema inputNodeSchema = inputGraph.getNodeTable().getSchema();
		
		Schema outputNodeSchema = enhanceNodeSchema(inputNodeSchema, metadata);
		Schema outputEdgeSchema = createEdgeSchema(inputNodeSchema, metadata);
		
		Graph outputGraph = new Graph(outputNodeSchema.instantiate(), outputEdgeSchema.instantiate(), false);
		
		
		TupleSet nodes = inputGraph.getNodeTable();
		Iterator joinTemp = nodes.tuples(ExpressionParser.predicate("[" + key + "] = \"" + join + "\""));
		int totalJoinNodes = 0;
		while(joinTemp.hasNext()) {
			totalJoinNodes += 1;
			joinTemp.next();
		}
		
		int nodeCount = inputGraph.getNodeCount();
		ObjectMatrix3D edgeMatrix = new SparseObjectMatrix3D(totalJoinNodes, nodeCount, nodeCount);
		ObjectMatrix2D nodeMatrix = new SparseObjectMatrix2D(nodeCount, totalJoinNodes);
		
		
		Iterator typeIterator = nodes.tuples(ExpressionParser.predicate("[" + key + "] = \"" + type + "\""));
		Iterator joinIterator = nodes.tuples(ExpressionParser.predicate("[" + key + "] = \"" + join + "\""));
		
		
		Vector typeNodes = new Vector();
		while(typeIterator.hasNext()) {
			typeNodes.add(inputGraph.getNode(((TableTuple) typeIterator.next()).getRow()));
		}
		
		int currentJoinNodeLocation = 0;
		while(joinIterator.hasNext()) {
			Node joinNode = inputGraph.getNode(((TableTuple) joinIterator.next()).getRow());
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
					
					int row = Math.min(first.getRow(), second.getRow()); //this eliminates multiple edges (by creating a triangular matrix)
					int column = Math.max(first.getRow(), second.getRow());
					
					edgeMatrix.setQuick(currentJoinNodeLocation, row, column, joinNode);
					nodeMatrix.setQuick(first.getRow(), currentJoinNodeLocation, joinNode);
					nodeMatrix.setQuick(second.getRow(), currentJoinNodeLocation, joinNode);
					
					//fold(first, second, joinNode, metadata);
				}
			}
		}
		
		for(int row = 0; row < edgeMatrix.rows(); row++) {
			for(int column = 0; column < edgeMatrix.columns(); column++) {
				ObjectMatrix1D joins = edgeMatrix.viewRow(row).viewColumn(column);
				if(joins.cardinality() > 0) {
					Node first = moveTo(inputGraph.getNode(row), outputGraph);
					Node second = moveTo(inputGraph.getNode(column), outputGraph);
					foldEdge(first, second, joins, metadata);
				}
			}
		}
		
		for(int row = 0; row < nodeMatrix.rows(); row++) {
			ObjectMatrix1D joins = nodeMatrix.viewRow(row);
			Node rowNode = inputGraph.getNode(row);
			if(typeNodes.contains(rowNode)) {
				Node node = moveTo(rowNode, outputGraph);
				if(joins.cardinality() > 0) {
					foldNode(node, joins, metadata);
				}
			}
		}
		
		
		
		Data outputData = new BasicData(outputGraph, Graph.class.getName());
		Dictionary attributes = outputData.getMetadata();
		attributes.put(DataProperty.MODIFIED, new Boolean(true));
		attributes.put(DataProperty.PARENT, this.data[0]);
		attributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		attributes.put(DataProperty.LABEL, "Graph of a join of " + type + " nodes across " + join + " nodes.");
    	
    	return new Data[]{outputData};
    }
    
    private void foldNode(Node node, ObjectMatrix1D joins, Properties metadata) {
    	for(Enumeration keys = metadata.propertyNames(); keys.hasMoreElements(); ) {
			String key = (String) keys.nextElement();
			
			String[] keyArray = (String[]) metadata.getProperty(key).split(SEPARATOR);
			String sourceField = keyArray[0];
			String operation = keyArray[1];
			
			if(!key.startsWith("edge.")) {
				foldField(operation, sourceField, key, joins, node);
			}
			
		}
		
	}

	private Schema createEdgeSchema(Schema joinSchema, Properties metadata) {
		Schema output = new Schema();
		output.addColumn("source", int.class);
		output.addColumn("target", int.class);
		
		boolean weightSpecifiedFlag = false;
		
		for(Enumeration keys = metadata.propertyNames(); keys.hasMoreElements(); ) {
			String key = (String) keys.nextElement();
			if(key.startsWith("edge.")) {
				String realKey = key.split(SEPARATOR)[1];
				if(realKey.equals(WEIGHT_COLUMN)) {
					weightSpecifiedFlag = true;
				} else if(realKey.equals("source") || realKey.equals("target")) {
					log.log(LogService.LOG_WARNING, "The metadata key " + realKey + " is not allowed for edges and will be ignored.");
				} else {
					String fromKey = metadata.getProperty(key).split(SEPARATOR)[0];
					output.addColumn(realKey, joinSchema.getColumnType(fromKey));
				}
			}
		}
		
		if(!weightSpecifiedFlag) {
			output.addColumn(WEIGHT_COLUMN, int.class, new Integer(1));
		}
		
		return output;
	}

	private void foldEdge(Node first, Node second, ObjectMatrix1D joins, Properties metadata) {
		
		boolean weightSpecifiedFlag = false;
		
		Edge edge = first.getGraph().getEdge(first, second);
		if(edge == null) {
			edge = first.getGraph().addEdge(first, second);
		}
		
		for(Enumeration keys = metadata.propertyNames(); keys.hasMoreElements(); ) {
			String key = (String) keys.nextElement();
			
			String[] keyArray = (String[]) metadata.getProperty(key).split(SEPARATOR);
			String sourceField = keyArray[0];
			String operation = keyArray[1];
			
			if(key.startsWith("edge.")) {
				
				
				
				
				
				String realKey = key.split(SEPARATOR)[1];
				
				if(realKey.equals(WEIGHT_COLUMN)) {
					weightSpecifiedFlag = true;
				}
				
				foldField(operation, sourceField, realKey, joins, edge);
				
				
				
				
				
			}
			
		}
		
		if(!weightSpecifiedFlag) {
			edge.setInt(WEIGHT_COLUMN, Util.number(joins));
		}
	}

	private void foldField(String operation, String sourceField, String key, ObjectMatrix1D joins, Tuple tuple) {
		
		int cardinality = joins.cardinality();
		
		if("sum".equals(operation)) {
			if(tuple.canSetDouble(key)) {
				tuple.setDouble(key, Util.doubleSum(joins, sourceField));
			} else if(tuple.canSetFloat(key)) {
				tuple.setFloat(key, Util.floatSum(joins, sourceField));
			} else if(tuple.canSetLong(key)) {
				tuple.setLong(key, Util.longSum(joins, sourceField));
			} else if(tuple.canSetInt(key)) {
				tuple.setFloat(key, Util.integerSum(joins, sourceField));
			}
		} else if("average".equals(operation)) {
			if(tuple.canSetDouble(key)) {
				tuple.setDouble(key, Util.doubleSum(joins, sourceField) / cardinality);
			} else if(tuple.canSetFloat(key)) {
				tuple.setFloat(key, Util.floatSum(joins, sourceField) / cardinality);
			} else if(tuple.canSetLong(key)) {
				tuple.setLong(key, Util.longSum(joins, sourceField) / cardinality);
			} else if(tuple.canSetInt(key)) {
				tuple.setFloat(key, Util.integerSum(joins, sourceField) / cardinality);
			}
		} else if("min".equals(operation)) {
			if(tuple.canSetDouble(key)) {
				tuple.setDouble(key, Util.doubleMin(joins, sourceField));
			} else if(tuple.canSetFloat(key)) {
				tuple.setFloat(key, Util.floatMin(joins, sourceField));
			} else if(tuple.canSetLong(key)) {
				tuple.setLong(key, Util.longMin(joins, sourceField));
			} else if(tuple.canSetInt(key)) {
				tuple.setFloat(key, Util.integerMin(joins, sourceField));
			}
		} else if("max".equals(operation)) {
			if(tuple.canSetDouble(key)) {
				tuple.setDouble(key, Util.doubleMax(joins, sourceField));
			} else if(tuple.canSetFloat(key)) {
				tuple.setFloat(key, Util.floatMax(joins, sourceField));
			} else if(tuple.canSetLong(key)) {
				tuple.setLong(key, Util.longMax(joins, sourceField));
			} else if(tuple.canSetInt(key)) {
				tuple.setFloat(key, Util.integerMax(joins, sourceField));
			}
		} else if("mode".equals(operation)) {
			final Map map = (Map) joins.aggregate(new NumberMapper(), new FieldFetcher(sourceField));
			
			Object[] possibles = map.keySet().toArray();
			Arrays.sort(possibles, new Comparator() {
				public int compare(Object one, Object two) {
					Integer first = (Integer) map.get(one);
					Integer second = (Integer) map.get(two);
					
					return first.compareTo(second);
				}
			});
			
			tuple.set(key, possibles[possibles.length - 1]);
		} else if("count".equals(operation)) {
			tuple.setInt(key, Util.number(joins));
		}
		
	}
	private Schema enhanceNodeSchema(Schema schema, Properties metadata) {
    	Schema output = new Schema();
    	for(Enumeration keys = metadata.propertyNames(); keys.hasMoreElements();) {
    		String key = (String) keys.nextElement();
    		if(!key.startsWith("edge.")) {
    			if(schema.getColumnIndex(key) != -1) {
    				log.log(LogService.LOG_ERROR, "The metadata key " + key + " is already in use for this graph. It will be overwriten with the new value.");
    			}
    			String[] split = metadata.getProperty(key).split(SEPARATOR);
				String fromKey = split[0];
				String operation = split[1];
    			Class type = (Class) typeMap.get(operation);
    			if(type == null) {
    				type = schema.getColumnType(fromKey);
    			}
   				output.addColumn(key, type);
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