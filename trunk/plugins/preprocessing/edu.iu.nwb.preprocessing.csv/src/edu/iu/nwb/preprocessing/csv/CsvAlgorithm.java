package edu.iu.nwb.preprocessing.csv;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.io.DataIOException;

public class CsvAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public CsvAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	CSVTableReader tableReader = new CSVTableReader();
    	tableReader.setHasHeader(true);
    	
    	Table nodes = null;
    	Table edges = null;
		try {
			nodes = tableReader.readTable((String) parameters.get("nodes"));
			edges = tableReader.readTable((String) parameters.get("edges"));
		} catch (DataIOException e) {
			throw new AlgorithmExecutionException("Error reading tables: "+e.getMessage(),e);
		}
		
		/* class ToIntegerExpression extends ColumnExpression {
			
			ToIntegerExpression(String name) {
				super(name);
			}
			
			public Class getType(Schema s) {
				return int.class;
			}
			
			public int getInt(Tuple t) {
				return Integer.parseInt(super.get(t).toString());
			}
			
			public String getString(Tuple t) {
				if(super.get(t) == null || "".equals(super.get(t))) {
					return "0";
				} else {
					return super.get(t).toString();
				}
			}
		}
		
		
		nodes.addColumn("_x_nwb_id", new ToIntegerExpression((String) parameters.get("nodeid")));
		edges.addColumn("_x_nwb_first", new ToIntegerExpression((String) parameters.get("first")));
		edges.addColumn("_x_nwb_second", new ToIntegerExpression((String) parameters.get("second"))); */
		
    	String nodeId = (String) parameters.get("nodeid");
		String first = (String) parameters.get("first");
		String second = (String) parameters.get("second");
		
		
		
		String nwbId = "_x_nwb_id";
		nodes.addColumn(nwbId, int.class);
		String nwbFirst = "_x_nwb_first";
		edges.addColumn(nwbFirst, int.class);
		String nwbSecond = "_x_nwb_second";
		edges.addColumn(nwbSecond, int.class);
		
		int nodeCount = nodes.getRowCount();
		Map newIds = new HashMap(nodeCount);
		
		int currentId = 0;
		
		for(int row = 0; row < nodeCount; row++) {
			Tuple node = nodes.getTuple(row);
			newIds.put(node.get(nodeId), new Integer(currentId));
			node.setInt(nwbId, currentId);
			currentId++;
		}
		
		int edgeCount = edges.getRowCount();
		for(int row = 0; row < edgeCount; row++) {
			Tuple edge = edges.getTuple(row);
			edge.setInt(nwbFirst, ((Integer) newIds.get(edge.get(first))).intValue());
			edge.setInt(nwbSecond, ((Integer) newIds.get(edge.get(second))).intValue());
		}
		
		
		Graph graph = new Graph(nodes, edges, false, nwbId, nwbFirst, nwbSecond);
    	
    	Data data = new BasicData(graph, Graph.class.getName());
    	Dictionary metadata = data.getMetadata();
        metadata.put(DataProperty.LABEL, "Prefuse Beta Graph from CSV");
        metadata.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
    	
    	return new Data[] { data };
    }
    
}