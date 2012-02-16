package edu.iu.cns.database.merge.generic.prepare.marked;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.database.DatabaseTable;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Ordering;

import edu.iu.cns.database.merge.generic.perform.MergeTableAlgorithm;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategy;
import edu.iu.cns.database.merge.generic.prepare.plain.CreateMergingTable;
import edu.iu.cns.database.merge.generic.prepare.plain.CreateMergingTableFactory;

/**
 * Uses a GroupingStrategy and (optionally) a PreferredFormComparator to mark a given merging table.
 * <p/>
 * Convenience methods are provided to (1) implicitly generate a plain merging table for marking
 * from a given Database and DatabaseTable or (2) directly perform the marked merges on same.
 */
public class MergeMarker {
	private final GroupingStrategy groupingStrategy;
	private final Comparator<? super Tuple> preferredFormComparator;
    

	public MergeMarker(GroupingStrategy groupingStrategy) {
		this(groupingStrategy, Ordering.arbitrary());
	}
	
	public MergeMarker(
			GroupingStrategy groupingStrategy,
			Comparator<? super Tuple> preferredFormComparator) {
		this.groupingStrategy = groupingStrategy;
		this.preferredFormComparator = preferredFormComparator;
	}
	
	
	/**
	 * Produces a copy of {@code table} with merging instructions marked.
	 * 
	 * @param table		A table to mark.
	 * @return
	 */
	public Table markTable(Table table) {
 		final Table workingTable = TableUtilities.copyTable(table);
 		
 		markGroups(groupingStrategy.splitIntoGroups(new Iterable<Tuple>() {
			@SuppressWarnings("unchecked")
			public Iterator<Tuple> iterator() {
				return workingTable.tuples();
			}
 		}));
 		
 		return workingTable;
 	}

	/** Produces a marked merging table for the {@code databaseTable} in {@code database}. */
	public Table createMarkedMergingTable(
			DatabaseTable databaseTable, Database database, CIShellContext context)
					throws AlgorithmExecutionException {
		return createMarkedMergingTable(databaseTable.toString(), database, context);
	}
	/** 
	 * Produces a marked merging table for the {@code DatabaseTable}
	 * named {@code databaseTableName} in {@code database}.
	 */
	public Table createMarkedMergingTable(
			String databaseTableName, Database database, CIShellContext context)
					throws AlgorithmExecutionException {
		return markTable(executeMergingTableCreation(databaseTableName, database, context));
	}
	
	/** See {@link #performMergesOn(String, Database, ProgressMonitor, CIShellContext).} */
	public Database performMergesOn(
			DatabaseTable databaseTable,
			Database database,
			ProgressMonitor monitor,
			CIShellContext context)
					throws AlgorithmExecutionException {
		return performMergesOn(databaseTable.toString(), database, monitor, context);
	}
	/**
	 * Creates a marked merging table for the {@code DatabaseTable} named {@code databaseTableName},
	 * performs the marked merges, and returns the updated {@code Database}.
	 */
	public Database performMergesOn(
			String databaseTableName,
			Database database,
			ProgressMonitor monitor,
			CIShellContext context)
					throws AlgorithmExecutionException {
		return executeMerge(
				markTable(executeMergingTableCreation(databaseTableName, database, context)),
				database,
				context,
				monitor);
	}
	
 	
 	
	public static Database executeMerge(
 			Table markedMergingTable,
 			Database database,
 			CIShellContext context,
 			ProgressMonitor monitor) throws AlgorithmExecutionException { 		
 		Data[] mergedDatabaseData = AlgorithmUtilities.executeAlgorithm(
 				new MergeTableAlgorithm.Factory(),
 				monitor,
 				new Data[]{
 					new BasicData(markedMergingTable, Table.class.getName()),
 					new BasicData(database, "db:any")
 				},
 				new Hashtable<String, Object>(),
 				context);
 		
 		return (Database) mergedDatabaseData[0].getData();
 	}

	/**
	 * Within each group, unify identifiers and mark all but the most preferred form non-primary.
	 */
	private void markGroups(ImmutableCollection<Collection<Tuple>> groups) {
 		for (Collection<Tuple> group : groups) { 			
			Tuple mostPreferredTuple = Collections.max(group, preferredFormComparator);
			Object clusterIdentifier = currentIdentifier(mostPreferredTuple);
			
			for (Tuple tuple : group) {
				assignIdentifier(tuple, clusterIdentifier);
				markNotPrimary(tuple);
			}
			
			markPrimary(mostPreferredTuple);
 		}
 	}


 	private static void markPrimary(Tuple tuple) {
 		tuple.setString(CreateMergingTable.PRIMARY_ENTITY_COLUMN, "*");
 	}

 	private static void assignIdentifier(Tuple tuple, Object selectedIdentifier) {
 		tuple.set(CreateMergingTable.MERGE_GROUP_IDENTIFIER_COLUMN, selectedIdentifier);		
 	}

 	private static Object currentIdentifier(Tuple tuple) {
 		return tuple.get(CreateMergingTable.MERGE_GROUP_IDENTIFIER_COLUMN);
 	}

 	private static void markNotPrimary(Tuple tuple) {
 		tuple.setString(CreateMergingTable.PRIMARY_ENTITY_COLUMN, "");
 	}
 	
  	private static Table executeMergingTableCreation(
  			String tableName, Database database, CIShellContext context)
  			throws AlgorithmExecutionException {
  		Dictionary<String, Object> parameters = new Hashtable<String, Object>();
  		parameters.put(CreateMergingTableFactory.TABLE_PARAMETER, tableName);
  		
  		Data[] mergingTableData = AlgorithmUtilities.executeAlgorithm(
  				new CreateMergingTableFactory(),
  				new Data[]{ new BasicData(database, "db:any") },
  				parameters,
  				context);

  		Table mergingTable = (Table) mergingTableData[0].getData();
  		
  		return mergingTable;
  	}


  	@Override
  	public String toString() {
  		return Objects.toStringHelper(this)
  						.add("groupingStrategy", groupingStrategy)
  						.add("preferredFormComparator", preferredFormComparator)
  						.toString();
  	}
}