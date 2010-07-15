package edu.iu.sci2.database.star.extract.network;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.swt.model.GUIModel;

import edu.iu.sci2.database.star.extract.network.query.QueryConstructor;

public class ExtractNetworkAlgorithm implements Algorithm, ProgressTrackable {
	private CIShellContext ciShellContext;
	private Data parentData;
	private GUIModel model;
	private QueryConstructor queryConstructor;
	private AlgorithmFactory networkQueryRunner;
	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

    public ExtractNetworkAlgorithm(
    		CIShellContext ciShellContext,
    		Data parentData,
    		GUIModel model,
    		QueryConstructor queryConstructor,
    		AlgorithmFactory networkQueryRunner) {
    	this.ciShellContext = ciShellContext;
    	this.parentData = parentData;
    	this.model = model;
    	this.queryConstructor = queryConstructor;
    	this.networkQueryRunner = networkQueryRunner;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	String nodeQuery = this.queryConstructor.constructNodeQuery(this.model);
    	String edgeQuery = this.queryConstructor.constructEdgeQuery(this.model);
    	String nodeIDColumn = this.queryConstructor.getNodeIDColumn(this.model);
    	String sourceNodeName = this.queryConstructor.getSourceNodeName(this.model);
    	String targetNodeName = this.queryConstructor.getTargetNodeName(this.model);
    	boolean isDirected = this.queryConstructor.isDirected(this.model);

    	try {
    		return runNetworkQuery(
    			nodeQuery, edgeQuery, nodeIDColumn, sourceNodeName, targetNodeName, isDirected);
    	} catch (Exception e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

    public ProgressMonitor getProgressMonitor() {
    	return this.progressMonitor;
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
    	this.progressMonitor = progressMonitor;
    }

    private Data[] runNetworkQuery(
    		String nodeQuery,
    		String edgeQuery,
    		String idColumn,
    		String sourceColumn,
    		String targetColumn,
    		boolean directed) throws Exception {
    	Dictionary<String, Object> parameters = new Hashtable<String, Object>();
    	parameters.put("node_query", nodeQuery);
    	parameters.put("edge_query", edgeQuery);
    	parameters.put("id_column", idColumn);
    	parameters.put("source_column", sourceColumn);
    	parameters.put("target_column", targetColumn);
    	parameters.put("directed", directed);

    	return AlgorithmUtilities.executeAlgorithm(
    		this.networkQueryRunner,
    		this.progressMonitor,
    		new Data[] { this.parentData },
    		parameters,
    		this.ciShellContext);
    }
}