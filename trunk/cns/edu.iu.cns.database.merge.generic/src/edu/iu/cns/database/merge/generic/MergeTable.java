package edu.iu.cns.database.merge.generic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
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

public class MergeTable implements Algorithm {
    private static final String INVALID_TABLE_NAME_HEADER_MESSAGE = "Unable to infer table to be merged. The last column header of the spreadsheet should be of the form '"
						+ CreateMergingTable.FROM_TABLE + " table.name'."; // TODO "table.name" not technically correct here.  User-friendliness is debatable.
	private Data[] data;
	private DatabaseService databaseService;
	private LogService logger;
	private List<String> problems = new ArrayList<String>();
    
    public MergeTable(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        
        this.databaseService = (DatabaseService) context.getService(DatabaseService.class.getName());
        this.logger = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	/*
    	 * look at the last column of the prefuse table to figure out which database table it applies to
    	 * figure out the primary key of that database table (if no primary key, throw exception)
    	 * create a duplicate of the database (database service, please work properly)
    	 * generate the groups of entities that need merging
    	 * for each group of entities
    	 * 	identify all foreign keys pointing to the entities that are not pointing to the selected primary entity
    	 * 	change those foreign keys to point to the selected primary entity
    	 *  delete all rows corresponding to the non-primary entities in the group
    	 */
    	
    	Table mergingTable = (Table) data[0].getData();
    	Database originalDatabase = (Database) data[1].getData();
    	//TODO: it would really be nice to have a way to clean up the output database if there is an error related to it.
    	Database outputDatabase = null;
    	Connection originalConnection = DatabaseUtilities.connect(originalDatabase, "Unable to communicate with the database.");
    	Connection outputConnection = null;
    	
    	DatabaseTable toBeMerged = inferTableToBeMerged(mergingTable);
    	try {
			if(toBeMerged.presentInDatabase(originalConnection)) {
				outputDatabase = databaseService.copyDatabase(originalDatabase);
				outputConnection = outputDatabase.getConnection();
				Collection<MergeGroup> mergeGroups = createMergeGroups(mergingTable, toBeMerged, outputConnection);
				int entitiesMergedAway = 0;
				int numberOfMerges = 0;
				for(MergeGroup group : mergeGroups) {
					try {
						int number = group.merge(outputConnection, toBeMerged);
						entitiesMergedAway += number;
						if(number > 0) {
							numberOfMerges += 1;
						}
					} catch (MergingErrorException e) {
						problems.add(e.getMessage());
					}
				}
				if(problems.size() > 0) {
					throw new AlgorithmExecutionException("The following problems were encountered while trying to merge: "
							+ formatProblems(problems));
				}
				
				logger.log(LogService.LOG_INFO, "Successfully merged " + entitiesMergedAway
						+ " entities into other entities in " + numberOfMerges + " merges, leaving "
						+ mergeGroups.size() + " entities in the database.");
				Data outputData = wrapWithMetadata(outputDatabase, "with merged " + toBeMerged.toString());
				return new Data[]{outputData};
				
			} else {
				throw new AlgorithmExecutionException("The table this merge data is for is not in the database.");
			}
		} catch (SQLException e) {
			throw new AlgorithmExecutionException("There was a problem communicating with the database.", e);
		} catch (DatabaseCopyException e) {
			throw new AlgorithmExecutionException("There was a problem creating the output data: " + e.getMessage(), e);
		} finally {
			DatabaseUtilities.closeConnectionQuietly(originalConnection);
			DatabaseUtilities.closeConnectionQuietly(outputConnection);
		}
    }

	private Collection<MergeGroup> createMergeGroups(Table mergingTable,
			DatabaseTable toBeMerged, Connection connection) throws AlgorithmExecutionException {
				
		try {
			Map<String, MergeGroup> groups = new HashMap<String, MergeGroup>();
			
			String[] primaryKeyColumns = toBeMerged.getPrimaryKeyColumns(connection);
			ColumnProjection primaryKeyColumnFilter = new NamedColumnProjection(primaryKeyColumns, true);
			
			ForeignKey[] foreignKeys = toBeMerged.getRelations(connection);
			
			TableIterator merges = mergingTable.iterator(mergingTable.rowsSortedBy(CreateMergingTable.MERGE_GROUP_IDENTIFIER_COLUMN, true));
			
			
			while(merges.hasNext()) {
				int row = merges.nextInt();
				Tuple tuple = mergingTable.getTuple(row);
				String groupIdentifier = tuple.getString(CreateMergingTable.MERGE_GROUP_IDENTIFIER_COLUMN);
				if(!groups.containsKey(groupIdentifier)) {
					groups.put(groupIdentifier, new MergeGroup(groupIdentifier, toBeMerged, primaryKeyColumnFilter, foreignKeys));
				}
				MergeGroup group = groups.get(groupIdentifier);
				try {
					group.addRecord(tuple);
				} catch (MergingErrorException e) {
					problems.add(e.getMessage());
				}
			}
			
			return groups.values();
			
		} catch (SQLException e) {
			throw new AlgorithmExecutionException("There was a problem creating the output data.", e);
		}
		
		
		
	}

	private String formatProblems(List<String> problems) {
		String delimiter = "\n\n  ";
		return delimiter + StringUtilities.implodeList(problems, delimiter) + "\n\n";
	}

	private DatabaseTable inferTableToBeMerged(Table mergingTable) throws AlgorithmExecutionException {
		String header = mergingTable.getColumnName(mergingTable.getColumnCount() - 1);
		String name = extractNameFromHeader(header);
		try {
			return DatabaseTable.fromRepresentation(name);
		} catch (InvalidRepresentationException e) {
			throw new AlgorithmExecutionException(INVALID_TABLE_NAME_HEADER_MESSAGE);
		}
	}

	private String extractNameFromHeader(String header) throws AlgorithmExecutionException {
		if(header.indexOf(CreateMergingTable.FROM_TABLE) == 0) {
			return header.substring(CreateMergingTable.FROM_TABLE.length());
		} else {
			throw new AlgorithmExecutionException(INVALID_TABLE_NAME_HEADER_MESSAGE);
		}
	}
	
	private Data wrapWithMetadata(Database database, String label) {
		Data outputData = new BasicData(database, this.data[1].getFormat());
		Dictionary metadata = outputData.getMetadata();
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.PARENT, data[1]);
		metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
		return outputData;
	}
}