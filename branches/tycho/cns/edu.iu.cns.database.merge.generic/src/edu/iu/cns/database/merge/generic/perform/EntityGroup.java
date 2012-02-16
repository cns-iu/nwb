package edu.iu.cns.database.merge.generic.perform;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.database.Remover;
import org.cishell.utilities.database.Repointer;

import prefuse.data.Tuple;
import prefuse.data.util.ColumnProjection;

import com.google.common.collect.Lists;

import edu.iu.cns.database.merge.generic.prepare.plain.CreateMergingTable;

public class EntityGroup {
	private Map<String, Object> primaryEntity = null;
	private List<Map<String, Object>> otherEntities = Lists.newArrayList();
	private String groupIdentifier;
	private ColumnProjection primaryKeyColumns;

	public EntityGroup(String groupIdentifier,
			ColumnProjection primaryKeyColumnFilter) {
		this.groupIdentifier = groupIdentifier;
		this.primaryKeyColumns = primaryKeyColumnFilter;
	}

	public String getGroupIdentifier() {
		return this.groupIdentifier;
	}

	public Map<String, Object> getPrimaryEntity() {
		return this.primaryEntity;
	}

	public List<Map<String, Object>> getAllEntities()
			throws MergingErrorException {
		if (this.primaryEntity == null) {
			throw new MergingErrorException(
					"There is no primary entity marked for the merging group identified by '"
							+ this.groupIdentifier + "'.");
		}

		List<Map<String, Object>> allEntities = new ArrayList<Map<String, Object>>(
				this.otherEntities);
		allEntities.add(this.primaryEntity);
		return allEntities;
	}

	public void addRecord(Tuple tuple) throws MergingErrorException {
		Map<String, Object> primaryKeyValues = extractPrimaryKeyValues(tuple);

		if (isPrimaryTuple(tuple)) {
			if (this.primaryEntity != null) {
				throw new MergingErrorException(
						"There is more than one primary entity marked "
								+ "for the merging group identified by '"
								+ this.groupIdentifier + "'.");
			}

			this.primaryEntity = primaryKeyValues;
		} else {
			this.otherEntities.add(primaryKeyValues);
		}

	}

	private static boolean isPrimaryTuple(Tuple tuple) {
		return CreateMergingTable.PRIMARY_ENTITY_TRUE_VALUE.equals(tuple
				.getString(CreateMergingTable.PRIMARY_ENTITY_COLUMN));
	}

	private Map<String, Object> extractPrimaryKeyValues(Tuple tuple) {
		Map<String, Object> values = new HashMap<String, Object>();
		for (int ii = 0; ii < tuple.getColumnCount(); ii++) {
			String columnName = tuple.getColumnName(ii);
			if (this.primaryKeyColumns.include(tuple.getTable().getColumn(ii),
					columnName)) {
				Object value = tuple.get(ii);
				values.put(columnName, value);
			}
		}
		return values;
	}

	public void repoint(Repointer repointer) throws SQLException {
		for (Map<String, Object> otherEntity : this.otherEntities) {
			repointer.repoint(this.primaryEntity, otherEntity);
		}
	}

	public void verify() throws MergingErrorException {
		if (this.primaryEntity == null) {
			throw new MergingErrorException(
					"There is no primary entity marked for the merging "
							+ "group identified by '" + this.groupIdentifier + "'");
		}
	}

	public void removeOtherEntities(Remover remover) throws SQLException {
		for (Map<String, Object> other : this.otherEntities) {
			remover.remove(other);
		}
	}

	public class MergingErrorException extends Exception {
		private static final long serialVersionUID = 1L;

		public MergingErrorException(String message) {
			super(message);
		}
	}
}
