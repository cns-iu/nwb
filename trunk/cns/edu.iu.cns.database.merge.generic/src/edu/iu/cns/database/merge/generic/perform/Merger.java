package edu.iu.cns.database.merge.generic.perform;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.utilities.database.DatabaseTable;
import org.cishell.utilities.database.Remover;

import prefuse.data.util.ColumnProjection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.iu.cns.database.merge.generic.perform.EntityGroup.MergingErrorException;


public class Merger {	
	private Set<ForeignKeyMerger> foreignKeyMergers = Sets.newHashSet();
	private Map<String, EntityGroup> groups = Maps.newHashMap();
	private DatabaseTable databaseTable;
	private int entitiesMergedAway;
	private ProgressMonitor monitor = ProgressMonitor.NULL_MONITOR;

	public Merger(DatabaseTable toBeMerged, ProgressMonitor monitor) {
		this.databaseTable = toBeMerged;
		this.monitor = monitor;
	}

	public List<String> merge(Connection outputConnection) {
		// The number of foreign keys to be repointed, + 1 for the deletion of entities merged away
		int work_units = foreignKeyMergers.size() + 1;
		int current_work_unit = 1;
    	monitor.start(ProgressMonitor.WORK_TRACKABLE, work_units);
		List<String> problems = Lists.newArrayList();
		for (EntityGroup group : groups.values()) {
			try {
				// Right now this means, do they all have primary entities
				group.verify();
			} catch (MergingErrorException e) {
				problems.add(e.getMessage());
				return problems;
			}
		}
		for (ForeignKeyMerger merger : foreignKeyMergers) {
			try {
				monitor.describeWork(
						"Repointing members of the table " + merger.getTable().toString() + "...");
				merger.merge(outputConnection, groups.values());
				monitor.worked(current_work_unit++);
			} catch (SQLException e) {
				e.printStackTrace();
				problems.add("There was a problem repointing members of the table "
						+ merger.getTable().toString() + " to point at the merged entities.");
			}
		}
		try {
			monitor.describeWork("Removing entities that were merged away...");
			Remover remover = databaseTable.constructRemover(outputConnection);
			for (EntityGroup group : groups.values()) {
				group.removeOtherEntities(remover);
			}
			this.entitiesMergedAway = remover.apply();
			monitor.worked(current_work_unit++);
		} catch (SQLException e) {
			e.printStackTrace();
			problems.add("After repointing, it was not possible to successfully remove all the entities that were merged away.");
		}
		return problems;
	}

	public int getEntitiesMergedAway() {
		return entitiesMergedAway;
	}

	public int getRemainingEntities() {
		return groups.size();
	}

	public void addForeignKeyMerger(ForeignKeyMerger foreignKeyMerger) {
		foreignKeyMergers.add(foreignKeyMerger);
	}

	public Collection<ForeignKeyMerger> getMergeUnits() {
		return foreignKeyMergers;
	}

	public EntityGroup getOrCreateEntityGroup(
			String groupIdentifier, ColumnProjection primaryKeyColumnFilter) {
		if(!groups.containsKey(groupIdentifier)) {
			EntityGroup newGroup = new EntityGroup(groupIdentifier, primaryKeyColumnFilter);
			groups.put(groupIdentifier, newGroup);
		}
		
		return groups.get(groupIdentifier);
	}

}
