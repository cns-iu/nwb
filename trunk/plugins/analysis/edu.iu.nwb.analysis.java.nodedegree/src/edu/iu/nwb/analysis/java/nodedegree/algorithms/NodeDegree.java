package edu.iu.nwb.analysis.java.nodedegree.algorithms;

import java.util.Date;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.nwb.analysis.java.nodedegree.components.NodeDegreeAnnotator;

public class NodeDegree implements Algorithm, ProgressTrackable {
	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;
	private ProgressMonitor monitor;
	private LogService logger;

	public NodeDegree(Data[] data, Dictionary parameters, CIShellContext context)  {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService)context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {

		Graph originalGraph = (Graph)this.data[0].getData();

		Graph annotatedGraph = createAnnotatedGraph(originalGraph);

		NodeDegreeAnnotator nodeAnnotator = new NodeDegreeAnnotator(this.getProgressMonitor());

		try {
			monitor.start(ProgressMonitor.WORK_TRACKABLE, 2*originalGraph.getNodeCount());
			nodeAnnotator.annotateGraph(originalGraph, annotatedGraph);
		} catch (InterruptedException e) {
			throw new AlgorithmExecutionException("Execution was unexpectedly interrupted.",e);
		}

		Data returnData = constructData(data[0],annotatedGraph,prefuse.data.Graph.class.toString(),
				DataProperty.NETWORK_TYPE,"Network with degree attribute added to node list");

		monitor.done();
		return new Data[]{returnData};
	}

	private Graph createAnnotatedGraph(final Graph originalGraph) throws AlgorithmExecutionException{
		final Schema originalGraphNodeSchema = originalGraph.getNodeTable().getSchema();
		final Schema originalGraphEdgeSchema = originalGraph.getEdgeTable().getSchema();

		Schema annotatedGraphNodeSchema = copySchema(originalGraphNodeSchema);
		if(annotatedGraphNodeSchema == null)
			throw new AlgorithmExecutionException("Attribute: degree, already exists. Please rename the attribute before rerunning this algorithm.");
		annotatedGraphNodeSchema = appendDegreeAnnotation(annotatedGraphNodeSchema);
		Schema annotatedGraphEdgeSchema = copySchema(originalGraphEdgeSchema);

		Table newNodeTable = annotatedGraphNodeSchema.instantiate(originalGraph.getNodeTable().getRowCount());
		Table newEdgeTable = annotatedGraphEdgeSchema.instantiate(originalGraph.getEdgeTable().getRowCount());

		Graph annotatedGraph = new Graph(newNodeTable,newEdgeTable,originalGraph.isDirected());

		return annotatedGraph;

	}

	private static Schema copySchema(final Schema original){
		boolean degreeColumnExists = false;
		Schema copy = new Schema();
		String columnName;

		for(int i = 0; i < original.getColumnCount(); i++){
			copy.addColumn(original.getColumnName(i), original.getColumnType(i));
		}

		return copy;
	}

	private Schema appendDegreeAnnotation(Schema targetSchema){
		int index = targetSchema.getColumnIndex("degree");

		if(index < 0){
			targetSchema.addColumn("degree", int.class);
		}
		else{
			return null;
		}

		return targetSchema;
	}

	private static Data constructData(Data parent, Object obj, String className, String type, String label){
		Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.PARENT, parent);
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);

		return outputData;
	}

	public ProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return this.monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		// TODO Auto-generated method stub
		this.monitor = monitor;
	}


}