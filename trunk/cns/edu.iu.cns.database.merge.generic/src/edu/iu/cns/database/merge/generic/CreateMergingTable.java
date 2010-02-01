package edu.iu.cns.database.merge.generic;

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
import org.cishell.utilities.database.DatabaseTable;
import org.cishell.utilities.database.InvalidRepresentationException;

import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;
import prefuse.data.util.TableIterator;

public class CreateMergingTable implements Algorithm {
	public static final String PRIMARY_ENTITY_COLUMN = "Primary Entity"; // TODO Change to "Is Primary Entity" or "Is Not Primary Entity", according to which is correct.
	public static final String MERGE_GROUP_IDENTIFIER_COLUMN = "Merge Group Identifier"; // TODO "Merge Group ID" should suffice.
	public static final String FROM_TABLE = "From table: ";
	private Data[] data;
    private Dictionary parameters;
    
    public CreateMergingTable(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	/*
    	 * First, identify all the tables in the database, and their schemas. (Check Micah's code for potential copy-paste)
    	 * In the parameters dialogue, give a drop down of the choices (only ones with primary keys)
    	 * if the table has no primary key, refuse to proceed
    	 * make a (prefuse) table of the contents of the table
    	 * add the two extra columns, with the appropriate values
    	 * add one more extra column, identifying exactly which database table the prefuse table is from
    	 */
    	Database database = (Database) data[0].getData();
    	Connection connection = DatabaseUtilities.connect(database, "Unable to communicate with the database.");
    	
		try {
	    	DatabaseTable table = DatabaseTable.fromRepresentation(
	    			(String) parameters.get(CreateMergingTableFactory.TABLE_PARAMETER)); 
	    	if(!table.hasPrimaryKey(connection)) {
	    		throw new AlgorithmExecutionException("The provided table is not a valid option because it lacks a primary key");
	    	}
	    	DatabaseDataSource tableSource = ConnectionFactory.getDatabaseConnection(connection);
	    	
    		try {
				Table extractedTable = extractMergingTable(table, tableSource);
				Data outputData = wrapWithMetadata(extractedTable, "Merging table for " + table);
	    		return new Data[] { outputData };
				
			} catch (DataIOException e) {
				throw new AlgorithmExecutionException("Unable to retrieve the entities from the table " + table, e);
			}
		} catch (SQLException e) {
			throw new AlgorithmExecutionException("There was a problem connecting to the database.", e);
		} catch (InvalidRepresentationException e) {
			throw new AlgorithmExecutionException("The provided table name is improperly formatted", e);
		} finally {
			DatabaseUtilities.closeConnectionQuietly(connection);
		}
    }

	private Table extractMergingTable(DatabaseTable table,
			DatabaseDataSource tableSource) throws DataIOException {
		Table extractedTable = tableSource.getData("SELECT * FROM " + table);
		extractedTable.addColumn(MERGE_GROUP_IDENTIFIER_COLUMN, String.class);
		extractedTable.addColumn(PRIMARY_ENTITY_COLUMN, String.class);
		TableIterator rows = extractedTable.iterator();
		while(rows.hasNext()) {
			int row = rows.nextInt();
			rows.setString(MERGE_GROUP_IDENTIFIER_COLUMN, "" + row);
			rows.setString(PRIMARY_ENTITY_COLUMN, "*");
		}
		extractedTable.addColumn(FROM_TABLE + table, String.class);
		return extractedTable;
	}

    
    private Data wrapWithMetadata(Table extractedTable, String label) {
		Data outputData = new BasicData(extractedTable, Table.class.getName());
		Dictionary metadata = outputData.getMetadata();
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.PARENT, data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		return outputData;
	}
}