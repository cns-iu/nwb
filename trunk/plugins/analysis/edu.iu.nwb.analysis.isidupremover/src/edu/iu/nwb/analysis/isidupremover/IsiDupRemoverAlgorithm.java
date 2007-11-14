package edu.iu.nwb.analysis.isidupremover;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

public class IsiDupRemoverAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private LogService log;
    private IsiDupRemover dupRemover;
    
    public IsiDupRemoverAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.log = (LogService) context.getService(
				LogService.class.getName());
        
        this.dupRemover = new IsiDupRemover();
    }

    
    public Data[] execute() {
    	
    	Table tableWithDups = (Table) data[0].getData();
    	
    	Table tableWithoutDups = dupRemover.removeDuplicatePublications(tableWithDups, log, true);
    	
    	BasicData tableWithoutDupsData = new BasicData(tableWithoutDups,
    			tableWithoutDups.getClass().getName());
    	
    	return new Data[] {tableWithoutDupsData};
    }
}