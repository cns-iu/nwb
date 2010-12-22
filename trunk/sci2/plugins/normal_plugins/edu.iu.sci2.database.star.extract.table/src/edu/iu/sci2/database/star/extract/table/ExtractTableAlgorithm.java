package edu.iu.sci2.database.star.extract.table;

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
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.extract.table.query.TableQueryConstructor;

public class ExtractTableAlgorithm implements Algorithm, ProgressTrackable {
    private CIShellContext ciShellContext;
    private Data parentData;
    private TableQueryConstructor queryConstructor;
    private AlgorithmFactory tableQueryRunner;
    private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;
    
    public ExtractTableAlgorithm(
    		CIShellContext ciShellContext,
    		Data parentData,
    		TableQueryConstructor queryConstructor,
    		AlgorithmFactory networkQueryRunner,
    		LogService logger) {
        this.ciShellContext = ciShellContext;
        this.parentData = parentData;
        this.queryConstructor = queryConstructor;
        this.tableQueryRunner = networkQueryRunner;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	String query = this.queryConstructor.constructQuery();

    	try {
    		return runTableQuery(query);
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

    private Data[] runTableQuery(String query) throws Exception {
    	Dictionary<String, Object> parameters = new Hashtable<String, Object>();
    	parameters.put("query", query);

    	return AlgorithmUtilities.executeAlgorithm(
    		this.tableQueryRunner,
    		this.progressMonitor,
    		new Data[] { this.parentData },
    		parameters,
    		this.ciShellContext);
    }
}