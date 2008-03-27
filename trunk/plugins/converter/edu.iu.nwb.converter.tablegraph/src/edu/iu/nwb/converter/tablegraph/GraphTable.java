package edu.iu.nwb.converter.tablegraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
//import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractExpression;

public class GraphTable implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;

	public GraphTable(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
	}

	public Data[] execute() {
		Graph graph = (Graph) this.data[0].getData();
		Table nodeTable = graph.getNodeTable();
		if(graph.getNodeKeyField() == null) {
			nodeTable.addColumn("id", new AbstractExpression() {

				public Class getType(Schema arg0) {
					return int.class;
				}
				
				public int getInt(Tuple t) {
					return t.getRow();
				}
				
				public Object get(Tuple t) {
					return new Integer(getInt(t));
				}
			});
		}
		
		Data nodeData = new BasicData(nodeTable, Table.class.getName());
		
		Dictionary nodeMetadata = nodeData.getMetadata();
		nodeMetadata.put(DataProperty.LABEL, "Node Table from Graph");
		nodeMetadata.put(DataProperty.PARENT, this.data[0]);
		nodeMetadata.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		
		Data edgeData = new BasicData(graph.getEdgeTable(), Table.class.getName());
		
		Dictionary edgeMetadata = edgeData.getMetadata();
		edgeMetadata.put(DataProperty.LABEL, "Edge Table from Graph");
		edgeMetadata.put(DataProperty.PARENT, this.data[0]);
		edgeMetadata.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		
		return new Data[]{nodeData, edgeData};
	}
}