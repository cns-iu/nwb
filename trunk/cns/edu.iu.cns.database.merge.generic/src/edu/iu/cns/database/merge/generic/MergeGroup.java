package edu.iu.cns.database.merge.generic;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.database.DatabaseTable;
import org.cishell.utilities.database.ForeignKey;

import prefuse.data.Tuple;
import prefuse.data.util.ColumnProjection;

public class MergeGroup {
	
	private Map<String, Object> primaryEntity = null;
	private List<Map<String, Object>> otherEntities = new ArrayList<Map<String, Object>>();
	private String groupIdentifier;
	private ColumnProjection primaryKeyColumns;
	private ForeignKey[] foreignKeys;
	private DatabaseTable databaseTable;

	public MergeGroup(String groupIdentifier, DatabaseTable toBeMerged, ColumnProjection primaryKeyColumnFilter, ForeignKey[] foreignKeys) {
		this.groupIdentifier = groupIdentifier;
		this.databaseTable = toBeMerged;
		this.primaryKeyColumns = primaryKeyColumnFilter;
		this.foreignKeys = foreignKeys;
	}

	public int merge(Statement statement, DatabaseTable toBeMerged) throws MergingErrorException {
		/*
		 *	identify all foreign keys pointing to the entities that are not pointing to the selected primary entity
    	 * 	change those foreign keys to point to the selected primary entity
    	 *  delete all rows corresponding to the non-primary entities in the group
    	 *  returns true if a merge occurs.
    	 */
		
		if(primaryEntity == null) {
			throw new MergingErrorException("There is no primary entity marked for the merging group identified by '" + groupIdentifier + "'");
		}
		if(otherEntities.size() == 0) {
			return 0;
		}
		
		for(ForeignKey foreignKey : foreignKeys) {
			try {
				foreignKey.repoint(otherEntities, primaryEntity, statement);
			} catch (SQLException e) {
				//e.printStackTrace();
				//make the stacktrace logged, maybe? but not a log the person sees.
				throw new MergingErrorException("There was a problem updating the connections in the table " + foreignKey.otherTable + " to reflect the merge.");
			}
		}
		
		removeOtherEntities(otherEntities, statement);
		return otherEntities.size();
		
	}

	private void removeOtherEntities(List<Map<String, Object>> otherEntities,
			Statement statement) throws MergingErrorException {
		try {
			databaseTable.deleteRowsByColumns(otherEntities, statement);
		} catch (SQLException e) {
			throw new MergingErrorException("Unable to merge some of the specified rows in the group " + groupIdentifier);
		}
	}

	public void addRecord(Tuple tuple) throws MergingErrorException {
		if(isPrimaryTuple(tuple)) {
			if(primaryEntity != null) {
				throw new MergingErrorException("There is more than one primary entity marked for the merging group identified by '"
						+ groupIdentifier + "'.");
			}
			primaryEntity = extractPrimaryKeyValues(tuple);
		} else {
			otherEntities.add(extractPrimaryKeyValues(tuple));
		}
		
	}

	private boolean isPrimaryTuple(Tuple tuple) {
		return "*".equals(tuple.getString(CreateMergingTable.PRIMARY_ENTITY_COLUMN));
	}
	
	private Map<String, Object> extractPrimaryKeyValues(Tuple tuple) {
		Map<String, Object> values = new HashMap<String, Object>();
		for(int ii = 0; ii < tuple.getColumnCount(); ii++) {
			String columnName = tuple.getColumnName(ii);
			if(primaryKeyColumns.include(tuple.getTable().getColumn(ii), columnName)) {
				Object value = tuple.get(ii);
				values.put(columnName, value);
			}
		}
		return values;
	}

}
