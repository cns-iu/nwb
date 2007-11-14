package edu.iu.nwb.analysis.isidupremover;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.analysis.isidupremover.tuplecomparison.ISIPubComparer;
import edu.iu.nwb.analysis.isidupremover.tuplecomparison.MainPubComparer;
import edu.iu.nwb.shared.isiutil.ISITag;

public class IsiDupRemover {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private static final String LOG_FILE_NAME = "isiduplicateremoverlog.txt";
    private LogService log;
    private ISIPubComparer mainPubComparer = new MainPubComparer();

    public Table removeDuplicatePublications (Table origTable,
    		LogService log, boolean printRunningLogToConsole) {
    	this.log = log;
    	
    	StringBuilder runningLog = new StringBuilder();
    
    	
    	Integer savedPubIndex = null;
    	String savedPubID = null;
    	
    	//iterate through the publications by ID
    	IntIterator publicationsByIDIter = origTable.rowsSortedBy(ISITag.UNIQUE_ID.name, true);
    	
    	/*
    	 * Since we are iterating through the publications by ID, publications
    	 * with the same ID will all be next to each other. As we go through
    	 * each section where IDs are identical, we keep track of which 
    	 * publication we think should remain. All other publications 
    	 * with that same ID will be eliminated.
    	 */
    	
    	List publicationsToRemove = new ArrayList();
    	
    	//for every publication in order of ID...
    	while (publicationsByIDIter.hasNext()) {
    		Integer currentPubIndex = (Integer) publicationsByIDIter.next();
    		String currentPubID = origTable.getString(currentPubIndex.intValue(), ISITag.UNIQUE_ID.name);
    		
    		//if this publication has a different ID than the last saved publication...
    		if (! currentPubID.equals(savedPubID)) {
    			//this publication is the first in a set of publications 
    			//with the same id. Save this publication.
    			savedPubIndex = currentPubIndex;	
    			savedPubID = currentPubID;
    		} else { 
    			//we have a pair of publications with the same ID.
    			//choose whether to eliminate our saved publication or this one.
    			Integer pubToRemoveIndex = determineWhichToRemove(
    					origTable, currentPubIndex, savedPubIndex, runningLog);
    		 
    			//if the current publication is chosen to be removed...
    			if (pubToRemoveIndex.equals(currentPubIndex)) {
    				//mark the current publication for removal
        			publicationsToRemove.add(currentPubIndex);
        			//and still hold on to the saved publication from before
    			} else { // the saved publication was chosen to be removed
    				//mark the saved publication for removal
    				publicationsToRemove.add(savedPubIndex);
    				//and make the new publication the saved publication
    				savedPubIndex = currentPubIndex;
    				savedPubID = currentPubID;
    			}
    		}
    	}
    	
    	if (printRunningLogToConsole) {
    		log.log(LogService.LOG_INFO, runningLog.toString());
    	}
    	
    	
    	
        log.log(LogService.LOG_INFO, publicationsToRemove.size() + " out of "
				+ origTable.getRowCount()
				+ " publication records were duplicates");

        
    	//create separate tables, one for unique publications 
    	Table noDupTable = new Table();
    	noDupTable.addColumns(origTable.getSchema());
    	//and the other for duplicates.
    	Table dupTable = new Table();
    	dupTable.addColumns(origTable.getSchema());
    	
    	//fill each table accordingly
    	
		Iterator origTableIndices = origTable.iterator();
		while (origTableIndices.hasNext()) {
			Integer pubIndex = (Integer) origTableIndices.next();

			if (! publicationsToRemove.contains(pubIndex)) {
				noDupTable.addTuple(origTable.getTuple(pubIndex.intValue()));
			} else {
				dupTable.addTuple(origTable.getTuple(pubIndex.intValue()));
			}
		}

    	return noDupTable;
    }
    
    private Integer determineWhichToRemove(Table table,
    		Integer currentPubIndex, Integer savedPubIndex,
    		StringBuilder runningLog) {
    	
    	Tuple currentPubTuple = table.getTuple(currentPubIndex.intValue());
    	Tuple savedPubTuple = table.getTuple(savedPubIndex.intValue());
    	
    	String commonID = currentPubTuple.getString(ISITag.UNIQUE_ID.name);
    	String currentPubTitle = currentPubTuple.getString(ISITag.TITLE.name);
    	String savedPubTitle = savedPubTuple.getString(ISITag.TITLE.name);
    	
    	runningLog.append("Found a pair of publication records with ID '" + commonID + "'\r\n");
    	if (currentPubTitle.equals(savedPubTitle)) {
    		String commonTitle = currentPubTitle;
    		runningLog.append("Both titled '" + commonTitle + "'\r\n");
    	} else {
    		runningLog.append("The first titled '" + currentPubTitle + "'\r\n");
    		runningLog.append("The second titled '" + savedPubTitle + "'\r\n");
    	}
    	
    	int compareResult = 
    		mainPubComparer.compare(currentPubTuple, savedPubTuple, runningLog);
    	
    	Integer pubToRemoveIndex;
    	if (compareResult > 0) {
    		runningLog.append("Removing second\r\n");
    		pubToRemoveIndex =  savedPubIndex;
    	} else if (compareResult < 0) {
    		runningLog.append("Removing first\r\n");
    		pubToRemoveIndex =  currentPubIndex;
    	} else {
    		runningLog.append("Arbitrarily removing first\r\n");
    		pubToRemoveIndex = currentPubIndex;
    	}
    	
    	runningLog.append("\r\n");
    	runningLog.append("--------------------\r\n");
    	runningLog.append("\r\n");
    	
		return pubToRemoveIndex;
	}
}
