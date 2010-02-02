package edu.iu.cns.database.extract.generic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.utilities.DatabaseUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;

public class ExtractTable implements Algorithm {
	private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    private LogService logger;
    
	private String query;
	private String label = "Extracted table";
    
    public ExtractTable(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.logger = (LogService) context.getService(LogService.class.getName());
        
        this.query = (String) this.parameters.get(ExtractTableFactory.QUERY_KEY);
        if(parameters.get(ExtractTableFactory.LABEL_KEY) != null) {
        	this.label = (String) parameters.get(ExtractTableFactory.LABEL_KEY);
        }
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	Database database = (Database) data[0].getData();
		Connection connection = DatabaseUtilities.connect(database, "Unable to communicate with the database.");
		try {
    		DatabaseDataSource tableSource = ConnectionFactory.getDatabaseConnection(connection);
    		Table extractedTable = tableSource.getData(query);
    		this.logger.log(LogService.LOG_INFO, "Executed table query: \r\n" + query);
    		
    		Data outputData = wrapWithMetadata(extractedTable);
    		return new Data[] { outputData };
		} catch (SQLException e) {
			throw new AlgorithmExecutionException("Unable to communicate with the selected database.", e);
		} catch (DataIOException e) {
			throw new AlgorithmExecutionException("There was a problem executing the query: " + e.getMessage(), e);
		} finally {
			DatabaseUtilities.closeConnectionQuietly(connection);
		}
		
    }

	

	private Data wrapWithMetadata(Table extractedTable) {
		Data outputData = new BasicData(extractedTable, Table.class.getName());
		Dictionary metadata = outputData.getMetadata();
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.PARENT, data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		return outputData;
	}
}