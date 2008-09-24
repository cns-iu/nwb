package edu.iu.nwb.analysis.isidupremover;

import java.util.Dictionary;
import java.util.Enumeration;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

public class ISIDupRemoverAlgorithm implements Algorithm {
	 Data[] data;
	    Dictionary parameters;
	    CIShellContext context;
	    
	    //TODO: Make log stuff saved to a directory specified in preferences
	    
	    private LogService log;
	    private ISIDupRemover dupRemover;
	    
	    public ISIDupRemoverAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
	        this.data = data;
	        this.parameters = parameters;
	        this.context = context;
	        
	        this.log = (LogService) context.getService(
					LogService.class.getName());
	        
	        this.dupRemover = new ISIDupRemover();
	    }

	    
	    public Data[] execute()throws AlgorithmExecutionException {
	    	
	    	Table tableWithDups = (Table) data[0].getData();
	    	TablePair dupAndNoDupTables = dupRemover.removeDuplicatePublications(tableWithDups, log, false);
	    	
	    	//unpack multiple value return
	    	Table tableWithoutDups = dupAndNoDupTables.getNoDupTable();
	    	Table duplicates = dupAndNoDupTables.getDupTable();
	    	
	    	BasicData tableWithoutDupsData = new BasicData(tableWithoutDups,
	    			tableWithoutDups.getClass().getName());
	    	copyOverMetaData(data[0], tableWithoutDupsData);
	    	
	    	Dictionary dupRemovedMetaData = tableWithoutDupsData.getMetadata();
	    	dupRemovedMetaData.put(DataProperty.PARENT, data[0]);
	    	dupRemovedMetaData.put(DataProperty.LABEL, tableWithoutDups.getRowCount() + " Unique ISI Records");
	    	
	    	return new Data[] {tableWithoutDupsData};
	    }
	    
	    private void copyOverMetaData(Data sourceData, Data targetData) {
	    	Dictionary sourceMetaData = sourceData.getMetadata();
	    	Dictionary targetMetaData = targetData.getMetadata();
	    	
	    	Enumeration keyEnum = sourceMetaData.keys();
	    	while (keyEnum.hasMoreElements()) {
	    		Object key = keyEnum.nextElement();
	    		
	    		targetMetaData.put(key, sourceMetaData.get(key));
	    	}
	    }
}