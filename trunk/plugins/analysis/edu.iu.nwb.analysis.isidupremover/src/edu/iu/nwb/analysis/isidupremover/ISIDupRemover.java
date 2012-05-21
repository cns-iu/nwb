package edu.iu.nwb.analysis.isidupremover;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.analysis.isidupremover.tuplecomparison.ISIPubComparer;
import edu.iu.nwb.analysis.isidupremover.tuplecomparison.MainPubComparer;
import edu.iu.nwb.shared.isiutil.ISITag;

public class ISIDupRemover {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    
    //TODO: Preferences should tell us where to put temp log file and whatnot
    private static final String LOG_FILE_NAME = "isiduplicateremoverlog";
    private LogService log;
    private ISIPubComparer mainPubComparer = new MainPubComparer();

    public TablePair removeDuplicatePublications (Table origTable,
    		LogService log, boolean printRunningLogToConsole) throws AlgorithmExecutionException {
    	
    	if (! tableSanityCheckPasses(origTable)) {
    		this.log.log(LogService.LOG_WARNING, "Unable to remove duplicates from table. Returning original table.");
    		return new TablePair(origTable, origTable);
    	}
    	
    	this.log = log;
    	
    	StringBuffer runningLog = new StringBuffer();

    	Integer savedPubIndex = null;
    	String savedPubID = null;
    	
    	int recordsWithoutUIDs = 0;
    	
    	//iterate through the publications by ID
    	IntIterator publicationsByIDIter = origTable.rowsSortedBy(ISITag.UNIQUE_ID.columnName, true);
    	
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
    		
    		String currentPubID = origTable.getString(currentPubIndex.intValue(), ISITag.UNIQUE_ID.columnName);
    		
    		//if this publication has no ID...
    		if (currentPubID == null) {
    			//skip it
    			recordsWithoutUIDs++;
    			continue;
    		}
    		
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
    	
    	//write log to file
    	
    	File logFile = null;	
    	try {
    	logFile = File.createTempFile(LOG_FILE_NAME, ".txt");
    	BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
    	writer.write(runningLog.toString());
    	writer.close();
    	} catch (IOException e) {
    		throw new AlgorithmExecutionException("Unable to write removed duplicates log.", e);
    	}
    	
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

		log.log(LogService.LOG_INFO,
				"The original "
						+ origTable.getRowCount()
						+ " records have been processed to remove duplicate unique ISI IDs leaving "
						+ noDupTable.getRowCount() + " records.");

    	if (recordsWithoutUIDs > 0) {
    	log.log(LogService.LOG_WARNING, "" + recordsWithoutUIDs + 
    			" records did not have unique IDs (specified with the UT tag in ISI format)," +
    			" so we were unable to determine whether there were duplicates of these records. " +
    			"The absence of a unique ID is most likely a flaw in the original data.");
    	}
    	if (logFile != null) {
    		log.log(LogService.LOG_INFO, "");
    	log.log(LogService.LOG_INFO, "Wrote log to " + logFile.getAbsolutePath());
    	}

    	
    	return new TablePair(noDupTable, dupTable);
    }
    
    private Integer determineWhichToRemove(Table table,
    		Integer currentPubIndex, Integer savedPubIndex,
    		StringBuffer runningLog) {
    	
    	Tuple currentPubTuple = table.getTuple(currentPubIndex.intValue());
    	Tuple savedPubTuple = table.getTuple(savedPubIndex.intValue());
    	
    	String commonID = currentPubTuple.getString(ISITag.UNIQUE_ID.columnName);
    	String currentPubTitle = currentPubTuple.getString(ISITag.TITLE.columnName);
    	String savedPubTitle = savedPubTuple.getString(ISITag.TITLE.columnName);
    	
    	runningLog.append("Found a pair of publication records with ID '" + commonID + "'\r\n");
    	
    	//check to see that both have a title before attempting to announce their titles.
    	//if one does not have a title, preemptively choose to remove it.
    	
    	if (currentPubTitle == null && savedPubTitle == null) {
    		runningLog.append("Neither have a title specified (Very unusual).");
    	} else if (currentPubTitle == null) {
    		runningLog.append("The first does not have a title.");
    		runningLog.append("Removing first.");
    		return currentPubIndex;
    	} else if (savedPubTitle == null) {
    		runningLog.append("The second does not have a title.");
    		runningLog.append("Removing second.");
    		return savedPubIndex;
    	}
    	
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
    
    private boolean tableSanityCheckPasses(Table isiTable) {

    	boolean hasAUniqueIDColumn = isiTable.canGetString(ISITag.UNIQUE_ID.columnName);

    	if (! hasAUniqueIDColumn) {
    		this.log.log(LogService.LOG_WARNING, "ISI Table does not have a unique ID column (abbreviated UT)." +
    				"It is possible that no records (a.k.a papers) in the original ISI file specified a unique ID." +
    				"Therefore, we are unable to determine which papers are duplicates.");
    		return false;
    	}
    	
    	
    	return true;
    }
}
