package edu.iu.cns.database.merge.generic.perform;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCopyException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.DatabaseUtilities;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.database.DatabaseTable;
import org.cishell.utilities.database.ForeignKey;
import org.cishell.utilities.database.InvalidRepresentationException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.ColumnProjection;
import prefuse.data.util.NamedColumnProjection;
import prefuse.data.util.TableIterator;
import edu.iu.cns.database.merge.generic.perform.EntityGroup.MergingErrorException;
import edu.iu.cns.database.merge.generic.prepare.plain.CreateMergingTable;

public class MergeTableAlgorithm implements Algorithm, ProgressTrackable {
	private static final String INVALID_TABLE_NAME_HEADER_MESSAGE = "Unable to infer table to be merged. "
			+ "The last column header of the spreadsheet should be of the form '"
			+ CreateMergingTable.FROM_TABLE + " table.name'.";
	private Data[] data;
	private DatabaseService databaseService;
	private LogService logger;
	private List<String> problems = new ArrayList<String>();
	private ProgressMonitor monitor = ProgressMonitor.NULL_MONITOR;
    
	public static class Factory implements AlgorithmFactory {
	    public Algorithm createAlgorithm(
	    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
	        return new MergeTableAlgorithm(data, context);
	    }
	}
    public MergeTableAlgorithm(Data[] data, CIShellContext context) {
        this.data = data;
        
		this.databaseService = (DatabaseService) context
				.getService(DatabaseService.class.getName());
        this.logger = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {    	
    	Table mergingTable = (Table) this.data[0].getData();
    	Database originalDatabase = (Database) this.data[1].getData();
    	/* TODO: it would really be nice to have a way to clean up the output database
    	 * if there is an error related to it.
    	 */
    	Database outputDatabase = null;
    	DatabaseTable toBeMerged = inferTableToBeMerged(mergingTable);
    	
    	Connection originalConnection = DatabaseUtilities.connect(
    			originalDatabase, "Unable to communicate with the database.");
    	Connection outputConnection = null;
    	
    	try {
			if (toBeMerged.presentInDatabase(originalConnection)) {
				outputDatabase = this.databaseService.copyDatabase(originalDatabase);
				outputConnection = outputDatabase.getConnection();
				Merger merger = collectMerges(mergingTable, toBeMerged, outputConnection);
				this.problems.addAll(merger.merge(outputConnection));
				if (this.problems.size() > 0) {
					throw new AlgorithmExecutionException(
							"The following problems were encountered while trying to merge: "
							+ formatProblems(this.problems));
				}
				
				this.logger.log(LogService.LOG_INFO,
						"Successfully merged " + merger.getEntitiesMergedAway()
						+ " entities into other entities, leaving "
						+ merger.getRemainingEntities() + " entities in the database.");
				Data outputData = wrapWithMetadata(
						outputDatabase, "with merged " + toBeMerged.toString());
				this.monitor.done();
				return new Data[]{ outputData };
				
			}
			throw new AlgorithmExecutionException(
					"The table this merge data is for is not in the database.");
		} catch (SQLException e) {
			throw new AlgorithmExecutionException(
					"There was a problem communicating with the database.", e);
		} catch (DatabaseCopyException e) {
			throw new AlgorithmExecutionException(
					"There was a problem creating the output data: " + e.getMessage(), e);
		} finally {
			DatabaseUtilities.closeConnectionQuietly(originalConnection);
			DatabaseUtilities.closeConnectionQuietly(outputConnection);
		}
    }
    
    
	private Merger collectMerges(Table mergingTable,
			DatabaseTable toBeMerged, Connection connection) throws AlgorithmExecutionException {
			
		Merger merger = new Merger(toBeMerged, this.monitor);
		try {			
			String[] primaryKeyColumns = toBeMerged.getPrimaryKeyColumns(connection);
			ColumnProjection primaryKeyColumnFilter =
					new NamedColumnProjection(primaryKeyColumns, true);
			
			ForeignKey[] foreignKeys = toBeMerged.getRelations(connection);
			for(ForeignKey foreignKey : foreignKeys) {
				/* MergeUnits are the units of work that will repoint the foreign keys
				 * referring to the entities merged away to point at the primary entities
				 */
				merger.addForeignKeyMerger(new ForeignKeyMerger(foreignKey));
			}
			
			TableIterator merges = mergingTable.iterator(
					mergingTable.rowsSortedBy(
							CreateMergingTable.MERGE_GROUP_IDENTIFIER_COLUMN, true));
			
			while(merges.hasNext()) {
				int row = merges.nextInt();
				Tuple tuple = mergingTable.getTuple(row);
				String groupIdentifier = tuple.getString(
						CreateMergingTable.MERGE_GROUP_IDENTIFIER_COLUMN);
				//for every key someone used for a merge group, there's an EntityGroup
				EntityGroup group = merger.getOrCreateEntityGroup(
						groupIdentifier, primaryKeyColumnFilter);
				try {
					group.addRecord(tuple);
				} catch (MergingErrorException e) {
					this.problems.add(e.getMessage());
				}
			}
			
			return merger;
		} catch (SQLException e) {
			throw new AlgorithmExecutionException(
					"There was a problem creating the output data.", e);
		}
	}

	private static String formatProblems(List<String> problems) {
		String delimiter = "\n\n  ";
		return delimiter + StringUtilities.implodeItems(problems, delimiter) + "\n\n";
	}

	private static DatabaseTable inferTableToBeMerged(Table mergingTable)
			throws AlgorithmExecutionException {
		String header = mergingTable.getColumnName(mergingTable.getColumnCount() - 1);
		String name = extractNameFromHeader(header);
		try {
			return DatabaseTable.fromRepresentation(name);
		} catch (InvalidRepresentationException e) {
			throw new AlgorithmExecutionException(INVALID_TABLE_NAME_HEADER_MESSAGE);
		}
	}

	private static String extractNameFromHeader(String header) throws AlgorithmExecutionException {
		if(header.indexOf(CreateMergingTable.FROM_TABLE) == 0) {
			return header.substring(CreateMergingTable.FROM_TABLE.length());
		}
		throw new AlgorithmExecutionException(INVALID_TABLE_NAME_HEADER_MESSAGE);
	}
	
	private Data wrapWithMetadata(Database database, String label) {
		Data outputData = new BasicData(database, this.data[1].getFormat());
		Dictionary<String, Object> metadata = outputData.getMetadata();
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.PARENT, this.data[1]);
		metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
		return outputData;
	}

	public ProgressMonitor getProgressMonitor() {
		return this.monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}
}