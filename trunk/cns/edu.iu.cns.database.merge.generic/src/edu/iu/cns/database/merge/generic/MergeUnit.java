package edu.iu.cns.database.merge.generic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.cishell.utilities.database.DatabaseTable;
import org.cishell.utilities.database.ForeignKey;
import org.cishell.utilities.database.Repointer;

public class MergeUnit {

	private ForeignKey foreignKey;

	public MergeUnit(ForeignKey foreignKey) {
		this.foreignKey = foreignKey;
	}

	public void merge(Connection connection, Collection<EntityGroup> groups) throws SQLException {
		Repointer repointer = foreignKey.constructRepointer(connection);
		for(EntityGroup group : groups) {
			group.repoint(repointer);
		}
		repointer.apply();
	}

	public DatabaseTable getTable() {
		return foreignKey.otherTable;
	}

}
