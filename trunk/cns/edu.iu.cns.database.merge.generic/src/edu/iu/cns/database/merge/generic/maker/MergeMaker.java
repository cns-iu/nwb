package edu.iu.cns.database.merge.generic.maker;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.iu.cns.database.merge.generic.CreateMergingTable;
import edu.iu.cns.database.merge.generic.CreateMergingTableFactory;
import edu.iu.cns.database.merge.generic.MergeTableFactory;

public class MergeMaker {

	private static Data[] mergeTable(
			String tableName,
			Data database,
			KeyMaker keyMaker,
			MergeCheck mergeCheck,
			boolean keyDeterminesMerge,
			PreferrableFormComparator preferrableFormComparator,
			CIShellContext context,
			ProgressMonitor monitor,
			String label) throws AlgorithmExecutionException {
		Data mergingTable = extractMarkedTable(tableName, database, keyMaker,
				mergeCheck, keyDeterminesMerge, preferrableFormComparator, context);

		return performMerge(mergingTable, database, context, monitor, label);
	}

	public static Data[] mergeTable(
			String tableName,
			Data database,
			KeyMaker keyMaker,
			boolean keyDeterminesMerge,
			PreferrableFormComparator preferrableFormComparator,
			CIShellContext context,
			ProgressMonitor monitor,
			String label) throws AlgorithmExecutionException {
		return mergeTable(
			tableName,
			database,
			keyMaker,
			null,
			keyDeterminesMerge,
			preferrableFormComparator,
			context,
			monitor,
			label);
	}

	public static Data[] mergeTable(
			String tableName,
			Data database,
			KeyMaker keyMaker,
			MergeCheck mergeCheck, 
			PreferrableFormComparator preferrableFormComparator,
			CIShellContext context,
			ProgressMonitor monitor,
			String label) throws AlgorithmExecutionException {
		return mergeTable(
			tableName,
			database,
			keyMaker,
			mergeCheck,
			false,
			preferrableFormComparator,
			context,
			monitor,
			label);
	}

	private static Data extractMarkedTable(
			String tableName,
			Data database,
			KeyMaker keyMaker,
			MergeCheck mergeCheck,
			boolean keyDeterminesMerge,
			PreferrableFormComparator preferrableFormComparator,
			CIShellContext context)
			throws AlgorithmExecutionException {
		Data mergingTableData = extractTable(tableName, database, context);
		Table mergingTable = (Table) mergingTableData.getData();
		MergeMaker.markMerges(
			mergingTable, keyMaker, mergeCheck, keyDeterminesMerge, preferrableFormComparator);

		return mergingTableData;
	}

	private static Data[] markTable(
			String tableName,
			Data database,
			KeyMaker keyMaker,
			MergeCheck mergeCheck,
			boolean keyDeterminesMerge,
			PreferrableFormComparator preferrableFormComparator,
			CIShellContext context,
			String label) throws AlgorithmExecutionException {
		Data tableData = extractMarkedTable(
			tableName,
			database,
			keyMaker,
			mergeCheck,
			keyDeterminesMerge,
			preferrableFormComparator,
			context);
		tableData.getMetadata().put(DataProperty.LABEL, label);

		return new Data[]{tableData};
	}

	public static Data[] markTable(
			String tableName,
			Data database,
			KeyMaker keyMaker,
			boolean keyDeterminesMerge,
			PreferrableFormComparator preferrableFormComparator,
			CIShellContext context,
			String label) throws AlgorithmExecutionException {
		return markTable(
			tableName,
			database,
			keyMaker,
			null,
			keyDeterminesMerge,
			preferrableFormComparator,
			context,
			label);
	}

	public static Data[] markTable(
			String tableName,
			Data database,
			KeyMaker keyMaker,
			MergeCheck mergeCheck,
			PreferrableFormComparator preferrableFormComparator,
			CIShellContext context,
			String label) throws AlgorithmExecutionException {
		return markTable(
			tableName,
			database,
			keyMaker,
			mergeCheck,
			false,
			preferrableFormComparator,
			context,
			label);
	}

	private static Data[] performMerge(
			Data mergingTableData,
			Data databaseData,
			CIShellContext context,
			ProgressMonitor monitor,
			String label)
			throws AlgorithmExecutionException {

		Algorithm mergingAlgorithm = new MergeTableFactory().createAlgorithm(
			new Data[]{mergingTableData, databaseData}, new Hashtable<Object, Object>(), context);

		if (mergingAlgorithm instanceof ProgressTrackable) {
			((ProgressTrackable) mergingAlgorithm).setProgressMonitor(monitor);
		}

		Data[] mergedOutput = mergingAlgorithm.execute();
		mergedOutput[0].getMetadata().put(DataProperty.LABEL, label);

		return mergedOutput;
	}




	private static Data extractTable(
			String tableName, Data databaseData, CIShellContext context)
			throws AlgorithmExecutionException {
		Dictionary<Object, Object> extractTableParams = new Hashtable<Object, Object>();
		extractTableParams.put(CreateMergingTableFactory.TABLE_PARAMETER, tableName);
		Data[] mergingTableData = new CreateMergingTableFactory().createAlgorithm(
			new Data[]{databaseData}, extractTableParams, context).execute();

		return mergingTableData[0];
	}

	private static void markMerges(Table mergingTable, KeyMaker keyMaker,
			MergeCheck mergeCheck, boolean keyDeterminesMerge,
			PreferrableFormComparator preferrableFormComparator) {
		List<Tuple> allTuples = Lists.newArrayList();
		ListMultimap<Object, Tuple> groups = ArrayListMultimap.create();
		Iterator<Tuple> tuples = mergingTable.tuples();
		while(tuples.hasNext()) {
			Tuple tuple = tuples.next();
			Object key = keyMaker.makeKey(tuple);
			groups.put(key, tuple);
			allTuples.add(tuple);
		}

		if(keyDeterminesMerge) {
			markGroups(preferrableFormComparator, groups);
		} else {
			markNetworkGroups(mergeCheck, preferrableFormComparator, allTuples,
					groups);
		}
	}

	public static void markMerges(Table mergingTable, KeyMaker keyMaker,
			boolean keyDeterminesMerge,
			PreferrableFormComparator preferrableFormComparator) {
		markMerges(mergingTable, keyMaker, null, true, preferrableFormComparator);
	}

	public static void markMerges(Table mergingTable, KeyMaker keyMaker,
			MergeCheck mergeCheck,
			PreferrableFormComparator preferrableFormComparator) {
		markMerges(mergingTable, keyMaker, mergeCheck, false, preferrableFormComparator);
	}

	private static void markNetworkGroups(MergeCheck mergeCheck,
			PreferrableFormComparator preferrableFormComparator,
			List<Tuple> allTuples, ListMultimap<Object, Tuple> groups) {
		ListMultimap<Tuple, Tuple> neighbors = determineNeighbors(mergeCheck,
				groups);

		markGroups(preferrableFormComparator, allTuples, neighbors);
	}

	private static void markGroups(
			PreferrableFormComparator preferrableFormComparator,
			ListMultimap<Object, Tuple> groups) {
		for(Object key : groups.keySet()) {
			List<Tuple> group = groups.get(key);
			Tuple primaryCandidate = group.get(0);
			markNotPrimary(primaryCandidate);
			Object clusterIdentifier = currentIdentifier(primaryCandidate);
			for(Tuple next : group) {
				assignIdentifier(next, clusterIdentifier);
				markNotPrimary(next);
				if(preferrableFormComparator.compare(next, primaryCandidate) > 0) {
					primaryCandidate = next;
				}
			}
			markPrimary(primaryCandidate);
		}
	}

	private static void markGroups(
			PreferrableFormComparator preferrableFormComparator,
			List<Tuple> allTuples, ListMultimap<Tuple, Tuple> neighbors) {
		Set<Tuple> marked = Sets.newHashSet();
		for(Tuple tuple : allTuples) {
			if(marked.contains(tuple)) {
				continue;
			} else {
				Object clusterIdentifier = currentIdentifier(tuple);
				Tuple primaryCandidate = tuple;
				Stack<Tuple> toCheck = new Stack<Tuple>();
				toCheck.add(tuple);
				while(toCheck.size() > 0) {
					Tuple next = toCheck.pop();
					if(marked.contains(next)) {
						continue;
					} else {
						marked.add(next);
						assignIdentifier(next, clusterIdentifier);
						markNotPrimary(next);
						if(preferrableFormComparator.compare(next, primaryCandidate) > 0) {
							primaryCandidate = next;
						}
						toCheck.addAll(neighbors.get(next));
					}
				}
				markPrimary(primaryCandidate);
			}
		}
	}

	private static ListMultimap<Tuple, Tuple> determineNeighbors(
			MergeCheck mergeCheck, ListMultimap<Object, Tuple> groups) {
		ListMultimap<Tuple, Tuple> neighbors = ArrayListMultimap.create();
		for(Object key : groups.keySet()) {
			List<Tuple> groupTuples = groups.get(key);
			for(int ii = 0; ii < groupTuples.size(); ii++) {
				Tuple first = groupTuples.get(ii);
				Object firstIdentifier = currentIdentifier(first);
				for(int jj = ii + 1; jj < groupTuples.size(); jj++) {
					Tuple second = groupTuples.get(jj);
					Object secondIdentifier = currentIdentifier(second);
					if(mergeCheck.shouldMerge(first, second)) {
						neighbors.put(first, second);
						neighbors.put(second, first);
					}
				}
			}    		
		}
		return neighbors;
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
}
