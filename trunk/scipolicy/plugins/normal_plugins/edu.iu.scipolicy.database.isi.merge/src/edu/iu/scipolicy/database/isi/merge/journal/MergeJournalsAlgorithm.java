package edu.iu.scipolicy.database.isi.merge.journal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.cns.database.merge.generic.maker.MergeCheck;
import edu.iu.cns.database.merge.generic.maker.MergeMaker;
import edu.iu.cns.database.merge.generic.maker.PreferrableFormComparator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.merge.AlwaysMerge;

/* Each source in the given ISI database may specify a "J9", a canonical journal identifier
 * like "nature" or "science". References also specify a journal identification string, which
 * is often identical to the J9, but not always, and generally isn't much like any other
 * identification string ISI uses. Neither use is completely free from typos, though J9 is
 * usually pretty good, and the reference journal identifiers are less so.
 * 
 * 
 * The included file mergeGroups.txt (see below for file format description) records known
 * alternate forms/typos for J9s.
 * 
 * This algorithm merges sources together according to those known groups, preferring to keep
 * that source entity having the best form as determined by the J9Comparator, picking either a
 * known-good form from the manual annotation task, or falling back on a form having
 * the most information about it.
 */
public class MergeJournalsAlgorithm implements Algorithm, ProgressTrackable {
	public static final String MERGE_GROUPS_FILE_NAME = "mergeGroups.txt";
	public static final String SOURCE_TABLE_ID = "APP" + "." + ISI.SOURCE_TABLE_NAME;
	public static final Map<String, String> J9_TO_PRIMARY_J9 = createMapFromJ9ToPrimaryJ9();
	
	private Data originalDatabaseData;
    private CIShellContext ciShellContext;
	private LogService logger;
	private ProgressMonitor monitor;	
        
    public MergeJournalsAlgorithm(Data[] data, CIShellContext ciShellContext) {
    	this.originalDatabaseData = data[0];
        this.ciShellContext = ciShellContext;
        this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
		if (J9_TO_PRIMARY_J9.isEmpty()) {
			String message = "Failed to load journal merging data.  If this problem persists " +
				"after restarting the tool, please contact the NWB development team at " +
				"nwb-helpdesk@googlegroups.com";
			throw new AlgorithmExecutionException(message);
		} else {
			logKnownJ9Statistics();
		}

    	KeyMaker primaryJ9KeyMaker = new JournalKeyMaker(J9_TO_PRIMARY_J9);
    	PreferrableFormComparator journalComparator = new JournalComparator(J9_TO_PRIMARY_J9);	    	
    	
    	return MergeMaker.mergeTable(
    			SOURCE_TABLE_ID, originalDatabaseData, primaryJ9KeyMaker, 
    			true, journalComparator, ciShellContext, monitor, "with journals merged");	    	
    }

    private void logKnownJ9Statistics() {
    	int numberOfCanonicalForms = new HashSet<String>(J9_TO_PRIMARY_J9.values()).size();	
    	int numberOfKnownVariants = J9_TO_PRIMARY_J9.size();			
		
		logger.log(
				LogService.LOG_INFO,
				"This algorithm can merge " + numberOfKnownVariants + " journal name variants " +
					"into " + numberOfCanonicalForms + " canonical forms.");
	}


	/* Expected merge file format:
     * 	- Plain text with one J9 per line.
     * 	- Merge groups are separated by an empty line.
     * 	- The first J9 of each merge group is the primary form.
     * 	- By convention, groups with only one member are not expected to be given.
     * 
     * We read these at class-load time to prevent needless re-reading per algorithm execution.
     * Since this is run during a static initialization, note that we cannot use the logger (or
     * really throw exceptions).  Keep the System.err.println's.  In any case, the conditions
     * that these represent should never occur without significant developer error (a misnamed
     * file, perhaps?).
     */
	private static Map<String, String> createMapFromJ9ToPrimaryJ9() {
		try {
			File mergeGroupsFile =
				FileUtilities.safeLoadFileFromClasspath(
						MergeJournalsAlgorithm.class, MERGE_GROUPS_FILE_NAME);
			BufferedReader mergeGroupsFileReader =
				new BufferedReader(new FileReader(mergeGroupsFile));
			
			Map<String, String> j9ToPrimary = new HashMap<String, String>();
			String line;
			String currentPrimary = null;
			while ((line = mergeGroupsFileReader.readLine()) != null) {
				line = line.toLowerCase();
				
				// Empty line?  New merge group starting.
				if (line.trim().length() == 0) {
					currentPrimary = null;
					continue;
				}
				
				// First line of merge group?  Then the current line is the primary J9.
				if (currentPrimary == null) {
					currentPrimary = line;
					j9ToPrimary.put(currentPrimary, currentPrimary);
				} else {
					// Otherwise this is an alternate form.
					j9ToPrimary.put(line, currentPrimary);
				}
			}
			
			return j9ToPrimary;
	    } catch (FileNotFoundException e) {
			System.err.println("Could not find merge file for sources: " + e.getMessage());
			return new HashMap<String, String>();
		} catch (IOException e) {
			System.err.println("Could not access merge file for sources: " + e.getMessage());
			return new HashMap<String, String>();
		}
	}
	
	public ProgressMonitor getProgressMonitor() {
		return monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}
}