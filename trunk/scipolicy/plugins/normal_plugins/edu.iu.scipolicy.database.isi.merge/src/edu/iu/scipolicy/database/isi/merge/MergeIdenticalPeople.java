package edu.iu.scipolicy.database.isi.merge;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.cns.database.merge.generic.maker.MergeCheck;
import edu.iu.cns.database.merge.generic.maker.MergeMaker;
import edu.iu.cns.database.merge.generic.maker.PreferrableFormComparator;

public class MergeIdenticalPeople implements Algorithm {
    private Data[] data;
    private CIShellContext context;
    
    public MergeIdenticalPeople(Data[] data, CIShellContext context) {
        this.data = data;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	String personTable = "APP.PERSON";

    	KeyMaker keyMaker = new IsiSimpleNameNormalized();
    	MergeCheck mergeCheck = new AlwaysMerge();
    	PreferrableFormComparator preferrableFormComparator = new IsiPersonPriorities();
    	
    	
    	return MergeMaker.mergeTable(personTable, data[0], keyMaker, mergeCheck,
				preferrableFormComparator, context, "with identical people merged");
    }
}