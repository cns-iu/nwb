package edu.iu.sci2.database.isi.merge.document_source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.util.ColumnProjection;
import prefuse.data.util.NamedColumnProjection;
import edu.iu.cns.database.merge.generic.analyze.mergetable.MergeTableAnalyzer;
import edu.iu.cns.database.merge.generic.analyze.mergetable.MergeTableAnalyzer.AnalysisException;
import edu.iu.cns.database.merge.generic.prepare.marked.MergeMarker;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.KeyBasedGroupingStrategy;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.scholarly.model.entity.Source;

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
public class MergeDocumentSourcesAlgorithm implements Algorithm, ProgressTrackable {
	public static final String MERGE_GROUPS_FILE_NAME = "JournalGroups.txt";
	public static final String SOURCE_TABLE_ID = "APP" + "." + ISI.SOURCE_TABLE_NAME;
	public static final Map<String, String> NAME_FORM_LOOKUP = createNameFormLookup();
	
	private Data originalDatabaseData;
    private CIShellContext ciShellContext;
	private LogService logger;
	private ProgressMonitor monitor; // TODO Actually use this..	
        
    public MergeDocumentSourcesAlgorithm(Data[] data, CIShellContext ciShellContext) {
    	this.originalDatabaseData = data[0];
        this.ciShellContext = ciShellContext;
        
        this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
		if (NAME_FORM_LOOKUP.isEmpty()) {
			String message = "Failed to load document source merging data.  "
					+ "If this problem persists after restarting the tool, "
					+ "please contact the NWB development team at nwb-helpdesk@googlegroups.com";
			throw new AlgorithmExecutionException(message);
		}
		logLookupStatistics();

		MergeMarker mergeMarker = new MergeMarker(
				new KeyBasedGroupingStrategy<String>(
						new DocumentSourceKeyFunction(NAME_FORM_LOOKUP)),
				new DocumentSourceComparator());
		
		Database originalDatabase = (Database) this.originalDatabaseData.getData();
		
		Table mergeTable = mergeMarker.createMarkedMergingTable(
				SOURCE_TABLE_ID, originalDatabase, this.ciShellContext);    	
		Database merged = MergeMarker.executeMerge(mergeTable, originalDatabase, 
				this.ciShellContext, null);
		
		Data mergedDatabaseData = DataFactory.likeParent(
				merged, this.originalDatabaseData, "with document sources merged");
		
		Data mergeTableData = DataFactory.withClassNameAsFormat(mergeTable,
				DataProperty.TABLE_TYPE, this.originalDatabaseData, "Merge Table: based on "
						+ SOURCE_TABLE_ID);
		List<Data> returnData = new ArrayList<Data>();
		returnData.add(mergedDatabaseData);
		returnData.add(mergeTableData);
		
		try {
			File mergeReportFile = File.createTempFile("Merge Report", ".txt");
			ColumnProjection columnFilter = new NamedColumnProjection(
					new String[] {
							Source.Field.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION.name(),
							Source.Field.FULL_TITLE.name()}, true);

			MergeTableAnalyzer.writeAnalysis(new FileOutputStream(mergeReportFile), mergeTable,
					columnFilter);
			
			Data mergeReportData = DataFactory.withClassNameAsFormat(
					mergeReportFile, DataProperty.TEXT_TYPE, this.originalDatabaseData,
					"Text Log: A Merge Report for the mergeTable.");
			returnData.add(mergeReportData);
			
		} catch (AnalysisException e) {
			this.logger.log(LogService.LOG_ERROR,
					"Could not analyze the mergeTable:\n\t" + e.getMessage());
		} catch (IOException e) {
			this.logger.log(LogService.LOG_ERROR,
					"Could not create the merge report file for the mergeTable:\n\t"
							+ e.getMessage());
		}
		
		return returnData.toArray(new Data[0]);
    }

    private void logLookupStatistics() {
    	int numberOfCanonicalForms = new HashSet<String>(NAME_FORM_LOOKUP.values()).size();	
    	int numberOfKnownVariants = NAME_FORM_LOOKUP.size();			
		
		this.logger.log(LogService.LOG_INFO, "This algorithm can merge "
				+ numberOfKnownVariants
				+ " document source name variants into "
				+ numberOfCanonicalForms + " canonical forms.");
		this.logger
				.log(LogService.LOG_WARNING,
						"Warning: while we use Web of Science's official list of "
								+ "Journal Title Abbreviations, that list does not cover all spellings of "
								+ "cited sources. Additionally, in some cited references it is not possible to "
								+ "disambiguate between members of a book or conference series and a "
								+ "journal with the same name.");
	}


	/* Expected merge file format:
     * 	- Plain text with one journal name form per line.
     * 	- Merge groups are separated by an empty line.
     * 	- The first member of each merge group is used as the key, but each name only appears once in the file, so this is arbitrary.
     * 	- Groups with one member are present (for people to add to later), and are loaded (since there's no reason not to).
     * 
     * We read these at class-load time to prevent needless re-reading per algorithm execution.
     * Since this is run during a static initialization, note that we cannot use the logger (or
     * really throw exceptions).  Keep the System.err.println's.  In any case, the conditions
     * that these represent should never occur without significant developer error (a misnamed
     * file, perhaps?).
     */
	private static Map<String, String> createNameFormLookup() {
		try {
			URL mergeGroupsUrl = MergeDocumentSourcesAlgorithm.class.getClass()
					.getResource(MERGE_GROUPS_FILE_NAME);
			InputStream in = mergeGroupsUrl.openStream();
			BufferedReader mergeGroupsUrlReader = new BufferedReader(
							new InputStreamReader(in, "UTF8"));
			
			Map<String, String> nameFormLookup = new HashMap<String, String>();
			String line;
			String currentKey = null;
			while ((line = mergeGroupsUrlReader.readLine()) != null) {
				line = line.toLowerCase().trim();
				
				// Empty line?  New merge group starting.
				if (line.length() == 0) {
					currentKey = null;
					continue;
				}
				
				// First line of merge group?  Then the current line is the primary J9.
				if (currentKey == null) {
					currentKey = line;
					nameFormLookup.put(currentKey, currentKey);
				} else {
					// Otherwise this is an alternate form.
					nameFormLookup.put(line, currentKey);
				}
			}
			
			return nameFormLookup;
	    } catch (FileNotFoundException e) {
			System.err.println("Could not find merge file for sources: " + e.getMessage());
			//throw new RuntimeException(e.getMessage());
			return new HashMap<String, String>();
		} catch (IOException e) {
			System.err.println("Could not access merge file for sources: " + e.getMessage());
			//throw new RuntimeException(e.getMessage());
			return new HashMap<String, String>();
		}
	}
	
	public ProgressMonitor getProgressMonitor() {
		return this.monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}
}