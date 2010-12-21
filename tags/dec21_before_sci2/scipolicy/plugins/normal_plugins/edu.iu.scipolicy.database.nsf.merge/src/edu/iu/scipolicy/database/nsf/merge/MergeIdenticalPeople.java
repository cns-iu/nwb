package edu.iu.scipolicy.database.nsf.merge;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;

import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.cns.database.merge.generic.maker.MergeCheck;
import edu.iu.cns.database.merge.generic.maker.MergeMaker;
import edu.iu.cns.database.merge.generic.maker.PreferrableFormComparator;

public class MergeIdenticalPeople implements Algorithm, ProgressTrackable {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
	private ProgressMonitor monitor;
    
    public MergeIdenticalPeople(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	String personTable = "APP.PERSON";

    	KeyMaker keyMaker = new NsfSimpleNameNormalized();
    	PreferrableFormComparator preferrableFormComparator = new NsfPersonPriorities();
    	
    	
    	return MergeMaker.mergeTable(personTable, data[0], keyMaker,
				true, preferrableFormComparator, context, monitor, "with identical people merged");
    }
    
    public ProgressMonitor getProgressMonitor() {
		return monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}
}