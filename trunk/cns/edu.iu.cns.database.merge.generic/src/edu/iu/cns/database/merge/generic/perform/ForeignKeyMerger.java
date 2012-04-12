package edu.iu.cns.database.merge.generic.perform;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.cishell.utilities.database.DatabaseTable;
import org.cishell.utilities.database.ForeignKey;
import org.cishell.utilities.database.Repointer;


/**
 * MergeUnits are the units of work that will repoint the foreign keys
 * referring to the entities merged away to point at the primary entities.
 */
public class ForeignKeyMerger {
	private ForeignKey foreignKey;

	public ForeignKeyMerger(ForeignKey foreignKey) {
		this.foreignKey = foreignKey;
	}

	public void merge(Connection connection, Collection<EntityGroup> groups) throws SQLException {
		Repointer repointer = this.foreignKey.constructRepointer(connection);
		for(EntityGroup group : groups) {
			group.repoint(repointer);
		}
		repointer.apply();
	}

	public DatabaseTable getTable() {
		return this.foreignKey.otherTable;
	}
}
