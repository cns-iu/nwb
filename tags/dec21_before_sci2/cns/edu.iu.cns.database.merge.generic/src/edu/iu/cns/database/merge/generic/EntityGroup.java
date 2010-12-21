package edu.iu.cns.database.merge.generic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.database.Remover;
import org.cishell.utilities.database.Repointer;

import prefuse.data.Tuple;
import prefuse.data.util.ColumnProjection;

public class EntityGroup {
	
	private Map<String, Object> primaryEntity = null;
	private List<Map<String, Object>> otherEntities = new ArrayList<Map<String, Object>>();
	private String groupIdentifier;
	private ColumnProjection primaryKeyColumns;

	public EntityGroup(String groupIdentifier, ColumnProjection primaryKeyColumnFilter) {
		this.groupIdentifier = groupIdentifier;
		this.primaryKeyColumns = primaryKeyColumnFilter;
	}

	public void addRecord(Tuple tuple) throws MergingErrorException {
		Map<String, Object> primaryKeyValues = extractPrimaryKeyValues(tuple);
		if(isPrimaryTuple(tuple)) {
			if(primaryEntity != null) {
				throw new MergingErrorException("There is more than one primary entity marked for the merging group identified by '"
						+ groupIdentifier + "'.");
			}
			primaryEntity = primaryKeyValues;
		} else {
			otherEntities.add(primaryKeyValues);
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

	public void repoint(Repointer repointer) throws SQLException {
		for(Map<String, Object> otherEntity : otherEntities) {
			repointer.repoint(primaryEntity, otherEntity);
		}
		
	}

	public void verify() throws MergingErrorException {
		if(primaryEntity == null) {
			throw new MergingErrorException("There is no primary entity marked for the merging group identified by '" + groupIdentifier + "'");
		}
	}

	public void removeOtherEntities(Remover remover) throws SQLException {
		for(Map<String, Object> other : otherEntities) {
			remover.remove(other);
		}
		
	}

}
