package edu.iu.sci2.preprocessing.scimaps;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Iterator;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import edu.iu.sci2.preprocessing.scimaps.journal.ScimapsJournalMatcher;

import prefuse.data.Table;
import prefuse.data.Tuple;

public class JournalNamingConvention implements Algorithm {
	private static final String STANDARDIZED_JOURNAL_NAME_COLUMN = "Reconciled Journal Name";
    private Data[] data;
    private String journalColumnName;
	private LogService logger;
    
    public JournalNamingConvention(Data[] data,
    				  Dictionary<String, Object> parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
        this.journalColumnName = parameters.get(JournalNamingConventionFactory.JOURNAL_FIELD_ID).toString();
    }
    
    

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	Table table = (Table) (data[0].getData());
    	Table outputTable;
		try {
			outputTable = processJournalName(table);
		} catch (IOException e) {
			this.logger.log(LogService.LOG_DEBUG, "Failed to instialize journal parser.", e);
			throw new AlgorithmExecutionException("Failed to instialize journal parser.");
		}
    	
        return prepareOutputData(outputTable);
    }
    
    private Table processJournalName(Table table) throws IOException {
    	
    	ScimapsJournalMatcher scimapsJournalMatcher = new ScimapsJournalMatcher();
    	
    	// Create new output table
    	Table outputTable = TableUtilities.copyTable(table);
    	outputTable.addColumn(STANDARDIZED_JOURNAL_NAME_COLUMN, String.class);
    	int standardizedJournalNameColumnIndex = outputTable.getColumnNumber(STANDARDIZED_JOURNAL_NAME_COLUMN);
    	
    	// Retrieve iterator 
		Iterator<?> rows = outputTable.tuples();

		// Process journal names
		int rowIndex = 0;
		while (rows.hasNext()) {
			Tuple row = (Tuple) rows.next();
			if (row.canGetString(journalColumnName)) {
				String name = row.getString(journalColumnName);
				String suggestedName = scimapsJournalMatcher.get(name);
				outputTable.setString(rowIndex, standardizedJournalNameColumnIndex, suggestedName);
			}
			rowIndex++;
		}
		return outputTable;
    }
    
    /**
	 * Create the appropriate {@link Data} container for the {@link Table}.
	 */
	private Data[] prepareOutputData(Table table) {

		Data[] outputData = {DataFactory.forObject(
				table, 
				Table.class.getName(), 
				DataProperty.TABLE_TYPE, 
				data[0], 
				"Standardized Journal Names")};

		return outputData;
	}
}