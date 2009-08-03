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

/* TODO: This thing isn't a converter in the CIShell sense.  It shouldn't have
 * converter in its project or package names, nor should it be in the converters
 * folder in the repository.
 */
public class GraphTable implements Algorithm {
	private Data[] data;

	public GraphTable(
			Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
	}

	public Data[] execute() {
		Graph graph = (Graph) data[0].getData();
		Table nodeTable = graph.getNodeTable();
		if (graph.getNodeKeyField() == null) {
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
		nodeMetadata.put(DataProperty.PARENT, data[0]);
		nodeMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		Data edgeData =
			new BasicData(graph.getEdgeTable(), Table.class.getName());
		
		Dictionary edgeMetadata = edgeData.getMetadata();
		edgeMetadata.put(DataProperty.LABEL, "Edge Table from Graph");
		edgeMetadata.put(DataProperty.PARENT, data[0]);
		edgeMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[]{ nodeData, edgeData };
	}
}