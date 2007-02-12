package edu.iu.nwb.preprocessing.csv;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.io.DataIOException;

public class CsvAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
	private LogService logger;
    
    public CsvAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger=(LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() {
    	
    	CSVTableReader tableReader = new CSVTableReader();
    	tableReader.setHasHeader(true);
    	
    	Table nodes = null;
    	Table edges = null;
		try {
			nodes = tableReader.readTable((String) parameters.get("nodes"));
			edges = tableReader.readTable((String) parameters.get("edges"));
		} catch (DataIOException e) {
			e.printStackTrace();
			return null;
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
		
    	Graph graph = new Graph(nodes, edges, false, (String) parameters.get("nodeid"), (String) parameters.get("first"), (String) parameters.get("second"));
    	
    	Data data = new BasicData(graph, Graph.class.getName());
    	Dictionary map = data.getMetaData();
        map.put(DataProperty.LABEL, "Prefuse Beta Graph from CSV");
        map.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
    	
    	return new Data[] { data };
    }
    
}