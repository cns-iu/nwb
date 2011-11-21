package edu.iu.sci2.database.isi.merge.people.identical;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.utilities.DataFactory;

import edu.iu.cns.database.merge.generic.prepare.marked.MergeMarker;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.KeyBasedGroupingStrategy;
import edu.iu.sci2.database.isi.merge.people.IsiPersonPriorities;

public class MergeIdenticalPeople implements Algorithm, ProgressTrackable {
    private Data[] data;
    private CIShellContext context;
	private ProgressMonitor monitor = ProgressMonitor.NULL_MONITOR;
    
    public MergeIdenticalPeople(Data[] data, CIShellContext context) {
        this.data = data;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	MergeMarker mergeMarker =
    			new MergeMarker(
    					new KeyBasedGroupingStrategy<String>(new IsiSimpleNameNormalized()),
    					new IsiPersonPriorities());
    	
    	Database merged = mergeMarker.performMergesOn(
    			"APP.PERSON", (Database) data[0].getData(), monitor, context);
    	
    	return new Data[]{ DataFactory.likeParent(
    			merged, data[0], "with identical people merged") };
    }

	public ProgressMonitor getProgressMonitor() {
		return monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}
}