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

public class IsiDupRemover {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private LogService log;
    private ISIPubComparer mainPubComparer = new MainPubComparer();

    public Table removeDuplicatePublications (Table origTable,
    		boolean shouldMutateOrigTable, LogService log,
    		boolean printRunningLogToConsole) {
    	this.log = log;
    	
    	StringBuilder runningLog = new StringBuilder();
    	
    	Table table;
    	if (shouldMutateOrigTable) {
    		table = origTable;	
    	} else {
    		table = GraphUtil.copyTable(origTable);
    	}
    	
    	Integer savedPubIndex = null;
    	String savedPubID = null;
    	
    	//iterate through the publications by ID
    	IntIterator publicationsByIDIter = table.rowsSortedBy(ISITag.UNIQUE_ID, true);
    	
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
    		String currentPubID = table.getString(currentPubIndex.intValue(), ISITag.UNIQUE_ID);
    		
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
    					table, currentPubIndex, savedPubIndex, runningLog);
    		 
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
    	
    	//if we have any publications to remove...
    	if (publicationsToRemove.size() > 0) {
    		//remove publications we marked as needing to be removed from the table
        	Iterator pubsToRemoveIter = publicationsToRemove.iterator();
        	while (pubsToRemoveIter.hasNext()) {
        		Integer pubToRemoveIndex = (Integer) pubsToRemoveIter.next();
        		table.removeRow(pubToRemoveIndex.intValue());
        	}
        	
        	log.log(LogService.LOG_INFO,  
        			publicationsToRemove.size() + " out of " 
        			+ table.getRowCount() + " publication records were duplicates");
    	} else {
    		//tell the user that we found no duplicates
    		log.log(LogService.LOG_INFO, "No duplicate publication records found");
    	}
    	
    	return table;
    }
    
    private Integer determineWhichToRemove(Table table,
    		Integer currentPubIndex, Integer savedPubIndex,
    		StringBuilder runningLog) {
    	
    	Tuple currentPubTuple = table.getTuple(currentPubIndex.intValue());
    	Tuple savedPubTuple = table.getTuple(savedPubIndex.intValue());
    	
    	String commonID = currentPubTuple.getString(ISITag.UNIQUE_ID);
    	String currentPubTitle = currentPubTuple.getString(ISITag.TITLE);
    	String savedPubTitle = savedPubTuple.getString(ISITag.TITLE);
    	
    	runningLog.append("Found two publication records with the same ID, '" + commonID + "'\r\n");
    	if (currentPubTitle.equals(savedPubTitle)) {
    		String commonTitle = currentPubTitle;
    		runningLog.append("Both publications records are titled '" + commonTitle + "'\r\n");
    	} else {
    		runningLog.append("The first publication record is titled '" + currentPubTitle + "'\r\n");
    		runningLog.append("The second publication record is title '" + savedPubTitle + "'\r\n");
    	}
    	
    	int compareResult = 
    		mainPubComparer.compare(currentPubTuple, savedPubTuple, runningLog);
    	
    	Integer pubToRemoveIndex;
    	if (compareResult > 0) {
    		runningLog.append("Removing the second publication\r\n");
    		pubToRemoveIndex =  savedPubIndex;
    	} else if (compareResult < 0) {
    		runningLog.append("Removing the first publication\r\n");
    		pubToRemoveIndex =  currentPubIndex;
    	} else {
    		runningLog.append("Arbitrarily removing the first publication\r\n");
    		pubToRemoveIndex = currentPubIndex;
    	}
    	
    	runningLog.append("\r\n");
    	runningLog.append("--------------------\r\n");
    	runningLog.append("\r\n");
    	
		return pubToRemoveIndex;
	}
}
